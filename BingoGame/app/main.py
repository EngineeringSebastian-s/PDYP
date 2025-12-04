import os
import subprocess
import sys
import time


def main():
    print("--- INICIANDO SISTEMA BINGO ---")

    base_dir = os.path.dirname(os.path.abspath(__file__))
    server_script = os.path.join(base_dir, "server", "main.py")
    client_script = os.path.join(base_dir, "client", "main.py")

    processes = []

    try:
        print("Lanzando Servidor...")
        p_server = subprocess.Popen([sys.executable, server_script])
        processes.append(p_server)

        time.sleep(2)

        nombres = ["Sebastian", "Esteban", "Profesor","Camilo","Ana","Maria"]

        for nombre in nombres:
            print(f"Lanzando Cliente para {nombre}...")
            p_client = subprocess.Popen([sys.executable, client_script, nombre])
            processes.append(p_client)
            time.sleep(0.5)

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