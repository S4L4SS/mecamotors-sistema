import React from 'react';

export default function WhatsAppButton() {
  const phone = '51999999999'; // Reemplaza con tu número real
  const message = encodeURIComponent('Hola, quisiera más información sobre los servicios de Mecamotors.');

  return (
    <a
      href={`https://wa.me/${phone}?text=${message}`}
      target="_blank"
      rel="noreferrer"
      className="whatsapp-button"
    >
      WhatsApp
    </a>
  );
}
