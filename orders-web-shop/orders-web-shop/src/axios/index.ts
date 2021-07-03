import axios from "axios";
import router from "@/router/index";
//import store from "@/store";

const message = (msg: string,type?: any) => { 
    /*   
    Message({
      message: msg,
      type: type || 'warning',
      duration:1500,
    });
    */
}

/** 
 * 跳转登录页
 * 携带当前页面路由，以期在登录页面完成登录后返回当前页面
 */
const toLogin = () => {
    router.replace({
      name: 'LoginPage',
    });
}

/** 
 * 请求失败后的错误统一处理 
 * @param {Number} status 请求失败的状态码
 */
const errorHandle = (status: number, other: string) => {
  // 状态码判断
  switch (status) {
    // -1: 未登录状态，跳转登录页
    case -1:
      toLogin();
      break;
    // 403 token过期
    // 清除token并跳转登录页
    case 403:
      message('登录过期，请重新登录');
      localStorage.removeItem('token');
      //store.commit('token', null);
      setTimeout(() => {
        toLogin();
      }, 1000);
      break;
    // 404请求不存在
    case 404:
      message('请求的资源不存在');
      break;
    default:
      message(other);
  }
}



/* 实例化请求配置 */
const axiosInst = axios.create({
  headers: {
    //php 的 post 传输请求头一定要这个 不然报错 接收不到值
    //"Content-Type": "application/x-www-form-urlencoded", 
  },
  // 请求时长
  timeout: 1000 * 30,
  // 请求的base地址 TODO:这块以后根据不同的模块调不同的api
  baseURL:'api',
    // baseURL:
    //   process.env.NODE_ENV === "development"
    //     ? "测试"
    //     : "正式",
  withCredentials: true,
})

/** 
 * 请求拦截器 
 * 每次请求前，如果存在token则在请求头中携带token 
 */ 
axiosInst.interceptors.request.use(
  config => {        
      // 登录流程控制中，根据本地是否存在token判断用户的登录情况        
      // 但是即使token存在，也有可能token是过期的，所以在每次的请求头中携带token        
      // 后台根据携带的token判断用户的登录情况，并返回给我们对应的状态码        
      // 而后我们可以在响应拦截器中，根据状态码进行一些统一的操作。        
    const token = '';//store.state.token;
    localStorage.setItem('token', token);
      token && (config.headers.Authorization = token)
      return config;
    },    
    // error => Promise.error(error)
)

 /* 
  // 响应拦截器
axiosInst.interceptors.response.use(
  // 请求成功
  res => res.status === 200 ? Promise.resolve(res.data) : Promise.reject(res),
  // 请求失败
  error => {
        const { response } = error;
        if (response) {
            // 请求已发出，但是不在2xx的范围 
            errorHandle(response.status, response.data.message);
            return Promise.reject(response);
        } else {
            // 处理断网的情况
            // eg:请求超时或断网时，更新state的network状态
            // network状态在app.vue中控制着一个全局的断网提示组件的显示隐藏
            // 后续增加断网情况下做的一些操作
            //store.commit('networkState', false);
        }
  }
)    
*/

// 只需要考虑单一职责，这块只封装axios
export default axiosInst;