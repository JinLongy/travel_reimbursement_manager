<template>
  <div class="page-wrapper">
    <div class="top-section">
      <div class="left" @click="goBack">
        <i class="el-icon-back" style="font-size: 22px"></i>
        <span class="common-title"> 新增 </span>
      </div>
      <div class="right">
        <el-button @click="saveHandler">保存</el-button>
        <el-button @click="submitHandler" class="submit-btn">提交审批</el-button>
      </div>
    </div>
    <el-card class="common-card">
      <div class="bottom-section">
        <div class="traveler-label">
          <span class="color-block"></span>
          基本信息
        </div>
        <el-form :model="basicFormData" label-width="100px" label-position="top" :rules="basicRules">
          <div class="inline-group">
            <el-form-item label="申请人" class="width-45" prop="applicant">
              <el-input v-model="basicFormData.applicant"></el-input>
            </el-form-item>
            <el-form-item label="费用承担部门" class="width-45" prop="feeDepart">
              <el-select v-model="basicFormData.feeDepart" placeholder="请选择" @focus="fetchApiOptions" class="width-100">
                <el-option
                  v-for="option in departmentList"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </div>

          <div class="inline-group">
            <el-form-item label="收款帐号" class="width-45" prop="feeAccount">
              <el-select v-model="basicFormData.feeAccount" placeholder="请选择" @focus="fetchApiOptions" class="width-100">
                <el-option
                  v-for="option in departmentList"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="项目（选填）" prop="project" class="width-45">
              <el-input v-model="basicFormData.project" class="width-100"></el-input>
            </el-form-item>
          </div>

          <el-form-item label="事由" prop="reason">
            <el-input v-model="basicFormData.reason"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <el-card class="common-card" style="margin: 20px 0">
      <div class="bottom-section">
        <div class="traveler-label">
          <span class="color-block"></span>
          费用信息
        </div>
        <el-form :model="feeFormData" label-width="100px" label-position="top" :rules="feeRules">
          <div class="inline-group">
            <el-form-item label="费用项类型" class="width-45" prop="feeItemType">
              <el-select v-model="feeFormData.feeItemType" placeholder="请选择" @focus="fetchApiOptions" class="width-100">
                <el-option
                  v-for="option in reimburseTypeList"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="用途" class="width-45" prop="usage" v-if="feeFormData.feeItemType == 1">
              <el-select v-model="feeFormData.usage" placeholder="请选择" @focus="fetchApiOptions" class="width-100">
                <el-option
                  v-for="option in departmentList"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </div>
          <div class="inline-group" v-if="feeFormData.feeItemType == 1">
            <el-form-item label="出发地" class="width-45" prop="startingPlace">
              <el-input v-model="feeFormData.startingPlace"></el-input>
            </el-form-item>
            <el-form-item label="目的地" class="width-45" prop="endingPlace">
              <el-input v-model="feeFormData.endingPlace"></el-input>
            </el-form-item>
          </div>
          <div class="inline-group" v-if="feeFormData.feeItemType">
            <el-form-item label="费用日期" class="width-45" prop="dateRange">
              <el-date-picker
                class="width-100"
                v-model="feeFormData.dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="费用金额" class="width-45" prop="feeNum" v-if="feeFormData.feeItemType == 1">
              <el-input-number
                v-model="feeFormData.feeNum"
                controls-position="right"
                :min="1"
                class="width-100"
              ></el-input-number>
            </el-form-item>
            <el-form-item label="团建月份" class="width-45" prop="month" v-if="feeFormData.feeItemType == 2">
              <el-date-picker v-model="feeFormData.month" type="month" placeholder="选择月" class="width-100"> </el-date-picker>
            </el-form-item>
          </div>
          <div class="inline-group" v-if="feeFormData.feeItemType == 2">
            <el-form-item label="人数" class="width-45" prop="peopleNum">
              <el-input-number
                v-model="feeFormData.peopleNum"
                controls-position="right"
                :min="1"
                class="width-100"
              ></el-input-number>
            </el-form-item>
            <el-form-item label="费用金额" class="width-45" prop="feeNum">
              <el-input-number
                v-model="feeFormData.feeNum"
                controls-position="right"
                :min="1"
                class="width-100"
              ></el-input-number>
            </el-form-item>
          </div>
        </el-form>
      </div>
    </el-card>

    <el-card class="common-card">
      <div class="bottom-section">
        <div class="traveler-label">
          <span class="color-block"></span>
          中国境内电子发票
        </div>
      </div>
      <el-upload class="width-100" drag action="https://jsonplaceholder.typicode.com/posts/" multiple>
        <div class="upload-row">
          <i class="el-icon-plus"></i>
          <div class="tips">添加电子发票（支持多个文件拖拽上传）</div>
        </div>
      </el-upload>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      basicFormData: {
        applicant: '',
        feeDepart: '',
        feeAccount: '',
        project: '',
        reason: '',
      },
      feeFormData: {
        feeItemType: '',
        usage: '',
        dateRange: [],
        startingPlace: '',
        endingPlace: '',
        peopleNum: '',
        month: '',
        feeNum: '',
      },
      reimburseTypeList: [
        { label: '交通费用', value: 1 },
        { label: '部门团建', value: 2 },
      ],
      purposeList: [
        { label: '普通差旅', value: 1 },
        { label: '内部会议', value: 2 },
      ],
      departmentList: [], // 初始化为空数组，等待从后端获取数据填充
      username: '', // 初始化为空字符串，用于存储当前登录用户名
      basicRules: {
        applicant: [{ required: true, message: '请输入报销人', trigger: 'blur' }],
        feeDepart: [{ required: true, message: '请选择费用承担部门', trigger: 'change' }],
        feeAccount: [{ required: true, message: '请选择收款帐号', trigger: 'change' }],
        reason: [{ required: true, message: '请输入事由', trigger: 'change' }],
      },
      feeRules: {
        feeItemType: [{ required: true, message: '请输入申请人', trigger: 'blur' }],
        usage: [{ required: true, message: '请选择申请类型', trigger: 'change' }],
        dateRange: [{ type: 'array', required: true, message: '请选择日期', trigger: 'change' }],
        startingPlace: [{ required: true, message: '请填写出发地', trigger: 'change' }],
        endingPlace: [{ required: true, message: '请填写目的地', trigger: 'change' }],
        peopleNum: [{ required: true, message: '请填写人数', trigger: 'change' }],
        feeNum: [{ required: true, message: '请填写费用金额', trigger: 'change' }],
        month: [{ required: true, message: '请选择团建月份', trigger: 'change' }],
      },
    }
  },
  created() {
    this.fetchCurrentUser()
    this.fetchApiOptions() // 页面加载时默认请求一次接口选项
  },
  methods: {
    goBack() {
      this.$router.go(-1)
    },
    save() {
      // Handle save action
    },
    submit() {
      // Handle submit action
    },
    addOrEdit() {
      // Handle add or edit action
    },
    fetchCurrentUser() {
      sessionStorage.getItem('loggedName')
    },
    fetchApiOptions() {
      // 假设后端返回格式为 [{ label: '接口1', value: 'api1' }, { label: '接口2', value: 'api2' }]
      this.$http
        .post('/api/apiOptions')
        .then((response) => {
          this.departmentList = response.data
        })
        .catch((error) => {
          console.error('Failed to fetch API options:', error)
        })
    },
  },
}
</script>

<style scoped lang="less">
.page-wrapper {
  min-height: 100%;
  padding: 40px;
  background: #f5f5f5;
  box-sizing: border-box;
}

.top-section {
  .flex_arrange(row, space-between);
  margin-bottom: 20px;
}

.middle-section {
  padding: 20px;
  .flex_arrange(column);
}

.bottom-section {
  padding: 20px;
}

.traveler-label {
  margin-bottom: 20px;
  .common-title;
  font-size: 26px;
  .flex_arrange(row, flex-start);
  .color-block {
    display: inline-block;
    width: 6px;
    height: 16px;
    background: @primary-color;
    margin-right: 12px;
  }
}
.inline-group {
  .flex_arrange(row, space-between);
  /* 在表单项之间添加间距 */
  .width-45 {
    width: 45%;
  }
  .width-100 {
    width: 100%;
  }
}
/deep/ .el-form-item__label {
  font-size: 20px;
}
.submit-btn {
  background: @primary-color;
  color: @white-color;
}
.upload-row {
  width: 100%;
  .flex_arrange(column, space-between);
  padding: 20px;
  color: #fe8158;
  .tips {
    margin-top: 8px;
    font-size: 12px;
    font-weight: 600;
  }
}
/deep/ .el-upload {
  width: 100%;
  .el-upload-dragger {
    width: 100%;
    height: auto;
  }
}
</style>
