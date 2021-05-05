import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/view/Login.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login
    }
  ]
})
