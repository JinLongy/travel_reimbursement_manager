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
        <div :style="{ height: height, width: width, flex: 1 }" :id="id" class="chart"></div>
        <!-- 全年报销类型分布图 -->
        <div :style="{ height: height, width: width, flex: 1 }" id="bar" class="chart"></div>
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
  name: 'echarts',
  props: {
    height: {
      type: String,
      default: '400px',
    },
    width: {
      type: String,
      default: '500px',
    },
    id: {
      type: String,
      default: 'demo',
    },
    top3: {
      type: String,
      default: 'demo2',
    },
  },
  data() {
    return {
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
      let myChart = echarts.init(document.getElementById(this.id))
      myChart.setOption({
        title: { text: '费用数据分析' },
        tooltip: {},
        xAxis: {
          type: 'category',
          data: this.firstx,
        },
        yAxis: {},
        series: [
          {
            name: '销量',
            type: 'bar',
            data: this.firsty,
          },
        ],
      })
    },
    show() {
      this.charts = echarts.init(document.getElementById('bar'))
      var option = {
        color: ['#d84430'],
        tooltip: {
          show: true,
        },
        yAxis: {
          axisTick: {
            show: true,
          },
          axisLine: {
            show: true,
          },
          axisLabel: {
            inside: true,
            verticalAlign: 'bottom',
            lineHeight: 40,
            color: 'rgba(0,0,0,0.87)',
            formatter: function (value, index) {
              if (index > 2) {
                return '{first|' + value + '}'
              } else {
                return '{other|' + value + '}'
              }
            },
            rich: {
              other: {
                color: 'rgba(0,0,0,0.87)',
                opacity: 0.57,
              },
              first: {
                color: 'rgba(0,0,0,0.87)',
              },
            },
          },
          data: this.yValue,
        },
        xAxis: {
          nameTextStyle: {
            color: 'rgba(255, 255, 255, 0.8)',
            align: 'right',
          },
          splitLine: {
            show: false,
          },
          axisLine: {
            show: false,
          },
          axisLabel: {
            color: 'rgba(255, 255, 255, 0.8)',
          },
        },
        grid: {
          top: '0%',
          bottom: '0%',
          left: '0%',
          right: '0%',
        },
        series: [
          {
            barWidth: 15,
            type: 'bar',
            data: this.xValue,
            itemStyle: {
              normal: {
                borderRadius: [3, 20, 20, 3],
                color: function (params) {
                  if (params.dataIndex === 5) {
                    return '#d84430'
                  } else if (params.dataIndex === 4) {
                    return '#f38237'
                  } else if (params.dataIndex === 3) {
                    return '#e2aa20'
                  } else {
                    return '#608289'
                  }
                },
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

.chart {
  flex: 1; /* Occupy equal space within the container */
  margin-right: 20px; /* Add spacing between charts */
}

.top3-chart {
  flex: 1; /* Occupy equal space within the container */
  display: flex;
  flex-direction: column; /* Align children vertically */
}

.chart-header {
  display: flex;
  justify-content: center; /* Center-align items horizontally */
  align-items: center; /* Center-align items vertically */
  margin-bottom: 10px; /* Add spacing between title and chart */
}

.chart-title {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.8);
}
</style>
