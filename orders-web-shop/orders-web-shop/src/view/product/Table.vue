<template>
  <div id="content" >
    <Table :columns="productCols" :data="porductList"></Table>
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
      let {resultList} = await getPorductList();
      this.porductList = resultList;
    }

    public get productCols(){
      return [ {
                title: '商品代码',
                key: 'productCode'
            },{
                title: '商品名称',
                key: 'productName'
            },{
              title: '品牌',
              key: 'brand'
            }];
    }
}
</script>