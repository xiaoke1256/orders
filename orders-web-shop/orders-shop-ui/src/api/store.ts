import axiosInst from '@/axios';
import { StoreMember, Store } from '@/types/store';

export const getStoresByAccountNo: () => Promise<StoreMember[]> = () => {
  return axiosInst.get(`/api/storeMember/byAccountNo`).then((resp) => resp.data);
}

export const saveStore: (store: Store) => Promise<boolean> = (store) => {
  return axiosInst.post('/api/store', store).then((resp) => resp.data);
}

export const updateStore: (store: Store) => Promise<boolean> = (store) => {
  const storeNo = store.storeNo;
  return axiosInst.put(`/api/store/${storeNo}`, store).then((resp) => resp.data);
}

export const getStore: (storeNo: string) => Promise<Store> = (storeNo) => {
  return axiosInst.get(`/api/store/${storeNo}`).then((resp) => resp.data);
}