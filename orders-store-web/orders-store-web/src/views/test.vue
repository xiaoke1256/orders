<template>
  <div>
          测试页面<br>
    <button v-on:click="showModal">模态弹窗</button>
    <my-modal title="消息" :visible.sync="testModal.isVisible">
	  <div slot="content">
	    内容
	  </div>
	  <div slot="ft" class="ft">
	    <a class="btn btn-primary" @click="">确定</a>
	    <a class="btn btn-default" @click="testModal.isVisible=false">取消</a>
	  </div>
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