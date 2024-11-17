import { Layout, Card, Flex  } from 'antd'
const { Header, Content, Footer } = Layout;

export function Login(){
    return (
    <>
      <Layout className="top h-full">
        <Header>
        </Header>
        <Content className="loginContent">
          <Flex justify={"flex-end"}>——欢迎访问商户端</Flex >
          <Card>

          </Card>
        </Content>
        <Footer style={{ textAlign: 'center' }}>2021 &copy; xiaoke1256</Footer>
      </Layout>
    </>
    )
}