import {fileURLToPath, URL} from 'node:url'

import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    proxy: {
      // 前端页面请求
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        // 把 /api 替换为 空字符串
        rewrite: (path) => path.replace(/^\/api/, ''),
      }
    }

  }
})
