<template>
  <div class="trace-wrapper">
    <el-dialog title="审批追踪" :visible.sync="traceVisible">
      <el-steps :active="activeStep">
        <el-step v-for="(item, index) in stepsList" :key="index" :title="item.title" :status="item.status"></el-step>
      </el-steps>
      <span slot="footer" class="dialog-footer"> </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'ApproveTrace',
  props: {
    traceVisible: {
      type: Boolean,
      default: false,
    },
    activeStep: {
      type: Number,
      default: 0,
    },
    stepsList: {
      type: Array,
      default: () => [
        // wait / process / finish / error / success
        {
          title: '申请人',
          status: 'success',
        },
        {
          title: '一级审批人',
          status: 'success',
        },
        {
          title: '二级审批人',
          status: 'success',
        },
        {
          title: '财务部门',
          status: 'wait',
        },
      ],
    },
  },
  data() {
    return {}
  },
  methods: {},
}
</script>

<style scoped lang="less">
.trace-wrapper {
  text-align: initial;
}
</style>
