<template>
  <el-dropdown trigger="click" @command="handleSetSize">
    <div>
      <svg-icon class-name="size-icon" icon-class="size" />
    </div>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item v-for="item of sizeOptions" :key="item.value" :disabled="size===item.value" :command="item.value">
          {{ item.label }}
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script>
export default {
  data() {
    return {
      sizeOptions: [
        { label: 'Default', value: 'default' },
        { label: 'Large', value: 'large' },
        { label: 'Small', value: 'small' }
      ]
    }
  },
  computed: {
    size() {
      return this.$store.getters.size
    }
  },
  methods: {
    handleSetSize(size) {
      this.$modal.loading('正在设置布局大小，请稍候...')
      this.$store.dispatch('app/setSize', size)
      setTimeout('window.location.reload()', 1000)
    }
  }
}
</script>
