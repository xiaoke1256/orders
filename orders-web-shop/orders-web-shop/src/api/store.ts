import axiosInst from '@/axios';
import {StoreWithMember} from '@/types/store';

export const getStoresByAccountNo:(accountNo:string)=>Promise<StoreWithMember[]> = (accountNo)=>{
    return axiosInst.get(`/storeMember/byAccountNo?accountNo=${accountNo}`).then((resp)=>resp.data);
}