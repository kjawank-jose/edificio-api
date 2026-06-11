import React, { useState } from 'react'

export default function WhatsAppModal({ cobro, onClose }) {
  const [copied, setCopied] = useState(false)

  const copyToClipboard = () => {
    navigator.clipboard.writeText(cobro.mensajeWhatsapp).then(() => {
      setCopied(true)
      setTimeout(() => setCopied(false), 2000)
    })
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>📱 Mensaje WhatsApp - {cobro.nombreDpto}</h2>
          <button className="close-btn" onClick={onClose}>&times;</button>
        </div>
        <div className="whatsapp-msg">{cobro.mensajeWhatsapp}</div>
        <div className="flex gap-2 mt-2">
          <button className="btn btn-success" onClick={copyToClipboard}>
            {copied ? '✅ Copiado!' : '📋 Copiar Mensaje'}
          </button>
          <a
            href={`https://wa.me/?text=${encodeURIComponent(cobro.mensajeWhatsapp)}`}
            target="_blank"
            rel="noopener noreferrer"
            className="btn btn-primary"
            style={{ textDecoration: 'none' }}
          >
            📱 Abrir WhatsApp
          </a>
        </div>
      </div>
    </div>
  )
}
