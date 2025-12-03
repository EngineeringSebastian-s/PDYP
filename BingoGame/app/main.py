import subprocess
import sys
import time
import os


def main():
    print("--- INICIANDO SISTEMA BINGO ---")

    # Rutas a los archivos (usando rutas relativas desde donde se ejecuta)
    base_dir = os.path.dirname(os.path.abspath(__file__))
    server_script = os.path.join(base_dir, "server", "main.py")
    client_script = os.path.join(base_dir, "client", "main.py")

    processes = []

    try:
        # 1. Iniciar Servidor
        print("Lanzando Servidor...")
        # sys.executable asegura que usemos el mismo python del entorno virtual
        p_server = subprocess.Popen([sys.executable, server_script])
        processes.append(p_server)

        # Esperar un momento a que el servidor levante el socket
        time.sleep(2)

        # 2. Iniciar 3 Clientes
        nombres = ["Sebastian", "Esteban", "Profesor","Camilo","Ana","Maria"]

        for nombre in nombres:
            print(f"Lanzando Cliente para {nombre}...")
            # Pasamos el nombre como argumento al script
            p_client = subprocess.Popen([sys.executable, client_script, nombre])
            processes.append(p_client)
            time.sleep(0.5)  # Pequeña pausa para que no se solapen las ventanas al abrir

        print("--- TODO CORRIENDO ---")
        print("Cierra las ventanas del juego para terminar los procesos.")

        # Esperar a que el servidor se cierre para matar todo
        p_server.wait()

    except KeyboardInterrupt:
        print("Interrupción detectada, cerrando...")
    finally:
        # Asegurar que todos los procesos mueran al salir
        print("Limpiando procesos...")
        for p in processes:
            if p.poll() is None:  # Si sigue vivo
                p.terminate()


if __name__ == "__main__":
    main()