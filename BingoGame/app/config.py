# app/config.py

HOST = "127.0.0.1"
PORT = 5000

# Colores de la UI
BG_COLOR = "#2C3E50"  # Azul oscuro (Fondo general)
FG_COLOR = "#ECF0F1"  # Blanco (Texto)
ACCENT_COLOR = "#E74C3C"  # Rojo (Botones importantes)
HIGHLIGHT_COLOR = "#F1C40F"  # Amarillo (Resaltado)

# Colores de las letras BINGO
BINGO_COLORS = {
    "B": "#E74C3C",  # Rojo
    "I": "#3498DB",  # Azul
    "N": "#F1C40F",  # Amarillo
    "G": "#2ECC71",  # Verde
    "O": "#E67E22"  # Naranja
}

# Rangos de números
COLUMN_RANGES = {
    0: range(1, 16),
    1: range(16, 31),
    2: range(31, 46),
    3: range(46, 61),
    4: range(61, 76)
}
