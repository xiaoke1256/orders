<template>
  <div>
    <StoreList v-if="!showForm" @exit="switchPage" />
    <StoreForm v-if="showForm" @exit="switchPage" :storeNo="storeNo" />
  </div>
</template> 
<script lang="ts" >
//import { Vue, Component } from 'vue-property-decorator'
import { Vue, Options } from "vue-class-component";
import StoreList from './StoreList.vue';
import StoreForm from './Form.vue';

@Options({ components: { StoreList, StoreForm } })
export default class Index extends Vue {
  public showForm = false;

  public storeNo = '';

  public switchPage(event: any) {
    this.showForm = !this.showForm;
    this.storeNo = event as string;
  }
}
</script>