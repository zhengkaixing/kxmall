import { defineConfig, loadEnv } from 'vite'
import path from 'path'
import { fileURLToPath } from 'url'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
import AutoImport from 'unplugin-auto-import/vite'

const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)

function resolve(dir) {
  return path.join(__dirname, dir)
}

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd())
  const port = Number(env.VITE_APP_PORT) || 80

  return {
    base: env.VITE_APP_CONTEXT_PATH || '/',
    plugins: [
      vue(),
      vueJsx(),
      AutoImport({
        imports: ['vue', 'vue-router', 'vuex']
      }),
      createSvgIconsPlugin({
        iconDirs: [resolve('src/assets/icons/svg')],
        symbolId: 'icon-[name]'
      })
    ],
    resolve: {
      alias: {
        '~': resolve('./'),
        '@': resolve('src')
      },
      extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue']
    },
    server: {
      host: '0.0.0.0',
      port,
      open: true,
      proxy: {
        [env.VITE_APP_BASE_API]: {
          target: 'http://localhost:8585',
          changeOrigin: true,
          rewrite: (p) => p.replace(new RegExp('^' + env.VITE_APP_BASE_API), '')
        }
      }
    },
    css: {
      preprocessorOptions: {
        scss: {
          silenceDeprecations: ['import', 'mixed-decls']
        }
      }
    },
    optimizeDeps: {
      entries: ['index.html', 'src/main.js']
    },
    build: {
      sourcemap: false,
      chunkSizeWarningLimit: 2000,
      rollupOptions: {
        output: {
          manualChunks: {
            vue: ['vue', 'vue-router', 'vuex'],
            'element-plus': ['element-plus', '@element-plus/icons-vue']
          }
        }
      }
    }
  }
})
