import Vue from 'vue'
import Router from 'vue-router'
import login from '@/views/login'
import home from '@/views/home'
import test from '@/views/test'
import testModalContent from '@/views/testModalContent'
import register from '@/views/register'

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
    },
    {
      path:'/testModalContent',
	  name: 'testModalContent',
	  component:testModalContent
    },
    {
      path:'/register',
  	  name: 'register',
  	  component:register
    }
  ]
})
