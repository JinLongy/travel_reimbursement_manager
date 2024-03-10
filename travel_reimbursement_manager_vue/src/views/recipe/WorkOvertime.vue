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
        <el-form :model="basicFormData" label-width="100px" label-position="top" :rules="rules">
          <div class="inline-group">
            <el-form-item label="申请人" class="width-45" prop="applicant">
              <el-input v-model="basicFormData.applicant"></el-input>
            </el-form-item>
            <el-form-item label="所属部门" class="width-45" prop="feeDepart">
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
            <el-form-item label="时间范围" class="width-45" prop="applicant">
              <el-date-picker
                class="width-100"
                v-model="basicFormData.dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item label="加班时长（H）" class="width-45" prop="project">
              <el-input v-model="basicFormData.project"></el-input>
            </el-form-item>
          </div>

          <el-form-item label="加班原因" prop="remark">
            <el-input type="textarea" v-model="basicFormData.remark" :rows="6"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      basicFormData: {
        applicant: '',
        applyType: '',
        dateRange: [],
        feeDepart: '',
        purpose: '',
        remark: '',
      },
      departmentList: [], // 初始化为空数组，等待从后端获取数据填充
      username: '', // 初始化为空字符串，用于存储当前登录用户名
      rules: {
        applicant: [{ required: true, message: '请输入申请人', trigger: 'blur' }],
        applyType: [{ required: true, message: '请选择申请类型', trigger: 'change' }],
        dateRange: [{ type: 'date', required: true, message: '请选择日期', trigger: 'change' }],
        feeDepart: [{ type: 'array', required: true, message: '请选择费用承担部门', trigger: 'change' }],
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
</style>
