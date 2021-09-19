import axiosInst from '@/axios';
import {Product, ProductSearchParms} from '@/types/product';

export const getPorductList:(params:ProductSearchParms) => Promise<{totalCount:number,resultList:Product[]}> = (params)=>{
    return axiosInst.get('/product',{params}).then((resp)=>resp.data);
}