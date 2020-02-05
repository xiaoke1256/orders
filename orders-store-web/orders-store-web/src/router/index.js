import Vue from 'vue'
import Router from 'vue-router'
import login from '@/views/login'
import home from '@/views/home'
import test from '@/views/test'
import testModalContent from '@/views/testModalContent'
import register from '@/views/regist/register'
import baseInfo from '@/views/regist/baseInfo'

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
      path:'/regist',
  	  name: 'regist',
  	  component: register,
  	  children:[
  		{
  	      path: 'baseInfo',
      	  name: 'baseInfo',
      	  component: baseInfo
  		}
  	  ]
    }
  ]
})
