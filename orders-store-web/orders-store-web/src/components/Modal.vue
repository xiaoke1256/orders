<template>
  <transition name="fade">
  <div class="modal-wrap" v-if="visible">
    <div class="modal" ref="modal" :style="sizeChange">
      <div class="hd">
        <span>{{title}}</span>
        <i class="btn-close iconfont icon-close" @click="close">X</i>
      </div>
      <div class="bd">
        <slot name="content">
          <iframe width="98%" height="90%" frameborder="0" v-bind:src="'/#/'+url" />
        </slot>
      </div>
      <slot name="ft">
        <!--<div class="ft" slot="ft">
          <a href="javscript:void(0)" class="btn make-sure" @click="sure">确定</a>
          <a href="javscript:void(0)" class="btn cancel" @click="close">取消</a>
        </div>-->
      </slot>
    </div>
    <div class="backdrop" ></div>
  </div>
  </transition>
</template>

<script>
  import classUtil from '../assets/js/classUtil';
  export default {
    name: "MyModal",
    props: {
      visible: {
        type: Boolean,
        default: false,
        required: true,
      },
      title: {
        type: String,
        default: '',
      },
      url:{
        type: String,
        default: '',
      },
      height:{
        type: Number,
        default: 200,
      },
      width:{
        type: Number,
        default: 250,
      }
    },
    computed:{
      sizeChange(){
        return {height:this.height+'px',width:this.width+'px'};
      }
    },
    watch:{
      /*
      visible:function(curVal) {
        if(curVal&&document.body.scrollHeight > window.innerHeight){
          classUtil.addClass(document.body,'backdrop-open')
        }else{
          classUtil.removeClass(document.body,'backdrop-open');
        }
      }
      */
    },
    methods: {
      close() {
        this.$emit('update:visible', false)
      },
    }
  }
</script>
<style>
  .backdrop {
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background: rgba(0,0,0,0.5);
  }
  .modal {
    background: #fff;
    z-index: 10;
    position: relative;
    border-radius: 10px;
    margin: 0 auto;
  }
  .hd {
    padding: 10px 10px;
    text-align: justify; 
    height:15px
  }
  .hd:after { 
    content: ""; 
    display: inline-block; 
    overflow: hidden; 
    width: 100%; 
  } 
  .hd span {
    display: inline-block; 
  }
  .hd .icon-close {
    cursor:default;
  }
  .bd{
    width: 100%; 
    height: 75%;
  }
  .bd iframe {
    _width: 100%; 
  }
</style>