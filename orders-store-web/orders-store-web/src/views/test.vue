<template>
  <div>
          测试页面<br>
    <button v-on:click="showModal">模态弹窗</button>
    <my-modal title="消息" :visible.sync="testModal.isVisible">
	  <router-view name="modalContent" />
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
	    }
	  }
	},
	methods:{
	  showModal(){
	  	this.testModal.isVisible=true;
	  }
	}
  }
</script>