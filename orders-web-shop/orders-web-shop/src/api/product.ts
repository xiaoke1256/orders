import axiosInst from '@/axios';
import {Product} from '@/types/product';

export const getPorductList:() => Promise<{totalCount:number,resultList:Product[]}> = ()=>{
    return axiosInst.get('/product').then((resp)=>resp.data);
}