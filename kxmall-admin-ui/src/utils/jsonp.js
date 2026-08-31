/**
 * 轻量 JSONP，替代 vue-jsonp
 */
export function jsonp(url, params = {}, timeout = 10000) {
  return new Promise((resolve, reject) => {
    const callbackName = 'jsonp_cb_' + Date.now() + '_' + Math.floor(Math.random() * 100000)
    const query = Object.keys(params)
      .map(key => encodeURIComponent(key) + '=' + encodeURIComponent(params[key] == null ? '' : params[key]))
      .join('&')
    const src = url + (url.includes('?') ? '&' : '?') + query + (query ? '&' : '') + 'callback=' + callbackName
    const script = document.createElement('script')
    let timer = setTimeout(() => {
      cleanup()
      reject(new Error('JSONP timeout'))
    }, timeout)
    function cleanup() {
      clearTimeout(timer)
      delete window[callbackName]
      if (script.parentNode) {
        script.parentNode.removeChild(script)
      }
    }
    window[callbackName] = (data) => {
      cleanup()
      resolve(data)
    }
    script.onerror = () => {
      cleanup()
      reject(new Error('JSONP error'))
    }
    script.src = src
    document.body.appendChild(script)
  })
}

export default jsonp
