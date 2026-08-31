const modules = import.meta.glob('../../../assets/icons/svg/*.svg', { eager: true })

const svgIcons = Object.keys(modules).map((path) => {
  const match = path.match(/\/([^/]+)\.svg$/)
  return match ? match[1] : path
})

export default svgIcons
