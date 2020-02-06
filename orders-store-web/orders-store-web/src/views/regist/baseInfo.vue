<template>
  <form class="mForm" >
    <div>
      <span>关联账号</span>
      <span><input type="text" v-model="baseInfo.accountNo" ></span>
    </div>
    <div>
      <span>姓名</span>
      <span><input type="text" v-model="baseInfo.leaderName" >*本平台采用实名制请填写真实姓名。</span>
    </div>
    <div>
      <span>身份证号</span>
      <span><input type="text" v-model="baseInfo.leaderIdCard" >*</span>
    </div>
    <div>
      <span>店铺名</span>
      <span><input type="text" v-model="baseInfo.storeName" >*</span>
    </div>
    <div>
      <span>所属企业</span>
      <span><input type="text" v-model="baseInfo.etpsName" >*与营业执照上的企业名称一致。</span>
    </div>
    <div>
      <span>法定代表人</span>
      <span><input type="text" v-model="baseInfo.legalRepName" >*与营业执照上的法定代表人一致。</span>
    </div>
    <div>
      <span>工商注册号</span>
      <span><input type="text" v-model="baseInfo.regNo" >*与营业执照上的内容一致。</span>
    </div>
    <div>
      <span>经营商品主要类别</span>
      <span><textarea v-model="baseInfo.productTypes" ></textarea></span>
    </div>
  </form>
</template>
<script>
import Vue from 'vue';
import '../../assets/css/style.css';

export default {
  name: 'baseInfo',
  data(){
    return {
      baseInfo:{
      }
    }
  },
  mounted(){
    Vue.set(this.baseInfo,"accountNo",'xxss@orders.com');
  }
}
</script>