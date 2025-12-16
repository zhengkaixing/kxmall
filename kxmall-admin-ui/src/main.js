/**
 * Vue应用主入口文件
 * 负责初始化Vue应用、注册全局组件、挂载全局方法等
 * @author kxmall
 * @date 2024
 */
import Vue from 'vue'
import Cookies from 'js-cookie'
import Element from 'element-ui'
import { VueJsonp } from 'vue-jsonp'
import VueMeta from 'vue-meta'

// 样式文件导入
import './assets/styles/element-variables.scss'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import '@/assets/styles/index.scss' // 全局样式
import '@/assets/styles/kxmall.scss' // kxmall自定义样式

// 核心模块导入
import App from './App'
import store from './store'
import router from './router'
import directive from './directive' // 自定义指令
import plugins from './plugins' // 插件
import './assets/icons' // 图标
import './permission' // 权限控制

// 工具方法导入
import { download } from '@/utils/request'
import { getDicts } from '@/api/system/dict/data'
import { getConfigKey, updateConfigByKey } from '@/api/system/config'
import { parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, handleTree } from '@/utils/kxmall'

// 全局组件导入
import Pagination from '@/components/Pagination' // 分页组件
import RightToolbar from '@/components/RightToolbar' // 自定义表格工具组件
import Editor from '@/components/Editor' // 富文本组件
import FileUpload from '@/components/FileUpload' // 文件上传组件
import ImageUpload from '@/components/ImageUpload' // 图片上传组件
import ImagePreview from '@/components/ImagePreview' // 图片预览组件
import DictTag from '@/components/DictTag' // 字典标签组件
import DictData from '@/components/DictData' // 字典数据组件
import TreeSelect from '@riophae/vue-treeselect' // 树形选择器组件
import CellRender from '@/components/Table/CellRender' // 表格单元格渲染组件

/**
 * 全局方法挂载
 * 将常用工具方法挂载到Vue原型上，方便在组件中通过this调用
 */
Vue.prototype.getDicts = getDicts // 获取字典数据
Vue.prototype.getConfigKey = getConfigKey // 获取配置项
Vue.prototype.updateConfigByKey = updateConfigByKey // 更新配置项
Vue.prototype.parseTime = parseTime // 时间格式化
Vue.prototype.resetForm = resetForm // 表单重置
Vue.prototype.addDateRange = addDateRange // 添加日期范围
Vue.prototype.selectDictLabel = selectDictLabel // 选择字典标签
Vue.prototype.selectDictLabels = selectDictLabels // 选择字典标签（多选）
Vue.prototype.download = download // 文件下载
Vue.prototype.handleTree = handleTree // 构造树形数据

/**
 * 全局组件注册
 * 注册后可在任何组件中直接使用，无需再次导入
 */
Vue.component('DictTag', DictTag) // 字典标签组件
Vue.component('Pagination', Pagination) // 分页组件
Vue.component('RightToolbar', RightToolbar) // 表格工具栏组件
Vue.component('Editor', Editor) // 富文本编辑器组件
Vue.component('FileUpload', FileUpload) // 文件上传组件
Vue.component('ImageUpload', ImageUpload) // 图片上传组件
Vue.component('ImagePreview', ImagePreview) // 图片预览组件
Vue.component('TreeSelect', TreeSelect) // 树形选择器组件
Vue.component('CellRender', CellRender) // 表格单元格渲染组件

/**
 * Vue插件注册
 */
Vue.use(VueJsonp) // JSONP插件
Vue.use(directive) // 自定义指令插件
Vue.use(plugins) // 自定义插件
Vue.use(VueMeta) // Meta标签管理插件
DictData.install() // 字典数据组件安装

/**
 * Element UI配置
 * 修改 el-dialog 默认点击遮罩为不关闭（提升用户体验，防止误操作）
 */
Element.Dialog.props.closeOnClickModal.default = false

/**
 * 使用Element UI
 * 从Cookie中获取用户设置的主题大小，默认为medium
 */
Vue.use(Element, {
  size: Cookies.get('size') || 'medium'
})

/**
 * 关闭生产环境提示
 */
Vue.config.productionTip = false

/**
 * 创建Vue实例并挂载到DOM
 * 这是整个应用的根实例
 */
new Vue({
  el: '#app',
  router, // 路由
  store, // 状态管理
  render: h => h(App) // 渲染根组件
})
