import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { VitePWA } from 'vite-plugin-pwa'

export default defineConfig({
  plugins: [
    react(),
    VitePWA({
      registerType: 'autoUpdate',
      manifest: false, // Usamos manifest.json manual
      workbox: {
        globPatterns: ['**/*.{js,css,html,ico,png,svg}']
      }
    })
  ],
  server: {
    host: true,        // <-- Escucha en 0.0.0.0 (todas las interfaces)
    port: 5173,
    strictPort: true,  // <-- No cambia de puerto si 5173 está ocupado
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // Backend en la misma PC
        changeOrigin: true,
        secure: false
      }
    }
  },
  preview: {
    host: true,        // <-- También para preview de producción
    port: 4173
  },
  build: {
    outDir: 'dist',
    sourcemap: true
  }
})

//export default defineConfig({
//  plugins: [react()],
//  server: {
//    port: 5173,
//    proxy: {
//      '/api': {
//        target: 'http://localhost:8080',
//        changeOrigin: true,
//      }
//    }
//  }
//})