<template>
  <el-color-picker
    v-model="theme"
    :predefine="['#409EFF', '#1890ff', '#304156', '#212121', '#11a983', '#13c2c2', '#6959CD', '#f5222d']"
    class="theme-picker"
    popper-class="theme-picker-dropdown"
  />
</template>

<script>
const ORIGINAL_THEME = '#409EFF'

export default {
  data() {
    return {
      theme: ''
    }
  },
  computed: {
    defaultTheme() {
      return this.$store.state.settings.theme
    }
  },
  watch: {
    defaultTheme: {
      handler(val) {
        this.theme = val
      },
      immediate: true
    },
    theme(val) {
      this.setTheme(val)
    }
  },
  created() {
    if (this.defaultTheme !== ORIGINAL_THEME) {
      this.setTheme(this.defaultTheme)
    }
  },
  methods: {
    setTheme(val) {
      if (typeof val !== 'string') return
      document.documentElement.style.setProperty('--el-color-primary', val)
      this.$store.dispatch('settings/changeSetting', {
        key: 'theme',
        value: val
      })
      this.$emit('change', val)
    }
  }
}
</script>

<style>
.theme-message,
.theme-picker-dropdown {
  z-index: 99999 !important;
}

.theme-picker .el-color-picker__trigger {
  height: 26px !important;
  width: 26px !important;
  padding: 2px;
}

.theme-picker-dropdown .el-color-dropdown__link-btn {
  display: none;
}
</style>
