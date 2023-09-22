export default {
  filters: {
    getStringOSSURL(str) {
      return JSON.parse(str)[0].url
    }
  }
}
