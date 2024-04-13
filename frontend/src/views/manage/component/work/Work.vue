<template>
  <a-card :bordered="false" class="card-area">
    <div style="width: 100%">
      <a-col :span="22" v-if="newsList.length > 0">
        <a-alert
          banner
          :message="newsContent"
          type="info"
        />
      </a-col>
      <a-col :span="2">
        <a-button type="primary" style="margin-top: 2px;margin-left: 10px" @click="newsNext">下一页</a-button>
      </a-col>
    </div>
    <br/>
    <br/>
    <a-row :gutter="30" v-if="userInfo != null">
      <a-col :span="6">
        <a-card :bordered="false">
          <span slot="title">
            <a-icon type="user" style="margin-right: 10px" />
            <span>用户信息</span>
          </span>
          <div>
            <a-avatar :src="'http://127.0.0.1:9527/imagesWeb/' + userInfo.images" shape="square" style="width: 100px;height: 100px;float: left;margin: 10px 0 10px 10px" />
            <div style="float: left;margin-left: 20px;margin-top: 8px">
              <span style="font-size: 20px;font-family: SimHei">{{ userInfo.name }}</span>
              <span style="font-size: 14px;font-family: SimHei">{{ userInfo.code }}</span>
            </div>
            <div style="float: left;margin-left: 20px;margin-top: 8px">
              <span style="font-size: 14px;font-family: SimHei">电话：{{ userInfo.phone == null ? '- -' : userInfo.phone }}</span>
            </div>
            <div style="float: left;margin-left: 20px;margin-top: 8px">
              <span style="font-size: 14px;font-family: SimHei">邮箱：{{ userInfo.email == null ? '- -' : userInfo.email }}</span>
            </div>
          </div>
        </a-card>
      </a-col>
    </a-row>
    <div style="background:#ECECEC; padding:30px;margin-top: 30px;margin-bottom: 30px">
      <a-row :gutter="30">
        <a-col :span="6" v-for="(item, index) in statusList" :key="index">
          <div style="background: #e8e8e8">
            <a-carousel autoplay style="height: 150px;" v-if="item.images !== undefined && item.images !== ''">
              <div style="width: 100%;height: 150px" v-for="(item, index) in item.images.split(',')" :key="index">
                <img :src="'http://127.0.0.1:9527/imagesWeb/'+item" style="width: 100%;height: 150px">
              </div>
            </a-carousel>
            <a-card :bordered="false">
              <span slot="title">
                <span style="font-size: 14px;font-family: SimHei">
                  {{ item.spaceName }} | {{ item.spaceAddress }}
                  <span style="margin-left: 15px;color: orange" v-if="item.status == -1">预约中</span>
                  <span style="margin-left: 15px;color: green" v-if="item.status == 0">空闲</span>
                  <span style="margin-left: 15px;color: red" v-if="item.status == 1">出租中</span>
                  <a style="text-align: right;margin-left: 10px" v-if="item.status == 0" @click="showModal(item)"><a-icon type="paper-clip" />下单</a>
                </span>
              </span>
            </a-card>
          </div>
        </a-col>
      </a-row>
      <a-modal
        title="选择停车车辆"
        :visible="visible"
        @ok="reserveSpace"
        @cancel="handleCancel"
      >
        <a-form :form="form" layout="vertical">
          <a-row :gutter="20">
            <a-col :span="12">
              <a-form-item label='车辆信息' v-bind="formItemLayout">
                <a-radio-group button-style="solid" v-decorator="[
                    'vehicleId',
                    {rules: [{ required: true, message: '请选择车辆' }]}
                  ]">
                  <a-radio-button :value="item.id" v-for="(item, index) in vehicleList" :key="index">
                    {{ item.vehicleNumber }}
                  </a-radio-button>
                </a-radio-group>
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label='租赁结束时间' v-bind="formItemLayout">
                <a-date-picker
                  show-time
                  format="YYYY-MM-DD HH:mm:ss"
                  placeholder="选择时间"
                  style="width: 100%"
                  v-decorator="[
                    'endDate',
                    {rules: [{ required: true, message: '请选择时间' }]}
                  ]"
                />
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-modal>
    </div>
  </a-card>
</template>

<script>
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')

const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'Work',
  data () {
    return {
      form: this.$form.createForm(this),
      formItemLayout,
      visible: false,
      statusList: [],
      vehicleList: [],
      newsContent: '',
      newsPage: 0,
      loading: false,
      userInfo: null,
      memberInfo: null,
      spaceInfo: null,
      newsList: []
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  mounted () {
    this.getWorkStatusList()
    this.selectMemberByUserId()
    this.selectVehicleByUserId()
  },
  methods: {
    newsNext () {
      if (this.newsPage + 1 === this.newsList.length) {
        this.newsPage = 0
      } else {
        this.newsPage += 1
      }
      this.newsContent = `《${this.newsList[this.newsPage].title}》 ${this.newsList[this.newsPage].content}`
    },
    showModal (row) {
      this.spaceInfo = row
      this.visible = true
    },
    handleCancel (e) {
      console.log('Clicked cancel button')
      this.visible = false
    },
    selectVehicleByUserId () {
      this.$get(`/cos/vehicle-info/user/${this.currentUser.userId}`).then((r) => {
        this.vehicleList = r.data.data
      })
    },
    reserveSpace () {
      this.form.validateFields((err, values) => {
        if (!err) {
          values.spaceId = this.spaceInfo.id
          values.endDate = moment(values.endDate).format('YYYY-MM-DD HH:mm:ss')
          this.$post('/cos/park-order-info/orderAdd', {
            ...values
          }).then((r) => {
            this.$message.success('下单成功！请前往我的订单进行支付')
            this.visible = false
            this.getWorkStatusList()
          })
        }
      })
    },
    selectMemberByUserId () {
      this.$get(`/cos/member-info/member/${this.currentUser.userId}`).then((r) => {
        this.userInfo = r.data.user
        this.memberInfo = r.data.member
        this.newsList = r.data.bulletin
        if (this.newsList.length !== 0) {
          this.newsContent = `《${this.newsList[0].title}》 ${this.newsList[0].content}`
        }
      })
    },
    getWorkStatusList () {
      this.$get(`/cos/space-status-info/status/list`).then((r) => {
        this.statusList = r.data.data
      })
    }
  }
}
</script>

<style scoped>
>>> .ant-card-meta-title {
  font-size: 13px;
  font-family: SimHei;
}
>>> .ant-card-meta-description {
  font-size: 12px;
  font-family: SimHei;
}
>>> .ant-divider-with-text-left {
  margin: 0;
}

>>> .ant-card-head-title {
  font-size: 13px;
  font-family: SimHei;
}
>>> .ant-card-extra {
  font-size: 13px;
  font-family: SimHei;
}
.ant-carousel >>> .slick-slide {
  text-align: center;
  height: 250px;
  line-height: 250px;
  overflow: hidden;
}

</style>
