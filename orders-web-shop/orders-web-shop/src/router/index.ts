import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/view/Login.vue'
import Home from '@/view/Home.vue'
import HomeIndex from '@/view/home/Index.vue'

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
    },
    {
      path: '/home',
      name: 'Home',
      component: Home,
      children:[
        {
          path: 'index',
          name: 'HomeIndex',
          component: HomeIndex
        },
      ]
    },
    {
      path: '/product',
      component: Home,
      children:[
        {
          path: 'index',
          name: 'ProductIndex',
          component: ()=>import('@/view/product/Table.vue')
        }
      ]
    },
    {
      path: '/store',
      component: Home,
      children:[
        {
          path: 'index',
          name: 'StoreIndex',
          component: ()=>import('@/view/store/Index.vue')
        }
      ]
    }
  ]
})
