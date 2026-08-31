function normalize(segments) {
  const parts = []
  for (const segment of segments) {
    if (!segment || segment === '.') continue
    if (segment === '..') {
      parts.pop()
    } else {
      parts.push(segment)
    }
  }
  return parts
}

export function resolve(...args) {
  let resolved = ''
  for (const arg of args) {
    if (arg == null) continue
    const value = String(arg)
    if (value.startsWith('/')) {
      resolved = value
    } else if (!resolved) {
      resolved = value
    } else {
      resolved = resolved.replace(/\/+$/, '') + '/' + value.replace(/^\/+/, '')
    }
  }
  const isAbs = resolved.startsWith('/')
  const parts = normalize(resolved.split('/'))
  const result = parts.join('/')
  if (isAbs) {
    return '/' + result.replace(/^\/+/, '')
  }
  return result || '.'
}

export function join(...args) {
  return resolve(...args)
}

export default {
  resolve,
  join
}
