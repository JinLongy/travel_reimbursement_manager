<template>
  <div>
    <el-card class="common-card" style="padding: 30px 40px">
      <div class="total-amount-wrapper">
        <div v-for="(item, index) in totalAmountList" :key="index" class="total-amount-item">
          <div class="total-amount-item-label">{{ item.title }}</div>
          <div class="total-amount-item-value">{{ item.amount }}</div>
        </div>
      </div>
      <div class="chart-row">
        <!-- 每月报销金额分布图 -->
        <div :style="{ height: height, width: width }" id="lineChart" class="chart"></div>
        <!-- 全年报销类型分布图 -->
        <div :style="{ height: height, width: width }" id="barChart" class="chart"></div>
        <div style="width: 220px">
          <el-table
            :data="tableData"
            style="font-size: 15px"
            height="550px"
            :header-cell-style="{ background: '#e4f4fc', color: '#111' }"
            :border="true"
            :row-key="getRowKeys"
          >
            <el-table-column prop="id" label="月份" width="108px" align="center"> </el-table-column>
            <el-table-column prop="modelKey" width="108px" label="报销金额" align="center"> </el-table-column>
            <div slot="empty">
              <el-empty description="暂无数据"></el-empty>
            </div>
          </el-table>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'HomeAnalyze',
  data() {
    return {
      height: '400px',
      width: '500px',
      totalAmountList: [
        { title: '已报销金额', amount: '9346.16', key: '' },
        { title: '待报销金额', amount: '4216.10', key: '' },
        { title: '待提交报销金额', amount: '7329.47', key: '' },
        { title: '累计报销金额', amount: '26148.28', key: '' },
      ],
      tableData: [],
      xValue: [1, 1, 1, 2, 3, 3],
      yValue: ['陕西移动', '山西移动', '北京移动', '山东移动', '河北移动', '河南移动'],
      firstx: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子'],
      firsty: [5, 20, 36, 10, 10, 20],
    }
  },
  mounted() {
    this.drawLine()
    this.show()
  },
  methods: {
    drawLine() {
      let myChart = echarts.init(document.getElementById('lineChart'))
      myChart.setOption({
        title: {
          text: '每月报销金额分布图',
          left: 'center',
          bottom: '5',
        },
        tooltip: {
          trigger: 'item',
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: [150, 230, 224, 218, 135, 147, 260, 150, 230, 224, 218, 135],
            type: 'line',
          },
        ],
      })
    },
    show() {
      this.charts = echarts.init(document.getElementById('barChart'))
      var option = {
        title: {
          text: '全年报销类型分布图',
          left: 'center',
          bottom: '20',
        },
        tooltip: {
          trigger: 'item',
        },
        legend: {
          orient: 'vertical',
          left: 'left',
        },
        series: [
          {
            type: 'pie',
            radius: '50%',
            data: [
              { value: 1048, name: '部门团建' },
              { value: 735, name: '差旅费用' },
              { value: 580, name: '交通费用' },
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)',
              },
            },
          },
        ],
      }
      this.charts.setOption(option)
      window.addEventListener('resize', () => {
        this.charts.resize()
      })
    },
  },
}
</script>

<style lang="less" scoped>
.total-amount-wrapper {
  .flex_arrange(row, space-between);
  .total-amount-item {
    background: #e4f4fc;
    padding: 20px;
    flex: 1;
    .flex_arrange(column);
    &-label {
      margin-bottom: 20px;
      font-size: 20px;
      color: #111;
    }
    &-value {
      font-size: 30px;
      font-weight: 600;
      color: #4095e5;
      margin-bottom: 20px;
    }
  }
  .total-amount-item + .total-amount-item {
    margin-left: 80px;
  }
}
.chart-row {
  .flex_arrange(row, space-between);
  margin-top: 100px;
  > div:nth-child(1),
  > div:nth-child(2) {
    flex: 1;
  }
  > div + div {
    margin-left: 60px;
  }
}
</style>
