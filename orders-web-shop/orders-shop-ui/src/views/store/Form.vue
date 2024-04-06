<template>
  <div>
    <div>
      <Form :model="store" :label-width="80">

        <Form-item label="店号">
          {{ store.storeNo }}
        </Form-item>

        <Form-item label="店名">
          <Input v-model="store.storeName" />
        </Form-item>

        <Form-item label="简介">
          <Input type="textarea" :rows="4" v-model="store.storeIntro" />
        </Form-item>

        <Form-item label="支付类型">
          <Select v-model="store.payType">
            <Option value="001">支付宝</Option>
            <Option value="002">微信支付</Option>
            <Option value="003">3rdpay</Option>
          </Select>
        </Form-item>

        <Form-item label="支付账号">
          <Input v-model="store.payAccountNo" />
        </Form-item>

      </Form>
    </div>
    <div class="btn-div">
      <Button type="primary" @click="saveStore">保存</Button>
      <Button @click="cancel">返回</Button>
    </div>
  </div>
</template> 
<script lang="ts" >
//import { Vue, Component, Prop, Emit } from 'vue-property-decorator'
import { Vue, Options } from "vue-class-component";
import { Store } from '@/types/store'
import { saveStore, getStore, updateStore } from '@/api/store'

class Props {
  storeNo?: string;
}

@Options({ emits: { cancel: { emit: "exit" } } })
export default class StoreForm extends Vue.with(Props) {

  public store: Store = { payType: '003' } as Store;

  public async mounted() {
    if (this.storeNo) {
      this.store = await getStore(this.storeNo);
      this.store.insertTime = undefined;
      this.store.updateTime = undefined;
    }
  }

  public async saveStore() {
    let result = false;
    if (this.storeNo) {
      //修改
      result = await updateStore(this.store);
    } else {
      //新增
      result = await saveStore(this.store);
    }

    if (result) {
      this.cancel();
    }
  }

  public cancel() {
    this.$emit("exit");
  }

}
</script>