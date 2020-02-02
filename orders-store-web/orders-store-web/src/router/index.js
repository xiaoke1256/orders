import Vue from 'vue'
import Router from 'vue-router'
import login from '@/views/login'
import home from '@/views/home'
import test from '@/views/test'

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'login',
      component: login
    },
    {
      path: '/home',
      name: 'home',
      component: home,
      children: [
    	{
    	  path: '/test',
    	  name: 'test',
    	  component: test 
    	}
      ]
    }
  ]
})
