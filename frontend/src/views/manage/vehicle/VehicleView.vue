<template>
  <a-modal v-model="show" title="车辆详情" @cancel="onClose" :width="1000">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="vehicleData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">车辆信息</span></a-col>
        <a-col :span="6"><b>车辆编号：</b>
          {{ vehicleData.vehicleNo }}
        </a-col>
        <a-col :span="6"><b>车牌号：</b>
          {{ vehicleData.vehicleNumber ? vehicleData.vehicleNumber : '- -' }}
        </a-col>
        <a-col :span="6"><b>车辆颜色：</b>
          {{ vehicleData.vehicleColor ? vehicleData.vehicleColor : '- -' }}
        </a-col>
        <a-col :span="6"><b>车辆名称：</b>
          {{ vehicleData.name }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="6"><b>排放标准：</b>
          {{ vehicleData.emissionStandard }}
        </a-col>
        <a-col :span="6"><b>发动机号码：</b>
          {{ vehicleData.engineNo }}
        </a-col>
        <a-col :span="6"><b>联系电话：</b>
          {{ vehicleData.phone }}
        </a-col>
        <a-col :span="6"><b>所属用户：</b>
          {{ vehicleData.userName }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="6"><b>燃料类型：</b>
          <span v-if="vehicleData.fuelType == 1" style="color: green">燃油</span>
          <span v-if="vehicleData.fuelType == 2" style="color: green">柴油</span>
          <span v-if="vehicleData.fuelType == 3" style="color: green">油电混动</span>
          <span v-if="vehicleData.fuelType == 4" style="color: green">电能</span>
        </a-col>
        <br/>
        <br/>
        <a-col :span="24"><b>备注：</b>
          {{ vehicleData.content }}
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">图册</span></a-col>
        <a-col :span="24">
          <a-upload
            name="avatar"
            action="http://127.0.0.1:9527/file/fileUpload/"
            list-type="picture-card"
            :file-list="fileList"
            @preview="handlePreview"
            @change="picHandleChange"
          >
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-col>
      </a-row>
      <br/>
    </div>
    <br/>
  </a-modal>
</template>

<script>
import baiduMap from '@/utils/map/baiduMap'
import moment from 'moment'
moment.locale('zh-cn')
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'vehicleView',
  props: {
    vehicleShow: {
      type: Boolean,
      default: false
    },
    vehicleData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.vehicleShow
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      repairInfo: null,
      reserveInfo: null,
      durgList: [],
      logisticsList: [],
      userInfo: null,
      vehicleInfo: null,
      shopInfo: null,
      brandInfo: null,
      typeInfo: null
    }
  },
  watch: {
    vehicleShow: function (value) {
      if (value) {
        this.imagesInit(this.vehicleData.images)
      }
    }
  },
  methods: {
    local (vehicleData) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let point = new BMap.Point(vehicleData.longitude, vehicleData.latitude)
      baiduMap.pointAdd(point)
      baiduMap.findPoint(point, 16)
      // let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions:{map: baiduMap.rMap(), autoViewport: true}});
      // driving.search(new BMap.Point(this.nowPoint.lng,this.nowPoint.lat), new BMap.Point(scenic.point.split(",")[0],scenic.point.split(",")[1]));
    },
    dataInit (vehicleNo) {
      this.$get(`/cos/vehicle-info/detail/${vehicleNo}`).then((r) => {
        this.vehicleInfo = r.data.vehicle
        this.shopInfo = r.data.shop
        this.brandInfo = r.data.brand
        this.typeInfo = r.data.type
      })
    },
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
