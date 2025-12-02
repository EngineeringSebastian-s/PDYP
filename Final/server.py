import socket
import threading
import random
import tkinter as tk
from tkinter import messagebox

HOST = "127.0.0.1"
PORT = 5000

# Colores y fuentes 
BG_COLOR = "#2C3E50"        # Azul oscuro
FG_COLOR = "#ECF0F1"        # Blanco grisáceo
ACCENT_COLOR = "#E74C3C"    # Rojo
HIGHLIGHT_COLOR = "#F1C40F" # Amarillo
FONT_MAIN = ("Helvetica", 12)
FONT_BIG = ("Helvetica", 48, "bold")

COLUMN_RANGES = {
    0: range(1, 16),    # B
    1: range(16, 31),   # I
    2: range(31, 46),   # N
    3: range(46, 61),   # G
    4: range(61, 76)    # O
}

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
        self.server_socket = None
        self.clients = []
        self.clients_lock = threading.Lock()

        self.game_started = False
        self.all_numbers = list(range(1, 76))
        random.shuffle(self.all_numbers)
        self.current_index = -1
        self.drawn_balls = [] # Lista de (letra, numero)
        self.drawn_numbers_set = set() # Set para busqueda rápida O(1)
        self.winner = None

        self.gui_update = gui_callback_update
        self.gui_log = gui_callback_log

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
            # Handshake inicial
            data = conn.recv(1024).decode("utf-8").strip()
            if not data.startswith("JOIN "):
                conn.close()
                return
            
            name = data[5:].strip()
            player = Player(name, conn, addr)

            # Generar cartón
            player.card = self.generate_card()
            card_str = self.card_to_string(player.card)
            
            # Enviar cartón
            conn.sendall(f"CARD {card_str}\n".encode("utf-8"))

            with self.clients_lock:
                if not self.game_started:
                    self.clients.append(player)
                    self.gui_log(f"Jugador conectado: {name}")
                else:
                    conn.sendall(b"END Juego_en_progreso\n")
                    conn.close()
                    return

            # Bucle de escucha con Buffer
            while True:
                chunk = conn.recv(1024)
                if not chunk: break
                buffer += chunk.decode("utf-8")
                
                while "\n" in buffer:
                    msg, buffer = buffer.split("\n", 1)
                    self.process_message(player, msg.strip())

        except Exception as e:
            self.gui_log(f"Error con {addr}: {e}")
        finally:
            if player:
                with self.clients_lock:
                    if player in self.clients:
                        self.clients.remove(player)
                        self.gui_log(f"Jugador desconectado: {player.name}")
            conn.close()

    def process_message(self, player, msg):
        if msg.startswith("HIT "):
            # Formato: HIT fil,col
            try:
                parts = msg[4:].split(",")
                r, c = int(parts[0]), int(parts[1])
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

    def validate_hit(self, player, r, c):
        if 0 <= r < 5 and 0 <= c < 5:
            num = player.card[r][c]
            if num in self.drawn_numbers_set:
                player.marked[r][c] = True
                # self.gui_log(f"{player.name} marcó {num}") # (Verbose)
            else:
                self.gui_log(f"ALERTA: {player.name} intentó marcar {num} sin haber salido.")

    def generate_card(self):
        card_cols = []
        for col in range(5):
            numbers = random.sample(list(COLUMN_RANGES[col]), 5)
            card_cols.append(numbers)
        # Transponer para tener filas
        rows = [[card_cols[c][r] for c in range(5)] for r in range(5)]
        return rows

    def card_to_string(self, card):
        rows_str = []
        for row in card:
            row_str = ",".join(str(n) for n in row)
            rows_str.append(row_str)
        return ";".join(rows_str)

    def next_ball(self):
        if not self.game_started:
            self.game_started = True
            self.gui_log("Juego Iniciado")

        if self.current_index + 1 >= len(self.all_numbers) or self.winner:
            return

        self.current_index += 1
        number = self.all_numbers[self.current_index]
        letter = self.get_letter(number)
        
        self.drawn_balls.append((letter, number))
        self.drawn_numbers_set.add(number)
        
        # Actualizar GUI
        self.gui_update(letter, number)
        
        # Enviar a todos
        msg = f"BALL {letter},{number}\n"
        with self.clients_lock:
            for p in self.clients:
                try:
                    p.conn.sendall(msg.encode("utf-8"))
                except:
                    pass

    def get_letter(self, n):
        if 1 <= n <= 15: return "B"
        if 16 <= n <= 30: return "I"
        if 31 <= n <= 45: return "N"
        if 46 <= n <= 60: return "G"
        return "O"

    def check_bingo(self, player):
        m = player.marked
        # Filas y Columnas
        for i in range(5):
            if all(m[i][c] for c in range(5)): return True
            if all(m[r][i] for r in range(5)): return True
        # Diagonales
        if all(m[i][i] for i in range(5)): return True
        if all(m[i][4-i] for i in range(5)): return True
        return False

    def end_game(self, reason="Fin"):
        msg = f"END {reason}\n"
        with self.clients_lock:
            for p in self.clients:
                try:
                    p.conn.sendall(msg.encode("utf-8"))
                    p.conn.close()
                except: pass
            self.clients.clear()
        
        messagebox.showinfo("Juego Terminado", reason)
        if self.server_socket:
            try: self.server_socket.close()
            except: pass

class ServerGUI:
    def __init__(self, root):
        self.root = root
        self.root.title("Administrador de Bingo")
        self.root.geometry("500x600")
        self.root.configure(bg=BG_COLOR)

        # Header
        header = tk.Frame(root, bg=BG_COLOR)
        header.pack(pady=20)
        tk.Label(header, text="BALOTA ACTUAL", font=("Helvetica", 14), bg=BG_COLOR, fg=FG_COLOR).pack()
        
        self.lbl_ball = tk.Label(header, text="--", font=FONT_BIG, bg="white", fg="black", 
                                 width=4, height=2, relief="ridge", borderwidth=4)
        self.lbl_ball.pack(pady=10)

        # Controles
        controls = tk.Frame(root, bg=BG_COLOR)
        controls.pack(pady=10)
        
        self.btn_next = tk.Button(controls, text="SACAR BALOTA", font=("Helvetica", 14, "bold"), 
                                  bg=HIGHLIGHT_COLOR, fg="black", command=self.next_ball, padx=20, pady=10)
        self.btn_next.pack()

        # Historial
        tk.Label(root, text="Log del Juego:", bg=BG_COLOR, fg=FG_COLOR).pack(anchor="w", padx=20)
        self.log_list = tk.Listbox(root, height=10, bg="#34495E", fg="white", font=("Consolas", 10))
        self.log_list.pack(fill=tk.BOTH, expand=True, padx=20, pady=(0, 10))

        # Boton Salir
        tk.Button(root, text="TERMINAR JUEGO", bg=ACCENT_COLOR, fg="white", 
                  command=self.end_game).pack(pady=10, fill=tk.X, padx=20)

        self.server = BingoServer(HOST, PORT, self.update_display, self.log_msg)
        self.server.start_network()

    def next_ball(self):
        self.server.next_ball()

    def update_display(self, letter, number):
        color = "white"
        if letter == "B": color = "#FFCDD2" 
        if letter == "I": color = "#E1F5FE" 
        if letter == "N": color = "#FFF9C4" 
        if letter == "G": color = "#C8E6C9" 
        if letter == "O": color = "#FFE0B2" 
        
        self.lbl_ball.config(text=f"{letter}\n{number}", bg=color)
        self.log_msg(f"Salió: {letter}-{number}")

    def log_msg(self, msg):
        self.log_list.insert(tk.END, msg)
        self.log_list.see(tk.END)

    def end_game(self):
        self.server.end_game("Terminado por Admin")
        self.root.quit()

if __name__ == "__main__":
    root = tk.Tk()
    app = ServerGUI(root)
    root.mainloop()