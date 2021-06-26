import axiosInst from '@/axios';

export const login:(loginName:string,password:string)=>Promise<string>=(loginName,password)=>{
    return axiosInst.post('/login/login',{loginName,password}).then((resp)=>resp.data);
};