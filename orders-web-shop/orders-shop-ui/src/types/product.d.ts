import SearchParms from './index';

export interface Product{
    public productCode:string;
	public productName:string;
	public productPrice:number;
	public storeNo:string;
	public inSeckill:string;
	public productStatus:string;
	public productIntro:string;
	public brand:string;
  }

  export interface ProductSearchParms extends SearchParms{
	productCode?:string;
	productName?:string;
	storeNo?:string;
	productTypeName?:string;
	needFullTypeName?:boolean;
  }