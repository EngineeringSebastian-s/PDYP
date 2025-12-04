import math
import os
import subprocess
import sys
import time
import tkinter as tk


def main():
    print("--- INICIANDO SISTEMA BINGO ---")

    base_dir = os.path.dirname(os.path.abspath(__file__))
    server_script = os.path.join(base_dir, "server", "main.py")
    client_script = os.path.join(base_dir, "client", "main.py")

    root = tk.Tk()
    root.withdraw()
    screen_width = root.winfo_screenwidth()
    screen_height = root.winfo_screenheight()
    root.destroy()

    processes = []

    try:
        print("Lanzando Servidor...")
        p_server = subprocess.Popen([sys.executable, server_script])
        processes.append(p_server)

        time.sleep(2)

        nombres = [
            "Santos",
            "Uribe",
            "Pastrana",
            "Betancur",
            "Barco",
            "Lleras",
            "Gaviria",
            "Turbay",
            "Ospina",
            "Valencia",
            "Gómez",
            "Rojas",
            "Samper",
            "Duque",
            "Petro",
            "Pérez",
            "Restrepo",
            "Pumarejo",
            "Holguín",
            "Suárez"
        ]
        n = len(nombres)

        cols = math.ceil(math.sqrt(n))
        rows = math.ceil(n / cols)

        margin = 20
        window_width = int((screen_width - (margin * (cols + 1))) / cols)
        window_height = int((screen_height - (margin * (rows + 1))) / rows)

        positions = []
        for i in range(n):
            col = i % cols
            row = i // cols
            x = margin + col * (window_width + margin)
            y = margin + row * (window_height + margin)
            positions.append((x, y))

        for (nombre, (x, y)) in zip(nombres, positions):
            print(f"Lanzando {nombre} en {x},{y} tamaño {window_width}x{window_height}")
            p_client = subprocess.Popen([
                sys.executable,
                client_script,
                nombre,
                str(x),
                str(y),
                str(window_width),
                str(window_height)
            ])
            processes.append(p_client)
            time.sleep(0.2)

        print("--- TODO CORRIENDO ---")
        print("Cierra las ventanas del juego para terminar los procesos.")

        p_server.wait()

    except KeyboardInterrupt:
        print("Interrupción detectada, cerrando...")
    finally:
        print("Limpiando procesos...")
        for p in processes:
            if p.poll() is None:
                p.terminate()


if __name__ == "__main__":
    main()