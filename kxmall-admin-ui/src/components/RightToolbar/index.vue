<template>
  <div class="top-right-btn" :style="style">
    <el-row>
      <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top" v-if="search">
        <el-button size="mini" circle icon="el-icon-search" @click="toggleSearch()" />
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="刷新" placement="top">
        <el-button size="mini" circle icon="el-icon-refresh" @click="refresh()" />
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="显隐列" placement="top" v-if="columns">
        <el-popover placement="bottom" width="120" trigger="click">
          <el-button size="mini" circle icon="el-icon-menu" slot="reference" style="margin-left: 10px;" />
          <el-checkbox-group v-model="checkedColumnsList" @change="onColumnCheckChange">
            <el-checkbox v-for="item in columns" :key="item.prop" :label="item.prop">
              {{ item.label }}
            </el-checkbox>
          </el-checkbox-group>
        </el-popover>
      </el-tooltip>
    </el-row>
    <!-- <el-dialog :title="title" :visible.sync="open" append-to-body>
      <el-transfer
        :titles="['显示', '隐藏']"
        v-model="value"
        :data="columns"
        @change="dataChange"
      ></el-transfer>
    </el-dialog> -->
  </div>
</template>
<script>
export default {
  name: "RightToolbar",
  data() {
    return {
      // // 显隐数据
      // value: [],
      // // 弹出层标题
      // title: "显示/隐藏",
      // // 是否显示弹出层
      // open: false,
      checkedColumnsList:[]
    };
  },
  props: {
    showSearch: {
      type: Boolean,
      default: true,
    },
    columns: {
      type: Array,
      default(){
        return []
      }
    },
    search: {
      type: Boolean,
      default: true,
    },
    gutter: {
      type: Number,
      default: 10,
    },
  },
  computed: {
    style() {
      const ret = {};
      if (this.gutter) {
        ret.marginRight = `${this.gutter / 2}px`;
      }
      return ret;
    }
  },
  created() {
    // 显隐列初始默认隐藏列
    // for (let item in this.columns) {
    //   if (this.columns[item].visible === false) {
    //     this.value.push(parseInt(item));
    //   }
    // }
    this.checkedColumnsList=this.columns.map(el=>el.prop)
  },
  methods: {
    // 搜索
    toggleSearch() {
      this.$emit("update:showSearch", !this.showSearch);
    },
    // 刷新
    refresh() {
      this.$emit("queryTable");
    },
    // // 右侧列表元素变化
    // dataChange(data) {
    //   for (let item in this.columns) {
    //     const key = this.columns[item].key;
    //     this.columns[item].visible = !data.includes(key);
    //   }
    // },
    // // 打开显隐列dialog
    // showColumn() {
    //   this.open = true;
    // },
    onColumnCheckChange(val){
      this.$emit('columns-change',val)
    }
  },
};
</script>
<style lang="scss" scoped>
::v-deep .el-transfer__button {
  border-radius: 50%;
  padding: 12px;
  display: block;
  margin-left: 0px;
}

::v-deep .el-transfer__button:first-child {
  margin-bottom: 10px;
}
</style>
