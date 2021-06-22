<style scoped>
	@font-face {
		font-family:'Lucida Calligraphy Italic';
		src: url('~/static/font/Lucida_Calligraphy_Italic.TTF');
	}
.layout{
    border: 1px solid #d7dde4;
    background: #f5f7f9;
    position: relative;
    border-radius: 4px;
}
.layout-logo{
    width: 150px;
    height: 30px;
    background: #5b6270;
    border-radius: 3px;
    float: left;
    position: relative;
    top: 15px;
    left: 20px;
    color:lightsteelblue;
    font:italic bold 50px  "Lucida Calligraphy Italic" ;
    letter-spacing:3px
}
.layout-nav{
    width: 420px;
    margin: 0 auto;
    margin-right: 20px;
}
.layout-footer-center{
    text-align: center;
}
.loginWelcome{
  text-align:left;
  font:italic bolder 16px;
  color:lightsteelblue;
  border-bottom:cornflowerblue solid 2px;
  margin-bottom: 15px;
}
.welcomeMsg{
  padding:0 50px;
  text-align: right;
  font:italic bolder 16px "yahei";
  color:lightsteelblue
}
</style>
<template>
  <div class="layout">
    <Layout :style="{minHeight: '99.6vh'}" >
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
        <Content :style="{padding: '20px 50px'}">
          <div class="welcomeMsg">{{welcomeMsg}}</div>
          <Card>
            <div style="min-height:300px;">
              <div style="display:inline-block;vertical-align:middle">
                <img src="~@/assets/login.jpg" >
              </div>
              <div style="display:inline-block;vertical-align:middle;width:320px"> 
                <Card style="height:413px;vertical-align:middle;padding:60px 0">
                  <div class="loginWelcome">
                    请您登陆：
                  </div>
                  <Form >
                    <Form-Item>
                      <Input prefix="ios-contact" placeholder="用户名" />
                    </Form-Item>
                    <Form-Item>
                      <Input prefix="md-key" placeholder="密码" />
                    </Form-Item>
                    <Button type="success" long>登陆</Button>
                  </Form>
                </Card>
              </div>
            </div>
          </Card>
        </Content>
        <Footer class="layout-footer-center">2021 &copy; xiaoke1256</Footer>
    </Layout>
  </div>
</template>
<script lang="ts">
import { Vue, Component } from 'vue-property-decorator'

@Component({components:{}})
export default class Login extends Vue {
  public welcomeMsg='——欢迎访问商户端';
  public userName:string="";
}
</script>

