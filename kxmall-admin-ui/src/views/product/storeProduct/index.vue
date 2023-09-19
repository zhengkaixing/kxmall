<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item label="" prop="storeName">
        <el-input v-model="queryParams.storeName" placeholder="请输入商品名称" clearable />
      </el-form-item>
      <el-form-item label="" prop="cateId">
        <TreeSelect
          v-model="queryParams.cateId"
          :options="options"
          placeholder="选择商品分类"
          no-options-text="没有数据"
          clearable
          style="display: inline-block; width: 215px;"
        />
      </el-form-item>
      <el-form-item label="" prop="isShow">
        <el-select v-model="queryParams.isShow" placeholder="请选择商品状态" clearable>
          <el-option label="已上架" :value="1" />
          <el-option label="已下架" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button
          v-hasPermi="['product:storeProduct:add']"
          type="danger"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
        <el-button
          v-hasPermi="['product:storeProductRule:edit']"
          size="mini"
          type="primary"
          :disabled="multiple"
          @click="onAuthorize"
        >授权商品到仓库</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="storeProductList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" />
      <el-table-column type="index" label="序号" align="center">
        <template slot-scope="{$index}">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="商品图片" align="center">
        <template slot-scope="{row}">
          <el-link :href="row.image|getStringOSSURL" target="_blank" :underline="false">
            <el-image :src="row.image|getStringOSSURL" title="点击打开" class="el-avatar" />
          </el-link>
        </template>
      </el-table-column>
      <el-table-column prop="storeName" label="商品名称" header-align="center" align="left" />
      <el-table-column prop="storeCategory.cateName" label="分类名称" header-align="center" align="left" />
      <el-table-column prop="price" label="商品价格" header-align="center" align="right" />
      <el-table-column prop="sales" label="销量" align="center" />
      <el-table-column prop="stock" label="库存" align="center" />
      <el-table-column label="商品类型" align="center">
        <template slot-scope="{row}">
          <el-tag v-if="row.isIntegral === 1" :type="'warning'">积分商品</el-tag>
          <el-tag v-else :type="'info'">普通商品</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center">
        <template slot-scope="{row}">
          <div @click="switchShow($index)">
            <el-tag v-if="row.isShow === 1" style="cursor: pointer">已上架</el-tag>
            <el-tag v-else style="cursor: pointer" :type="'info'">已下架</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="{row}">
          <el-button
            v-hasPermi="['product:storeProductRule:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(row)"
          >修改</el-button>
          <el-button
            v-hasPermi="['product:storeProductRule:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改商品对话框 -->
    <el-dialog
      ref="dialog"
      :title="title"
      :visible.sync="open"
      width="1400px"
      append-to-body
      custom-class="store-product-dialog"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="24">
          <!-- 商品信息-->
          <el-col :span="8">
            <el-form-item label="商品名称" prop="storeName">
              <el-input v-model="form.storeName" placeholder="请输入商品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="商品分类" prop="cateId">
              <TreeSelect v-model="form.cateId" :options="options" placeholder="选择商品分类" no-options-text="没有数据" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="商品关键字" prop="keyword">
              <el-input v-model="form.keyword" placeholder="请输入商品关键字" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="单位" prop="unitName">
              <el-input v-model="form.unitName" placeholder="请输入单位" />
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="商品简介" prop="storeInfo">
              <el-input v-model="form.storeInfo" type="textarea" :rows="3" placeholder="请输入商品简介" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="商品封面图" prop="image">
              <imageUpload v-model="form.image" :limit="1" value-type="json" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="商品轮播图" prop="sliderImage">
              <imageUpload v-model="form.sliderImage" :limit="4" value-type="json" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="开启积分兑换" props="isIntegral">
              <el-radio-group v-model="form.isIntegral">
                <el-radio :label="0">不开启</el-radio>
                <el-radio :label="1">开启</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="商品规格" props="specType">
              <el-radio-group v-model="form.specType" @change="onSpecTypeChange">
                <el-radio :label="0">单规格</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="form.specType === 1">
          <!-- 多规格添加-->
          <el-col :span="24">
            <el-form-item label="选择规格" prop="selectRule">
              <el-select v-model="form.selectRule" style="width: 23%;">
                <el-option v-for="(item, index) in ruleList" :key="index" :value="item.ruleName">
                  {{ item.ruleName }}
                </el-option>
              </el-select>
              <el-button type="primary" style="margin-left: 5px;" size="mini" @click="onRuleSelect">确定</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <template v-if="showRuleValue">
          <el-row v-for="(item, index) in currentRuleValue" :key="index" style="margin-top: 15px;padding:0 20px">
            <el-col :span="24">
              <el-row style="margin-bottom: 15px;">
                <el-col :span="2" style="height: 1px;" />
                <el-col :span="22">
                  <el-tag closable type="info" size="medium" @close="onDelRuleValue(index)">
                    {{ item.value }}
                  </el-tag>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="2" style="height: 1px;" />
                <el-col :span="22">
                  <el-tag
                    v-for="(detail, i) in item.detail"
                    :key="i"
                    closable
                    style="margin-right: 8px;margin-bottom: 8px;"
                    size="medium"
                    @close="onDelRuleValueDetail(index, i)"
                  >
                    {{ detail }}
                  </el-tag>
                  <el-input v-model="ruleValueDetails[index]" placeholder="请输入属性名" style="width: 18%;">
                    <el-button slot="append" @click="onAddRuleValueDetail(index)">添加</el-button>
                  </el-input>
                </el-col>
              </el-row>
            </el-col>
          </el-row>
          <el-row style="margin-top: 15px;padding:0 20px">
            <el-col :span="2" style="height: 1px;" />
            <el-col :span="22">
              <el-form :model="ruleValueDetailAdd" inline>

                <el-form-item label="规格">
                  <el-input v-model="ruleValueDetailAdd.name" placeholder="请输入规格" />
                </el-form-item>
                <el-form-item label="规格值" label-width="80px">
                  <el-input v-model="ruleValueDetailAdd.value" placeholder="请输入规格值" />
                </el-form-item>
                <el-form-item label-width="10px">
                  <el-button type="primary" size="mini" @click="onAddRuleValue">添加新规格</el-button>
                  <el-button type="success" size="mini" @click="generateAddRuleValue">立即生成</el-button>
                </el-form-item>
              </el-form>
            </el-col>
          </el-row>
        </template>
        <el-row v-if="form.specType === 1 && form.header.length !== 0 && attrs.length !== 0">
          <!-- 多规格设置-->
          <!-- 多规格表格-->
          <el-col :span="24">
            <el-form-item label="商品属性">
              <el-table :data="attrs" size="small">
                <el-table-column
                  v-for="(item, index) in form.header"
                  :key="index"
                  :label="item.title"
                  :property="item.slot"
                  align="center"
                >
                  <template slot-scope="{row,$index,column}">
                    <div v-if="column.property == 'pic'">
                      <imageUpload
                        v-model="row.pic"
                        :limit="1"
                        value-type="json"
                        :is-show-tip="false"
                        class="table-image-upload"
                      />
                    </div>
                    <div v-else-if="column.property.indexOf('value') != -1">
                      {{ row[column.property] }}
                    </div>
                    <div v-else-if="column.property == 'action'">
                      <el-button type="danger" size="mini" @click="delAttr($index)">删除</el-button>
                    </div>
                    <div v-else>
                      <el-input v-model="row[column.property]" />
                    </div>
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <!-- 单规格表格-->
          <el-col v-if="form.specType === 0" :span="24">
            <el-form-item>
              <el-table :data="attr" size="small">
                <el-table-column label="图片" align="center">
                  <template slot-scope="{row}">
                    <imageUpload
                      v-model="row.pic"
                      :limit="1"
                      value-type="json"
                      :is-show-tip="false"
                      class="table-image-upload"
                    />
                  </template>
                </el-table-column>
                <el-table-column label="售价" align="center">
                  <template slot-scope="{row}">
                    <el-input v-model="row.price" type="text" />
                  </template>
                </el-table-column>
                <el-table-column label="成本价" align="center">
                  <template slot-scope="{row}">
                    <el-input v-model="row.cost" type="text" />
                  </template>
                </el-table-column>
                <el-table-column label="原价" align="center">
                  <template slot-scope="{row}">
                    <el-input v-model="row.otPrice" type="text" />
                  </template>
                </el-table-column>
                <el-table-column label="库存" align="center">
                  <template slot-scope="{row}">
                    <el-input v-model="row.stock" type="text" maxlength="7" />
                  </template>
                </el-table-column>
                <el-table-column label="商品编号" align="center">
                  <template slot-scope="{row}">
                    <el-input v-model="row.barCode" type="text" />
                  </template>
                </el-table-column>
                <el-table-column label="重量（KG）" align="center">
                  <template slot-scope="{row}">
                    <el-input v-model="row.weight" type="text" />
                  </template>
                </el-table-column>
                <el-table-column label="体积（m³）" align="center">
                  <template slot-scope="{row}">
                    <el-input v-model="row.volume" type="text" />
                  </template>
                </el-table-column>
                <el-table-column label="所需兑换积分" align="center" width="120">
                  <template slot-scope="{row}">
                    <el-input v-model="row.integral" type="text" />
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="商品详情" prop="description">
              <editor v-model="form.description" :min-height="192" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="虚拟销量" prop="ficti">
              <el-input-number v-model="form.ficti" :min="0" placeholder="请输入虚拟销量" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="购买返回积分" prop="giveIntegral">
              <el-input-number v-model="form.giveIntegral" :min="0" placeholder="请输入积分" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="排序">
              <el-input-number v-model="form.sort" :min="0" placeholder="请输入排序" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="佣金设置">
              <el-radio-group v-model="form.isSub">
                <el-radio :label="1" class="radio">单独设置</el-radio>
                <el-radio :label="0">默认设置</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="form.isSub === 1">
          <el-col :span="24">
            <!--单规格返佣-->
            <el-form-item v-if="form.specType === 0" label="商品属性">
              <el-table :data="attr" size="small">
                <el-table-column label="图片" align="center">
                  <template slot-scope="{row}">
                    <el-image :src="row.pic">
                      <div slot="error">
                        <i class="el-icon-picture-outline" />
                      </div>
                    </el-image>
                  </template>
                </el-table-column>
                <el-table-column prop="price" label="售价" align="center" />
                <el-table-column prop="cost" label="成本价" align="center" />
                <el-table-column prop="otPrice" label="原价" align="center" />
                <el-table-column prop="stock" label="库存" align="center" />
                <el-table-column prop="barCode" label="商品编号" align="center" />
                <el-table-column prop="weight" label="重量（KG）" align="center" width="100" />
                <el-table-column prop="volume" label="体积（m³）" align="center" width="100" />
                <el-table-column label="一级返佣" align="center">
                  <template slot-scope="{row}">
                    <el-input v-model="row.brokerage" type="text" />
                  </template>
                </el-table-column>
                <el-table-column label="二级返佣" align="center">
                  <template slot-scope="{row}">
                    <el-input v-model="row.brokerageTwo" type="text" />
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>
            <el-form-item v-if="form.specType === 1" label="商品属性">
              <el-table :data="attrs" size="small">
                <el-table-column label="图片" align="center">
                  <template slot-scope="{row}">
                    <el-image :src="row.pic" :width="60" :height="60">
                      <div slot="error">
                        <i class="el-icon-picture-outline" />
                      </div>
                    </el-image>
                  </template>
                </el-table-column>
                <el-table-column prop="sku" label="规格" align="center" />
                <el-table-column prop="price" label="售价" align="center" />
                <el-table-column prop="cost" label="成本价" align="center" />
                <el-table-column prop="otPrice" label="原价" align="center" />
                <el-table-column prop="stock" label="库存" align="center" />
                <el-table-column prop="barCode" label="商品编号" align="center" />
                <el-table-column prop="weight" label="重量（KG）" align="center" width="100" />
                <el-table-column prop="volume" label="体积（m³）" align="center" width="100" />
                <el-table-column prop="integral" label="所需兑换积分" align="center" width="120" />
                <el-table-column label="一级返佣" align="center">
                  <template slot-scope="{row}">
                    <el-input v-model="row.brokerage" type="text" />
                  </template>
                </el-table-column>
                <el-table-column label="二级返佣" align="center">
                  <template slot-scope="{row}">
                    <el-input v-model="row.brokerageTwo" type="text" />
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="商品状态">
              <el-radio-group v-model="form.isShow">
                <el-radio :label="1">上架</el-radio>
                <el-radio :label="0">下架</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="热卖单品">
              <el-radio-group v-model="form.isHot">
                <el-radio :label="1">开启</el-radio>
                <el-radio :label="0">关闭</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="猜你喜欢">
              <el-radio-group v-model="form.isBenefit">
                <el-radio :label="1">开启</el-radio>
                <el-radio :label="0">关闭</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="精品推荐">
              <el-radio-group v-model="form.isBest">
                <el-radio :label="1">开启</el-radio>
                <el-radio :label="0">关闭</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="首发新品">
              <el-radio-group v-model="form.isNew">
                <el-radio :label="1">开启</el-radio>
                <el-radio :label="0">关闭</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="选择仓库" :visible.sync="storage.show" width="300px">
      <div style="text-align: center;">
        <el-select v-model="storage.id" :placeholder="`请选择仓库`" clearable>
          <el-option v-for="item in storage.list" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="confirmAuthorize">确 定</el-button>
        <el-button @click="storage.show=false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStoreProduct, getStoreProduct, delStoreProduct, addStoreProduct, updateStoreProduct, isFormatAttr, onsale, batchAuthorizeGoods } from '@/api/product/storeProduct'
import { listStoreCategoryTree } from '@/api/product/storeCategory'
import { listAllStorage } from '@/api/storage/storage'
import getStringOSSURL from '@/mixin/getStringOSSURL'

export default {
  name: 'StoreProduct',
  mixins: [getStringOSSURL],
  data() {
    return {
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 商品表格数据
      storeProductList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        storeName: undefined,
        cateId: undefined,
        isShow: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        storeName: [
          { required: true, message: '请输入商品名称', trigger: 'blur' }
        ],
        cateId: [
          { required: true, message: '请选择商品分类', trigger: 'change' }
        ],
        keyword: [
          { required: true, message: '请输入商品关键字', trigger: 'blur' }
        ],
        unitName: [
          { required: true, message: '请输入单位', trigger: 'blur' }
        ],
        storeInfo: [
          { required: true, message: '请输入商品简介', trigger: 'blur' }
        ],
        specType: [
          { required: true, message: '请选择商品规格', trigger: 'change' }
        ]
        // selectRule: [
        //   { required: true, message: '请选择商品规格属性', trigger: 'change' }
        // ],
        // tempId: [
        //   { required: true, message: '请选择运费模板', trigger: 'change' }
        // ]
      },
      options: [],
      ruleList: [],
      attrs: [],
      attr: [],
      ruleValueDetailAdd: {},
      currentRuleValue: null,
      ruleValueDetails: [],
      showRuleValue: false,
      storage: {
        show: false,
        id: '',
        list: []
      }
    }
  },
  watch: {
    open(val) {
      if (val) {
        this.$nextTick(() => {
          this.scrollToTop()
        })
      }
    }
  },
  created() {
    this.listStoreCategoryTree()
    this.getList()
  },
  methods: {
    scrollToTop() {
      this.$refs.dialog.$el.getElementsByClassName('el-dialog__body')[0].scrollTop = 0
    },
    listStoreCategoryTree() {
      this.options = []
      return new Promise((resolve, reject) => {
        listStoreCategoryTree().then(({ data }) => {
          const { content } = data
          this.options = [].concat(content)
          resolve()
        }, err => reject(err))
      })
    },
    /** 查询商品列表 */
    getList() {
      this.loading = true
      listStoreProduct(this.queryParams).then(response => {
        this.storeProductList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    resetRuleValue() {
      this.ruleValueDetailAdd = { name: '', value: '' }
      this.currentRuleValue = []
      this.ruleValueDetails = []
      this.showRuleValue = false
    },
    // 表单重置
    reset() {
      this.form = {
        storeName: undefined,
        cateId: undefined,
        unitName: undefined,
        storeInfo: undefined,
        sliderImage: [],
        specType: 0,
        isIntegral: 0,
        selectRule: undefined,
        tempId: undefined,
        description: undefined,
        ficti: undefined,
        giveIntegral: undefined,
        sort: undefined,
        isSub: 0,
        isShow: 1,
        isHot: 1,
        isBenefit: 1,
        isBest: 1,
        isNew: 1,
        header: []
      }
      this.attrs = []
      this.attr = [
        {
          imageArr: [],
          pic: '',
          price: 1,
          cost: 0,
          otPrice: 0,
          stock: 1,
          seckillStock: 0,
          seckillSrice: 0,
          pinkStock: 0,
          pinkPrice: 0,
          barCode: '',
          weight: 0,
          volume: 0,
          brokerage: 0,
          brokerageTwo: 0,
          integral: 0
        }
      ]
      this.ruleList = []
      this.resetRuleValue()
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.loading = true
      this.reset()
      getStoreProduct(0).then(({ data }) => {
        const { ruleList } = data
        this.loading = false
        this.ruleList = ruleList
        this.open = true
        this.title = '添加商品'
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true
      this.reset()
      const id = row.id || this.ids
      getStoreProduct(id).then(({ data }) => {
        const { ruleList, productInfo } = data
        this.loading = false
        this.form = productInfo
        this.form.header = []
        this.ruleList = ruleList
        this.attr = [productInfo.attr]
        if (productInfo.specType === 1) {
          this.currentRuleValue = productInfo.items
        }
        if (this.form.specType === 1) {
          this.showRuleValue = true
        }
        this.generateAddRuleValue()
        this.open = true
        this.title = '修改商品'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate((valid, err) => {
        if (valid) {
          this.buttonLoading = true
          if (this.form.specType === 0) {
            this.form.attrs = this.attr
            this.form.header = []
            this.form.items = []
          } else {
            this.form.items = this.currentRuleValue
            this.form.attrs = this.attrs
          }
          if (this.form.specType === 1 && this.attrs.length === 0) {
            return this.$message.warning('请点击生成规格！')
          }
          const params = { ...this.form }
          const { image, sliderImage } = params
          if (typeof image === 'string') {
            params.image = JSON.parse(image)
          }
          if (typeof sliderImage === 'string') {
            params.sliderImage = JSON.parse(sliderImage)
          }
          if (this.form.id != null) {
            updateStoreProduct(params).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            }).finally(() => {
              this.buttonLoading = false
            })
          } else {
            addStoreProduct(params).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            }).finally(() => {
              this.buttonLoading = false
            })
          }
        } else {
          let msg = ''
          for (const field in err) {
            const { message } = err[field][0]
            msg += '；' + message
          }
          this.$message.warning(msg.substring(1))
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除？').then(() => {
        this.loading = true
        return delStoreProduct(ids)
      }).then(() => {
        this.loading = false
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      }).finally(() => {
        this.loading = false
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('product/storeProduct/export', {
        ...this.queryParams
      }, `storeProduct_${new Date().getTime()}.xlsx`)
    },
    switchShow(index) {
      const row = this.storeProductList[index]
      const { isShow, id } = row
      this.$modal.confirm(`确定进行${isShow === 1 ? '下架' : '上架'}操作?`).then(() => {
        const status = isShow === 1 ? 0 : 1
        onsale(id, { status }).then(() => {
          this.$message.success('操作成功')
          this.getList()
        })
      })
    },
    onDelRuleValue(index) {
      this.currentRuleValue.splice(index, 1)
    },
    onDelRuleValueDetail(index, i) {
      this.currentRuleValue[index].detail.splice(i, 1)
    },
    onAddRuleValueDetail(index) {
      const { ruleValueDetails } = this
      const value = ruleValueDetails[index]?.trim()
      if (!value) {
        this.$message.warning('请输入属性名')
        return
      }
      this.currentRuleValue[index].detail.push(value)
      ruleValueDetails.splice(index, 1, '')
    },
    onAddRuleValue() {
      const { ruleValueDetailAdd } = this
      const { name, value } = ruleValueDetailAdd
      if (name.trim() === '' || value.trim() === '') {
        this.$message.warning('请输入规格和规格值')
        return
      }
      this.currentRuleValue.push({ value: name, detail: [value] })
      this.ruleValueDetailAdd = { name: '', value: '' }
    },
    onRuleSelect() {
      this.currentRuleValue = this.ruleList.filter(el => {
        return this.form.selectRule === el.ruleName
      })[0].ruleValue
      this.showRuleValue = true
    },
    generateAddRuleValue() {
      const { currentRuleValue, form } = this
      const { id = 0, specType } = form
      let attrs = []
      if (specType === 1) {
        attrs = currentRuleValue
      }
      isFormatAttr(id, { attrs }).then(({ data }) => {
        const { header, value } = data
        // this.currentRuleValue=attr
        this.form.header = header
        this.attrs = value
      })
    },
    onSpecTypeChange(val) {
      if (val === 0) {
        this.showRuleValue = false
      }
    },
    delAttr(index) {
      this.attrs.splice(index, 1)
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    onAuthorize() {
      this.storage.id = ''
      listAllStorage().then(({ data }) => {
        this.storage.list = data
        this.storage.show = true
      })
    },
    confirmAuthorize() {
      const { storage, ids } = this
      const { id: storageId } = storage
      if (!storageId) {
        return this.$message.warning('请选择仓库')
      }
      batchAuthorizeGoods({ ids, storageId }).then(() => {
        this.$modal.msgSuccess('操作成功')
        storage.show = false
      })
    }
  }
}
</script>
<style  lang="scss" scoped>
::v-deep .vue-treeselect__control {
  height: 31px;
  line-height: 31px;
}

.table-image-upload {
  ::v-deep .el-upload-list--picture-card .el-upload-list__item {
    width: 60px;
    height: 60px;
  }

  ::v-deep .el-upload--picture-card {
    width: 60px;
    height: 60px;
    line-height: 69px;
  }
}
</style>
<style lang="scss">
.store-product-dialog {
  .el-dialog__body {
    height: 600px;
    overflow: auto;
  }
}
</style>
