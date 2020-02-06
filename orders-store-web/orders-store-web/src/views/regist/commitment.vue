<template>
  <form class="mForm" >
    <div>
      <h4>承诺书</h4>
      <p>
        Balabala,bala balab alabalabala,balabalabalabala,balabal abala balabalaba labalabala.balab alaba labala,balab alabala.balab ala.
        Bala bala bala Balabala balabalabala.
      </p>
      <p>
        Balabala,balaba,Balabala,balabaB alabala,balaba.balabalaba labala,balabal abala balabalaba labalabala.balab alaba labala,,balabalaba
        labala,balabal abala balabalaba labalabala.balab alaba labala,,balabala balabala,balabal abala balabalaba labalabala.balab alaba 
        labala,,balabala balabala,balabal abala balabalaba labalabala.balab alaba labala.
      </p>
    </div>
    <div>
      <input type="checkbox" v-model="commitment.promised" value="1" > 我已仔细阅读上述承诺，并已完全同意上述内容。
    </div>
  </form>
</template>
<script>
import Vue from 'vue';
import '../../assets/css/style.css';

export default {
  name: 'commitment',
  data(){
    return {
      commitment:{}
    }
  },
  methods:{
  }
}
</script>
<style>
.mForm p{
  text-indent: 2em;
  margin:0
}
</style>