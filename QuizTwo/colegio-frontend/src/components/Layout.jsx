import { Link, Outlet } from "react-router-dom";

export default function Layout() {
    return (
        <div style={{ maxWidth: 960, margin: "20px auto", fontFamily: "system-ui, sans-serif" }}>
            <header style={{ display: "flex", gap: 16, marginBottom: 24 }}>
                <h2 style={{ marginRight: "auto" }}>Colegio Cambridge</h2>
                <Link to="/areas">Áreas</Link>
                <Link to="/oficinas">Oficinas</Link>
                <Link to="/salones">Salones</Link>
                <Link to="/empleados">Empleados</Link>
                <Link to="/reporte">Reporte</Link>

            </header>
            <Outlet />
        </div>
    );
}
