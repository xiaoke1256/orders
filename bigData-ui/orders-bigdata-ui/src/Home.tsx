import { Layout, Breadcrumb,Card, Flex, Image, Form, Input, Button  } from 'antd';
import { LogoutOutlined, UserOutlined } from '@ant-design/icons';
import './Home.css';
// import { BrowserRouter } from "react-router-dom";

const { Header, Content, Footer,Sider } = Layout;

export function Home(){

  const logout = ()=>{

  }

  const nickName = "测试用户";

  return (
    <Layout className="top">
      <Header>
        <Flex justify="space-between"  >
          <div className="hearder-logo">Orders</div>
          <div className="hearder-nav">
            <div >
              <a onClick={logout} >
                <LogoutOutlined />
              </a>
              <a>
                <UserOutlined />
                {nickName }
              </a>
            </div>
          </div>
        </Flex>
        {/* <Menu mode="hor izontal" theme="dark" active-name="1">
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
            {/* <BrowserRouter></BrowserRouter> */}
          </Content>
        </Layout>
      </Layout>
      <Footer className="layout-footer-center">2021 &copy; xiaoke1256</Footer>
    </Layout>
  )
}