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
                  <Form ref="logForm" :model="loginForm" >
                    <Form-Item prop="loginName" :rules="[{required: true, message: '用户名不能为空'}]" >
                      <Input placeholder="用户名" v-model="loginForm.loginName" >
                        <Icon type="md-person" slot="prepend"></Icon>
                      </Input>
                    </Form-Item>
                    <Form-Item prop="password" :rules="[{required: true, message: '密码不能为空'}]">
                      <Input type="password" placeholder="密码" v-model="loginForm.password" >
                        <Icon type="md-lock" slot="prepend"></Icon>
                      </Input>
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
import { Form } from 'iview'
import { login } from '@/api/login'
import { LoginForm } from '@/types/login';

@Component({components:{}})
export default class Login extends Vue {
  public welcomeMsg='——欢迎访问商户端';

  public loginForm: LoginForm = {} as LoginForm;

  public async login(){
    console.log("this.$refs.logForm.validate:"+(<Form>this.$refs.logForm).validate);
    (this.$refs.logForm as Form).validate(async(valid)=>{
      console.log("valid");
      if(valid!=true){
        return;
      }
      const result = await login(this.loginForm.loginName,this.loginForm.password);
      const token = result.token;
      const user = result.user;
      if(typeof token !== 'string' || !token){
        throw new Error("登陆错误");
      }
      console.log("token:"+token);
      sessionStorage.setItem("token",token);
      sessionStorage.setItem("nickName",user.nickName);
      sessionStorage.setItem("loginName",this.loginForm.loginName);
      this.$router.push({
            name: 'Home'
          });
    
    });
  }
    
}
</script>

