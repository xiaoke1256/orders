import axiosInst from '@/axios';
import {Product} from '@/types/product';

export const getPorductList:() => Promise<{resultList:Product[]}> = ()=>{
    return axiosInst.get('/product').then((resp)=>resp.data);
}