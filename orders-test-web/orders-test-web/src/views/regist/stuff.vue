<template>
  <form class="mForm" >
    <div>
      <span>营业执照</span>
      <span><button @click="doNothing" >上传</button></span>
    </div>
  </form>
</template>
<script>
import Vue from 'vue';
import '../../assets/css/style.css';

export default {
  name: 'stuff',
  data(){
    return {
      stuffs:[
      ]
    }
  },
  methods:{
    doNothing(){
    }
  }
}
</script>
