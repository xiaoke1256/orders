import { Layout, Breadcrumb, Menu, Card, Flex, Image, Form, Input, Button  } from 'antd';
import { LogoutOutlined, UserOutlined } from '@ant-design/icons';

import './Home.css';
import { Outlet } from "react-router-dom";

const { Header, Content, Footer,Sider } = Layout;

export function Home(){

  const logout = ()=>{

  }

  const onMenuClick = (menu:{})=>{
    console.log(menu);
  }

  const menus = [
    {
      key:"01",
      label:"菜单1",
      icon:"",
      children:
        [
          {key:"0101",label:"子菜单1"},
          {key:"0102",label:"子菜单233"}
        ]
    },
    {
      key:"02",
      label:"菜单2",
      icon:"",
      children:
        [
          {key:"0201",label:"子菜单1"},
          {key:"0202",label:"子菜单2"}
        ]
    },
  ];

  const nickName = "测试用户";

  return (
    <Layout className="top">
      <Header>
        <Flex justify="space-between"  >
          <div className="hearder-logo">Orders</div>
          <div className="hearder-nav">
            <div >
              <a>
                <UserOutlined />
                {nickName }
              </a>
              <a onClick={logout} >
                <LogoutOutlined />
              </a>
            </div>
          </div>
        </Flex>
        {/* <Menu mode="horizontal" theme="dark" active-name="1">
          <div className="layout-logo">Orders</div>

          <div className="layout-nav">
            <a onClick={logout} >
              <Icon type="ios-log-out"></Icon>
            </a>
            <a>
              <Icon type="md-person" />
              {{ nickName }}
            </a>
          </div>
          {/*--欢迎访问商户端--*/}
        {/*</Menu> */}
      </Header>
      <Layout className="mainWithMenu">
        <Sider hide-trigger className="leftMenu">
        <Menu
          onClick={onMenuClick}
          // style={{ width: 256 }}
          // defaultSelectedKeys={['1']}
          // defaultOpenKeys={['sub1']}
          theme="light"
          mode="inline"
          items={menus}
        />
          {/* <Menu active-name="1-2" theme="light" width="auto" :open-names="openMunes" @on-select="changeCurrent">
            <Submenu v-for="menu in menus" :key="menu.menuCode" :name="menu.menuCode">
              <template #title>
                <Icon :type="menu.icon" />
                 `${menu.menuName}`
              </template>
              <Menu-Item v-for="subMenu in menu.children" :key="subMenu.menuCode" :name="subMenu.menuCode">
                <router-link :to="subMenu.path">{{ subMenu.menuName }}</router-link>
              </Menu-Item>
            </Submenu>
          </Menu> */}
        </Sider>
        <Layout className="mainLayout">
          <Breadcrumb routes={[{ title:'主页',href:"#" }]} ></Breadcrumb>
          <Content>
            <Outlet/>
          </Content>
        </Layout>
      </Layout>
      <Footer className="layout-footer-center">2021 &copy; xiaoke1256</Footer>
    </Layout>
  )
}