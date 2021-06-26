import axios, { AxiosInstance } from 'ts-axios-new';

const instance = axios.create({
     baseURL: 'https://some-domain.com/api/',
     timeout: 1000,
     headers: {'X-Custom-Header': 'foobar'}
 });

 instance.interceptors.response.use(
    // 请求成功
    res => res.status === 200 ? Promise.resolve(res.data) : Promise.reject(res),
    // 请求失败
    error => {
          const { response } = error;
          if (response) {
              // 请求已发出，但是不在2xx的范围 
              //errorHandle(response.status, response.data.message);
              return Promise.reject(response);
          } else {
              // 处理断网的情况
              // eg:请求超时或断网时，更新state的network状态
              // network状态在app.vue中控制着一个全局的断网提示组件的显示隐藏
              // 后续增加断网情况下做的一些操作
              //store.commit('networkState', false);
          }
    }
  );


export const login:(loginName:string,password:string)=>Promise<string>=(loginName,password)=>{
    return instance.post('/login/login',{loginName,password}).then((resp)=>resp.data);
};