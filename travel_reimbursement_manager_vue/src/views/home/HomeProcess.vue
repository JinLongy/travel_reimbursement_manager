<template>
  <div>
    <el-card class="common-card">
      <div class="header">
        <div class="main-title-row">流程列表</div>
        <!-- <el-button @click="showDialog = true" class="submit-btn">新增流程</el-button> -->
        <el-button @click="saveProcessModel" class="submit-btn">新增流程</el-button>
      </div>

      <!--表格-->
      <el-table
        ref="reimbursementTable"
        :data="tab1Data"
        stripe
        style="width: 100%; font-size: 15px"
        height="550px"
        @select="selectOneTravelInfo"
        @select-all="selectAllTravelInfo"
        :header-cell-style="{ background: '#bababe', color: 'black' }"
        :border="true"
        :row-key="getRowKeys"
      >
        <el-table-column prop="id" label="序号" width="100" align="center"> </el-table-column>
        <el-table-column prop="modelKey" label="流程名称" align="center"> </el-table-column>
        <el-table-column prop="modelName" label="业务场景" align="center"> </el-table-column>
        <el-table-column prop="userName" width="120" label="提交人" align="center"> </el-table-column>
        <el-table-column prop="userName" width="120" label="更新人" align="center"> </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center" :formatter="statusFormatter"> </el-table-column>
        <el-table-column prop="updateTime" label="最后更新时间" width="180" align="center" :formatter="formatDate">
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" align="center" :formatter="formatDate"> </el-table-column>
        <el-table-column label="操作" width="280" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click="viewModelXml(scope.row)"> 查看 </el-button>
            <el-button type="text" @click="editInfo(scope.row)" :disabled="scope.row.status === 1"> 编辑 </el-button>
          </template>
        </el-table-column>
        <div slot="empty">
          <el-empty description="暂无数据"></el-empty>
        </div>
      </el-table>

      <!--分页功能-->
      <div id="page">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="query.page"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="query.limit"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          :background="true"
        >
        </el-pagination>
      </div>
    </el-card>
    <el-dialog title="新增流程" :visible.sync="showDialog" width="400px">
      <el-form :model="xmlSaveRequest">
        <el-form-item label="流程名称">
          <el-input v-model="xmlSaveRequest.modelName"></el-input>
        </el-form-item>
        <el-form-item label="业务场景">
          <el-input v-model="xmlSaveRequest.modelKey"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelAddModel">取 消</el-button>
        <el-button @click="saveProcessModel" class="submit-btn">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      showDialog: false,
      loadingTab1: false,
      tab1Data: [],
      // 已登录的用户名
      loggedName: sessionStorage.getItem('loggedName'),
      dialogTableVisible: false,
      // 查询条件
      query: {
        empno: sessionStorage.getItem('uid'),
        reimbursementDate: '',
        // 默认当前页为 1
        page: 1,
        limit: 6,
      },
      // 数据总条数，默认为0
      total: 0,
      // 新增差旅弹出层是否显示
      dialogFormVisible: false,
      // 查看差旅弹出层
      showTravelInfoVisible: false,
      // 查看被驳回的差旅弹出层
      showRejectedTravelInfoVisible: false,
      // 弹出层左侧提示文字的宽度
      formLabelWidth: '120px',
      // 获取查询时间
      searchTime: '',
      modelCount: 10,
      definitionCount: 10,
      instanceCount: 0,
      processDefinitionKey: 'Process_1',
      xmlSaveRequest: {
        xmlStr: null,
        modelKey: null,
        modelName: null,
        user: null,
      },
    }
  },
  methods: {
    reimbursement() {
      this.$router.push('/reimbursement')
    },
    fetchDataForTab1() {
      this.loadingTab1 = true
      let data = {
        empno: 3,
        page: 1,
        limit: 10,
      }
      this.$http
        .get('/process/modelMetaList')
        .then((response) => {
          this.tab1Data = response.data
          this.modelCount = this.tab1Data.length
        })
        .catch((error) => {
          console.error('Error fetching data for tab1:', error)
        })
        .finally(() => {
          this.loadingTab1 = false
        })
    },
    saveProcessModel() {
      /* this.xmlSaveRequest.user = sessionStorage.getItem('loggedName')
      this.$http.post('/process/saveModelMeta', this.xmlSaveRequest).then((res) => {
        console.log(res)
        this.$message.info('新增模型定义成功，请点击编辑按钮操作流程编辑')
      })
      this.showDialog = false */
      this.$router.push('/processPanel')
    },
    cancelAddModel() {
      this.xmlSaveRequest.modelKey = null
      this.xmlSaveRequest.modelName = null
      this.showDialog = false
    },
    getRowKeys(row) {
      // id 是后台传递的每行信息唯一标识
      return row.travelId
    },
    // 选中一条差旅信息
    selectOneTravelInfo(selection) {
      this.selectedItems = selection
    },
    // 选中全部差旅信息
    selectAllTravelInfo(selection) {
      this.selectedItems = selection
    },
    statusFormatter(cellValue) {
      if (cellValue.status == null || cellValue.status === 0) {
        return '编辑中'
      } else {
        return '已发布'
      }
    },
    instanceFormatter(cellValue) {
      if (cellValue.isEnded == true) {
        return '已结束'
      } else {
        return '进行中'
      }
    },
    formatDate(cellValue) {
      let v = cellValue.createTime
      if (v == null) {
        v = cellValue.updateTime
      }
      let d = new Date(v)
      let year = d.getFullYear()
      let month = ('0' + (d.getMonth() + 1)).slice(-2)
      let day = ('0' + d.getDate()).slice(-2)
      let hour = ('0' + d.getHours()).slice(-2)
      let minutes = ('0' + d.getMinutes()).slice(-2)
      return `${year}年${month}月${day}日 ${hour}:${minutes}`
    },
    // 获取流程模版列表
    getModelList() {
      this.$http
        .get('/process/modelMetaList')
        .then((res) => {
          this.tab1Data = res.data
          this.total = res.data.length
        })
        .catch((err) => {
          this.$message.error('查询审批流程模版列表失败，请联系管理员')
        })
    },
    viewModelXml(row) {
      this.$router.push({ path: '/panel', query: { rowData: row, mode: 'view' } })
    },
    editInfo(row) {
      this.$router.push({ path: '/panel', query: { rowData: row, mode: 'edit' } })
    },
    publish(row) {
      console.log(row)
      const xml = row.xmlStr
      //xml转json,用json处理后在转xml
      const jsonObj = this.$x2js.xml2js(xml)
      this.getFormProperty(jsonObj)
      let newXml = this.$x2js.js2xml(jsonObj)

      this.xmlSaveRequest.xmlStr = newXml
      this.xmlSaveRequest.id = row.id
      this.xmlSaveRequest.modelName = row.modelName
      this.xmlSaveRequest.modelKey = row.modelKey

      this.$http
        .post('/deployXml', this.xmlSaveRequest)
        .then((res) => {
          this.$message.info('发布流程模版定义')
        })
        .catch((err) => {
          this.$message.error('保存xml数据失败，请联系管理员~')
        })
    },
    delete(row) {},
    getFormProperty(json) {
      for (let e in json) {
        if (e == 'extensionElements' && json.extensionElements.formData && json.extensionElements.formData.formField) {
          let formProperty = JSON.parse(JSON.stringify(json.extensionElements.formData.formField))
          if (this.isArrayFn(formProperty)) {
            formProperty.forEach((x) => {
              x.__prefix = 'activiti'
              x._name = x._label
              if (x._defaultValue) {
                x._default = x._defaultValue
              }
            })
          } else {
            formProperty.__prefix = 'activiti'
            formProperty._name = formProperty._label
            if (formProperty._defaultValue) {
              formProperty._default = formProperty._defaultValue
            }
          }
          json.extensionElements.formProperty = formProperty
          delete json.extensionElements.formData
        } else if (e.includes('camunda:')) {
          let str = e.replace('camunda:', 'activiti:')
          json[str] = json[e]
          delete json[e]
        } else if (typeof json[e] == 'object') {
          this.getFormProperty(json[e])
        }
      }
    },
    queryInstance(row) {
      this.$router.push({ path: '/instance', query: { rowData: row, mode: 'view' } })
    },
    // 当每页多少条被改变时，val代表改变后的每页多少条
    handleSizeChange(val) {
      this.query.limit = val
      this.search()
    },
    // 当前页被改变时，val代表当前页
    handleCurrentChange(val) {
      this.query.page = val
      this.getModelList()
    },
    // 自定义索引
    calIndex(index) {
      return index + 1 + (this.query.page - 1) * this.query.limit
    },
    search() {
      this.query.page = 1
      this.getModelList()
    },
  },
  watch: {},
  created() {
    this.fetchDataForTab1()
  },
}
</script>

<style scoped lang="less">
.header {
  .flex_arrange(row, space-between);
  margin-bottom: 20px;
  .main-title-row {
    font-size: 18px;
    text-align: left;
    font-weight: 900;
  }
}
.submit-btn {
  background: @primary-color;
  color: @white-color;
}
</style>
