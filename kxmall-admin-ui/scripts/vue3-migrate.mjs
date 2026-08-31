/**
 * 将 Vue 2 模板语法批量转换为 Vue 3 / Element Plus
 * 运行: node scripts/vue3-migrate.mjs
 */
import fs from 'fs'
import path from 'path'
import { fileURLToPath } from 'url'

const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)
const SRC = path.resolve(__dirname, '../src')

const ICON_SPECIAL = {
  'el-icon-picture-outline': 'Picture',
  'el-icon-picture-outline-round': 'PictureRounded',
  'el-icon-s-operation': 'Operation',
  'el-icon-s-promotion': 'Promotion',
  'el-icon-s-custom': 'UserFilled',
  'el-icon-s-home': 'HomeFilled',
  'el-icon-s-fold': 'Fold',
  'el-icon-s-unfold': 'Expand',
  'el-icon-s-tools': 'Tools',
  'el-icon-s-shop': 'Shop',
  'el-icon-s-goods': 'Goods',
  'el-icon-s-order': 'List',
  'el-icon-s-data': 'DataAnalysis',
  'el-icon-s-check': 'Select',
  'el-icon-s-platform': 'Platform',
  'el-icon-s-marketing': 'TrendCharts',
  'el-icon-s-finance': 'Wallet',
  'el-icon-s-cooperation': 'Coordinate',
  'el-icon-s-claim': 'Checked',
  'el-icon-s-open': 'View',
  'el-icon-s-flag': 'Flag',
  'el-icon-s-comment': 'ChatDotRound',
  'el-icon-s-release': 'Promotion',
  'el-icon-s-help': 'QuestionFilled',
  'el-icon-s-grid': 'Grid',
  'el-icon-document-add': 'DocumentAdd',
  'el-icon-document-copy': 'DocumentCopy',
  'el-icon-copy-document': 'CopyDocument',
  'el-icon-circle-plus-outline': 'CirclePlus',
  'el-icon-remove-outline': 'Remove',
  'el-icon-edit-outline': 'Edit',
  'el-icon-delete-solid': 'DeleteFilled',
  'el-icon-warning-outline': 'Warning',
  'el-icon-info-outline': 'InfoFilled',
  'el-icon-question': 'QuestionFilled',
  'el-icon-star-on': 'StarFilled',
  'el-icon-star-off': 'Star',
  'el-icon-more-outline': 'MoreFilled',
  'el-icon-d-arrow-left': 'DArrowLeft',
  'el-icon-d-arrow-right': 'DArrowRight',
  'el-icon-d-caret': 'DCaret',
  'el-icon-caret-left': 'CaretLeft',
  'el-icon-caret-right': 'CaretRight',
  'el-icon-caret-bottom': 'CaretBottom',
  'el-icon-caret-top': 'CaretTop',
  'el-icon-upload2': 'Upload',
  'el-icon-download': 'Download',
  'el-icon-refresh-right': 'RefreshRight',
  'el-icon-refresh-left': 'RefreshLeft',
  'el-icon-circle-check': 'CircleCheck',
  'el-icon-circle-close': 'CircleClose',
  'el-icon-circle-plus': 'CirclePlus',
  'el-icon-success': 'SuccessFilled',
  'el-icon-error': 'CircleCloseFilled',
  'el-icon-video-camera-solid': 'VideoCameraFilled',
  'el-icon-camera-solid': 'CameraFilled',
  'el-icon-goods': 'Goods',
  'el-icon-s-opportunity': 'Opportunity',
  'el-icon-time': 'Clock',
  'el-icon-date': 'Calendar',
  'el-icon-notebook-1': 'Notebook',
  'el-icon-notebook-2': 'Notebook',
  'el-icon-mobile-phone': 'Iphone',
  'el-icon-service': 'Service',
  'el-icon-sold-out': 'SoldOut',
  'el-icon-sell': 'Sell',
  'el-icon-shopping-cart-2': 'ShoppingCart',
  'el-icon-shopping-cart-1': 'ShoppingCart',
  'el-icon-shopping-bag-1': 'ShoppingBag',
  'el-icon-shopping-bag-2': 'ShoppingBag',
  'el-icon-chat-dot-round': 'ChatDotRound',
  'el-icon-chat-line-round': 'ChatLineRound',
  'el-icon-chat-round': 'ChatRound',
  'el-icon-chat-dot-square': 'ChatDotSquare',
  'el-icon-chat-line-square': 'ChatLineSquare',
  'el-icon-chat-square': 'ChatSquare',
  'el-icon-message-solid': 'Message',
  'el-icon-bottom-right': 'BottomRight',
  'el-icon-bottom-left': 'BottomLeft',
  'el-icon-top-right': 'TopRight',
  'el-icon-top-left': 'TopLeft'
}

function toPascal(name) {
  return name.split('-').filter(Boolean).map(s => s.charAt(0).toUpperCase() + s.slice(1)).join('')
}

function iconName(cls) {
  const key = cls.replace(/\s+.*/, '')
  if (ICON_SPECIAL[key]) return ICON_SPECIAL[key]
  return toPascal(key.replace(/^el-icon-/, ''))
}

function findMatchingClose(html, startIdx, tagName) {
  const openRe = new RegExp('<' + tagName + '\\b', 'g')
  const closeRe = new RegExp('</' + tagName + '>', 'g')
  let i = startIdx
  let depth = 0
  while (i < html.length) {
    openRe.lastIndex = i
    closeRe.lastIndex = i
    const open = openRe.exec(html)
    const close = closeRe.exec(html)
    if (!close) return -1
    if (open && open.index < close.index) {
      depth++
      i = open.index + 1
    } else {
      depth--
      if (depth < 0) return close.index + close[0].length
      i = close.index + 1
    }
  }
  return -1
}

function wrapNamedSlots(template) {
  // template 上的旧插槽
  template = template.replace(/<template([^>]*)\s+slot-scope="([^"]+)"([^>]*)\s+slot="([^"]+)"/g, '<template$1$3 #$4="$2"')
  template = template.replace(/<template([^>]*)\s+slot="([^"]+)"([^>]*)\s+slot-scope="([^"]+)"/g, '<template$1$3 #$2="$4"')
  template = template.replace(/<template([^>]*)\s+slot="([^"]+)"/g, '<template$1 #$2')
  template = template.replace(/<template([^>]*)\s+slot-scope="([^"]+)"/g, '<template$1 #default="$2"')

  // 非 template 元素上的 slot="xxx"
  const slotAttrRe = /<([a-zA-Z0-9-]+)([^>]*?)\s+slot="([^"]+)"([^>]*?)(\/?)>/g
  let match
  const replacements = []
  while ((match = slotAttrRe.exec(template)) !== null) {
    const [full, tag, before, slotName, after, selfClose] = match
    const start = match.index
    const openEnd = start + full.length
    const cleanedOpen = `<${tag}${before}${after}>`
    if (selfClose === '/' || full.endsWith('/>')) {
      replacements.push({
        start,
        end: openEnd,
        text: `<template #${slotName}>${cleanedOpen.replace(/>$/, ' />')}</template>`
      })
      continue
    }
    const closeEnd = findMatchingClose(template, openEnd, tag)
    if (closeEnd === -1) continue
    const inner = template.slice(openEnd, closeEnd)
    replacements.push({
      start,
      end: closeEnd,
      text: `<template #${slotName}>${cleanedOpen}${inner}</template>`
    })
  }
  for (let i = replacements.length - 1; i >= 0; i--) {
    const r = replacements[i]
    template = template.slice(0, r.start) + r.text + template.slice(r.end)
  }
  return template
}

function convertIcons(template) {
  template = template.replace(/\b(icon|prefix-icon|suffix-icon)="el-icon-([a-z0-9-]+)"/g, (_, attr, name) => {
    return `${attr}="${iconName('el-icon-' + name)}"`
  })
  template = template.replace(/<i\s+([^>]*?)class="([^"]*el-icon-[^"]*)"([^>]*)><\/i>/g, (_, pre, cls, post) => {
    const icons = cls.split(/\s+/).filter(c => c.startsWith('el-icon-') && c !== 'el-icon--right' && c !== 'el-input__icon')
    const rest = cls.split(/\s+/).filter(c => !c.startsWith('el-icon-') || c === 'el-icon--right' || c === 'el-input__icon')
    if (!icons.length) return `<i ${pre}class="${cls}"${post}></i>`
    const name = iconName(icons[0])
    const restClass = rest.filter(c => c && c !== 'el-input__icon').join(' ')
    const classAttr = restClass ? ` class="${restClass}"` : ''
    return `<el-icon${classAttr} ${pre}${post}><${name} /></el-icon>`
  })
  template = template.replace(/<i\s+([^>]*?)class="([^"]*el-icon-[^"]*)"([^>]*)\s*\/>/g, (_, pre, cls, post) => {
    const icons = cls.split(/\s+/).filter(c => c.startsWith('el-icon-') && c !== 'el-icon--right' && c !== 'el-input__icon')
    if (!icons.length) return `<i ${pre}class="${cls}"${post} />`
    const name = iconName(icons[0])
    return `<el-icon ${pre}${post}><${name} /></el-icon>`
  })
  return template
}

function transformVue(content) {
  const parts = content.split(/(<script[\s\S]*?<\/script>)/)
  for (let i = 0; i < parts.length; i++) {
    if (parts[i].startsWith('<script')) {
      parts[i] = transformJs(parts[i])
      continue
    }
    if (parts[i].includes('<style')) {
      const styleParts = parts[i].split(/(<style[\s\S]*?<\/style>)/)
      for (let j = 0; j < styleParts.length; j++) {
        if (styleParts[j].startsWith('<style')) {
          styleParts[j] = styleParts[j]
            .replace(/::v-deep\s*/g, ':deep() ')
            .replace(/\/deep\/\s*/g, ':deep() ')
            .replace(/>>>/g, ':deep()')
            .replace(/@import\s+['"]~@\//g, '@import "@/')
            .replace(/@import\s+['"]~/g, '@import "')
            .replace(/\.sidebarLogoFade-leave-to/g, '.sidebarLogoFade-leave-from')
            .replace(/\.el-button--mini/g, '.el-button--small')
            .replace(/\.el-submenu/g, '.el-sub-menu')
            .replace(/\.fade-enter,/g, '.fade-enter-from,')
            .replace(/\.fade-enter\s*\{/g, '.fade-enter-from {')
            .replace(/\.fade-transform-enter\s*\{/g, '.fade-transform-enter-from {')
            .replace(/\.breadcrumb-enter,/g, '.breadcrumb-enter-from,')
            .replace(/\.sidebarLogoFade-enter,/g, '.sidebarLogoFade-enter-from,')
        } else {
          styleParts[j] = transformTemplate(styleParts[j])
        }
      }
      parts[i] = styleParts.join('')
    } else {
      parts[i] = transformTemplate(parts[i])
    }
  }
  return parts.join('')
}

function transformTemplate(template) {
  template = wrapNamedSlots(template)
  // visible.sync on dialog/drawer/popover
  template = template.replace(/:visible\.sync="/g, 'v-model="')
  template = template.replace(/v-model="([^"]+)"\s+:visible\.sync="\1"/g, 'v-model="$1"')
  // remaining .sync
  template = template.replace(/:([A-Za-z0-9-]+)\.sync="/g, 'v-model:$1="')
  template = template.replace(/\ssize="mini"/g, ' size="small"')
  template = template.replace(/\ssize='mini'/g, " size='small'")
  template = template.replace(/\ssize="medium"/g, ' size="default"')
  template = template.replace(/\.native\b/g, '')
  template = template.replace(/\scustom-class="/g, ' class="')
  template = template.replace(/v-on="\$listeners"/g, 'v-bind="$attrs"')
  template = convertIcons(template)
  template = template.replace(/\s\|getStringOSSURL/g, '')
  template = template.replace(/\s\|\s*getStringOSSURL/g, '')
  template = template.replace(/<treeselect\b/g, '<tree-select')
  template = template.replace(/<\/treeselect>/g, '</tree-select>')
  template = template.replace(/<el-submenu\b/g, '<el-sub-menu')
  template = template.replace(/<\/el-submenu>/g, '</el-sub-menu>')
  return template
}

function transformJs(code) {
  code = code.replace(/beforeDestroy\s*\(/g, 'beforeUnmount(')
  code = code.replace(/destroyed\s*\(/g, 'unmounted(')
  code = code.replace(/process\.env\.VUE_APP_/g, 'import.meta.env.VITE_APP_')
  code = code.replace(/process\.env\.NODE_ENV/g, 'import.meta.env.MODE')
  code = code.replace(/this\.\$set\(\s*([^,]+),\s*([^,]+),\s*([^)]+)\)/g, '$1[$2] = $3')
  code = code.replace(/Vue\.set\(\s*([^,]+),\s*([^,]+),\s*([^)]+)\)/g, '$1[$2] = $3')
  code = code.replace(/this\.\$delete\(\s*([^,]+),\s*([^)]+)\)/g, 'delete $1[$2]')
  code = code.replace(/v-on="\$listeners"/g, 'v-bind="$attrs"')
  code = code.replace(/\$listeners/g, '$attrs')
  code = code.replace(/from ['"]@riophae\/vue-treeselect['"]/g, "from '@/components/TreeSelect'")
  code = code.replace(/import ['"]@riophae\/vue-treeselect\/dist\/vue-treeselect\.css['"]\s*;?/g, '')
  code = code.replace(/import path from ['"]path['"]/g, "import path from '@/utils/path'")
  code = code.replace(/from ['"]vue-count-to['"]/g, "from 'vue3-count-to'")
  code = code.replace(/from ['"]vue-print-nb['"]/g, "from 'vue3-print-nb'")
  code = code.replace(/from ['"]element-ui['"]/g, "from 'element-plus'")
  code = code.replace(/import variables from ['"]@\/assets\/styles\/variables\.scss['"]/g, "import variables from '@/assets/styles/variables.module.scss'")
  // v-model value/input → keep working via modelValue also handled in components
  code = code.replace(/this\.\$ELEMENT\.size/g, 'undefined')
  return code
}

function walk(dir, acc = []) {
  for (const name of fs.readdirSync(dir)) {
    const full = path.join(dir, name)
    const stat = fs.statSync(full)
    if (stat.isDirectory()) walk(full, acc)
    else acc.push(full)
  }
  return acc
}

const files = walk(SRC)
let count = 0
for (const file of files) {
  if (!/\.(vue|js)$/.test(file)) continue
  // skip already rewritten infra if needed
  const rel = path.relative(SRC, file).replace(/\\/g, '/')
  if (['main.js', 'router/index.js', 'store/index.js', 'permission.js'].includes(rel)) continue
  const orig = fs.readFileSync(file, 'utf8')
  let next = orig
  if (file.endsWith('.vue')) next = transformVue(orig)
  else next = transformJs(orig)
  if (next !== orig) {
    fs.writeFileSync(file, next)
    count++
    console.log('updated', rel)
  }
}
console.log('done, updated', count, 'files')
