import { http } from "./http";

export const getAreas = () => http.get("/api/areas").then(r => r.data);
export const getArea = (id) => http.get(`/api/areas/${id}`).then(r => r.data);
export const createArea = (data) => http.post("/api/areas", data).then(r => r.data);
export const updateArea = (id, data) => http.put(`/api/areas/${id}`, data).then(r => r.data);
export const deleteArea = (id) => http.delete(`/api/areas/${id}`);
