import axiosInst from '@/axios';
import {StoreMember} from '@/types/store';

export const getStoresByAccountNo:(accountNo:string)=>Promise<StoreMember[]> = (accountNo)=>{
    return axiosInst.get(`/storeMember/byAccountNo?accountNo=${accountNo}`).then((resp)=>resp.data);
}