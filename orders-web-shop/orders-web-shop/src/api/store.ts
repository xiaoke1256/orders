import axiosInst from '@/axios';
import {StoreMember,Store} from '@/types/store';

export const getStoresByAccountNo:(accountNo:string)=>Promise<StoreMember[]> = (accountNo)=>{
  return axiosInst.get(`/storeMember/byAccountNo?accountNo=${accountNo}`).then((resp)=>resp.data);
}

export const saveStore:(store:Store) => Promise<boolean> = (store) => {
  return axiosInst.post('/store',store).then((resp)=>resp.data);
}

export const getStore:(storeNo:string) => Promise<Store> = (storeNo) => {
  return axiosInst.get(`/store/${storeNo}`).then((resp)=>resp.data);
}