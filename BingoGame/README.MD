# BingoGame -- Sistema Cliente/Servidor en Python

Este proyecto implementa un **sistema completo de Bingo** usando Python,
sockets y una interfaz gráfica con Tkinter.\
Permite levantar un servidor que gestiona balotas, jugadores, validación
de bingo y reglas; y múltiples clientes que reciben su cartón y
participan en tiempo real.

------------------------------------------------------------------------

## Características principales

-   Servidor de Bingo con manejo de:
    -   Generación de balotas (ejemplo: `B,9`).
    -   Validación de marcaciones (`HIT r,c`).
    -   Validación de Bingo.
    -   Cierre de partida.
-   Clientes gráficos (Tkinter) con:
    -   Cartón aleatorio.
    -   Marcación automática cuando llega una balota.
    -   Botón de **BINGO**.
    -   Comunicación por sockets.
-   Script principal que:
    -   Inicia el servidor.
    -   Abre varios clientes automáticamente.

------------------------------------------------------------------------

## Requisitos

-   Python 3.10 o superior.
-   Paquetes definidos en `requirements.txt`\
    *(Tkinter viene incluido con la instalación estándar de Python en
    Windows/Mac.)*

Instalar dependencias:

``` bash
pip install -r requirements.txt
```

------------------------------------------------------------------------

## Estructura del proyecto

La estructura del proyecto es la siguiente:

    BingoGame/
    │
    ├── app/
    │   ├── client/
    │   │   └── main.py        # Cliente gráfico del bingo
    │   │
    │   ├── server/
    │   │   └── main.py        # Servidor del bingo (socket server)
    │   │
    │   ├── config.py          # Configuración general (host, puerto, colores)
    │   └── __init__.py
    │
    ├── main.py                # Lanza el servidor y varios clientes automáticamente
    ├── requirements.txt
    └── README.md              # (este archivo)

------------------------------------------------------------------------

## ▶Ejecución

###  1. Iniciar todo el sistema automáticamente

Desde la carpeta raíz:

``` bash
python main.py
```

Este archivo:

-   Inicia el servidor del Bingo.
-   Abre varios clientes (nombres simulados).
-   Cada cliente recibe un cartón y escucha balotas automáticas.

------------------------------------------------------------------------

###  2. Ejecutar el servidor manualmente

``` bash
python app/server/main.py
```

###  3. Ejecutar un cliente manualmente

``` bash
python app/client/main.py TU_NOMBRE
```

Ejemplo:

``` bash
python app/client/main.py Esteban
```

------------------------------------------------------------------------

## Configuración

La configuración del host, puerto y colores del encabezado BINGO está
en:

    app/config.py

Ejemplo común:

``` python
HOST = "127.0.0.1"
PORT = 5000
BINGO_COLORS = {
    "B": "#f44336",
    "I": "#ff9800",
    "N": "#ffeb3b",
    "G": "#4caf50",
    "O": "#2196f3"
}
```

------------------------------------------------------------------------

## Flujo del Sistema

1.  El servidor genera todas las balotas del Bingo.

2.  Cada cliente recibe un cartón.

3.  El servidor envía balotas en formato:

        BALL B,9

4.  Cada cliente:

    -   Muestra la balota en pantalla.

    -   Identifica si el número existe en su cartón.

    -   Marca automáticamente.

    -   Notifica al servidor con:

            HIT r,c

5.  Si un cliente presiona **BINGO**, envía:

        BINGO NombreJugador

6.  El servidor valida.

7.  Si el bingo es real → anuncia al ganador.\
    Si es falso → envía alerta al jugador.

------------------------------------------------------------------------

## Mejoras futuras sugeridas

-   Persistencia de ganadores en archivo JSON o SQLite.
-   Animación visual de balotas.
-   Interfaz mejorada con customtkinter o PyQt.
-   Partidas remotas por internet (actualmente local LAN).
-   Sala de espera y múltiples partidas en simultáneo.

------------------------------------------------------------------------

## Licencia

Proyecto libre para uso educativo y ampliación.

------------------------------------------------------------------------

## Autor

Desarrollado para propósitos académicos y de práctica en sistemas
distribuidos, sockets y GUI en Python.
