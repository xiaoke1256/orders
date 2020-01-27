<template>
  <div >
    <select  >
      <option>-请选择-</option>
      <option v-for="x in stores" :value="x.storeNo">{{x.storeName}}</option>
    </select>
    <button>登录</button>
  </div>
</template>
<script>
export default {
  name: 'login',
  data () {
    return {
      stores: []
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
