<template>
  <div class="root">
    <header class="banner">
    </header>
    <aside class="nav">
      <ul>
        <li><router-link to="/test">店铺管理</router-link></li>
        <li><a>商品管理</a></li>
      </ul>
    </aside>
    
    <router-view/>
    
    <footer>
      <a>关于我们</a>
      <a>合作伙伴</a>
      <a>客服电话</a>
    </footer>
  </div>
</template>
<script>
  import Vue from 'vue';
  import '../assets/css/style.css';

  export default {
    name: 'root'
  }
</script>

