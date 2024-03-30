<template>
  <div class="layout">
    <Layout class="top">
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
        <div class="welcomeMsg">{{ welcomeMsg }}</div>
        <Card>
          <div class="loginMain">
            <div>
              <img src="~@/assets/login.jpg">
            </div>
            <div style="width:320px">
              <Card>
                <div class="loginWelcome" style="display:inline-block;width:100%">
                  <div style="display:inline-block;width:49%">请您登陆：</div>
                  <div style="display:inline-block;width:49%;text-align:right">
                    <a @click="loginTypeSwich">
                      <Icon :type="isScanLogin ? 'md-desktop' : 'md-expand'" />
                      {{ isScanLogin ? '账号登录' : '扫码登录' }}
                    </a>
                  </div>
                </div>
                <Form v-if="!isScanLogin" ref="logForm" :model="loginForm">
                  <FormItem prop="loginName" :rules="[{ required: true, message: '用户名不能为空' }]">
                    <Input prefix="md-person" placeholder="用户名" v-model="loginForm.loginName">
                    </Input>
                  </FormItem>
                  <FormItem prop="password" :rules="[{ required: true, message: '密码不能为空' }]">
                    <Input prefix="md-lock" type="password" placeholder="密码" v-model="loginForm.password">
                    </Input>
                  </FormItem>
                  <Button type="success" long @click="login">登录</Button>
                </Form>
                <div v-if="isScanLogin">
                  <Form ref="logForm" :model="loginForm">
                    <Input placeholder="用户名" v-model="loginForm.loginName">
                    </Input>
                    <Input placeholder="随机码" readonly v-model="loginForm.randomCode">
                    </Input>
                    <Input placeholder="sessionId" readonly v-model="loginForm.sessionId">
                    </Input>
                  </Form>
                  <div style="word-break:break-all">
                    {{ encodedUrl }}
                  </div>
                </div>
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
import { Vue, Options } from "vue-class-component";
import { Form } from 'iview'
import { login, getSessionId, getloginPublicKey } from '@/api/login'
import { LoginForm } from '@/types/login';
import { JSEncrypt } from 'jsencrypt';

@Options({ components: {} })
export default class Login extends Vue {
  public welcomeMsg = '——欢迎访问商户端';

  public loginForm: LoginForm = {} as LoginForm;

  private webSocket: WebSocket | undefined;

  // 是否扫码登录
  public isScanLogin = false;

  //扫码登录时的公钥
  public publicKey = '';

  public basePath = '';

  public get encodedUrl() {
    if (!this.loginForm.loginName ||
      !this.loginForm.randomCode ||
      !this.publicKey) {
      return '';
    }
    const encrypt = new JSEncrypt();
    encrypt.setPublicKey('-----BEGIN PUBLIC KEY-----' + this.publicKey + '-----END PUBLIC KEY-----');
    let encodeMessage = encrypt.encrypt(this.loginForm.loginName + this.loginForm.randomCode);
    if (typeof (encodeMessage) == 'boolean') {
      return 'encrypt error!';
      //encodeMessage = encodeMessage.replace('==','');
    }

    const encodeStr = this.basePath + '/login/loginWith2dCode/' + this.loginForm.sessionId + '?encodeMessage='
      + encodeURIComponent(encodeMessage) + '&randomCode=' + this.loginForm.randomCode
    return encodeStr;
  }


  public async login() {
    console.log("this.$refs.logForm.validate:" + (<Form>this.$refs.logForm).validate);
    (this.$refs.logForm as Form).validate(async (valid) => {
      console.log("valid");
      if (valid != true) {
        return;
      }
      try {
        const result = await login(this.loginForm.loginName, this.loginForm.password);
        //console.log("result:" + result);
        const token = result.token;
        const refreshToken = result.refreshToken;
        const user = result.user;
        if (typeof token !== 'string' || !token) {
          throw new Error("登录异常");
        }
        console.log("token:" + token);
        sessionStorage.setItem("token", token);
        sessionStorage.setItem("refreshToken", refreshToken);
        sessionStorage.setItem("nickName", user.nickName);
        sessionStorage.setItem("loginName", this.loginForm.loginName);
        this.$router.push({
          path: 'home/index'
        });
      } catch (error) {
        /*empty */
      }

    });
  }

  public loginTypeSwich() {
    this.isScanLogin = !this.isScanLogin;
  }
}
</script>
