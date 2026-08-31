<template>
  <el-tree-select
    v-model="innerValue"
    :data="treeData"
    :props="treeProps"
    :placeholder="placeholder"
    :clearable="clearable"
    :filterable="filterable"
    :disabled="disabled"
    check-strictly
    :render-after-expand="false"
    :default-expand-all="defaultExpandAll"
    style="width: 100%"
  />
</template>

<script>
export default {
  name: 'TreeSelect',
  props: {
    modelValue: {
      default: undefined
    },
    value: {
      default: undefined
    },
    options: {
      type: Array,
      default: () => []
    },
    placeholder: {
      type: String,
      default: '请选择'
    },
    clearable: {
      type: Boolean,
      default: true
    },
    filterable: {
      type: Boolean,
      default: true
    },
    disabled: {
      type: Boolean,
      default: false
    },
    disableBranchNodes: {
      type: Boolean,
      default: false
    },
    showCount: {
      type: Boolean,
      default: false
    },
    normalizer: {
      type: Function,
      default: null
    },
    defaultExpandAll: {
      type: Boolean,
      default: false
    }
  },
  emits: ['update:modelValue', 'input'],
  computed: {
    innerValue: {
      get() {
        return this.modelValue !== undefined ? this.modelValue : this.value
      },
      set(val) {
        this.$emit('update:modelValue', val)
        this.$emit('input', val)
      }
    },
    treeProps() {
      return { value: 'id', label: 'label', children: 'children', disabled: 'disabled' }
    },
    treeData() {
      return this.transform(this.options || [])
    }
  },
  methods: {
    transform(list) {
      if (!Array.isArray(list)) return []
      return list.map(node => {
        const mapped = this.normalizer ? this.normalizer({ ...node }) : node
        const childrenSrc = mapped.children || node.children
        const item = {
          id: mapped.id !== undefined ? mapped.id : (node.id !== undefined ? node.id : node.value),
          label: mapped.label !== undefined ? mapped.label : (node.label || node.name),
          disabled: !!mapped.isDisabled
        }
        if (childrenSrc && childrenSrc.length) {
          item.children = this.transform(childrenSrc)
        }
        return item
      })
    }
  }
}
</script>
