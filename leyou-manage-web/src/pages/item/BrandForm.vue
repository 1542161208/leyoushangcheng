<template>
  <v-form v-model="valid" ref="myBrandForm">
    <v-text-field v-model="brand.name" label="请输入品牌名称" required :rules="nameRules"/>
    <v-text-field v-model="brand.letter" label="请输入品牌首字母" required :rules="letterRules"/>
    <v-cascader
      url="/item/category/list"
      multiple
      required
      v-model="brand.categories"
      label="请选择商品分类"/>
    <v-layout row>
      <v-flex xs3>
        <span style="font-size: 16px; color: #444">品牌LOGO：</span>
      </v-flex>
      <v-flex>
        <v-upload
          v-model="brand.image" url="/upload/image" :multiple="false" :pic-width="250" :pic-height="90"
        />
      </v-flex>
    </v-layout>
    <v-layout class="my-4" row>
      <v-spacer/>
      <v-btn @click="submit" color="primary">提交</v-btn>
      <v-btn @click="clear">重置</v-btn>
    </v-layout>
  </v-form>
</template>

<script>
  export default {
    name: "brand-form",
    props: {
      oldBrand: {
        type: Object
      },
      isEdit: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        valid: false, // 表单校验结果标记
        brand: {
          name: '', // 品牌名称
          letter: '', // 品牌首字母
          image: '',// 品牌logo
          categories: [], // 品牌所属的商品分类数组
        },
        nameRules: [
          v => !!v || "品牌名称不能为空",
          v => v.length > 1 || "品牌名称至少2位"
        ],
        letterRules: [
          v => !!v || "首字母不能为空",
          v => /^[a-zA-Z]{1}$/.test(v) || "品牌字母只能是1个字母"
        ]
      }
    },
    methods: {
      submit() {
        // 打印看一下这个qs
        console.log(this.$qs);
        // 表单校验
        if (this.$refs.myBrandForm.validate()) {
          // 定义一个请求参数对象，通过解构表达式来获取brand中的属性
          const {categories, letter, ...params} = this.brand;
          // 数据库中只要保存分类的id即可，因此我们对categories的值进行处理,只保留id，并转为字符串
          params.cids = categories.map(c => c.id).join(",");
          // 将字母都处理为大写
          params.letter = letter.toUpperCase();
          // 将数据提交到后台
          // this.$http.post('/item/brand', this.$qs.stringify(params))
          this.$http({
            method: this.isEdit ? 'put' : 'post',
            url: '/item/brand',
            /*data: params*/
            data:this.$qs.stringify(params)
            /**
             *  data:params写法默认是以josn对象{name:"黑马",image:"",cids:"",letter:"H"}的方式传递到后台
             *  使用这个方法以普通参数(非json格式)的方式传递到后台
             *  qs:是一个第三方库,我们可以用npm install qs --save 来安装,我们在项目中已经集成了,大家无需安装
             *  这个工具的名字,QS:即Query String 请求参数字符串
             *  什么是请求参数字符串？例如：name=jack&age=21
             *  QS工具可以便捷的实现JS的Object与QueryString的转换
             *  为什么使用 this.$qs呢？(this代表vue对象)
             *  其实在mains.js中这么写：
             *  import qs from 'qs';
             *  Vue.prototype.$qs=qs;
             *  不然我们在每个页面都要导入这个qs
             *  这个qs对象有两个方法：
             *    1.stringify
             *      this.$qs.stringify({name:"张三",age:20})->查询字符串 "name=张三&age=20"
             *    2.parse
             *      this.$qs.parse("name=张三&age=20")->json对象 {name:"张三",age:20}
             */
          }).then(() => {
            // 关闭窗口
            this.$emit("close");
            this.$message.success("保存成功！");
          })
            .catch(() => {
              this.$message.error("保存失败！");
            });
        }
      },
      clear() {
        // 重置表单
        this.$refs.myBrandForm.reset();
        // 需要手动清空商品分类
        this.categories = [];
      }
    },
    watch: {
      oldBrand: {// 监控oldBrand的变化
        handler(val) {
          if (val) {
            // 注意不要直接复制，否则这边的修改会影响到父组件的数据，copy属性即可
            this.brand = Object.deepCopy(val)
          } else {
            // 为空，初始化brand
            this.brand = {
              name: '',
              letter: '',
              image: '',
              categories: [],
            }
          }
        },
        deep: true
      }
    }
  }
</script>

<style scoped>

</style>
