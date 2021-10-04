export interface Store{
    storeNo:string;
	storeName:string;
	storeIntro:string;
	payType:string;
	payAccountNo:string;
	insertTime?:string;
	updateTime?:string;
}

export interface StoreMember{
	storeMemberId:number;
    storeNo:string;
	accountNo:string;
	role:string;
	isDefaultStore:string;
	store:Store;
}