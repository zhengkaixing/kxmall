<script lang="jsx">
import draggable from 'vuedraggable'
import render from '@/utils/generator/render'
import { CopyDocument, Delete } from '@element-plus/icons-vue'

const components = {
  itemBtns(h, element, index, parent) {
    const { copyItem, deleteItem } = this.$attrs
    return [
      <span class="drawing-item-copy" title="复制" onClick={event => {
        copyItem(element, parent); event.stopPropagation()
      }}>
        <el-icon><CopyDocument /></el-icon>
      </span>,
      <span class="drawing-item-delete" title="删除" onClick={event => {
        deleteItem(index, parent); event.stopPropagation()
      }}>
        <el-icon><Delete /></el-icon>
      </span>
    ]
  }
}
const layouts = {
  colFormItem(h, element, index, parent) {
    const { activeItem } = this.$attrs
    let className = this.activeId === element.formId ? 'drawing-item active-from-item' : 'drawing-item'
    if (this.formConf.unFocusedComponentBorder) className += ' unfocus-bordered'
    return (
      <el-col span={element.span} class={className}
        onClick={event => { activeItem(element); event.stopPropagation() }}>
        <el-form-item label-width={element.labelWidth ? `${element.labelWidth}px` : null}
          label={element.label} required={element.required}>
          <render key={element.renderKey} conf={element} onInput={ event => {
            element['defaultValue'] = event
          }} />
        </el-form-item>
        {components.itemBtns.apply(this, arguments)}
      </el-col>
    )
  },
  rowFormItem(h, element, index, parent) {
    const { activeItem } = this.$attrs
    const className = this.activeId === element.formId ? 'drawing-row-item active-from-item' : 'drawing-row-item'
    const that = this
    if (element.type === 'flex') {
      return (
        <el-col span={element.span}>
          <el-row gutter={element.gutter} class={className}
            onClick={event => { activeItem(element); event.stopPropagation() }}>
            <span class="component-name">{element.componentName}</span>
            <el-row type={element.type} justify={element.justify} align={element.align}>
              <draggable list={element.children} item-key="renderKey" animation={340} group="componentsGroup" class="drag-wrapper">
                {{
                  item: ({ element: childEl, index: i }) => layouts[childEl.layout].call(that, h, childEl, i, element.children)
                }}
              </draggable>
            </el-row>
            {components.itemBtns.apply(this, arguments)}
          </el-row>
        </el-col>
      )
    }
    return (
      <el-col span={element.span}>
        <el-row gutter={element.gutter} class={className}
          onClick={event => { activeItem(element); event.stopPropagation() }}>
          <span class="component-name">{element.componentName}</span>
          <draggable list={element.children} item-key="renderKey" animation={340} group="componentsGroup" class="drag-wrapper">
            {{
              item: ({ element: childEl, index: i }) => layouts[childEl.layout].call(that, h, childEl, i, element.children)
            }}
          </draggable>
          {components.itemBtns.apply(this, arguments)}
        </el-row>
      </el-col>
    )
  }
}

function layoutIsNotFound() {
  throw new Error(`没有与${this.element.layout}匹配的layout`)
}

export default {
  components: {
    render,
    draggable
  },
  props: [
    'element',
    'index',
    'drawingList',
    'activeId',
    'formConf'
  ],
  render() {
    const layout = layouts[this.element.layout]

    if (layout) {
      return layout.call(this, null, this.element, this.index, this.drawingList)
    }
    return layoutIsNotFound()
  }
}
</script>
