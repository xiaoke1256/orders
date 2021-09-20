<template>
  <div >
    <Collapse v-if="storeWithMembers && storeWithMembers.length>0" v-model="defaultNo">
      <Panel v-for="storeWithMember in storeWithMembers" :name="storeWithMember.storeNo" :key="storeWithMember.storeNo">
            {{storeWithMember.store.storeName}}
            <p slot="content">创建于：2021年10月；店长xxx;您于xxxx年xx月加入本店。</p>
      </Panel>
    </Collapse>
    <div v-else>
        你还没有属于你自己的网店。
    </div>
  </div>
</template> 
<script lang="ts" >
import { Vue, Component } from 'vue-property-decorator'
import {getStoresByAccountNo} from '@/api/store'
import {StoreWithMember} from '@/types/store'

@Component({})
export default class StoreList extends Vue {
  public defaultNo='';
  public storeWithMembers:StoreWithMember[]=[];

  public async mounted(){
      const accountNo = sessionStorage.getItem('loginName')
      this.storeWithMembers = await getStoresByAccountNo(accountNo as string);
  }
}
</script>