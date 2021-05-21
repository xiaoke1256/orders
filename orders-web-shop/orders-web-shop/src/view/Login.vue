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
    font:italic bold 40px  "Lucida Calligraphy Italic" ;
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
          <div>——欢迎访问商户端</div>
          <Card>
            <div style="min-height:300px;">
              <div style="display:inline-block;vertical-align:top">
                <img src="~@/assets/login.jpg" >
              </div>
              <div style="display:inline-block;vertical-align:top;height:413px"> 
                <Form>
                  <Form-Item>
                    <Input  />
                  </Form-Item>
                </Form>
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
  public msg='这是登录页面';
  public userName:string="";
}
</script>

