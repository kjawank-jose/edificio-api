import React from 'react'

export default function CobrosTable({ cobros, onWhatsApp }) {
  if (!cobros || cobros.length === 0) {
    return (
      <div className="empty-state">
        <div className="empty-state-icon">📭</div>
        <p>No hay cobros registrados para este mes</p>
      </div>
    )
  }

  return (
    <table>
      <thead>
        <tr>
          <th>Depto</th>
          <th>Propietario</th>
          <th>Agua (S/)</th>
          <th>Luz (S/)</th>
          <th>Área Común</th>
          <th>Total</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        {cobros.map(c => (
          <tr key={c.codigoDpto}>
            <td>
              <strong>{c.codigoDpto}</strong>
              <br />
              <small style={{ color: 'var(--text-secondary)' }}>{c.nombreDpto}</small>
            </td>
            <td>{c.propietario}</td>
            <td>
              S/ {(c.pagoAgua || 0).toFixed(2)}
              <br />
              <small>{(c.consumoAguaM3 || 0).toFixed(3)} m³</small>
            </td>
            <td>
              {c.tieneLuz ? (
                <>
                  S/ {(c.pagoLuz || 0).toFixed(2)}
                  <br />
                  <small>{(c.consumoLuzKwh || 0).toFixed(2)} kWh</small>
                </>
              ) : '-'}
            </td>
            <td>S/ {(c.pagoAreaComun || 0).toFixed(2)}</td>
            <td><strong>S/ {(c.pagoTotal || 0).toFixed(2)}</strong></td>
            <td>
              <button className="btn btn-sm btn-success" onClick={() => onWhatsApp(c)}>
                📱 WhatsApp
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  )
}