<template>
  <div class="page-wrapper">
    <div class="top-section">
      <div class="left" @click="goBack">
        <i class="el-icon-back" style="font-size: 22px"></i>
        <span class="common-title"> 申请单详情 </span>
      </div>
    </div>
    <el-card class="common-card">
      <div class="bottom-section">
        <div class="traveler-label">
          <span class="color-block"></span>
          基本信息
        </div>
        <el-descriptions class="margin-top" title="" :column="2" direction="vertical">
          <el-descriptions-item label="申请人"> <span class="descriptions-value">kooriookami</span></el-descriptions-item>
          <el-descriptions-item label="申请类型"><span class="descriptions-value">18100000000</span></el-descriptions-item>
          <el-descriptions-item label="出行时间范围"><span class="descriptions-value">苏州市</span></el-descriptions-item>
          <el-descriptions-item label="费用承担部门"><span class="descriptions-value">学校</span> </el-descriptions-item>
          <el-descriptions-item label="差旅目的"><span class="descriptions-value">学校</span></el-descriptions-item>
          <el-descriptions-item label="项目">
            <span class="descriptions-value">江苏省苏州市吴中区吴中大道</span>
          </el-descriptions-item>
          <el-descriptions-item label="事由" :span="2">
            <span class="descriptions-value">江苏省苏州市吴中区吴中大道</span>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div class="opt-row">
        <el-button type="success" @click="passHandler">审批通过</el-button>
        <el-button type="danger" @click="showDialog = true">审批拒绝</el-button>
      </div>
    </el-card>
    <el-dialog title="审批驳回" :visible.sync="showDialog">
      <div class="reject-reason-label">请输入驳回原因</div>
      <el-input type="textarea" v-model="rejectReason" :rows="6"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showDialog = false">关闭</el-button>
        <el-button type="warning" @click="rejectHandler">提交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      showDialog: false,
      rejectReason: '',
    }
  },
  methods: {
    goBack() {
      this.$router.go(-1)
    },
    passHandler() {
      this.$message({
        message: '审批通过',
        type: 'success',
      })
    },
    rejectHandler() {
      if (!this.rejectReason) {
        this.$message({
          message: '请填写拒绝原因',
          type: 'warning',
        })
        return
      }
      this.goBack()
    },
  },
  watch: {},
  created() {},
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
/deep/.el-descriptions :not(.is-bordered) .el-descriptions-item__cell {
  color: #999999;
}
.descriptions-value {
  color: #333;
  font-size: 15px;
  line-height: 30px;
}
.opt-row {
  .flex_arrange(row, flex-end);
  margin: 150px 0 50px;
}
.reject-reason-label {
  font-size: 16px;
  color: #666;
  margin-bottom: 15px;
}
.dialog-footer {
  .flex_arrange;
}
</style>
