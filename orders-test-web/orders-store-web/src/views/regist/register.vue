<template>
  <div >
    <header class="banner">
      <h3>注册页面</h3>
    </header>
    <div class="content">
      <div class="steps">
        <span><router-link to="/regist/baseInfo">基本信息</router-link></span>
        <span><router-link to="/regist/stuff">证明材料</router-link></span>
        <span><router-link to="/regist/commitment">承诺书</router-link></span>
      </div>
      <router-view 
         @baseInfoChange="getBaseInfo"
         @promisedChange="getPromised" />
      <div>
        <button>暂存</button>
        <button>提交</button>
      </div>
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
      registApply:{}
    }
  },
  methods:{
    getBaseInfo(newBaseInfo){
      let baseInfo = JSON.parse(JSON.stringify(newBaseInfo));
      //console.log(JSON.stringify(newBaseInfo));
      this.registApply.baseInfo = baseInfo;
    },
    getPromised(newCommitment){
      let commitment = JSON.parse(JSON.stringify(newCommitment));
      this.registApply.commitment = commitment;
    }
  },
  mounted() {
    this.$router.push({name:'baseInfo'});
    
    //从后台查询有无暂存的注册申请.
    this.registApply = {
      baseInfo:{
        leaderName:'张三',
        storeName:'某某杂货店'
      }
    };
  }
}
</script>
<style>
div.steps {
  margin:10px;
}
</style>