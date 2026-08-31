import { ElMessage, ElMessageBox, ElNotification, ElLoading } from 'element-plus'

let loadingInstance

export default {
  msg(content) {
    ElMessage.info(content)
  },
  msgError(content) {
    ElMessage.error(content)
  },
  msgSuccess(content) {
    ElMessage.success(content)
  },
  msgWarning(content) {
    ElMessage.warning(content)
  },
  alert(content) {
    ElMessageBox.alert(content, '系统提示')
  },
  alertError(content) {
    ElMessageBox.alert(content, '系统提示', { type: 'error' })
  },
  alertSuccess(content) {
    ElMessageBox.alert(content, '系统提示', { type: 'success' })
  },
  alertWarning(content) {
    ElMessageBox.alert(content, '系统提示', { type: 'warning' })
  },
  notify(content) {
    ElNotification.info(content)
  },
  notifyError(content) {
    ElNotification.error(content)
  },
  notifySuccess(content) {
    ElNotification.success(content)
  },
  notifyWarning(content) {
    ElNotification.warning(content)
  },
  confirm(content) {
    return ElMessageBox.confirm(content, '系统提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
  },
  prompt(content) {
    return ElMessageBox.prompt(content, '系统提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
  },
  loading(content) {
    loadingInstance = ElLoading.service({
      lock: true,
      text: content,
      background: 'rgba(0, 0, 0, 0.7)'
    })
  },
  closeLoading() {
    loadingInstance.close()
  }
}
