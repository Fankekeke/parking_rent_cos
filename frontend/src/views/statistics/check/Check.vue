<template>
  <a-card :bordered="false" class="card-area">
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
                </span>
              </span>
            </a-card>
          </div>
        </a-col>
      </a-row>
    </div>
  </a-card>
</template>

<script>
export default {
  name: 'Work',
  data () {
    return {
      statusList: [],
      loading: false
    }
  },
  mounted () {
    this.getWorkStatusList()
  },
  methods: {
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
