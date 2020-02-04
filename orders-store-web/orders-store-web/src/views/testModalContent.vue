<template>
  <div>
          这里是模态对话框的内容<br>
    Hello {{userName}}!<br>
    <button @click="closeWindow" >关闭</button>
  </div>
</template>
<script>
import Vue from 'vue';
export default {
  name: 'testModalContent',
  data(){
    return {
      userName:''
    }
  },
  mounted(){
    console.log('init of testModalContent.');
    this.userName = this.$route.query.userName;
  },
  methods:{
    closeWindow(){
      window.parent.closeWindow();
    }
  }
  
}
</script>