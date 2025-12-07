# Portafolio de Programación Distribuida y Paralela

Este repositorio compila los proyectos desarrollados para la asignatura de **Programación Distribuida y Paralela**, explorando diferentes paradigmas de comunicación entre procesos, arquitecturas multicapa y computación concurrente.

**Autor:** Sebastián López Osorno
**Institución:** Politécnico Colombiano Jaime Isaza Cadavid

-----

## 📂 Arquitectura del Portafolio

El repositorio se divide en 3 módulos principales, cada uno abordando un desafío específico de los sistemas distribuidos:

### 1\. Comunicación Síncrona y Concurrencia (Sockets)

Ubicación: `/BingoGame`

  * **🕹️ Bingo Multiplayer:**
      * **Concepto:** Sistema distribuido basado en arquitectura **Cliente-Servidor**.
      * **Tecnología:** Python (Sockets TCP/IP, `threading`, `tkinter`).
      * **Desafío Técnico:** Sincronización de múltiples hilos (clientes) en tiempo real, gestión de estado compartido en el servidor y protocolo de comunicación personalizado (`BALL`, `HIT`, `BINGO`).

### 2\. Interoperabilidad y Servicios Web

Ubicación: `/Cambridge`

  * **🏫 Sistema de Gestión Escolar:**
      * **Concepto:** Arquitectura orientada a servicios (SOA) moderna con frontend desacoplado.
      * **Stack Tecnológico:**
        1.  **Backend Core:** Java Spring Boot (JPA/Hibernate, H2) exponiendo API REST.
        2.  **API Gateway / Wrapper:** Python (FastAPI + Strawberry) sirviendo como capa de **GraphQL** sobre REST.
        3.  **Frontend:** React + Vite consumiendo los servicios.
      * **Desafío Técnico:** Implementación de CRUDs complejos, integración de sistemas heterogéneos (Java/Python) y exposición de datos mediante esquemas GraphQL.

[Image of GraphQL vs REST architecture diagram]

### 3\. Computación Remota (RMI)

Ubicación: `/Sudoku`

  * **🧩 Sudoku Solver:**
      * **Concepto:** Invocación de Métodos Remotos (Java RMI).
      * **Tecnología:** Java Spring Boot + Thymeleaf.
      * **Desafío Técnico:** El "motor" de resolución del Sudoku (algoritmo de **Backtracking**) se expone como un servicio RMI. El controlador web actúa como cliente RMI, delegando la carga computacional al servicio remoto.

-----

## 🛠️ Stack Tecnológico

| Componente | Tecnologías |
| :--- | :--- |
| **Lenguajes** | Java 21, Python 3.10+, JavaScript (ES6+) |
| **Frameworks Backend** | Spring Boot 3.5, FastAPI (Python) |
| **Frontend** | React (Vite), Thymeleaf, Tkinter (Desktop) |
| **Protocolos** | TCP Sockets, HTTP/REST, GraphQL, Java RMI |
| **Persistencia** | H2 Database (In-Memory) |

-----

## 🚀 Guía de Ejecución

Asegúrate de tener instalados: **Java JDK 21**, **Python 3.10+** y **Node.js**.

### 🔹 Proyecto 1: Bingo (Sockets)

Este proyecto incluye un orquestador que lanza el servidor y múltiples clientes automáticamente.

```bash
# Desde la raíz del repositorio
pip install -r BingoGame/requirements.txt
python BingoGame/app/main.py
```

### 🔹 Proyecto 2: Cambridge (Full Stack)

Este sistema requiere levantar 3 terminales distintas:

**Terminal 1: Backend (Java)**

```bash
cd Cambridge/Cambridge-API
./mvnw spring-boot:run
# API REST corriendo en http://localhost:8080
```

**Terminal 2: GraphQL Gateway (Python)**

```bash
cd Cambridge/Cambridge-GraphQL
pip install -r requirements.txt
python app/main.py
# GraphQL Playground en http://127.0.0.1:8081/graphql
```

**Terminal 3: Frontend (React)**

```bash
cd Cambridge/Cambridge-Web
npm install
npm run dev
# Web App en http://localhost:5173
```

### 🔹 Proyecto 3: Sudoku (RMI)

El servicio RMI y el servidor web se inician juntos en este contenedor Spring Boot.

```bash
cd Sudoku
./mvnw spring-boot:run
```

  * Acceder al navegador: `http://localhost:8080`
  * *Nota:* El servicio RMI se registrará automáticamente en el puerto `1099`.

-----

## 📄 Licencia

Este portafolio es de carácter académico.
Dado que este repositorio mezcla **Java (Maven)**, **Python (Virtualenvs/Caché)** y **Node.js (node\_modules)**, es muy propenso a ensuciarse con archivos temporales si no tienes cuidado.

**¿Te gustaría que te genere un archivo `.gitignore` unificado y robusto que cubra las tres tecnologías para ponerlo en la raíz?** Así evitarás subir binarios compilados o carpetas pesadas por error.
