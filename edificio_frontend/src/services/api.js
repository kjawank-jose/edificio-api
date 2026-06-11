import axios from 'axios'

const API = axios.create({
  baseURL: '/api',  // Vite redirige automáticamente al backend
  headers: {
    'Content-Type': 'application/json',
  },
})

// Interceptor para agregar token
API.interceptors.request.use((config) => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (user.token) {
    config.headers.Authorization = `Bearer ${user.token}`
  }
  return config
})

// Interceptor para errores
API.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 || error.response?.status === 403) {
      localStorage.removeItem('user')
      window.location.reload()
    }
    return Promise.reject(error)
  }
)

export default API

// Auth
export const login = (credentials) => API.post('/auth/login', credentials)

// Facturación
export const getMesActual = () => API.get('/facturacion/mes-actual')
export const getHistorial = () => API.get('/facturacion/historial')
export const getMesByPeriodo = (periodo) => API.get(`/facturacion/${periodo}`)
export const ingresarDatos = (data) => API.post('/facturacion/ingresar', data)
export const cerrarMes = (data) => API.post('/facturacion/cerrar-mes', data)
export const abrirMes = (data) => API.post('/facturacion/abrir-mes', data)
export const getHistorialDpto = (codigo) => API.get(`/facturacion/dpto/${codigo}/historial`)

// Departamentos
export const getDepartamentos = () => API.get('/departamentos')