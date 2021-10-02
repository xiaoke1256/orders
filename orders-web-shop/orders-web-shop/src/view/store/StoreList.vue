<template>
  <div >
    <div v-if="storeMembers && storeMembers.length>0">
      <Collapse v-model="defaultNo">
        <Panel v-for="storeMember in storeMembers" :name="storeMember.storeNo" :key="storeMember.storeNo">
            {{storeMember.store.storeName}}
            <p slot="content">您于{{storeMember.store.insertTime}}创建本店。</p>
        </Panel>
      </Collapse>
    </div>
    <div v-else>
        你还没有属于你自己的网店。
    </div>
    <div class="btn-div" >
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