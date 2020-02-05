<template>
  <div >
    <header class="banner">
              注册页面
    </header>
    <div class="content">
      <div class="steps">
        <span><router-link to="/regist/baseInfo">基本信息</router-link></span>
        <span><router-link to="/baseInfo">证明材料</router-link></span>
        <span><router-link to="/baseInfo">承诺书</router-link></span>
      </div>
      <router-view/>
    </div>
  </div>
</template>

<script>
import Vue from 'vue';
import '../../assets/css/style.css';
export default {
  name: 'regist',
  data () {
    return {
    }
  },
  methods:{
  },
  mounted() {
    this.$router.push({name:'baseInfo'});
  }
}
</script>