import { h } from 'vue'
import { makeMap } from '@/utils/index'
import { Plus, Upload } from '@element-plus/icons-vue'

const isAttr = makeMap(
  'accept,accept-charset,accesskey,action,align,alt,async,autocomplete,'
  + 'autofocus,autoplay,autosave,bgcolor,border,buffered,challenge,charset,'
  + 'checked,cite,class,code,codebase,color,cols,colspan,content,http-equiv,'
  + 'name,contenteditable,contextmenu,controls,coords,data,datetime,default,'
  + 'defer,dir,dirname,disabled,download,draggable,dropzone,enctype,method,for,'
  + 'form,formaction,headers,height,hidden,high,href,hreflang,http-equiv,'
  + 'icon,id,ismap,itemprop,keytype,kind,label,lang,language,list,loop,low,'
  + 'manifest,max,maxlength,media,method,GET,POST,min,multiple,email,file,'
  + 'muted,name,novalidate,open,optimum,pattern,ping,placeholder,poster,'
  + 'preload,radiogroup,readonly,rel,required,reversed,rows,rowspan,sandbox,'
  + 'scope,scoped,seamless,selected,shape,size,type,text,password,sizes,span,'
  + 'spellcheck,src,srcdoc,srclang,srcset,start,step,style,summary,tabindex,'
  + 'target,title,type,usemap,value,width,wrap'
)

function vModel(self, dataObject, defaultValue) {
  dataObject.modelValue = defaultValue
  dataObject['onUpdate:modelValue'] = val => {
    self.$emit('update:modelValue', val)
    self.$emit('input', val)
  }
}

export default {
  props: ['conf'],
  render() {
    const dataObject = {}
    const confClone = JSON.parse(JSON.stringify(this.conf))
    const children = []

    const tag = confClone.tag
    if (tag === 'el-button' && confClone.default) {
      children.push(confClone.default)
    }
    if (tag === 'el-select' && confClone.options) {
      confClone.options.forEach(item => {
        children.push(h('el-option', { label: item.label, value: item.value, disabled: item.disabled }))
      })
    }
    if (tag === 'el-radio-group' && confClone.options) {
      confClone.options.forEach(item => {
        if (confClone.optionType === 'button') {
          children.push(h('el-radio-button', { label: item.value }, () => item.label))
        } else {
          children.push(h('el-radio', { label: item.value, border: confClone.border }, () => item.label))
        }
      })
    }
    if (tag === 'el-checkbox-group' && confClone.options) {
      confClone.options.forEach(item => {
        if (confClone.optionType === 'button') {
          children.push(h('el-checkbox-button', { label: item.value }, () => item.label))
        } else {
          children.push(h('el-checkbox', { label: item.value, border: confClone.border }, () => item.label))
        }
      })
    }
    if (tag === 'el-upload') {
      if (confClone['list-type'] === 'picture-card') {
        children.push(h('el-icon', null, () => h(Plus)))
      } else {
        children.push(h('el-button', { size: 'small', type: 'primary', icon: Upload }, () => confClone.buttonText))
      }
    }

    Object.keys(confClone).forEach(key => {
      const val = confClone[key]
      if (key === 'vModel') {
        vModel(this, dataObject, confClone.defaultValue)
      } else if (key !== 'tag' && key !== 'options' && key !== 'default') {
        if (!isAttr(key) || true) {
          dataObject[key] = val
        }
      }
    })
    return h(this.conf.tag, dataObject, () => children)
  }
}
