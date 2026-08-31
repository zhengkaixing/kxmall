<template>
  <el-image
    :src="`${realSrc}`"
    fit="cover"
    :style="`width:${realWidth};height:${realHeight};`"
    :preview-src-list="realSrcList"
  >
    <template #error><div class="image-slot">
      <el-icon ><Picture /></el-icon>
    </div></template>
  </el-image>
</template>

<script>

export default {
  name: "ImagePreview",
  props: {
    src: {
      type: String,
      default: ""
    },
    width: {
      type: [Number, String],
      default: ""
    },
    height: {
      type: [Number, String],
      default: ""
    }
  },
  computed: {
    realSrc() {
      if (!this.src) {
        return;
      }
      let real_src = this.src.split(",")[0];
      return real_src;
    },
    realSrcList() {
      if (!this.src) {
        return;
      }
      let real_src_list = this.src.split(",");
      let srcList = [];
      real_src_list.forEach(item => {
        return srcList.push(item);
      });
      return srcList;
    },
    realWidth() {
      return typeof this.width == "string" ? this.width : `${this.width}px`;
    },
    realHeight() {
      return typeof this.height == "string" ? this.height : `${this.height}px`;
    }
  },
};
</script>

<style lang="scss" scoped>
.el-image {
  border-radius: 5px;
  background-color: #ebeef5;
  box-shadow: 0 0 5px 1px #ccc;
  :deep() .el-image__inner {
    transition: all 0.3s;
    cursor: pointer;
    &:hover {
      transform: scale(1.2);
    }
  }
  :deep() .image-slot {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    color: #909399;
    font-size: 30px;
  }
}
</style>
