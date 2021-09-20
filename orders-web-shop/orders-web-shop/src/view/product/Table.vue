<template>
  <div >
    <Table :columns="productCols" :data="porductList"></Table>
    <Page :total="totalCount" :page-size="pageSize" @on-change="onPageChange" show-sizer show-total transfer />
  </div>
</template>
<script lang="ts" >
import { Vue, Component } from 'vue-property-decorator'
import {Product, ProductSearchParms} from '@/types/product';
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
      const parms = {pageNo:this.pageNo,pageSize:this.pageSize,needFullTypeName:true} as ProductSearchParms;
      let {totalCount,resultList} = await getPorductList(parms);
      this.porductList = resultList;
      this.totalCount = totalCount;
    }

    public onPageChange(pageNo:number){
      console.log("pageNo："+pageNo);
      this.pageNo = pageNo;
      console.log("this.pageNo："+this.pageNo);
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
        },{
          title: '分类',
          key: 'fullProductTypeName'
        }];
    }
}
</script>
<style>
.ivu-page{
  text-align: left;
}
</style>