<template>
  <div>
    <el-select v-model="form.province" placeholder="请选择省" style="width:165px" clearable filterable @change="changeProvince">
      <el-option
        v-for="item in province"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      />
    </el-select>
    <el-select v-model="form.city" placeholder="请选择市" style="width:165px" clearable filterable @change="changeCity">
      <el-option
        v-for="item in city"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      />
    </el-select>
    <el-select v-model="form.county" placeholder="请选择区" style="width:165px" clearable filterable @change="changeCounty">
      <el-option
        v-for="item in county"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      />
    </el-select>
  </div>
</template>
<script>
import { getProvinceAll, getCity, getCounty } from '@/api/region/region'
const formModel = {
  city: '',
  province: '',
  county: ''
}
export default {
  name: 'RegionSelector',
  props: {
    value: {
      type: Object,
      default() {
        return formModel
      }
    }
  },
  data() {
    return {
      form: {},
      city: [],
      province: [],
      county: []
    }
  },
  watch: {
    value: {
      handler(val) {
        this.resetForm()
        if (val) {
          this.form = val
          this.getCity()
          this.getCounty()
        }
      },
      immediate: true
    }
  },
  created() {
    this.getProvinceAll()
  },
  methods: {
    resetForm() {
      for (const key in formModel) {
        formModel[key] = ''
      }
      this.form = formModel
    },
    emitValue() {
      this.$emit('input', this.form)
    },
    getProvinceAll() {
      getProvinceAll().then(res => {
        this.province = res
      })
    },
    getCity() {
      if (this.form.province === '') {
        return
      }
      getCity({ provinceId: this.form.province }).then(res => {
        this.city = res
      })
    },
    getCounty() {
      if (this.form.city === '') {
        return
      }
      getCounty({ cityId: this.form.city }).then(res => {
        this.county = res
      })
    },
    changeProvince() {
      this.form.city = ''
      this.form.county = ''
      this.city = []
      this.county = []
      this.emitValue()
      this.getCity()
    },
    changeCity() {
      this.form.county = ''
      this.county = []
      this.emitValue()
      this.getCounty()
    },
    changeCounty() {
      this.emitValue()
    }
  }
}
</script>
<style  lang="scss" scoped>
::v-deep .el-select {
  margin-right: 10px;
}
</style>
