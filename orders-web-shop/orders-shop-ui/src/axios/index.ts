import axios from "axios";
import router from "@/router/index";
import qs from "qs";
//import store from "@/store";
import { Message, Notice } from 'view-ui-plus';
import ViewUIPlus from 'view-ui-plus';

const message = (msg: string, type?: any) => {
  console.log("msg:" + msg);
  if (msg) {
    Message.error(msg);
  }
  //Vue.getCurrentInstance.prototype.$Message.error(msg);
}

/** 
 * 跳转登录页
 * 携带当前页面路由，以期在登录页面完成登录后返回当前页面
 */
const toLogin = () => {
  router.replace({
    name: 'Login',
  });
}

/** 
 * 请求失败后的错误统一处理 
 * @param {Number} status 请求失败的状态码
 */
const errorHandle = (status: number, code: string, other: string) => {
  // 状态码判断
  switch (status) {
    // -1: 未登录状态，跳转登录页
    case -1:
      toLogin();
      break;
    // 403 token过期
    // 清除token并跳转登录页
    case 401:
      message('登录过期');
      toLogin();
      break;
    case 403:
      message('你无权限访问该资源');
      break;
    // 404请求不存在
    case 404:
      message('请求的资源不存在');
      break;
    case 504:
      message('与服务器失去连接');
      break;
    default:
      if (code == '99') {
        message("系统异常!");
      } else if (code == '80') {
        Notice.error({ title: '操作异常', desc: other })
      } else {
        message(other);
      }
  }
}



/* 实例化请求配置 */
const axiosInst = axios.create({
  headers: {
    //php 的 post 传输请求头一定要这个 不然报错 接收不到值
    "Content-Type": "application/x-www-form-urlencoded",
  },
  // 请求时长
  timeout: 1000 * 30,
  // 请求的base地址 TODO:这块以后根据不同的模块调不同的api
  //baseURL: 'http://localhost:8763/store_intra/',//process.env.BASE_API,
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
    const token = sessionStorage.getItem('token');
    console.log("token in:" + token);
    token && (config.headers.Authorization = token)
    return config;
  },
  // error => Promise.error(error)
)

/**数据格式化 */
axiosInst.interceptors.request.use(
  config => {
    if (config.method?.toLowerCase() === 'post' || config.method?.toLowerCase() === 'put') {
      if (!(config.data instanceof FormData)) {
        config.data = qs.stringify(config.data);
      }
    }
    return config;
  },
)

axiosInst.interceptors.request.use(
  config => {
    // const instance = getCurrentInstance()
    // const { proxy } = instance != null ? instance : { proxy: undefined }
    ViewUIPlus.LoadingBar.start();
    return config;
  },
)


// 响应拦截器
axiosInst.interceptors.response.use(
  // 请求成功
  res => {
    //Vue.getCurrentInstance.prototype.$Loading.finish();
    ViewUIPlus.LoadingBar.finish();
    console.log("res.config.method:" + res.config.method);
    if (res.config.method && res.config.method.toLowerCase() !== 'get') {
      // const instance = getCurrentInstance()
      // const { proxy } = instance != null ? instance : { proxy: undefined }
      Notice.success({ title: '操作成功。' });
    }
    return Promise.resolve(res);
  },
  // 请求失败
  async error => {
    //Vue.getCurrentInstance.prototype.$Loading.error();
    ViewUIPlus.LoadingBar.error();
    const { response } = error;
    if (response) {
      // 请求已发出，但是不在2xx的范围 
      if (response.status === 401) {  //401特殊处理
        console.log("refresh url:" + response.config.url);
        console.log("refresh url:" + (response.config.url.indexOf('/login/refresh') >= 0));
        if (!sessionStorage.getItem('token') || response.config.url.indexOf('/login/refresh') >= 0) {
          console.log("refresh url" + response.config.url);
          setTimeout(() => {
            toLogin();
          }, 1000);
          return await Promise.reject(response);
        }

        //调用refresh 获取新的tonken
        const { refreshToken, token } = await getRefreshToken(sessionStorage.getItem('refreshToken') as string);
        //得到新的token后重新发送请求。
        sessionStorage.setItem('token', token);
        sessionStorage.setItem('refreshToken', refreshToken);
        return await axiosInst.request(response.config);
      } else {
        errorHandle(response.status, response.data.code, response.data.msg);
      }
      return await Promise.reject(response);
    } else {
      // 处理断网的情况
      message('与服务器失去连接');
      // eg:请求超时或断网时，更新state的network状态
      // network状态在app.vue中控制着一个全局的断网提示组件的显示隐藏
      // 后续增加断网情况下做的一些操作
      //store.commit('networkState', false);
      return await Promise.reject(response);
    }
  }
)

const getRefreshToken: (refreshToken: string) => Promise<{ token: string; refreshToken: string }> = (refreshToken) => {
  return axiosInst.post(`/login/refresh?refreshToken=${refreshToken}`).then((resp) => resp.data);
}

export default axiosInst;