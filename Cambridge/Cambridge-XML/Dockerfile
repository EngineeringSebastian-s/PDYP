# Usamos una imagen ligera de Python
FROM python:3.9-slim

# Evita que Python genere archivos .pyc y permite ver los logs en tiempo real
ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos las dependencias e instalamos
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copiamos el código fuente (asumiendo que tu archivo está en una carpeta 'app')
# Esto copiará el contenido de tu carpeta local 'app' a '/app' en el contenedor
COPY app/ .

# Exponemos el puerto 5000
EXPOSE 8094

# Comando para ejecutar la aplicación
# IMPORTANTE: Ver nota abajo sobre el host 0.0.0.0
CMD ["python", "main.py"]