import { http } from "./http";

export const getSalones   = () => http.get("/api/salones").then(r => r.data);
export const getSalon     = (id) => http.get(`/api/salones/${id}`).then(r => r.data);
export const createSalon  = (data) => http.post("/api/salones", data).then(r => r.data);
export const updateSalon  = (id, data) => http.put(`/api/salones/${id}`, data).then(r => r.data);
export const deleteSalon  = (id) => http.delete(`/api/salones/${id}`);
