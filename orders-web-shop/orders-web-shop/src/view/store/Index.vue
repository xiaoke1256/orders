<template>
  <div >
    <Collapse v-model="value">
      <Panel name="1">
            XX店
            <p slot="content">创建于：2021年10月；店长xxx;您于xxxx年xx月加入本店。</p>
        </Panel>
        <Panel name="2">
            XX店
            <p slot="content">创建于：2021年10月；店长xxx;您于xxxx年xx月加入本店。</p>
        </Panel>
        <Panel name="3">
            XX店
            <p slot="content">创建于：2021年10月；店长xxx;您于xxxx年xx月加入本店。<a>设为默认</a></p>
        </Panel>
    </Collapse>
  </div>
</template> 
<script lang="ts" >
import { Vue, Component } from 'vue-property-decorator'

@Component({})
export default class Index extends Vue {
  public value='1';
}
</script>