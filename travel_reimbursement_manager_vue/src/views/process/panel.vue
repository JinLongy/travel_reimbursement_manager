<template>
  <div class="page-wrapper">
    <div class="top-section">
      <div class="left" @click="goBack">
        <i class="el-icon-back" style="font-size: 22px"></i>
        <span class="common-title">返回</span>
      </div>
      <div class="right">
        <el-button @click="saveHandler" class="submit-btn">保存</el-button>
      </div>
    </div>
    <el-card class="common-card">
      <div id="container" style="min-width: 400px; min-height: 600px"></div>
      <div class="drawer">
        <el-form label-width="80">
          <el-form-item label="节点文本">
            <el-input v-model="nodeText" @change="changeNodeText" style="width: 200px"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script>
import { Graph, Shape } from '@antv/x6'
import { Stencil } from '@antv/x6-plugin-stencil'
import { Transform } from '@antv/x6-plugin-transform'
import { Selection } from '@antv/x6-plugin-selection'
import { Snapline } from '@antv/x6-plugin-snapline'
import { Keyboard } from '@antv/x6-plugin-keyboard'
import { Clipboard } from '@antv/x6-plugin-clipboard'
import { History } from '@antv/x6-plugin-history'
import insertCss from 'insert-css'

export default {
  name: 'ProcessPanel',
  data() {
    return {
      graph: null,
      stencil: null,
      selectCell: null,
      nodeText: '',
    }
  },
  created() {},
  mounted() {
    this.init()
  },
  methods: {
    init() {
      const container = document.getElementById('container')
      const stencilContainer = document.createElement('div')
      stencilContainer.id = 'stencil'
      const graphContainer = document.createElement('div')
      graphContainer.id = 'graph-container'
      container.appendChild(stencilContainer)
      container.appendChild(graphContainer)
      this.styleInit()
      this.graphInit()
      this.stencilInit()
      this.keyboardInit()
      this.shapeInit()
      this.portsInit()
    },
    // 初始化画布
    graphInit() {
      this.graph = new Graph({
        container: document.getElementById('graph-container'),
        grid: true,
        mousewheel: {
          enabled: true,
          zoomAtMousePosition: true,
          modifiers: 'ctrl',
          minScale: 0.5,
          maxScale: 3,
        },
        connecting: {
          router: 'manhattan',
          connector: {
            name: 'rounded',
            args: {
              radius: 8,
            },
          },
          anchor: 'center',
          connectionPoint: 'anchor',
          allowBlank: false,
          snap: {
            radius: 20,
          },
          createEdge() {
            return new Shape.Edge({
              attrs: {
                line: {
                  stroke: '#A2B1C3',
                  strokeWidth: 2,
                  targetMarker: {
                    name: 'block',
                    width: 12,
                    height: 8,
                  },
                },
              },
              zIndex: 0,
            })
          },
          validateConnection({ targetMagnet }) {
            return !!targetMagnet
          },
        },
        highlighting: {
          magnetAdsorbed: {
            name: 'stroke',
            args: {
              attrs: {
                fill: '#5F95FF',
                stroke: '#5F95FF',
              },
            },
          },
        },
      })
      // 使用插件
      this.graph
        .use(
          new Transform({
            resizing: true,
            rotating: true,
          }),
        )
        .use(
          new Selection({
            rubberband: true,
            showNodeSelectionBox: true,
          }),
        )
        .use(new Snapline())
        .use(new Keyboard())
        .use(new Clipboard())
        .use(new History())
    },
    // 初始化 stencil
    stencilInit() {
      this.stencil = new Stencil({
        title: '流程图',
        target: this.graph,
        stencilGraphWidth: 200,
        stencilGraphHeight: 180,
        collapsable: true,
        groups: [
          {
            title: '基础流程图',
            name: 'group',
          },
        ],
        layoutOptions: {
          columns: 2,
          columnWidth: 80,
          rowHeight: 55,
        },
      })
      document.getElementById('stencil').appendChild(this.stencil.container)
    },
    // 快捷键与事件
    keyboardInit() {
      this.graph.bindKey(['meta+c', 'ctrl+c'], () => {
        const cells = this.graph.getSelectedCells()
        if (cells.length) {
          this.graph.copy(cells)
        }
        return false
      })
      this.graph.bindKey(['meta+x', 'ctrl+x'], () => {
        const cells = this.graph.getSelectedCells()
        if (cells.length) {
          this.graph.cut(cells)
        }
        return false
      })
      this.graph.bindKey(['meta+v', 'ctrl+v'], () => {
        if (!this.graph.isClipboardEmpty()) {
          const cells = this.graph.paste({ offset: 32 })
          this.graph.cleanSelection()
          this.graph.select(cells)
        }
        return false
      })

      // undo redo
      this.graph.bindKey(['meta+z', 'ctrl+z'], () => {
        if (this.graph.canUndo()) {
          this.graph.undo()
        }
        return false
      })
      this.graph.bindKey(['meta+shift+z', 'ctrl+shift+z'], () => {
        if (this.graph.canRedo()) {
          this.graph.redo()
        }
        return false
      })

      // select all
      this.graph.bindKey(['meta+a', 'ctrl+a'], () => {
        const nodes = this.graph.getNodes()
        if (nodes) {
          this.graph.select(nodes)
        }
      })

      // delete
      this.graph.bindKey('backspace', () => {
        const cells = this.graph.getSelectedCells()
        if (cells.length) {
          this.graph.removeCells(cells)
        }
      })

      // zoom
      this.graph.bindKey(['ctrl+1', 'meta+1'], () => {
        const zoom = this.graph.zoom()
        if (zoom < 1.5) {
          this.graph.zoom(0.1)
        }
      })
      this.graph.bindKey(['ctrl+2', 'meta+2'], () => {
        const zoom = this.graph.zoom()
        if (zoom > 0.5) {
          this.graph.zoom(-0.1)
        }
      })
      this.graph.on('selection:changed', (args) => {
        args.added.forEach((cell) => {
          if (cell.isNode() && cell.store.data.attrs?.id == 'audit-shape') {
            if (cell.store.data.attrs?.id == 'audit-shape') {
              this.selectCell = cell
              this.nodeText = cell.store.data.attrs?.label?.text || ''
            }
          }
        })
      })
    },
    // 初始化图形
    shapeInit() {
      const ports = {
        groups: {
          top: {
            position: 'top',
            attrs: {
              circle: {
                r: 4,
                magnet: true,
                stroke: '#5F95FF',
                strokeWidth: 1,
                fill: '#fff',
                style: {
                  visibility: 'hidden',
                },
              },
            },
          },
          right: {
            position: 'right',
            attrs: {
              circle: {
                r: 4,
                magnet: true,
                stroke: '#5F95FF',
                strokeWidth: 1,
                fill: '#fff',
                style: {
                  visibility: 'hidden',
                },
              },
            },
          },
          bottom: {
            position: 'bottom',
            attrs: {
              circle: {
                r: 4,
                magnet: true,
                stroke: '#5F95FF',
                strokeWidth: 1,
                fill: '#fff',
                style: {
                  visibility: 'hidden',
                },
              },
            },
          },
          left: {
            position: 'left',
            attrs: {
              circle: {
                r: 4,
                magnet: true,
                stroke: '#5F95FF',
                strokeWidth: 1,
                fill: '#fff',
                style: {
                  visibility: 'hidden',
                },
              },
            },
          },
        },
        items: [
          {
            group: 'top',
          },
          {
            group: 'right',
          },
          {
            group: 'bottom',
          },
          {
            group: 'left',
          },
        ],
      }

      Graph.registerNode(
        'custom-rect',
        {
          inherit: 'rect',
          width: 66,
          height: 36,
          attrs: {
            body: {
              strokeWidth: 1,
              stroke: '#5F95FF',
              fill: '#EFF4FF',
            },
            text: {
              fontSize: 12,
              fill: '#262626',
            },
          },
          ports: { ...ports },
        },
        true,
      )

      Graph.registerNode(
        'custom-circle',
        {
          inherit: 'circle',
          width: 45,
          height: 45,
          attrs: {
            body: {
              strokeWidth: 1,
              stroke: '#5F95FF',
              fill: '#EFF4FF',
            },
            text: {
              fontSize: 12,
              fill: '#262626',
            },
          },
          ports: { ...ports },
        },
        true,
      )

      const shapeStart = this.graph.createNode({
        shape: 'custom-rect',
        label: '开始',
        attrs: {
          id: 'start-shape',
          body: {
            rx: 20,
            ry: 26,
          },
        },
      })
      const shapeAudit = this.graph.createNode({
        shape: 'custom-rect',
        attrs: {
          id: 'audit-shape',
          body: {
            rx: 6,
            ry: 6,
          },
        },
        label: '',
      })
      const shapeEnd = this.graph.createNode({
        shape: 'custom-circle',
        label: '结束',
        attrs: {
          id: 'end-shape',
        },
      })
      this.stencil.load([shapeStart, shapeAudit, shapeEnd], 'group')
    },
    // 控制连接桩显示/隐藏
    portsInit() {
      const showPorts = (ports, show) => {
        for (let i = 0, len = ports.length; i < len; i += 1) {
          ports[i].style.visibility = show ? 'visible' : 'hidden'
        }
      }
      this.graph.on('node:mouseenter', () => {
        const container = document.getElementById('graph-container')
        const ports = container.querySelectorAll('.x6-port-body')
        showPorts(ports, true)
      })
      this.graph.on('node:mouseleave', () => {
        const container = document.getElementById('graph-container')
        const ports = container.querySelectorAll('.x6-port-body')
        showPorts(ports, false)
      })
    },
    styleInit() {
      insertCss(`
          #container {
            display: flex;
            border: 1px solid #dfe3e8;
          }
          #stencil {
            width: 180px;
            height: 600px;
            position: relative;
            border-right: 1px solid #dfe3e8;
          }
          #graph-container {
            width: calc(100% - 180px);
            height: 600px;
          }
          .x6-widget-stencil  {
            background-color: #fff;
          }
          .x6-widget-stencil-title {
            background-color: #fff;
          }
          .x6-widget-stencil-group-title {
            background-color: #fff !important;
          }
          .x6-widget-transform {
            margin: -1px 0 0 -1px;
            padding: 0px;
            border: 1px solid #239edd;
          }
          .x6-widget-transform > div {
            border: 1px solid #239edd;
          }
          .x6-widget-transform > div:hover {
            background-color: #3dafe4;
          }
          .x6-widget-transform-active-handle {
            background-color: #3dafe4;
          }
          .x6-widget-transform-resize {
            border-radius: 0;
          }
          .x6-widget-selection-inner {
            border: 1px solid #239edd;
          }
          .x6-widget-selection-box {
            opacity: 0;
          }`)
    },
    changeNodeText() {
      this.selectCell && this.selectCell.attr('label/text', this.nodeText)
    },
    goBack() {
      this.$router.go(-1)
    },
    saveHandler() {
      // Handle save action
    },
  },
  computed: {},
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
.submit-btn {
  background: @primary-color;
  color: @white-color;
}
.drawer {
  width: 100%;
  height: 80px;
  background: #fff;
  margin-top: 20px;
}
</style>
