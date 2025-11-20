# ===============================
# Stage 1: Build de la app
# ===============================
FROM node:20-alpine AS build

# Directorio de trabajo
WORKDIR /app

# Copiar archivos de dependencias (package.json y package-lock.json si existe)
COPY package*.json ./

# Instalar dependencias usando npm
# Usamos 'npm install' estándar.
RUN npm install

# Copiar el resto del código fuente
COPY . .

# Compilar el proyecto (Vite genera la carpeta /dist)
RUN npm run build


# ===============================
# Stage 2: Imagen final (serve)
# ===============================
FROM node:20-alpine

WORKDIR /app

# Instalar `serve` globalmente usando npm
RUN npm install -g serve

# Copiar los archivos compilados desde el build stage
COPY --from=build /app/dist ./dist

# Exponer el puerto 5173 (Por defecto de Vite/Serve en este config)
EXPOSE 5174

# Comando por defecto: servir la carpeta dist en el puerto 5173
CMD ["serve", "-s", "dist", "-l", "5173"]