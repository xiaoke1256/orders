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
        <Content class="loginContent">
          <div class="welcomeMsg">{{welcomeMsg}}</div>
          <Card>
            <div class="loginMain" >
              <div >
                <img src="~@/assets/login.jpg" >
              </div>
              <div style="width:320px"> 
                <Card>
                  <div class="loginWelcome">
                    请您登陆：
                  </div>
                  <Form >
                    <Form-Item>
                      <Input prefix="ios-contact" placeholder="用户名" v-model="loginName" />
                    </Form-Item>
                    <Form-Item>
                      <Input prefix="md-key" type="password" placeholder="密码" v-model="password" />
                    </Form-Item>
                    <Button type="success" long @click="login">登陆</Button>
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
import {login} from '@/api/login'

@Component({components:{}})
export default class Login extends Vue {
  public welcomeMsg='——欢迎访问商户端';
  public loginName:string="";
  public password:string="";

  public async login(){
    const result = await login(this.loginName,this.password);
    const token = result.token;
    if(typeof token !== 'string' || !token){
      throw new Error("登陆错误");
    }
    console.log("token:"+token);
    sessionStorage.setItem("token",token);
    this.$router.push({
          name: 'Home'
        });
  }
}
</script>

