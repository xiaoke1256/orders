// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import iView from 'iview';
import './assets/style/public.css';
import './assets/style/login.css';
import './assets/style/home.css';
import './assets/style/style.css';
import 'iview/dist/styles/iview.css'; 
import * as filters from './plugin/filters';

Vue.use(iView);

Vue.config.productionTip = false

Vue.prototype.$Message.config({
    top: 50,
    duration: 3
});

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
});



Object.keys(filters).forEach(key=>{
  Vue.filter(key,(<{[key:string]:Function}>filters)[key])//插入过滤器名和对应方法
})



