<template>
  <el-container>
    <!-- Main Section -->
    <el-main>
      <el-card class="common-card">
        <el-row type="flex" align="center" justify="space-between">
          <el-col :span="7">
            <div class="container blue">
              <div class="title-row">
                <i class="el-icon-s-ticket"> </i>
                <span class="title blue-title">差旅</span>
                <span class="des">国内、国际差旅、异地派遣</span>
              </div>
              <div class="button-row">
                <el-button class="blue-title" size="mini" @click="recipeSQD">差旅申请</el-button>
                <el-button class="blue-title" size="mini" @click="recipeBDX">差旅报销</el-button>
              </div>
            </div>
          </el-col>
          <el-col :span="7">
            <div class="container yellow">
              <div class="title-row">
                <i class="el-icon-s-ticket"> </i>
                <span class="title yellow-title">加班</span>
                <span class="des">工作日、周末、法定节假日加班</span>
              </div>
              <div class="button-row">
                <el-button class="yellow-title" size="mini" @click="workOverTime">加班申请</el-button>
              </div>
            </div>
          </el-col>
          <el-col :span="7">
            <div class="container pink">
              <div class="title-row">
                <i class="el-icon-s-ticket"> </i>
                <span class="title pink-title">报销</span>
                <span class="des">晚餐补贴、团建、交通等</span>
              </div>
              <div class="button-row">
                <el-button class="pink-title" size="mini" @click="reimbursement">发起报销</el-button>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>
      <el-card class="common-card" style="margin-top: 20px">
        <div class="main-title-row">我的单据</div>
        <div v-if="loadingTab1"></div>
        <div v-else>
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
            <el-table-column prop="id" label="单据信息" width="160" align="center"> </el-table-column>
            <el-table-column prop="type" label="业务类型" width="100" align="center"> </el-table-column>
            <el-table-column prop="status" label="状态" width="120" align="center"> </el-table-column>
            <el-table-column prop="remark" label="事由" align="center"> </el-table-column>
            <el-table-column prop="feeTotal" label="金额" width="120" align="center" :formatter="formatMoney"> </el-table-column>
            <el-table-column prop="createTime" label="报销时间" width="130" align="center" :formatter="formatDate">
            </el-table-column>
            <el-table-column label="操作" width="280" align="center">
              <template slot-scope="scope">
                <el-button type="text" @click="editInfo(scope.row)">编辑 </el-button>
                <el-button type="text" style="margin-left: 0" @click="copyInfo(scope.row)">复制 </el-button>
                <el-button type="text" style="margin-left: 0" @click="deleteInfo(scope.row)">删除 </el-button>
                <el-button type="text" style="margin-left: 0" @click="approval(scope.row)">审批 </el-button>
              </template>
            </el-table-column>
            <div slot="empty">暂无数据</div>
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
        </div>
      </el-card>
    </el-main>
  </el-container>
</template>

<script>
export default {
  data() {
    return {
      uid: sessionStorage.getItem('uid'),
      loadingTab1: false,
      tab1Data: [],
      // 点击查看时当前行差旅信息的编号
      selectedTravelId: '',
      // 被选中项的集合
      selectedItems: [],
      // 保存差旅信息时需要向后台传递的内容
      associateTickets: {
        // 新增补助信息数据源
        subsidyForm: [],
        // 车票关联差旅信息的数据源
        tickets: [],
        travelForm: {
          // 驳回原因
          rejectionReason: '',
          // 申请差旅报销员工编号
          empno: sessionStorage.getItem('uid'),
          // 出差事由
          travelDesc: '',
          // 报销金额
          reimbursementTotal: '',
          // 报销金额大写
          moneyUppercase: '',
          // 出差的出发时间
          startTime: '',
          // 出差的到达时间
          arrivalTime: '',
          // 出差天数
          travelDays: '',
          // 报销日期
          reimbursementDate: '',
          // 差旅编号
          travelId: '',
          // 报销状态
          status: '',
        },
      },
      // 补助金额
      subsidy: '',
      // 暂存选中的待新增车票
      ticketsCache: [],
      // 当前登录员工未关联差旅的车票
      tickets: [],
      // 已登录的用户名
      loggedName: sessionStorage.getItem('loggedName'),
      dialogTableVisible: false,
      // 差旅信息表格数据源
      travelInfos: [
        {
          travelId: '001',
          travelDesc: '国内出差',
          status: '已提交',
          reason: '客户洽谈',
          reimbursementTotal: '2500.00',
          reimbursementDate: '2023-03-01',
          // ... 其他字段 ...
        },
        {
          travelId: '002',
          travelDesc: '国际出差',
          status: '审批中',
          reason: '项目对接',
          reimbursementTotal: '5000.00',
          reimbursementDate: '2023-04-15',
          // ... 其他字段 ...
        },
        // 更多差旅信息...
      ],
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
    }
  },
  methods: {
    recipeSQD() {
      this.$router.push('/recipeSQD')
    },
    recipeBDX() {
      this.$router.push('/recipeBXD')
    },
    workOverTime() {
      this.$router.push('/workOverTime')
    },
    reimbursement() {
      this.$router.push('/reimbursement')
    },
    fetchDataForTab1() {
      this.loadingTab1 = true
      let data = {
        uid: this.uid,
      }
      this.$http
        .post('/receipt/list', data)
        .then((response) => {
          this.tab1Data = response.data
        })
        .catch((error) => {
          console.error('Error fetching data for tab1:', error)
        })
        .finally(() => {
          this.loadingTab1 = false
        })
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
    // 获取差旅报销信息
    getTravelInfoList() {
      this.$http
        .post('/travelInfoListByPage', this.query)
        .then((res) => {
          this.travelInfos = res.data.data
          this.total = res.data.count
        })
        .catch((err) => {
          this.$message.error('差旅报销信息获取失败，请联系管理员~')
        })
    },
    editInfo(row) {
      let type = 'sqd'
      let url = '/receipt/sqd/query/' + row.id
      if (row.type.includes('Reimbursement')) {
        url = '/receipt/bxd/query/' + row.id
        type = 'bxd'
      }
      this.$http
        .get(url)
        .then((res) => {
          // TODO 展示单据信息
        })
        .catch((err) => {
          this.$message.error('获取单据信息失败')
        })
      this.dialogFormVisible = true
    },
    approval(row) {
      if (row.url != null && row.url !== '') {
        this.$router.push({ path: '/instance', query: { rowData: row, mode: 'view' } })
      } else {
        this.$message.warning('无审批流程')
      }
    },
    // 当每页多少条被改变时，val代表改变后的每页多少条
    handleSizeChange(val) {
      this.query.limit = val
      this.search()
    },
    // 当前页被改变时，val代表当前页
    handleCurrentChange(val) {
      this.query.page = val
      this.getTravelInfoList()
    },
    // 自定义索引
    calIndex(index) {
      return index + 1 + (this.query.page - 1) * this.query.limit
    },
    search() {
      this.query.page = 1
      this.getTravelInfoList()
    },
    formatMoney(cellValue) {
      if (cellValue.feeTotal != null) {
        return cellValue.feeTotal + ' CNY'
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
  },
  watch: {},
  created() {
    this.fetchDataForTab1() // Fetch initial data for tab1 when component is created
  },
}
</script>

<style scoped lang="less">
.container {
  .flex_arrange(column);
  padding: 10px;
  .title-row {
    display: flex;
    align-items: center;
    .title {
      margin: 0 8px;
      font-weight: 900;
    }
    .des {
      color: #999;
      font-size: 12px;
    }
  }
  .button-row {
    display: flex;
    margin-top: 8px;
  }
}

.blue {
  background-color: #bee4f8;
  .blue-title {
    color: #3a98ed;
  }
}

.yellow {
  background-color: #f8e2c1;
  .yellow-title {
    color: #e99d42;
  }
}

.pink {
  background-color: #f5dadd;
  .pink-title {
    color: #de868f;
  }
}

.main-title-row {
  font-size: 18px;
  text-align: left;
  font-weight: 900;
  margin-bottom: 20px;
}
</style>
