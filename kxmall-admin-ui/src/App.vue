<template>
  <el-config-provider :size="size" :z-index="3000">
    <router-view />
  </el-config-provider>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'App',
  computed: {
    ...mapGetters(['size'])
  },
  watch: {
    '$store.state.settings.title': {
      handler() {
        this.setTitle()
      }
    },
    '$store.state.settings.dynamicTitle': {
      handler() {
        this.setTitle()
      }
    }
  },
  mounted() {
    this.setTitle()
  },
  methods: {
    setTitle() {
      const pageTitle = this.$store.state.settings.dynamicTitle && this.$store.state.settings.title
      document.title = pageTitle ? `${pageTitle} - ${import.meta.env.VITE_APP_TITLE}` : import.meta.env.VITE_APP_TITLE
    }
  }
}
</script>
