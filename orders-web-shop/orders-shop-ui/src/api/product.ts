import axiosInst from '@/axios';
import { Product, ProductSearchParms } from '@/types/product';

export const getPorductList: (params: ProductSearchParms) => Promise<{ totalCount: number, resultList: Product[] }> = (params) => {
    return axiosInst.get('/api/product', { params }).then((resp) => resp.data);
}

export const switchOnShef: (productNo: string, onOrOff: string) => Promise<boolean> = (productNo, onOrOff) => {
    return axiosInst.post(`/api/product/${productNo}/switchShfs`, { onOrOff }).then((resp) => resp.data);
}

export const incStorage: (productCode: string, incNum: number) => Promise<boolean> = (productCode, incNum) => {
    return axiosInst.post(`/api/product/incStorage`, { productCode,incNum}).then((resp) => resp.data);
}