export interface LoginForm{
  public loginName:string;
  public password:string;
  public randomCode:string;// 随机校验码
  public sessionId:string;
}