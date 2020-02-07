
var App = new Vue({
  el: '#app',
  router:router
//  store,
//  axiosReq,
//  components: {App},
//  template: '<App/>'
});


const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}