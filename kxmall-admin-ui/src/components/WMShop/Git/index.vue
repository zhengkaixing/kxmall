<template>
  <div>
    <svg-icon icon-class="github" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'WMShopGit',
  data() {
    return {
      url: 'https://gitee.com/JavaLionLi/kxmall'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
