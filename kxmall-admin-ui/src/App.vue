<template>
  <el-config-provider :size="size" :z-index="3000">
    <router-view />
    <theme-picker />
  </el-config-provider>
</template>

<script>
import ThemePicker from '@/components/ThemePicker'
import { mapGetters } from 'vuex'

export default {
  name: 'App',
  components: { ThemePicker },
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
<style scoped>
#app .theme-picker {
  display: none;
}
</style>
