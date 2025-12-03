import socket
import threading
import tkinter as tk
from tkinter import messagebox
import sys
import os
import random

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..')))
from app.config import HOST, PORT, BINGO_COLORS


class BingoClient:
    def __init__(self, master, auto_name=None):
        self.master = master
        self.master.title(f"Cliente: {auto_name}" if auto_name else "Bingo")
        self.sock = None
        self.card = []
        self.marked = [[False] * 5 for _ in range(5)]
        self.buttons = []
        self.player_name = auto_name
        self.running = True

        self.build_ui()
        if self.player_name:
            self.connect_server()
        else:
            # Si no hay nombre automático, preguntar (lógica antigua)
            pass

    def build_ui(self):
        frame = tk.Frame(self.master)
        frame.pack(pady=10, padx=10)

        for i, h in enumerate(["B", "I", "N", "G", "O"]):
            tk.Label(frame, text=h, bg=BINGO_COLORS[h], width=4).grid(row=0, column=i)

        self.buttons = []
        for r in range(5):
            row = []
            for c in range(5):
                btn = tk.Button(
                    frame,
                    text="0",
                    width=4,
                    height=2,
                    state=tk.DISABLED,
                    command=lambda rr=r, cc=c: self.mark(rr, cc),
                )
                btn.grid(row=r + 1, column=c)
                row.append(btn)
            self.buttons.append(row)

        self.lbl_info = tk.Label(self.master, text="Esperando...", font=("Arial", 14))
        self.lbl_info.pack(pady=10)

        self.btn_bingo = tk.Button(
            self.master,
            text="BINGO!",
            bg="purple",
            fg="white",
            state=tk.DISABLED,
            command=self.send_bingo,
        )
        self.btn_bingo.pack(fill=tk.X, padx=20, pady=10)

    def connect_server(self):
        try:
            self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.sock.connect((HOST, PORT))
            self.sock.sendall(f"JOIN {self.player_name}\n".encode())

            data = self.sock.recv(4096).decode().strip()
            if data.startswith("CARD "):
                self.setup_card(data[5:])
                threading.Thread(target=self.listen, daemon=True).start()
        except Exception as e:
            messagebox.showerror("Error", str(e))

    def setup_card(self, s):
        rows = s.split(";")
        self.card = [[int(x) for x in r.split(",")] for r in rows]
        for r in range(5):
            for c in range(5):
                self.buttons[r][c].config(text=self.card[r][c], state=tk.NORMAL, bg="white")
        self.btn_bingo.config(state=tk.NORMAL)

    def mark(self, r, c):
        # Toggle marcado
        self.marked[r][c] = not self.marked[r][c]
        color = "#FFF176" if self.marked[r][c] else "white"
        self.buttons[r][c].config(bg=color)

        # Enviar al servidor (si socket válido)
        try:
            if self.sock:
                self.sock.sendall(f"HIT {r},{c}\n".encode())
        except Exception:
            # Ignorar errores de envío (por simplicidad)
            pass

    def send_bingo(self):
        try:
            if self.sock:
                self.sock.sendall(f"BINGO {self.player_name}\n".encode())
        except Exception:
            pass

    def find_number(self, num):
        for r in range(5):
            for c in range(5):
                try:
                    if self.card[r][c] == num:
                        return r, c
                except Exception:
                    pass
        return None

    def on_ball(self, letter, number):
        if number is None:
            # Mostrar solo la letra si no hay número (caso improbable)
            self.lbl_info.config(text=f"Balota: {letter or '--'}")
            return

        # Actualizar etiqueta de información
        self.lbl_info.config(text=f"Balota: {letter}-{number}")

        # Buscar número en el cartón y marcar si existe y no está marcado
        pos = self.find_number(number)
        if pos:
            r, c = pos
            if not self.marked[r][c]:
                # Llamamos a mark (está diseñado para correr en hilo UI)
                self.mark(r, c)

    def listen(self):
        """
        Escucha mensajes del servidor. Asumimos que las balotas llegan exactamente en formato:
        'BALL B,9' (con coma entre letra y número). No se 'parsea' en forma compleja,
        solo se separa por la coma.
        """
        buff = ""
        while self.running:
            try:
                chunk = self.sock.recv(1024)
                if not chunk:
                    break
                buff += chunk.decode()
                while "\n" in buff:
                    msg, buff = buff.split("\n", 1)
                    msg = msg.strip()
                    if not msg:
                        continue

                    if msg.startswith("BALL "):
                        # Aquí sabemos que el payload es "B,9"
                        payload = msg[5:].strip()  # e.g. "B,9"
                        # Separar por coma -> letter, num_str
                        try:
                            letter_part, num_part = payload.split(",", 1)
                            letter = letter_part.strip().upper()
                            number = int(num_part.strip())
                        except Exception:
                            # Si por alguna razón no se puede convertir, ignorar la balota
                            letter = None
                            number = None

                        # Programar la actualización en el hilo principal de Tkinter
                        try:
                            self.master.after(0, lambda L=letter, N=number: self.on_ball(L, N))
                        except Exception:
                            # Fallback directo si after falla (no recomendado, pero seguro)
                            try:
                                self.on_ball(letter, number)
                            except Exception:
                                pass

                    elif msg.startswith("END "):
                        reason = msg[4:].strip()
                        def end_proc():
                            messagebox.showinfo("Fin", reason)
                            self.running = False
                            try:
                                self.master.quit()
                            except:
                                pass
                        try:
                            self.master.after(0, end_proc)
                        except:
                            end_proc()
            except Exception:
                break

        # Cierre limpio cuando se sale del bucle
        try:
            if self.sock:
                self.sock.close()
        except:
            pass


if __name__ == "__main__":
    name = sys.argv[1] if len(sys.argv) > 1 else f"Jugador_{random.randint(100, 999)}"
    root = tk.Tk()
    app = BingoClient(root, auto_name=name)
    root.mainloop()