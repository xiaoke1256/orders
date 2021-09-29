<template>
  <div >
    <Collapse v-if="storeMembers && storeMembers.length>0" v-model="defaultNo">
      <Panel v-for="storeMember in storeMembers" :name="storeMember.storeNo" :key="storeMember.storeNo">
            {{storeMember.store.storeName}}
            <p slot="content">创建于：2021年10月；店长xxx;您于xxxx年xx月加入本店。</p>
      </Panel>
    </Collapse>
    <div v-else>
        你还没有属于你自己的网店。
    </div>
    <div >
      <Button @click="add">新开网店</Button>
    </div>
  </div>
</template> 
<script lang="ts" >
import { Vue, Component, Emit } from 'vue-property-decorator'
import {getStoresByAccountNo} from '@/api/store'
import {StoreMember} from '@/types/store'

@Component({})
export default class StoreList extends Vue {
  public defaultNo='';
  public storeMembers:StoreMember[]=[];

  public async mounted(){
      const accountNo = sessionStorage.getItem('loginName')
      this.storeMembers = await getStoresByAccountNo(accountNo as string);
  }

  @Emit("exit")
  public add(){

  }
}
</script>