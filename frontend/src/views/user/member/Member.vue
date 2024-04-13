<template>
  <a-card :bordered="false" class="card-area">
    <a-row :gutter="24">
      <a-col :span="12" v-if="userInfo != null" style="margin-top: 30px">
        <a-card :bordered="false">
          <a-row>
            <a-col :span="5">
              <a-avatar :src="'http://127.0.0.1:9527/imagesWeb/' + userInfo.images" shape="square" style="width: 100px;height: 100px;"/>
            </a-col>
            <a-col :span="12">
              <div style="font-size: 20px;font-family: SimHei">{{ userInfo.name }}</div>
              <p style="font-size: 13px;font-family: SimHei" v-if="memberInfo == null">暂未开通会员</p>
              <p style="font-size: 13px;font-family: SimHei" v-if="memberInfo != null">{{ memberInfo.ruleName }}</p>
              <p style="font-size: 13px;font-family: SimHei" v-if="memberInfo != null">会员到期时间：{{ memberInfo.endDate }}</p>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
    <div style="background:#ECECEC; padding:30px;margin-top: 30px;margin-bottom: 30px">
      <a-row :gutter="30">
        <a-col :span="8" v-for="(item, index) in statusList" :key="index">
          <div style="background: #e8e8e8">
            <a-card :bordered="false">
              <div style="text-align: center;">
                <a-icon type="meh" theme="twoTone" style="font-size: 80px;margin-top: 10px;margin-bottom: 10px"/>
                <p style="font-size: 25px;text-align: center">{{ item.name }}</p>
                <p style="font-size: 13px;font-family: SimHei">价格：{{ item.price }}元 | 天数：{{ item.days }}天 <a @click="memberSend(item)" v-if="memberInfo == null">购买</a></p>
              </div>
            </a-card>
          </div>
        </a-col>
      </a-row>
    </div>
  </a-card>
</template>

<script>
import {mapState} from 'vuex'
export default {
  name: 'Work',
  data () {
    return {
      statusList: [],
      loading: false,
      userInfo: null,
      memberInfo: null
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
  },
  methods: {
    selectMemberByUserId () {
      this.$get(`/cos/member-info/member/${this.currentUser.userId}`).then((r) => {
        this.userInfo = r.data.user
        this.memberInfo = r.data.member
      })
    },
    memberSend (row) {
      let data = { subject: `${row.name}缴费`, totalAmount: row.price, body: '', ruleId: row.id, userId: this.currentUser.userId }
      this.$post('/cos/pay/member', data).then((r) => {
        // console.log(r.data.msg)
        // 添加之前先删除一下，如果单页面，页面不刷新，添加进去的内容会一直保留在页面中，二次调用form表单会出错
        const divForm = document.getElementsByTagName('div')
        if (divForm.length) {
          document.body.removeChild(divForm[0])
        }
        const div = document.createElement('div')
        div.innerHTML = r.data.msg // data就是接口返回的form 表单字符串
        // console.log(div.innerHTML)
        document.body.appendChild(div)
        document.forms[0].setAttribute('target', '_self') // 新开窗口跳转
        document.forms[0].submit()
      })
    },
    getWorkStatusList () {
      this.$get(`/cos/rule-info/list`).then((r) => {
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
