<template>
  <div>
    <div v-if="storeMembers && storeMembers.length > 0">
      <Collapse v-model="defaultNo">
        <Panel v-for="storeMember in storeMembers" :name="storeMember.storeNo" :key="storeMember.storeNo">
          {{ storeMember.store.storeName }}
          <template v-if="isSameDate(storeMember.store.insertTime, storeMember.insertTime) && storeMember.role === '1'"
            #content>
            您于{{ dateFmt(storeMember.store.insertTime) }}创建本店。
            <div class="btn-div">
              <ButtonGroup><Button type="primary" @click="toModify(storeMember.storeNo)" ghost>修改</Button><Button
                  type="primary" @click="closeStore" ghost>关店</Button></ButtonGroup>
            </div>
          </template>
          <template v-else #content>
            创建于{{ dateFmt(storeMember.store.insertTime) }}；您于{{ dateFmt(storeMember.insertTime) }}加入本店。
          </template>
        </Panel>
      </Collapse>
    </div>
    <div v-else>
      你还没有属于你自己的网店。
    </div>
    <div class="btn-div">
      <Button type="primary" @click="add" ghost>新开网店</Button>
    </div>
  </div>
</template> 
<script lang="ts" >
//import { Vue, Component, Emit } from 'vue-property-decorator'
import { Vue, Options } from "vue-class-component";
import { getStoresByAccountNo } from '@/api/store'
import { StoreMember } from '@/types/store'
import { dateFmt } from '@/plugin/filters'

@Options({})
export default class StoreList extends Vue {
  public defaultNo = '';
  public storeMembers: StoreMember[] = [];

  public async mounted() {
    this.storeMembers = await getStoresByAccountNo();
    for (const storeMember of this.storeMembers) {
      if (storeMember.isDefaultStore === '1') {
        this.defaultNo = storeMember.storeNo;
        break;
      }
    }
  }

  public add() {
    //this.$emit(eventName,data)
    this.$emit("exit")
  }

  public toModify(storeNo: string) {
    //return storeNo;
    this.$emit("exit", storeNo)
  }

  public isSameDate(d1: Date | string | undefined, d2: Date | string | undefined) {
    return dateFmt(d1, 'yyyy-MM-dd') === dateFmt(d2, 'yyyy-MM-dd')
  }

  public closeStore() {
    this.$Notice.warning({ title: '提示', desc: '请确保所有商品已下架。' });
  }

  public dateFmt(d: Date | string | undefined) {
    dateFmt(d, 'yyyy年M月d日')
  }
}
</script>