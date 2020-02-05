<template>
  <div >
    <select v-model="storeNo" >
      <option v-for="x in stores" :value="x.storeNo">{{x.storeName}}</option>
    </select>
    <button v-on:click="doLogin">登录</button>
    <button v-on:click="toRegister">注册新账号</button>
  </div>
</template>
<script>
import Vue from 'vue';
export default {
  name: 'login',
  data () {
    return {
      stores: [],
      storeNo:''
    }
  },
  methods:{
    doLogin(){
      if(this.storeNo==='' || !this.storeNo){
        alert('请选择登录账号');//以后用相关组件替换掉。
        return false;
      }
      //把token放到sessionStorage.
      window.sessionStorage.setItem("storeNo", this.storeNo);
      this.$router.push('/home');
    },
    toRegister(){
      this.$router.push('/register');
    }
  },
  mounted () {
    this.$http.get('/api/store/queryAvailable').then(function(rep){
      this.stores = rep.data;
    },function(rep){
      console.error(rep);
      throw new Error(rep);
    })
  }
}
</script>
