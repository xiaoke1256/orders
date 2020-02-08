//Vue.use(Router);
var router = new VueRouter({
	routes:[{
		path: '/login',
        name: 'login',
		component: {
		  template :
		    '<div > \
	    	  <select  > \
	    	    <option>-请选择-</option>  \
	    	  </select>  \
	    	  <button>登录</button>  \
	        </div>'
	      }
	   }
	]
});