<template>
  <div>
      {{msg}}
  </div>
</template>
<script lang="ts">
import { Vue, Component } from 'vue-property-decorator'

@Component
export default class Login extends Vue {
  public msg='这是登录页面';

}
</script>
