import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/view/Login.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Index',
      redirect:'/login'
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    }
  ]
})
