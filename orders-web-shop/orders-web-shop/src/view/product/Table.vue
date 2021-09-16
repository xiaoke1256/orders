<template>
  <div id="content" >
    <Table :columns="productCols" :data="porductList"></Table>
    <Page :total="totalCount" :page-size="pageSize" @on-change="onPageChange" show-sizer show-total transfer />
  </div>
</template>
<script lang="ts" >
import { Vue, Component } from 'vue-property-decorator'
import {Product} from '@/types/product';
import {getPorductList} from '@/api/product';

@Component({components:{}})
export default class ProductTable extends Vue {
    public porductList = [] as Product[];
    public pageNo = 1;
    public pageSize = 10;
    public totalCount = 0;

    public async mounted(){
      this.search();
    }

    public async search(){
      let {totalCount,resultList} = await getPorductList();
      this.porductList = resultList;
      this.totalCount = totalCount;
    }

    public onPageChange(pageNo:number){
      this.pageNo = pageNo;
      this.search();
    }

    public onPageSizeChange(pageSize:number){
      this.pageSize = pageSize;
      this.search();
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
<style>
.ivu-page{
  text-align: left;
}
</style>