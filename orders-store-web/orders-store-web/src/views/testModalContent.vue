<template>
  <div>
          这里是模态对话框的内容<br>
    Hello {{userName}}!<br>
         我要对大家说的话：<input name="msg" v-model="msg" > <button @click="sendMsg" >发送</button> <br>
    <button @click="closeWindow" >关闭</button>
  </div>
</template>
<script>
import Vue from 'vue';
export default {
  name: 'testModalContent',
  data(){
    return {
      userName:'',
      msg:'HahaHa'
    }
  },
  mounted(){
    console.log('init of testModalContent.');
    this.userName = this.$route.query.userName;
  },
  methods:{
    closeWindow(){
      window.parent.closeWindow();
    },
    sendMsg(){
      let msgObj = {userName:this.userName,msg:this.msg};
      window.parent.sendMsg(msgObj);
    }
  }
  
}
</script>