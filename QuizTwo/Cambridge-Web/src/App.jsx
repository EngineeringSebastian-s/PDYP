import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Layout from "./components/Layout";
import AreasList from "./pages/AreasList";
import AreaForm from "./pages/AreaForm";
import OficinasList from "./pages/OficinasList";
import OficinaForm from "./pages/OficinaForm";
import SalonesList from "./pages/SalonesList";
import SalonForm from "./pages/SalonForm";
import EmpleadosList from "./pages/EmpleadosList";
import EmpleadoForm from "./pages/EmpleadoForm";
import ReporteAreas from "./pages/ReporteAreas";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route index element={<Navigate to="/areas" replace />} />
          <Route path="areas" element={<AreasList />} />
          <Route path="areas/nueva" element={<AreaForm />} />
          <Route path="areas/:id/editar" element={<AreaForm />} />
          <Route path="oficinas" element={<OficinasList />} />
          <Route path="oficinas/nueva" element={<OficinaForm />} />
          <Route path="oficinas/:id/editar" element={<OficinaForm />} />
          <Route path="salones" element={<SalonesList />} />
          <Route path="salones/nuevo" element={<SalonForm />} />
          <Route path="salones/:id/editar" element={<SalonForm />} />
          <Route path="empleados" element={<EmpleadosList />} />
          <Route path="empleados/nuevo" element={<EmpleadoForm />} />
          <Route path="empleados/:id/editar" element={<EmpleadoForm />} />
          <Route path="reporte" element={<ReporteAreas />} />

        </Route>
      </Routes>
    </BrowserRouter>
  );
}
