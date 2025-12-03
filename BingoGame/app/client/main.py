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
                btn = tk.Button(frame, text="0", width=4, height=2, state=tk.DISABLED,
                                command=lambda rr=r, cc=c: self.mark(rr, cc))
                btn.grid(row=r + 1, column=c)
                row.append(btn)
            self.buttons.append(row)

        self.lbl_info = tk.Label(self.master, text="Esperando...", font=("Arial", 14))
        self.lbl_info.pack(pady=10)

        self.btn_bingo = tk.Button(self.master, text="BINGO!", bg="purple", fg="white", state=tk.DISABLED,
                                   command=self.send_bingo)
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
                self.buttons[r][c].config(text=self.card[r][c], state=tk.NORMAL)
        self.btn_bingo.config(state=tk.NORMAL)

    def mark(self, r, c):
        self.marked[r][c] = not self.marked[r][c]
        color = "#FFF176" if self.marked[r][c] else "white"
        self.buttons[r][c].config(bg=color)
        self.sock.sendall(f"HIT {r},{c}\n".encode())

    def send_bingo(self):
        self.sock.sendall(f"BINGO {self.player_name}\n".encode())

    def listen(self):
        buff = ""
        while self.running:
            try:
                chunk = self.sock.recv(1024)
                if not chunk: break
                buff += chunk.decode()
                while "\n" in buff:
                    msg, buff = buff.split("\n", 1)
                    if msg.startswith("BALL "):
                        self.lbl_info.config(text=f"Balota: {msg[5:]}")
                    elif msg.startswith("END "):
                        messagebox.showinfo("Fin", msg[4:])
                        self.running = False;
                        self.master.quit()
            except:
                break


if __name__ == "__main__":
    name = sys.argv[1] if len(sys.argv) > 1 else f"Jugador_{random.randint(100, 999)}"
    root = tk.Tk()
    app = BingoClient(root, auto_name=name)
    root.mainloop()