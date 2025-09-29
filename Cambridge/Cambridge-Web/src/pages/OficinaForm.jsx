import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {getAreas} from "../api/areas";
import {createOficina, getOficina, updateOficina} from "../api/oficinas";

export default function OficinaForm() {
    const {id} = useParams();
    const editing = Boolean(id);
    const [codigo, setCodigo] = useState("");
    const [areaId, setAreaId] = useState("");
    const [areas, setAreas] = useState([]);
    const [err, setErr] = useState(null);
    const nav = useNavigate();

    useEffect(() => {
        const load = async () => {
            const ars = await getAreas();
            setAreas(ars);
            if (editing) {
                const o = await getOficina(id);
                setCodigo(o.codigo);
                setAreaId(String(o.areaId));
            } else if (ars.length > 0) {
                setAreaId(String(ars[0].id));
            }
        };
        load().catch(() => setErr("No se pudo cargar la información"));
    }, [id, editing]);

    const onSubmit = async (e) => {
        e.preventDefault();
        try {
            const payload = {id: editing ? Number(id) : null, codigo, areaId: Number(areaId)};
            if (editing) await updateOficina(id, payload);
            else await createOficina(payload);
            nav("/oficinas");
        } catch (e) {
            setErr(e?.response?.data?.message || "Error al guardar");
        }
    };

    return (
        <div>
            <h3>{editing ? "Editar Oficina" : "Nueva Oficina"}</h3>
            {err && <p style={{color: "crimson"}}>{err}</p>}

            <form onSubmit={onSubmit} style={{display: "grid", gap: 12, maxWidth: 420}}>
                <label style={{display: "grid", gap: 6}}>
                    Código
                    <input value={codigo} onChange={e => setCodigo(e.target.value)} required maxLength={50}/>
                </label>

                <label style={{display: "grid", gap: 6}}>
                    Área
                    <select value={areaId} onChange={e => setAreaId(e.target.value)} required>
                        {areas.map(a => <option key={a.id} value={a.id}>{a.nombre}</option>)}
                    </select>
                </label>

                <div style={{display: "flex", gap: 8}}>
                    <button type="submit">{editing ? "Actualizar" : "Crear"}</button>
                    <button type="button" onClick={() => nav("/oficinas")}>Cancelar</button>
                </div>
            </form>
        </div>
    );
}
