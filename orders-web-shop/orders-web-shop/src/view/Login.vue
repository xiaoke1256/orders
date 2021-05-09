<template>
  <div>
      {{msg}}
      <Row>
        <Col span="12">
          <Input v-model="userName"/>
        </Col>
      </Row>
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
