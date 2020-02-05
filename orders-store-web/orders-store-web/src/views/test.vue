<template>
  <div>
          测试页面<br>
    <button v-on:click="showModal">模态弹窗</button>
    
    <div>
    {{msgText}}
    </div>
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
	    },
	    msgText:''
	  }
	},
	methods:{
	  showModal(){
	  	this.testModal.isVisible=true;
	  	this.testModal.url='testModalContent?userName=John';
	  	this.testModal.width=500;
	  },
	  closeWindow(){
	    this.testModal.isVisible=false;
	  },
	  sendMsg(msgObj){
	    let text = msgObj.userName+'对大家说：“'+msgObj.msg+'”';
	    this.msgText = text;
	    this.closeWindow();
	  }
	},
	mounted(){
	  
	},
	created(){
        let self = this;
        console.log("this.vueId:"+this.vueId);
        window.closeWindow = (infojson)=>{
             self.closeWindow(infojson)        
        };
        window.sendMsg = (infojson)=>{
             self.sendMsg(infojson)        
        }
    }    
  };
  
</script>