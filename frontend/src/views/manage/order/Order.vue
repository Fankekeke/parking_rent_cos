<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="车位名称"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.spaceName"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="车牌号码"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.vehicleNumber"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="用户名称"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.userName"/>
              </a-form-item>
            </a-col>
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
          </span>
        </a-row>
      </a-form>
    </div>
    <div>
      <div class="operator">
<!--        <a-button type="primary" ghost @click="add">新增</a-button>-->
        <a-button @click="batchDelete">删除</a-button>
      </div>
      <!-- 表格区域 -->
      <a-table ref="TableInfo"
               :columns="columns"
               :rowKey="record => record.id"
               :dataSource="dataSource"
               :pagination="pagination"
               :loading="loading"
               :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
               :scroll="{ x: 900 }"
               @change="handleTableChange">
        <template slot="operation" slot-scope="text, record">
          <a-icon type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="orderOverViewOpen(record)" title="订单结算" v-if="record.status == 2"></a-icon>
          <a-icon type="control" theme="twoTone" @click="download(record)" title="下 载" style="margin-left: 15px" v-if="record.status >= 1"></a-icon>
          <a-icon type="file-search" @click="orderViewOpen(record)" title="详 情" style="margin-left: 15px"></a-icon>
        </template>
      </a-table>
    </div>
    <order-add
      v-if="orderAdd.visiable"
      @close="handleorderAddClose"
      @success="handleorderAddSuccess"
      :orderAddVisiable="orderAdd.visiable">
    </order-add>
    <order-edit
      ref="orderEdit"
      @close="handleorderEditClose"
      @success="handleorderEditSuccess"
      :orderEditVisiable="orderEdit.visiable">
    </order-edit>
    <order-over
      @close="handleorderOverViewClose"
      @success="handleorderOverSuccess"
      :orderShow="orderOverView.visiable"
      :orderData="orderOverView.data">
    </order-over>
    <order-view
      @close="handleorderViewClose"
      :orderShow="orderView.visiable"
      :orderData="orderView.data">
    </order-view>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import orderAdd from './OrderAdd.vue'
import orderEdit from './OrderEdit.vue'
import orderView from './OrderView.vue'
import orderOver from './OrderOver.vue'
import {mapState} from 'vuex'
import moment from 'moment'
import { newSpread, fixedForm, saveExcel } from '@/utils/spreadJS'
moment.locale('zh-cn')

export default {
  name: 'order',
  components: {orderAdd, orderEdit, orderView, orderOver, RangeDate},
  data () {
    return {
      advanced: false,
      orderAdd: {
        visiable: false
      },
      orderEdit: {
        visiable: false
      },
      orderOverView: {
        visiable: false,
        data: null
      },
      orderView: {
        visiable: false,
        data: null
      },
      queryParams: {},
      filteredInfo: null,
      sortedInfo: null,
      paginationInfo: null,
      dataSource: [],
      selectedRowKeys: [],
      loading: false,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      userList: []
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    columns () {
      return [ {
        title: '订单用户',
        dataIndex: 'name'
      }, {
        title: '车牌号码',
        dataIndex: 'vehicleNumber',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '车辆图片',
        dataIndex: 'vehicleImages',
        customRender: (text, record, index) => {
          if (!record.vehicleImages) return <a-avatar shape="square" icon="user" />
          return <a-popover>
            <template slot="content">
              <a-avatar shape="square" size={132} icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.vehicleImages.split(',')[0] } />
            </template>
            <a-avatar shape="square" icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.vehicleImages.split(',')[0] } />
          </a-popover>
        }
      }, {
        title: '车位名称',
        dataIndex: 'spaceName',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '开始时间',
        dataIndex: 'startDate',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '结束时间',
        dataIndex: 'endDate',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '总时长（分钟）',
        dataIndex: 'totalTime',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '总费用（元）',
        dataIndex: 'totalPrice',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '缴费状态',
        dataIndex: 'status',
        customRender: (text, row, index) => {
          switch (text) {
            case '0':
              return <a-tag color="red">未缴费</a-tag>
            case '1':
              return <a-tag color="green">已缴费</a-tag>
            case '2':
              return <a-tag color="orange">押金退款中</a-tag>
            case '3':
              return <a-tag color="green">完成</a-tag>
            case '4':
              return <a-tag color="red">押金驳回</a-tag>
            default:
              return '- -'
          }
        }
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: {customRender: 'operation'}
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    download (row) {
      this.$message.loading('正在生成', 0)
      let spread = newSpread('textTable')
      let sheet = spread.getActiveSheet()
      sheet.suspendPaint()
      sheet.setValue(1, 2, row.name)
      sheet.setValue(1, 4, row.payDate)
      sheet.setValue(4, 2, row.spaceName)
      sheet.setValue(4, 3, row.totalTime)
      sheet.setValue(4, 4, row.price + ' 元')
      sheet.setValue(5, 4, row.totalPrice + ' 元')
      sheet.setValue(7, 1, row.content)
      spread = fixedForm(spread, 'textTable', { title: `${row.payDate}缴费表` })
      saveExcel(spread, `${row.payDate}缴费表.xlsx`)
      this.$message.destroy()
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    add () {
      this.orderAdd.visiable = true
    },
    handleorderAddClose () {
      this.orderAdd.visiable = false
    },
    handleorderAddSuccess () {
      this.orderAdd.visiable = false
      this.$message.success('新增订单成功')
      this.search()
    },
    edit (record) {
      this.$refs.orderEdit.setFormValues(record)
      this.orderEdit.visiable = true
    },
    orderViewOpen (row) {
      this.orderView.data = row
      this.orderView.visiable = true
    },
    handleorderOverSuccess () {
      this.orderOverView.visiable = false
      this.$message.success('订单结算成功')
      this.search()
    },
    handleorderOverViewClose () {
      this.orderOverView.visiable = false
    },
    orderOverViewOpen (row) {
      this.orderOverView.data = row
      this.orderOverView.visiable = true
    },
    handleorderViewClose () {
      this.orderView.visiable = false
    },
    handleorderEditClose () {
      this.orderEdit.visiable = false
    },
    handleorderEditSuccess () {
      this.orderEdit.visiable = false
      this.$message.success('修改订单成功')
      this.search()
    },
    handleDeptChange (value) {
      this.queryParams.deptId = value || ''
    },
    batchDelete () {
      if (!this.selectedRowKeys.length) {
        this.$message.warning('请选择需要删除的记录')
        return
      }
      let that = this
      this.$confirm({
        title: '确定删除所选中的记录?',
        content: '当您点击确定按钮后，这些记录将会被彻底删除',
        centered: true,
        onOk () {
          let ids = that.selectedRowKeys.join(',')
          that.$delete('/cos/park-order-info/' + ids).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          })
        },
        onCancel () {
          that.selectedRowKeys = []
        }
      })
    },
    search () {
      let {sortedInfo, filteredInfo} = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.fetch({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams,
        ...filteredInfo
      })
    },
    reset () {
      // 取消选中
      this.selectedRowKeys = []
      // 重置分页
      this.$refs.TableInfo.pagination.current = this.pagination.defaultCurrent
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
        this.paginationInfo.pageSize = this.pagination.defaultPageSize
      }
      // 重置列过滤器规则
      this.filteredInfo = null
      // 重置列排序规则
      this.sortedInfo = null
      // 重置查询参数
      this.queryParams = {}
      this.fetch()
    },
    handleTableChange (pagination, filters, sorter) {
      // 将这三个参数赋值给Vue data，用于后续使用
      this.paginationInfo = pagination
      this.filteredInfo = filters
      this.sortedInfo = sorter

      this.fetch({
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...this.queryParams,
        ...filters
      })
    },
    fetch (params = {}) {
      // 显示loading
      this.loading = true
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.size = this.paginationInfo.pageSize
        params.current = this.paginationInfo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.size = this.pagination.defaultPageSize
        params.current = this.pagination.defaultCurrent
      }
      if (params.delFlag === undefined) {
        delete params.delFlag
      }
      this.$get('/cos/park-order-info/page', {
        ...params
      }).then((r) => {
        let data = r.data.data
        const pagination = {...this.pagination}
        pagination.total = data.total
        this.dataSource = data.records
        this.pagination = pagination
        // 数据加载完毕，关闭loading
        this.loading = false
      })
    }
  },
  watch: {}
}
</script>
<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
