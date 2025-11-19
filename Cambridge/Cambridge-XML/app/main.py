from flask import Flask, request, Response
import requests
import xmltodict
import json

app = Flask(__name__)

# URL de tu API Backend original
BACKEND_URL = "http://localhost:8080"


# --- FUNCIONES AUXILIARES DE CONVERSIÓN ---

def xml_to_json_payload(xml_data):
    """Convierte XML string a Diccionario Python (para enviar como JSON)."""
    if not xml_data:
        return None
    try:
        # Parseamos el XML
        parsed = xmltodict.parse(xml_data)
        # Obtenemos la raíz (ej: <SalonDTO>...</SalonDTO>) -> {'SalonDTO': {...}}
        root_key = list(parsed.keys())[0]
        # Retornamos solo el contenido interno para el JSON
        return parsed[root_key]
    except Exception as e:
        print(f"Error parseando XML request: {e}")
        return {}


def json_to_xml_response(json_data, root_tag="Response"):
    """Convierte Diccionario Python (respuesta JSON) a XML string."""
    try:
        # Si es una lista (ej: respuesta de listar salones), la envolvemos
        if isinstance(json_data, list):
            # xmltodict necesita un objeto raíz, no una lista directa
            # Creamos una estructura: <root_tag><item>...</item><item>...</item></root_tag>
            wrapper = {root_tag: {"item": json_data}}
            return xmltodict.unparse(wrapper, pretty=True)
        else:
            # Si es un objeto único
            return xmltodict.unparse({root_tag: json_data}, pretty=True)
    except Exception as e:
        return xmltodict.unparse({"error": "Error procesando respuesta JSON"}, pretty=True)


def ejecutar_peticion(method, endpoint, root_response_tag="Response"):
    """
    Orquestador central:
    1. Recibe XML
    2. Transforma a JSON
    3. Llama al Backend
    4. Recibe JSON
    5. Responde XML
    """
    url = f"{BACKEND_URL}{endpoint}"
    payload = None

    # 1. Procesar Body (Si es POST o PUT)
    if method in ['POST', 'PUT'] and request.data:
        payload = xml_to_json_payload(request.data)

    headers = {'Content-Type': 'application/json'}

    try:
        # 2. Petición al Backend
        response = requests.request(method, url, json=payload, headers=headers)

        # 3. Procesar Respuesta
        if response.status_code == 204:
            return Response("", status=204)

        if response.content:
            try:
                response_json = response.json()
                # Convertimos el JSON del backend a XML
                xml_resp = json_to_xml_response(response_json, root_tag=root_response_tag)
                return Response(xml_resp, status=response.status_code, mimetype='application/xml')
            except Exception:
                # Si el backend devuelve error en texto plano
                return Response(f"<error>{response.text}</error>", status=response.status_code,
                                mimetype='application/xml')

        return Response("", status=response.status_code)

    except requests.exceptions.ConnectionError:
        return Response("<error>No se pudo conectar con el Backend en puerto 8080</error>", status=503,
                        mimetype='application/xml')


# --- DEFINICIÓN DE ENDPOINTS (MAPEO 1 a 1) ---

# ================= SALONES =================
@app.route('/api/salones', methods=['GET'])
def listar_salones():
    return ejecutar_peticion('GET', '/api/salones', root_response_tag='ListaSalones')


@app.route('/api/salones', methods=['POST'])
def crear_salon():
    # Espera XML: <SalonDTO><codigo>...</codigo></SalonDTO>
    return ejecutar_peticion('POST', '/api/salones', root_response_tag='SalonDTO')


@app.route('/api/salones/<int:id>', methods=['GET'])
def obtener_salon(id):
    return ejecutar_peticion('GET', f'/api/salones/{id}', root_response_tag='SalonDTO')


@app.route('/api/salones/<int:id>', methods=['PUT'])
def actualizar_salon(id):
    return ejecutar_peticion('PUT', f'/api/salones/{id}', root_response_tag='SalonDTO')


@app.route('/api/salones/<int:id>', methods=['DELETE'])
def eliminar_salon(id):
    return ejecutar_peticion('DELETE', f'/api/salones/{id}')


# ================= OFICINAS =================
@app.route('/api/oficinas', methods=['GET'])
def listar_oficinas():
    return ejecutar_peticion('GET', '/api/oficinas', root_response_tag='ListaOficinas')


@app.route('/api/oficinas', methods=['POST'])
def crear_oficina():
    # Espera XML: <OficinaDTO><codigo>...</codigo><areaId>...</areaId></OficinaDTO>
    return ejecutar_peticion('POST', '/api/oficinas', root_response_tag='OficinaDTO')


@app.route('/api/oficinas/<int:id>', methods=['GET'])
def obtener_oficina(id):
    return ejecutar_peticion('GET', f'/api/oficinas/{id}', root_response_tag='OficinaDTO')


@app.route('/api/oficinas/<int:id>', methods=['PUT'])
def actualizar_oficina(id):
    return ejecutar_peticion('PUT', f'/api/oficinas/{id}', root_response_tag='OficinaDTO')


@app.route('/api/oficinas/<int:id>', methods=['DELETE'])
def eliminar_oficina(id):
    return ejecutar_peticion('DELETE', f'/api/oficinas/{id}')


# ================= EMPLEADOS =================
@app.route('/api/empleados', methods=['GET'])
def listar_empleados():
    return ejecutar_peticion('GET', '/api/empleados', root_response_tag='ListaEmpleados')


@app.route('/api/empleados', methods=['POST'])
def crear_empleado():
    # Espera XML: <EmpleadoCreateDTO><nombre>...</nombre>...</EmpleadoCreateDTO>
    return ejecutar_peticion('POST', '/api/empleados', root_response_tag='EmpleadoDTO')


@app.route('/api/empleados/<int:id>', methods=['GET'])
def obtener_empleado(id):
    return ejecutar_peticion('GET', f'/api/empleados/{id}', root_response_tag='EmpleadoDTO')


@app.route('/api/empleados/<int:id>', methods=['PUT'])
def actualizar_empleado(id):
    return ejecutar_peticion('PUT', f'/api/empleados/{id}', root_response_tag='EmpleadoDTO')


@app.route('/api/empleados/<int:id>', methods=['DELETE'])
def eliminar_empleado(id):
    return ejecutar_peticion('DELETE', f'/api/empleados/{id}')


# ================= AREAS =================
@app.route('/api/areas', methods=['GET'])
def listar_areas():
    return ejecutar_peticion('GET', '/api/areas', root_response_tag='ListaAreas')


@app.route('/api/areas', methods=['POST'])
def crear_area():
    return ejecutar_peticion('POST', '/api/areas', root_response_tag='AreaDTO')


@app.route('/api/areas/<int:id>', methods=['GET'])
def obtener_area(id):
    return ejecutar_peticion('GET', f'/api/areas/{id}', root_response_tag='AreaDTO')


@app.route('/api/areas/<int:id>', methods=['PUT'])
def actualizar_area(id):
    return ejecutar_peticion('PUT', f'/api/areas/{id}', root_response_tag='AreaDTO')


@app.route('/api/areas/<int:id>', methods=['DELETE'])
def eliminar_area(id):
    return ejecutar_peticion('DELETE', f'/api/areas/{id}')


# ================= REPORTES =================
@app.route('/api/reportes/areas-empleados', methods=['GET'])
def reporte_areas_empleados():
    return ejecutar_peticion('GET', '/api/reportes/areas-empleados', root_response_tag='ReporteAreas')


if __name__ == '__main__':
    # Corremos en puerto 5000 para dejar libre el 8080 del backend
    print("Iniciando API Intermedia XML <-> JSON en http://localhost:5000")
    app.run(debug=True, port=5000)