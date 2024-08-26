<template>
  <div class="layout">
    <Layout class="top">
      <Header>
        <Menu mode="horizontal" theme="dark" active-name="1">
          <div class="layout-logo">Orders</div>

          <div class="layout-nav">
            <a @click="logout">
              <Icon type="ios-log-out"></Icon>
            </a>
            <a>
              <Icon type="md-person" />
              {{ nickName }}
            </a>
          </div>
          <!--欢迎访问商户端-->
        </Menu>
      </Header>
      <Layout class="mainWithMenu">
        <Sider hide-trigger class="leftMenu">
          <Menu active-name="1-2" theme="light" width="auto" :open-names="openMunes" @on-select="changeCurrent">
            <Submenu v-for="menu in menus" :key="menu.menuCode" :name="menu.menuCode">
              <template #title>
                <Icon :type="menu.icon" />
                {{ menu.menuName }}
              </template>
              <Menu-Item v-for="subMenu in menu.children" :key="subMenu.menuCode" :name="subMenu.menuCode">
                <router-link :to="subMenu.path">{{ subMenu.menuName }}</router-link>
              </Menu-Item>
            </Submenu>
          </Menu>
        </Sider>
        <Layout class="mainLayout">
          <Breadcrumb>
            <BreadcrumbItem>主页</BreadcrumbItem>
            <BreadcrumbItem v-for="(path, idx) in breadcrumPath" :key="idx">{{ path }}</BreadcrumbItem>
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
import { Vue, Options } from "vue-class-component";
import { MenuItem } from '@/types'

@Options({ components: {} })
export default class Home extends Vue {
  public nickName: string | null = '';

  public menus: MenuItem[] = [
    { menuName: '店铺设置', menuCode: '1', icon: 'ios-navigate', children: [{ menuName: '我的店铺', menuCode: '1-1', path: '/store/index' }] },
    { menuName: '我的商品', menuCode: '2', icon: 'ios-keypad', children: [{ menuName: '商品列表', menuCode: '2-1', path: '/product/index' },{ menuName: '库存管理', menuCode: '2-2', path: '/product/storage' }] },
    /*{menuName:'我的订单',menuCode:'3',icon:'ios-keypad',children:[{menuName:'订单中心',menuCode:'3-1',path:''}]},*/
    /*{menuName:'小数据',menuCode:'4',icon:'ios-analytics',children:[{menuName:'图表 1',menuCode:'4-1',path:''},{menuName:'图表 2',menuCode:'4-2',path:''}]}*/
  ] as MenuItem[];

  public currentMenuCode = '';

  public mounted() {
    this.nickName = sessionStorage.getItem('loginName');
    this.currentMenuCode = '1-1';
    this.setPpenMunes();
  }

  public logout() {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('loginName');
    this.$router.push('/');
  }

  /**
   * 面包屑路径
   */
  public get breadcrumPath() {
    const pathes: string[] = [];
    let menuCode = this.currentMenuCode;
    while (menuCode.length > 0) {
      const menu = this.findMenu(menuCode, this.menus);
      if (menu == null) {
        break;
      }
      pathes.push(menu.menuName);
      const index = menuCode.lastIndexOf('-');
      if (index < 0) {
        break;
      }
      menuCode = menuCode.substring(0, index);
    }
    pathes.reverse();
    return pathes;
  }

  /**
   * 需要展开的一级菜单
   */
  public openMunes: string[] = [];
  public setPpenMunes() {
    if (!this.currentMenuCode) {
      this.openMunes = [];
      return;
    }
    const index = this.currentMenuCode.lastIndexOf('-');
    if (index < 0) {
      this.openMunes = [];
      return;
    }
    this.openMunes = [this.currentMenuCode.substring(0, index)];
  }

  private findMenu(menuCode: string, menus: MenuItem[]): MenuItem | null {
    for (const menu of menus) {
      if (menu.menuCode === menuCode) {
        return menu;
      }
    }
    for (const menu of menus) {
      if (menu.children && menu.children.length > 0) {
        const subMenu = this.findMenu(menuCode, menu.children);
        if (subMenu != null) {
          return subMenu;
        }
      }
    }
    return null;
  }

  public changeCurrent(currentCode: string) {
    this.currentMenuCode = currentCode;
  }
}
</script>