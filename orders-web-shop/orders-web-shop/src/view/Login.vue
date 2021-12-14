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
                  <div class="loginWelcome" style="display:inline-block;width:100%">
                    <div style="display:inline-block;width:49%">请您登陆：</div>
                    <div style="display:inline-block;width:49%;text-align:right">
                      <a @click="loginTypeSwich">
                        <Icon :type="isScanLogin?'md-desktop':'md-expand'" />
                        {{isScanLogin?'账号登录':'扫码登录'}}
                      </a>
                    </div>
                  </div>
                  <Form v-if="!isScanLogin" ref="logForm" :model="loginForm" >
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
                    <Button type="success" long @click="login">登录</Button>
                  </Form>
                  <div v-if="isScanLogin">
                    <Form ref="logForm" :model="loginForm" >
                      <Input placeholder="用户名" v-model="loginForm.loginName" >
                      </Input>
                      <Input placeholder="随机码" readonly v-model="loginForm.randomCode" >
                      </Input>
                      <Input placeholder="sessionId" readonly v-model="loginForm.sessionId" >
                      </Input>
                    </Form>
                    <div style="word-break:break-all">
                      {{encodedUrl}}
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
import { Vue, Component } from 'vue-property-decorator'
import { Form } from 'iview'
import { login, getSessionId, getloginPublicKey } from '@/api/login'
import { uuid } from '@/api/common'
import { LoginForm } from '@/types/login';
import { JSEncrypt } from 'jsencrypt';
import axiosInst from '@/axios';
import { setTimeout } from 'timers/promises';
import { encode } from 'punycode';

@Component({components:{}})
export default class Login extends Vue {
  public welcomeMsg='——欢迎访问商户端';

  public loginForm: LoginForm = {} as LoginForm;

  private webSocket:WebSocket|undefined;

  // 是否扫码登录
  public isScanLogin:boolean = false;

  //扫码登录时的公钥
  public publicKey:string = '';

  public basePath = '';

  public mounted(){
     //注册 webSocket
    let url = '';
    if(process.env.NODE_ENV==='development'){
      this.basePath = 'http://localhost:8763/store_intra';
      url = 'ws://localhost:8763/store_intra/login';
    }else if(process.env.NODE_ENV==='development'){
      this.basePath = 'http://peer1:8763/store_intra';
      url = 'ws://peer1:8763/store_intra/login';
    }
    console.log("uri:",url);

    this.loginForm.sessionId=uuid();

    this.webSocket = new WebSocket(url+'?sessionId='+this.loginForm.sessionId);
    this.webSocket.onmessage = (ev)=>{
      //处理登录成功后要做的事
      console.log("login success");
      //ev.data 中包含用户信息及两个token
      let {token,refreshToken,user} = ev.data;
      if(typeof token !== 'string' || !token){
        throw new Error("登录异常");
      }
      console.log("token:"+token);
      sessionStorage.setItem("token",token);
      sessionStorage.setItem("refreshToken",refreshToken);
      sessionStorage.setItem("nickName",user.nickName);
      sessionStorage.setItem("loginName",this.loginForm.loginName);
      this.$router.push({
            path: 'home/index'
          });
    };
    this.webSocket.onerror = ()=>{
      window.setTimeout(()=>{this.mounted()},3000);
    }

    this.webSocket.onclose = ()=>{
      console.log("websocket 关闭。");
    }
    //10分钟后关闭连接
    window.setTimeout(()=>{this.webSocket?.close()},10*60*1000);
    //页面离开时
    window.addEventListener('unload',this.onUnload);

    this.webSocket.onopen = async()=>{
      //成功建立WebSocket连接 后
      this.loginForm.randomCode = Math.floor(Math.random()*Math.pow(16,6)).toString(16);
      this.publicKey = await getloginPublicKey(this.loginForm.sessionId);
    }
  }

  public destroy(){
    window.removeEventListener('unload',this.onUnload);
  }

  public get encodedUrl(){
    if(!this.loginForm.loginName ||
       !this.loginForm.randomCode || 
       !this.publicKey){
      return '';
    }
    const encrypt = new JSEncrypt();
    encrypt.setPublicKey('-----BEGIN PUBLIC KEY-----' + this.publicKey + '-----END PUBLIC KEY-----');
    let encodeMessage = encrypt.encrypt(this.loginForm.loginName+this.loginForm.randomCode);
    if(typeof(encodeMessage) == 'boolean'){
      return 'encrypt error!';
      //encodeMessage = encodeMessage.replace('==','');
    }
    
    const encodeStr = this.basePath+'/login/loginWith2dCode/'+this.loginForm.sessionId+'?encodeMessage='
        +encodeURIComponent(encodeMessage)+'&randomCode='+this.loginForm.randomCode
    return encodeStr;
  }

  public async login(){
    console.log("this.$refs.logForm.validate:"+(<Form>this.$refs.logForm).validate);
    (this.$refs.logForm as Form).validate(async(valid)=>{
      console.log("valid");
      if(valid!=true){
        return;
      }
      const result = await login(this.loginForm.loginName,this.loginForm.password);
      const token = result.token;
      const refreshToken = result.refreshToken;
      const user = result.user;
      if(typeof token !== 'string' || !token){
        throw new Error("登录异常");
      }
      console.log("token:"+token);
      sessionStorage.setItem("token",token);
      sessionStorage.setItem("refreshToken",refreshToken);
      sessionStorage.setItem("nickName",user.nickName);
      sessionStorage.setItem("loginName",this.loginForm.loginName);
      this.$router.push({
            path: 'home/index'
          });
    
    });
  }

  public onUnload(){
    this.webSocket?.close()
  }

  public loginTypeSwich(){
    this.isScanLogin = !this.isScanLogin;
  }
    
}
</script>

