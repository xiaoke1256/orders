export interface Store{
    storeNo:string;
	storeName:string;
	storeIntro:string;
	payType:string;
	payAccountNo:string;
	insertTime:string;
	updateTime:string;
}

export interface StoreWithMember{
    storeNo:string;
	accountNo:string;
	role:string;
	isDefaultStore:string;
	store:Store;
}