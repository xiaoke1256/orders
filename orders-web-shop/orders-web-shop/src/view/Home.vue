<template>
  <div class="layout">
    <Layout class="top" >
      <Header>
        <Menu mode="horizontal" theme="dark" active-name="1">
          <div class="layout-logo">Orders</div>
          <!--
          <div class="layout-nav">
            <MenuItem name="1">
              <Icon type="ios-navigate"></Icon>
              公司首页
            </MenuItem>
            <MenuItem name="2">
              <Icon type="ios-keypad"></Icon>
              网站地图
            </MenuItem>
            <MenuItem name="3">
              <Icon type="ios-analytics"></Icon>
              意见与建议
            </MenuItem>
          </div>
          -->
          <!--欢迎访问商户端-->
        </Menu>
      </Header>
      <Layout>
        <Sider hide-trigger class="leftMenu">
          <Menu active-name="1-2" theme="light" width="auto" :open-names="['1']">
            <Submenu name="1">
                <template slot="title">
                    <Icon type="ios-navigate"></Icon>
                    Item 1
                </template>
                <MenuItem name="1-1">Option 1</MenuItem>
                <MenuItem name="1-2">Option 2</MenuItem>
                <MenuItem name="1-3">Option 3</MenuItem>
            </Submenu>
            <Submenu name="2">
                <template slot="title">
                    <Icon type="ios-keypad"></Icon>
                    Item 2
                </template>
                <MenuItem name="2-1">Option 1</MenuItem>
                <MenuItem name="2-2">Option 2</MenuItem>
            </Submenu>
            <Submenu name="3">
                <template slot="title">
                    <Icon type="ios-analytics"></Icon>
                    Item 3
                </template>
                <MenuItem name="3-1">Option 1</MenuItem>
                <MenuItem name="3-2">Option 2</MenuItem>
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
              Content
          </Content>
          <Footer class="layout-footer-center">2021 &copy; xiaoke1256</Footer>
        </Layout>
      </Layout>
    </Layout>
  </div>
</template>
<script lang="ts" >
import { Vue, Component } from 'vue-property-decorator'

@Component({components:{}})
export default class Home extends Vue {
    
}
</script>