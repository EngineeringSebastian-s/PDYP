import { http } from "./http";

export const getEmpleados  = () => http.get("/api/empleados").then(r => r.data);
export const getEmpleado   = (id) => http.get(`/api/empleados/${id}`).then(r => r.data);
export const createEmpleado = (data) => http.post("/api/empleados", data).then(r => r.data);
export const updateEmpleado = (id, data) => http.put(`/api/empleados/${id}`, data).then(r => r.data);
export const deleteEmpleado = (id) => http.delete(`/api/empleados/${id}`);
