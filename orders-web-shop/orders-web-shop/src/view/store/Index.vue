<template>
  <div >
    <StoreList/>
  </div>
</template> 
<script lang="ts" >
import { Vue, Component } from 'vue-property-decorator'
import StoreList from './StoreList.vue';

@Component({components:{StoreList}})
export default class Index extends Vue {
  public value='1';
}
</script>