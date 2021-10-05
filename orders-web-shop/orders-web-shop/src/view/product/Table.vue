<template>
  <div >
    <div>
      <Form :model="searchBean">
        <Row :gutter="16" >
          <Col span="8">
            <Form-item>
              <Input v-model="searchBean.productName" search placeholder="商品名称" @on-search="search"/>
            </Form-item>
          </Col>
          <Col span="6">
            <Form-item>
              <Select v-model="searchBean.storeNo" clearable @on-change="search" >
                <Option v-for="store in stores" :key="store.storeNo" :value="store.storeNo">{{store.storeName}}</Option>
              </Select>
            </Form-item>
          </Col>
        </Row>
      </Form>  
    </div>
    <Table :columns="productCols" :data="porductList"></Table>
    <Page :total="totalCount" :page-size="pageSize" @on-change="onPageChange" show-sizer show-total transfer />
  </div>
</template>
<script lang="ts" >
import { Vue, Component } from 'vue-property-decorator'
import {Product, ProductSearchParms} from '@/types/product';
import {Store} from '@/types/store';
import {getPorductList,switchOnShef} from '@/api/product';
import {getStoresByAccountNo} from '@/api/store';
import {Switch} from 'iview';

@Component({components:{}})
export default class ProductTable extends Vue {
    public searchBean:ProductSearchParms = {};

    //可以选择的商店
    public stores:Store[]=[];

    public porductList = [] as Product[];
    public pageNo = 1;
    public pageSize = 10;
    public totalCount = 0;

    public async mounted(){
      const storeMembers = await getStoresByAccountNo();
      for(const storeMember of storeMembers){
        this.stores.push(storeMember.store);
        if(storeMember.isDefaultStore==='1'){
          this.searchBean.storeNo = storeMember.storeNo;
        }
      }
      this.search();
    }

    public async search(){
      const parms = {pageNo:this.pageNo,pageSize:this.pageSize,needFullTypeName:true,...this.searchBean} as ProductSearchParms;
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
        },{
          title: '价格',
          key: 'productPrice',
          render:(h:any,params:any)=>{
            const p = <Product>params.row;
            return h('div',{},p.productPrice/1000);
          }
        },{
          title: '上下架',
          key: 'productStatus',
          render:(h:any,params:any)=>{
            const p = <Product>params.row;
            return h(Switch,
              {
                props:{ 
                  "size":"large",
                  "trueValue":"1",
                  "falseValue":"0",
                  "value":p.productStatus,
                  "beforeChange":()=>{
                    return switchOnShef(p.productCode,p.productStatus=='1'?'0':'1')
                  }
                }
              },
              [
                h('span',{slot:"open"},'上架'),
                h('span',{slot:"close"},'下架')
              ]
            );
          }
        }];
    }
}
</script>
<style>
.ivu-page{
  text-align: left;
}
</style>