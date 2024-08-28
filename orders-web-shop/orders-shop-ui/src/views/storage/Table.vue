<template>
  <div>
    <div>
      <Form :model="searchBean">
        <Row :gutter="16">
          <Col span="8">
          <Form-item>
            <Input v-model="searchBean.productName" search placeholder="商品名称" @on-search="search" />
          </Form-item>
          </Col>
          <Col span="6">
          <Form-item>
            <Select v-model="searchBean.storeNo" clearable @on-change="search">
              <Option v-for="store in stores" :key="store.storeNo" :value="store.storeNo">{{ store.storeName }}</Option>
            </Select>
          </Form-item>
          </Col>
        </Row>
      </Form>
    </div>
    <Table :columns="productCols" :data="porductList">
      <template #buttons="{ row }">
        <Button type="primary" @click="onClickIncStorage(row)">出入库</Button>
      </template>
    </Table>
    <Page :total="totalCount" :page-size="pageSize" @on-change="onPageChange" show-sizer show-total transfer />
  </div>
  <!-- 弹窗 -->
  <Modal
      v-model="modal"
      @on-ok="onIncStorageOk"
      @on-cancel="onCancel"
      >
      <template #header>
        <dev v-if="product">
          <B>{{product.productName}}</B> - {{ product.productCode }}
        </dev>        
      </template>
      <dev v-if="product">
        <Row><Col span="12">当前库存：</Col><Col span="12"><Input v-model="product.stockNum" disabled="true" /></Col></Row>
        <Row><Col span="12">入库：</Col><Col span="12"><InputNumber :max="10000" :min="-10000" v-model="incNum" /></Col></Row>
      </dev>
  </Modal>
</template>
<script lang="ts" >
import { Vue, Options } from "vue-class-component";
import { Product, ProductSearchParms } from '@/types/product';
import { Store } from '@/types/store';
import { getPorductList, switchOnShef, incStorage } from '@/api/product';
import { getStoresByAccountNo } from '@/api/store';

@Options({ components: {} })
export default class ProductTable extends Vue {
  
  //模态弹窗
  public modal = false
  //当前的商品
  public product:Product|undefined ;
  //需要新增或减少的数据量
  public incNum = 1;

  public searchBean: ProductSearchParms = {};

  //可以选择的商店
  public stores: Store[] = [];

  public porductList = [] as Product[];
  public pageNo = 1;
  public pageSize = 10;
  public totalCount = 0;

  public async mounted() {
    const storeMembers = await getStoresByAccountNo();
    for (const storeMember of storeMembers) {
      this.stores.push(storeMember.store);
      if (storeMember.isDefaultStore === '1') {
        this.searchBean.storeNo = storeMember.storeNo;
      }
    }
    this.search();
  }

  public async search() {
    const parms = { pageNo: this.pageNo, pageSize: this.pageSize, needFullTypeName: true, ...this.searchBean } as ProductSearchParms;
    let { totalCount, resultList } = await getPorductList(parms);
    this.porductList = resultList;
    this.totalCount = totalCount;
  }

  public onPageChange(pageNo: number) {
    console.log("pageNo：" + pageNo);
    this.pageNo = pageNo;
    console.log("this.pageNo：" + this.pageNo);
    this.search();
  }

  public onPageSizeChange(pageSize: number) {
    this.pageSize = pageSize;
    this.search();
  }

  public get productCols() {
    return [{
      title: '商品代码',
      key: 'productCode'
    }, {
      title: '商品名称',
      key: 'productName'
    }, {
      title: '品牌',
      key: 'brand'
    }, {
      title: '分类',
      key: 'fullProductTypeName'
    }, {
      title: '库存',
      key: 'stockNum'
    }, {
      title: '出入库',
      key: 'stockNum',
      slot: 'buttons'
    }];
  }

  public onSwitchStatus(productCode: string, productStatus: string) {
    return switchOnShef(productCode, productStatus == '1' ? '0' : '1')
  }

  public onClickIncStorage(product: Product){
    this.product = product
    this.modal = true;
  }
  /**
   * 出入库的确定按钮
   */
  public async onIncStorageOk(){
    if(!this.product){
      alert('未选择商品。');
      return;
    }
    const result = await incStorage(this.product.productCode,this.incNum);
    if(result){
      await this.search();
    }
    this.incNum = 1;  
    
  }
  //取消按钮
  public onCancel(){
    //do nothing
  }
}
</script>
<style>
.ivu-page {
  text-align: left;
}
</style>