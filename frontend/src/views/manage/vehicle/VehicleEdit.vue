<template>
  <a-modal v-model="show" title="修改车辆" @cancel="onClose" :width="1200">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        修改
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="6">
          <a-form-item label='车牌号' v-bind="formItemLayout">
            <a-input v-decorator="[
            'vehicleNumber',
            { rules: [{ required: true, message: '请输入车牌号!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='车辆名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'name',
            { rules: [{ required: true, message: '请输入名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='车辆颜色' v-bind="formItemLayout">
            <a-input v-decorator="[
            'vehicleColor'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='发动机号码' v-bind="formItemLayout">
            <a-input v-decorator="[
            'engineNo'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='排放标准' v-bind="formItemLayout">
            <a-input v-decorator="[
            'emissionStandard'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='燃料类型' v-bind="formItemLayout">
            <a-select v-decorator="[
              'fuelType',
              { rules: [{ required: true, message: '请输入燃料类型!' }] }
              ]">
              <a-select-option value="1">燃油</a-select-option>
              <a-select-option value="2">柴油</a-select-option>
              <a-select-option value="3">油电混动</a-select-option>
              <a-select-option value="4">电能</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='所属用户' v-bind="formItemLayout">
            <a-select v-decorator="[
              'userId',
              { rules: [{ required: true, message: '请输入所属用户!' }] }
              ]">
              <a-select-option :value="item.id" v-for="(item, index) in shopList" :key="index">{{ item.name }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='照片' v-bind="formItemLayout">
            <a-upload
              name="avatar"
              action="http://127.0.0.1:9527/file/fileUpload/"
              list-type="picture-card"
              :file-list="fileList"
              @preview="handlePreview"
              @change="picHandleChange"
            >
              <div v-if="fileList.length < 2">
                <a-icon type="plus" />
                <div class="ant-upload-text">
                  Upload
                </div>
              </div>
            </a-upload>
            <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
              <img alt="example" style="width: 100%" :src="previewImage" />
            </a-modal>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='备注' v-bind="formItemLayout">
            <a-textarea :rows="6" v-decorator="[
            'content'
            ]"/>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script>
import {mapState} from 'vuex'
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
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'vehicleEdit',
  props: {
    vehicleEditVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.vehicleEditVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      rowId: null,
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      formLoading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      shopList: [],
      vehicleTypeList: [],
      brandList: []
    }
  },
  mounted () {
    this.selectShopList()
  },
  methods: {
    selectShopList () {
      this.$get(`/cos/user-info/list`).then((r) => {
        this.shopList = r.data.data
      })
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
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    responsibleInit (responsible) {
      this.formLoading = false
      if (responsible !== null && responsible !== '') {
        let responsibleList = []
        responsible.split(',').forEach((id, index) => {
          responsibleList.push(parseInt(id))
        })
        this.checkedList = responsibleList
        console.log(JSON.stringify(this.checkedList))
        this.onChange(this.checkedList)
      }
      setTimeout(() => {
        this.formLoading = true
      }, 200)
    },
    setFormValues ({...vehicle}) {
      this.rowId = vehicle.id
      let fields = ['vehicleNo', 'vehicleNumber', 'vehicleColor', 'name', 'engineNo', 'carryPassengers', 'principal', 'brand', 'phone', 'userId', 'factoryDate', 'useType', 'emissionStandard', 'fuelType', 'content', 'shopId', 'dayPrice']
      let obj = {}
      Object.keys(vehicle).forEach((key) => {
        setTimeout(() => {
          if (key === 'userId') {
            vehicle[key] = vehicle[key].toString()
          }
        }, 200)
        if (key === 'factoryDate' && vehicle[key] != null) {
          vehicle[key] = moment(vehicle[key])
        }
        if (key === 'images') {
          this.fileList = []
          this.imagesInit(vehicle['images'])
        }
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = vehicle[key]
        }
      })
      this.form.setFieldsValue(obj)
    },
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        if (image.response !== undefined) {
          images.push(image.response)
        } else {
          images.push(image.name)
        }
      })
      this.form.validateFields((err, values) => {
        values.id = this.rowId
        values.images = images.length > 0 ? images.join(',') : null
        if (!err) {
          if (values.factoryDate) {
            values.factoryDate = moment(values.factoryDate).format('YYYY-MM-DD')
          }
          this.loading = true
          this.$put('/cos/vehicle-info', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
