<template>
  <div >
    <div v-if="storeMembers && storeMembers.length>0">
      <Collapse v-model="defaultNo">
        <Panel v-for="storeMember in storeMembers" :name="storeMember.storeNo" :key="storeMember.storeNo">
            {{storeMember.store.storeName}}
            <div v-if="isSameDate(storeMember.store.insertTime,storeMember.insertTime) && storeMember.role==='1'" 
              slot="content">
              您于{{storeMember.store.insertTime|dateFmt('yyyy年M月d日')}}创建本店。
              <div class="btn-div"><ButtonGroup><Button type="primary" @click="toModify(storeMember.storeNo)" ghost>修改</Button><Button type="primary" @click="closeStore" ghost>关店</Button></ButtonGroup></div>
            </div>
            <div v-else slot="content">
              创建于{{storeMember.store.insertTime|dateFmt('yyyy年M月d日')}}；您于{{storeMember.insertTime|dateFmt('yyyy年M月d日')}}加入本店。
            </div>
        </Panel>
      </Collapse>
    </div>
    <div v-else>
        你还没有属于你自己的网店。
    </div>
    <div class="btn-div" >
      <Button type="primary" @click="add" ghost>新开网店</Button>
    </div>
  </div>
</template> 
<script lang="ts" >
import { Vue, Component, Emit } from 'vue-property-decorator'
import {getStoresByAccountNo} from '@/api/store'
import {StoreMember} from '@/types/store'
import {dateFmt} from '@/plugin/filters'

@Component({})
export default class StoreList extends Vue {
  public defaultNo='';
  public storeMembers:StoreMember[]=[];

  public async mounted(){
      this.storeMembers = await getStoresByAccountNo();
      for(const storeMember of this.storeMembers){
        if(storeMember.isDefaultStore==='1'){
          this.defaultNo = storeMember.storeNo;
          break;
        }
      }
  }

  @Emit("exit")
  public add(){

  }

  @Emit("exit")
  public toModify(storeNo:string){
    return storeNo;
  }

  public isSameDate(d1:Date|string,d2:Date|string){
    return dateFmt(d1,'yyyy-MM-dd') === dateFmt(d2,'yyyy-MM-dd')
  }

  public closeStore(){
    this.$Notice.warning({title:'提示',desc:'请确保所有商品已下架。'});
  }
}
</script>