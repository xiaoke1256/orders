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
              <BreadcrumbItem>Home</BreadcrumbItem>
              <BreadcrumbItem>Components</BreadcrumbItem>
              <BreadcrumbItem>Layout</BreadcrumbItem>
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

@Component({components:{}})
export default class Home extends Vue {
  public nickName:string|null = '';

  public mounted(){
    this.nickName = sessionStorage.getItem('loginName');
  }

  public logout(){
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('loginName');
    this.$router.push('/');
  }
}
</script>