export interface SearchParms{
    pageNo:number;
    pageSize:number;
}

export interface MenuItem{
    menuName:string;
    menuCode:string;
    icon:string;
    path:string;
    children:MenuItem[];
}