import { http } from "./http";

export const getOficinas  = () => http.get("/api/oficinas").then(r => r.data);
export const getOficina   = (id) => http.get(`/api/oficinas/${id}`).then(r => r.data);
export const createOficina = (data) => http.post("/api/oficinas", data).then(r => r.data);
export const updateOficina = (id, data) => http.put(`/api/oficinas/${id}`, data).then(r => r.data);
export const deleteOficina = (id) => http.delete(`/api/oficinas/${id}`);
