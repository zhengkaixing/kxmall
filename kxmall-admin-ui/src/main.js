/**
 * Vue应用主入口文件
 * 负责初始化Vue应用、注册全局组件、挂载全局方法等
 */
import { createApp } from 'vue'
import Cookies from 'js-cookie'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
import locale from 'element-plus/es/locale/lang/zh-cn'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import '@/assets/styles/index.scss'
import '@/assets/styles/kxmall.scss'

import App from './App'
import store from './store'
import router from './router'
import directive from './directive'
import plugins from './plugins'
import print from 'vue3-print-nb'
import { download } from '@/utils/request'
import './permission'
import { useDict } from '@/utils/dict'
import { parseTime, resetForm, addDateRange, handleTree, selectDictLabel, selectDictLabels } from '@/utils/kxmall'
import { getConfigKey, updateConfigByKey } from '@/api/system/config'
import DictData from '@/components/DictData'

import 'virtual:svg-icons-register'
import SvgIcon from '@/components/SvgIcon'
import elementIcons from '@/components/SvgIcon/svgicon'

import Pagination from '@/components/Pagination'
import RightToolbar from '@/components/RightToolbar'
import Editor from '@/components/Editor'
import FileUpload from '@/components/FileUpload'
import ImageUpload from '@/components/ImageUpload'
import ImagePreview from '@/components/ImagePreview'
import DictTag from '@/components/DictTag'
import TreeSelect from '@/components/TreeSelect'
import CellRender from '@/components/Table/CellRender'

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(store)
app.use(router)
app.use(plugins)
app.use(print)
app.use(elementIcons)
app.use(directive)
app.use(DictData)
app.component('svg-icon', SvgIcon)
app.component('DictTag', DictTag)
app.component('Pagination', Pagination)
app.component('RightToolbar', RightToolbar)
app.component('Editor', Editor)
app.component('FileUpload', FileUpload)
app.component('ImageUpload', ImageUpload)
app.component('ImagePreview', ImagePreview)
app.component('TreeSelect', TreeSelect)
app.component('CellRender', CellRender)

app.config.globalProperties.useDict = useDict
app.config.globalProperties.download = download
app.config.globalProperties.parseTime = parseTime
app.config.globalProperties.resetForm = resetForm
app.config.globalProperties.handleTree = handleTree
app.config.globalProperties.addDateRange = addDateRange
app.config.globalProperties.getConfigKey = getConfigKey
app.config.globalProperties.updateConfigByKey = updateConfigByKey
app.config.globalProperties.selectDictLabel = selectDictLabel
app.config.globalProperties.selectDictLabels = selectDictLabels

app.use(ElementPlus, {
  locale,
  size: Cookies.get('size') || 'default'
})

app.mount('#app')
