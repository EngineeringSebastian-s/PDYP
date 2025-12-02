import socket
import threading
import tkinter as tk
from tkinter import simpledialog, messagebox

HOST = "127.0.0.1"
PORT = 5000

COLORS = {
    "B": "#E74C3C", # Rojo
    "I": "#3498DB", # Azul
    "N": "#F1C40F", # Amarillo
    "G": "#2ECC71", # Verde
    "O": "#E67E22"  # Naranja
}
BG_APP = "#ECF0F1"

class BingoClient:
    def __init__(self, master):
        self.master = master
        self.master.title("Cliente Bingo")
        self.master.configure(bg=BG_APP)
        self.master.geometry("400x550")

        self.sock = None
        self.card = []
        self.marked = []
        self.buttons = []
        self.player_name = ""
        self.running = True

        self.current_call_var = tk.StringVar(value="Esperando...")

        self.build_ui()
        self.ask_name_and_connect()

    def build_ui(self):
        # Header Info
        top_frame = tk.Frame(self.master, bg=BG_APP)
        top_frame.pack(pady=15)
        
        tk.Label(top_frame, text="ÚLTIMA BALOTA:", font=("Helvetica", 10), bg=BG_APP).pack()
        self.lbl_call = tk.Label(top_frame, textvariable=self.current_call_var, 
                                 font=("Helvetica", 24, "bold"), bg="white", width=8, relief="solid")
        self.lbl_call.pack(pady=5)

        # GRID BINGO
        grid_frame = tk.Frame(self.master, bg="black", bd=2)
        grid_frame.pack(pady=10, padx=20)

        headers = ["B", "I", "N", "G", "O"]
        for i, h in enumerate(headers):
            lbl = tk.Label(grid_frame, text=h, bg=COLORS[h], fg="white", 
                           font=("Arial", 14, "bold"), width=4, height=1)
            lbl.grid(row=0, column=i, sticky="nsew", padx=1, pady=1)

        self.buttons = []
        for r in range(5):
            row_btns = []
            for c in range(5):
                btn = tk.Button(grid_frame, text="", font=("Arial", 12, "bold"),
                                width=4, height=2, bg="white", state=tk.DISABLED,
                                command=lambda rr=r, cc=c: self.mark_spot(rr, cc))
                btn.grid(row=r+1, column=c, padx=1, pady=1)
                row_btns.append(btn)
            self.buttons.append(row_btns)

        # Boton BINGO
        self.btn_bingo = tk.Button(self.master, text="¡ B I N G O !", bg="#8E44AD", fg="white",
                                   font=("Arial", 16, "bold"), state=tk.DISABLED,
                                   command=self.send_bingo)
        self.btn_bingo.pack(pady=20, fill=tk.X, padx=40)

    def ask_name_and_connect(self):
        self.master.after(100, self._connect_logic)

    def _connect_logic(self):
        name = simpledialog.askstring("Bienvenido", "Ingresa tu nombre para jugar:")
        if not name:
            self.master.quit()
            return
        self.player_name = name
        self.connect_server()

    def connect_server(self):
        try:
            self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.sock.connect((HOST, PORT))
            
            # Enviar JOIN
            self.sock.sendall(f"JOIN {self.player_name}\n".encode("utf-8"))
            
            # Recibir respuesta inicial (CARD)
            data = self.sock.recv(4096).decode("utf-8").strip()
            
            if data.startswith("CARD "):
                self.setup_card(data[5:])
                threading.Thread(target=self.listen_server, daemon=True).start()
            elif data.startswith("END"):
                messagebox.showerror("Error", "El juego ya está en progreso.")
                self.master.quit()
            else:
                messagebox.showerror("Error", "Respuesta extraña del servidor.")
                self.master.quit()

        except Exception as e:
            messagebox.showerror("Error de Conexión", f"No se pudo conectar: {e}")
            self.master.quit()

    def setup_card(self, card_str):
        # Parsear cartón
        rows = card_str.split(";")
        self.card = []
        for r_str in rows:
            self.card.append([int(x) for x in r_str.split(",")])
        
        self.marked = [[False]*5 for _ in range(5)]
        
        # Llenar UI
        for r in range(5):
            for c in range(5):
                num = self.card[r][c]
                self.buttons[r][c].config(text=str(num), state=tk.NORMAL)
        
        self.btn_bingo.config(state=tk.NORMAL)

    def mark_spot(self, r, c):
        # Lógica visual y envio de HIT
        current_state = self.marked[r][c]
        new_state = not current_state
        self.marked[r][c] = new_state
        
        if new_state:
            self.buttons[r][c].config(bg="#FFF176") # Amarillo claro al marcar
            # Enviar al servidor para validación
            self.send_msg(f"HIT {r},{c}")
        else:
            self.buttons[r][c].config(bg="white")

    def send_msg(self, msg):
        if self.sock:
            try:
                self.sock.sendall(f"{msg}\n".encode("utf-8"))
            except: pass

    def send_bingo(self):
        self.send_msg(f"BINGO {self.player_name}")

    def listen_server(self):
        buffer = ""
        while self.running and self.sock:
            try:
                chunk = self.sock.recv(1024)
                if not chunk: break
                buffer += chunk.decode("utf-8")

                while "\n" in buffer:
                    msg, buffer = buffer.split("\n", 1)
                    msg = msg.strip()
                    if msg:
                        self.process_server_msg(msg)
            except:
                break
        
    def process_server_msg(self, msg):
        if msg.startswith("BALL "):
            # BALL B,15
            parts = msg[5:].split(",")
            letter, number = parts[0], parts[1]
            self.master.after(0, lambda: self.current_call_var.set(f"{letter} - {number}"))
        

        elif msg.startswith("END "):
            reason = msg[4:]
            self.master.after(0, lambda: messagebox.showinfo("Juego Terminado", reason))
            self.running = False
            self.master.after(0, self.master.quit)

if __name__ == "__main__":
    root = tk.Tk()
    app = BingoClient(root)
    root.mainloop()