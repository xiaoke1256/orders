import { Layout, Card, Flex, Image  } from 'antd';
import './common.css';
import './Login.css'



const { Header, Content, Footer } = Layout;

export function Login(){
    return (
    <>
      <Layout>
        <Header>
          <div className="hearder-logo" >Orders</div>
        </Header>
        <Content className="loginContent">
          <Flex justify={"flex-end"} className='welcomeMsg' >——欢迎访问Orders大数据</Flex >
          <Card>
            <div>
              <Image preview={false} src="/login.jpg" />
            </div>
          </Card>
        </Content>
        <Footer className='layout-footer-center'>2021 &copy; xiaoke1256</Footer>
      </Layout>
    </>
    )
}