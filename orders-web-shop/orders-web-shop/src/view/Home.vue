<template>
  <div class="layout">
    <Layout class="top" >
      <Header>
        <Menu mode="horizontal" theme="dark" active-name="1" @on-select="onSelect()" >
          <div class="layout-logo">Orders</div>
          
          <div class="layout-nav">
            <a @click="logout"><Icon type="ios-log-out"></Icon></a>
            <a>
              <Icon type="md-person" />
              {{nickName}}
            </a>
          </div>
          <!--欢迎访问商户端-->
        </Menu>
      </Header>
      <Layout class="mainWithMenu">
        <Sider hide-trigger class="leftMenu">
          <Menu active-name="1-2" theme="light" width="auto" :open-names="['1']">
            <Submenu name="1">
                <template slot="title">
                    <Icon type="ios-navigate"></Icon>
                    店铺设置
                </template>
                <MenuItem name="1-1"><router-link to="/store/index">我的店铺</router-link></MenuItem>
            </Submenu>
            <Submenu name="2">
                <template slot="title">
                    <Icon type="ios-keypad"></Icon>
                    我的商品
                </template>
                <MenuItem name="4-1"><router-link to="/product/index">商品列表</router-link></MenuItem>
            </Submenu>
            <Submenu name="3">
                <template slot="title">
                    <Icon type="ios-keypad"></Icon>
                    我的订单
                </template>
                <MenuItem name="4-1">订单中心</MenuItem>
            </Submenu>
            <Submenu name="4">
                <template slot="title">
                    <Icon type="ios-analytics"></Icon>
                    小数据
                </template>
                <MenuItem name="4-1">图表 1</MenuItem>
                <MenuItem name="4-2">图表 2</MenuItem>
            </Submenu>
          </Menu>
        </Sider>
        <Layout class="mainLayout" >
          <Breadcrumb>
              <BreadcrumbItem>主页</BreadcrumbItem>
              <BreadcrumbItem v-for="(path,idx) in breadcrumPath" :key="idx" >{{path}}</BreadcrumbItem>
          </Breadcrumb>
          <Content>
            <router-view></router-view>
          </Content>
        </Layout>
      </Layout>
      <Footer class="layout-footer-center">2021 &copy; xiaoke1256</Footer>
    </Layout>
  </div>
</template>
<script lang="ts" >
import { Vue, Component } from 'vue-property-decorator'
import {MenuItem} from '@/types'
import { Submenu } from 'iview';

@Component({components:{}})
export default class Home extends Vue {
  public nickName:string|null = '';

  public menus:MenuItem[]=[
    {menuName:'店铺设置',menuCode:'1',icon:'ios-navigate',children:[{menuName:'我的店铺',menuCode:'1-1',path:'/store/index'}]},
    {menuName:'我的商品',menuCode:'2',icon:'ios-keypad',children:[{menuName:'商品列表',menuCode:'2-1',path:'/product/index'}]},
    {menuName:'我的订单',menuCode:'3',icon:'ios-keypad',children:[{menuName:'订单中心',menuCode:'3-1',path:''}]},
    {menuName:'小数据',menuCode:'4',icon:'ios-analytics',children:[{menuName:'图表 1',menuCode:'4-1',path:''},{menuName:'图表 2',menuCode:'4-2',path:''}]}
  ] as MenuItem[];

  public currentMenuCode:string='';

  public mounted(){
    this.nickName = sessionStorage.getItem('loginName');
    this.currentMenuCode = '1-1';
  }

  public logout(){
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('loginName');
    this.$router.push('/');
  }

  public get breadcrumPath(){
    const pathes:string[] = [];
    let menuCode = this.currentMenuCode;
    while(menuCode.length>0){
      const menu = this.findMenu(menuCode,this.menus)
      if(menu==null){
        break;
      }
      pathes.push(menu.menuName);
      const index = menuCode.lastIndexOf('-');
      if(index<0){
        break;
      }
      menuCode = menuCode.substring(0,index);
    }
    pathes.reverse();
    return pathes;
  }

  private findMenu(menuCode:string,menus:MenuItem[]):MenuItem|null{
    for(const menu of menus){
      if(menu.menuCode===menuCode){
        return menu;
      }
    }
     for(const menu of this.menus){
       if(menu.children){
         const subMenu = this.findMenu(menuCode,menu.children);
         if(subMenu!=null){
           return subMenu;
         }
       }
     }
    return null;
  }
}
</script>