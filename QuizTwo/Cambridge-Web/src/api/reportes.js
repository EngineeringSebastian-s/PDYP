import { http } from "./http";
export const getReporteAreas = () => http.get("/api/reportes/areas-empleados").then(r => r.data);
