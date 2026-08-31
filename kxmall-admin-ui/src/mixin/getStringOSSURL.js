export default {
  methods: {
    getStringOSSURL(str) {
      if (!str) return ''
      try {
        return JSON.parse(str)[0].url
      } catch (e) {
        return str
      }
    }
  }
}
