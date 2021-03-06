import axiosInst from '@/axios';

import {UserInfo} from '@/types/commons';

export const login:(loginName:string,password:string)=>Promise<{token:string;user:UserInfo}>=(loginName,password)=>{
    return axiosInst.post('/login/login',{loginName,password}).then((resp)=>resp.data);
};