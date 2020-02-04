<template>
  <div>
          测试页面<br>
    <button v-on:click="showModal">模态弹窗</button>
    <my-modal title="消息" :visible.sync="testModal.isVisible" :url="testModal.url" :width="testModal.width"  >
	</my-modal>
  </div>
</template>
<script>
  import Vue from 'vue';
  export default {
    name: 'test',
    data () {
	  return {
	    testModal:{
	      isVisible: false,
	      url:'',
	      width:10
	    }
	  }
	},
	methods:{
	  showModal(){
	  	this.testModal.isVisible=true;
	  	this.testModal.url='testModalContent?userName=John';
	  	this.testModal.width=500;
	  }
	}
  }
</script>