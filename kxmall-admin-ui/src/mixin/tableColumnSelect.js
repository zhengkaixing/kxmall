export default{
  data(){
    return {
      selectedColumnsProp:[]
    }
  },
  created(){
    this.selectedColumnsProp=this.columns.map(el=>el.prop)
  },
  computed:{
    filteredColumns(){
      return this.columns.filter(el=>this.selectedColumnsProp.indexOf(el.prop)>-1)
    }
  },
  methods:{
    onColumnChange(val){
      this.selectedColumnsProp=[].concat(val)
    }
  }
}