<script>
export default {
  props:{
    index:{
      type:Number
    },
    row:{
      type:Object
    },
    column:{
      type:Object
    },
    colConfig:{
      type:Object
    },
    render:{
      type:Function,
      required:true
    }
  },
  render (h) {
    return this.render(h,this.$props)
  }
}
</script>