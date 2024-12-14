import { Layout, Card, Flex, Image, Form, Input, Button  } from 'antd';
import './common.css';
import './Login.css'



const { Header, Content, Footer } = Layout;

export function Login(){
    const isScanLogin = false;

    return (
    <>
      <Layout>
        <Header>
          <div className="hearder-logo" >Orders</div>
        </Header>
        <Content className="loginContent">
          <Flex justify={"flex-end"} className='welcomeMsg' >——欢迎访问Orders大数据</Flex >
          <Card>
            <div className="loginMain">
              <div>
                <Image preview={false} src="/login.jpg" />
              </div>
              <div style={{width:'320px'}}>
                <Card>
                  <div className="loginWelcome" style={{display:'inline-block',width:'100%'}} >
                    <div style={{display:"inline-block",width:"49%"}}>请您登陆：</div>
                    <div style={{display:"inline-block",width:"49%",textAlign:"right"}}>
                    </div>
                  </div>
                  <Form >
                    <Form.Item
                      name="loginName"
                      rules={[{ required: true, message: '用户名不能为空' }]}
                    >
                      <Input  placeholder="用户名"/>
                    </Form.Item>
                    <Form.Item
                      name="password"
                      rules={[{ required: true, message: '密码不能为空' }]}
                    >
                      <Input.Password  type="password" placeholder="密码" />
                    </Form.Item>
                    <Button type="primary" block onClick={()=>{}} >登录</Button>
                  </Form>
                </Card>
              </div>
            </div>
          </Card>
        </Content>
        <Footer className='layout-footer-center'>2021 &copy; xiaoke1256</Footer>
      </Layout>
    </>
    )
}