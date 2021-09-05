<template>
  <div id="content" >
    商品列表
  </div>
</template>
<script lang="ts" >
import { Vue, Component } from 'vue-property-decorator'
import {Product} from '@/types/product';
import {getPorductList} from '@/api/product';

@Component({components:{}})
export default class ProductTable extends Vue {
    public porductList = [] as Product[];
    public async mounted(){
        this.porductList = await getPorductList();
    }
}
</script>