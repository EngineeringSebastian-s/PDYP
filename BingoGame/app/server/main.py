import socket
import threading
import random
import tkinter as tk
from tkinter import messagebox
import sys
import os

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..')))

from app.config import HOST, PORT, BG_COLOR, FG_COLOR, ACCENT_COLOR, HIGHLIGHT_COLOR, COLUMN_RANGES, BINGO_COLORS


class Player:
    def __init__(self, name, conn, addr):
        self.name = name
        self.conn = conn
        self.addr = addr
        self.card = []
        self.marked = [[False] * 5 for _ in range(5)]


class BingoServer:
    def __init__(self, host, port, gui_callback_update, gui_callback_log):
        self.host = host
        self.port = port
        self.gui_update = gui_callback_update
        self.gui_log = gui_callback_log
        self.server_socket = None
        self.clients = []
        self.clients_lock = threading.Lock()
        self.game_started = False
        self.all_numbers = list(range(1, 76))
        random.shuffle(self.all_numbers)
        self.current_index = -1
        self.drawn_numbers_set = set()
        self.winner = None

    def start_network(self):
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.server_socket.bind((self.host, self.port))
        self.server_socket.listen()
        self.gui_log(f"Servidor iniciado en {self.host}:{self.port}")
        threading.Thread(target=self.accept_clients, daemon=True).start()

    def accept_clients(self):
        while True:
            try:
                conn, addr = self.server_socket.accept()
                threading.Thread(target=self.handle_new_client, args=(conn, addr), daemon=True).start()
            except OSError:
                break

    def handle_new_client(self, conn, addr):
        player = None
        buffer = ""
        try:
            data = conn.recv(1024).decode("utf-8").strip()
            if not data.startswith("JOIN "):
                conn.close();
                return

            name = data[5:].strip()
            player = Player(name, conn, addr)
            player.card = self.generate_card()

            # Enviar cartón
            card_str = self.card_to_string(player.card)
            conn.sendall(f"CARD {card_str}\n".encode("utf-8"))

            with self.clients_lock:
                if not self.game_started:
                    self.clients.append(player)
                    self.gui_log(f"Conectado: {name}")
                else:
                    conn.sendall(b"END Juego_en_progreso\n")
                    conn.close();
                    return

            while True:
                chunk = conn.recv(1024)
                if not chunk: break
                buffer += chunk.decode("utf-8")
                while "\n" in buffer:
                    msg, buffer = buffer.split("\n", 1)
                    self.process_message(player, msg.strip())
        except Exception as e:
            self.gui_log(f"Error {addr}: {e}")
        finally:
            if player:
                with self.clients_lock:
                    if player in self.clients: self.clients.remove(player)
            conn.close()

    def process_message(self, player, msg):
        if msg.startswith("HIT "):
            try:
                r, c = map(int, msg[4:].split(","))
                self.validate_hit(player, r, c)
            except:
                pass
        elif msg.startswith("BINGO"):
            self.gui_log(f"¡{player.name} canta BINGO! Verificando...")
            if self.winner is None:
                if self.check_bingo(player):
                    self.winner = player
                    self.gui_log(f"*** GANADOR: {player.name} ***")
                    self.end_game(f"Ganador:{player.name}")
                else:
                    self.gui_log(f"BINGO FALSO de {player.name}")

    def generate_card(self):
        cols = [random.sample(list(COLUMN_RANGES[c]), 5) for c in range(5)]
        return [[cols[c][r] for c in range(5)] for r in range(5)]

    def card_to_string(self, card):
        return ";".join([",".join(map(str, row)) for row in card])

    def check_bingo(self, player):
        m = player.marked
        # Filas, Columnas, Diagonales
        if any(all(row) for row in m): return True
        if any(all(m[r][c] for r in range(5)) for c in range(5)): return True
        if all(m[i][i] for i in range(5)): return True
        if all(m[i][4 - i] for i in range(5)): return True
        return False

    def next_ball(self):
        if self.current_index + 1 >= len(self.all_numbers) or self.winner: return
        self.game_started = True
        self.current_index += 1
        num = self.all_numbers[self.current_index]

        letter = "B" if num <= 15 else "I" if num <= 30 else "N" if num <= 45 else "G" if num <= 60 else "O"
        self.drawn_numbers_set.add(num)
        self.gui_update(letter, num)

        msg = f"BALL {letter},{num}\n"
        with self.clients_lock:
            for p in self.clients:
                try:
                    p.conn.sendall(msg.encode("utf-8"))
                except:
                    pass

    def end_game(self, reason="Fin"):
        msg = f"END {reason}\n"
        with self.clients_lock:
            for p in self.clients:
                try:
                    p.conn.sendall(msg.encode("utf-8")); p.conn.close()
                except:
                    pass
            self.clients.clear()
        messagebox.showinfo("Fin", reason)
        try:
            self.server_socket.close()
        except:
            pass

    def validate_hit(self, player, r, c):
        if 0 <= r < 5 and 0 <= c < 5:
            num = player.card[r][c]

            # Si el número ya salió, permitir marcar
            if num in self.drawn_numbers_set:
                player.marked[r][c] = True
            else:
                # Aviso en el servidor
                self.gui_log(f"ALERTA: {player.name} intentó marcar {num} sin haber salido.")


class ServerGUI:
    def __init__(self, root):
        self.root = root
        self.root.title("Servidor Bingo")
        self.root.geometry("400x500")
        self.root.configure(bg=BG_COLOR)

        tk.Label(root, text="BINGO SERVER", font=("Arial", 20, "bold"), bg=BG_COLOR, fg=FG_COLOR).pack(pady=10)

        self.lbl_ball = tk.Label(root, text="--", font=("Arial", 50, "bold"), width=4, height=2)
        self.lbl_ball.pack(pady=10)

        tk.Button(root, text="SACAR BALOTA", bg=HIGHLIGHT_COLOR, command=self.next_ball, font=("Arial", 14)).pack(
            pady=5)

        self.log = tk.Listbox(root, height=8, bg="#34495E", fg="white")
        self.log.pack(fill=tk.BOTH, expand=True, padx=10, pady=10)

        self.server = BingoServer(HOST, PORT, self.update_display, self.log_msg)
        self.server.start_network()

    def next_ball(self):
        self.server.next_ball()

    def update_display(self, letter, number):
        self.lbl_ball.config(text=f"{letter}\n{number}", bg=BINGO_COLORS.get(letter, "white"))
        self.log_msg(f"Salió: {letter}-{number}")

    def log_msg(self, msg):
        self.log.insert(tk.END, msg)
        self.log.see(tk.END)


if __name__ == "__main__":
    root = tk.Tk()
    app = ServerGUI(root)
    root.mainloop()