import router from './router'
import store from './store'
import { ElMessage } from 'element-plus'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'
import { isRelogin } from '@/utils/request'

NProgress.configure({ showSpinner: false })

const whiteList = ['/login', '/auth-redirect', '/bind', '/register']

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (to.meta.title) {
    document.title = to.meta.title + ' - ' + import.meta.env.VITE_APP_TITLE
  }
  if (getToken()) {
    to.meta.title && store.dispatch('settings/setTitle', to.meta.title)
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      if (store.getters.roles.length === 0) {
        isRelogin.show = true
        store.dispatch('GetInfo').then(() => {
          isRelogin.show = false
          store.dispatch('GenerateRoutes').then(accessRoutes => {
            accessRoutes.forEach(route => {
              router.addRoute(route)
            })
            next({ ...to, replace: true })
          })
        }).catch(err => {
          store.dispatch('LogOut').then(() => {
            ElMessage.error(err)
            next({ path: '/' })
          })
        })
      } else {
        next()
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next(`/login?redirect=${to.fullPath}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
