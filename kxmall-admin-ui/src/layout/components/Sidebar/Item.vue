<template>
  <div>
    <svg-icon v-if="icon" :icon-class="icon" />
    <span v-if="title" :title="title">{{ title }}</span>
  </div>
</template>

<script>
export default {
  name: 'MenuItem',
  props: {
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    }
  }
}
</script>
