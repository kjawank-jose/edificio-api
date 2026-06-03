import React, { useState, useEffect } from 'react'
import { useAuth } from '../hooks/useAuth'
import { useToast } from '../components/ToastContainer'
import { getMesActual } from '../services/api'
import StatsCard from '../components/StatsCard'
import CobrosTable from '../components/CobrosTable'
import WhatsAppModal from '../components/WhatsAppModal'

export default function DashboardPage() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [selectedCobro, setSelectedCobro] = useState(null)
  const { user } = useAuth()
  const { addToast } = useToast()

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    try {
      setLoading(true)
      const { data } = await getMesActual()
      setData(data)
    } catch (err) {
      if (err.response?.data?.includes('No hay un mes activo')) {
        setData({ noMes: true })
      } else {
        addToast('Error al cargar datos', 'error')
      }
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return (
      <div className="page">
        <div className="grid">
          {[1,2,3,4].map(i => <div key={i} className="card skeleton" style={{height: 120}} />)}
        </div>
      </div>
    )
  }

  if (data?.noMes) {
    return (
      <div className="page">
        <div className="card">
          <div className="empty-state">
            <div className="empty-state-icon">📭</div>
            <p style={{fontSize: '1.2rem', marginBottom: 8}}>No hay un mes activo</p>
            <p>Abre un nuevo mes desde la pestaña Admin</p>
          </div>
        </div>
      </div>
    )
  }

  const stats = [
    { title: 'Periodo', value: data?.periodo || '-', subtitle: data?.nombreMes || '' },
    { title: 'Total Agua', value: `S/ ${(data?.totalRecaudadoAgua || 0).toFixed(2)}`, subtitle: `${data?.sedapalM3 || 0} m³` },
    { title: 'Total Luz', value: `S/ ${(data?.totalRecaudadoLuz || 0).toFixed(2)}`, subtitle: `${data?.luzKwh || 0} kWh` },
    { title: 'Total General', value: `S/ ${(data?.totalGeneral || 0).toFixed(2)}`, subtitle: `Área común: S/ ${(data?.areaComunPorDpto || 0).toFixed(2)}` },
  ]

  return (
    <div className="page">
      <div className="grid">
        {stats.map((s, i) => <StatsCard key={i} {...s} />)}
      </div>

      <div className="table-container">
        <div className="table-header">
          <h2>📋 Cobros del Mes Actual</h2>
          <span className={`status status-${(data?.estado || '').toLowerCase()}`}>
            {data?.estado || 'N/A'}
          </span>
        </div>
        <CobrosTable
          cobros={data?.cobros || []}
          onWhatsApp={setSelectedCobro}
        />
      </div>

      {selectedCobro && (
        <WhatsAppModal
          cobro={selectedCobro}
          onClose={() => setSelectedCobro(null)}
        />
      )}
    </div>
  )
}