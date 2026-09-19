<template>
  <div class="cart-wrapper">
    <div class="mui-content cart-content">
    <div><img :src="logo" alt="" class="logo"/></div>

    <div class="mui-row">
      <!-- 左侧菜单 -->
      <div class="mui-col-xs-3" style="border-right: 1px solid #c8c7cc">
        <div id="segmentedControls" class="mui-segmented-control mui-segmented-control-inverted mui-segmented-control-vertical">
          <a class="mui-control-item" :class="{'menu-active':menuIndex==index1}" @click="menuIndex = index1" v-for="(item,index1) in data">{{item.name}}</a>
        </div>
      </div>
      <!-- 商品列表 -->
      <div id="segmentedControlContents" class="mui-col-xs-9">
        <div v-for="(menu,index) in data" v-show="index == menuIndex">
          <div class="itembox mui-row" v-for="item in menu.goods">
            <div class="mui-col-xs-3">
              <img :src="item.icon"/>
            </div>
            <div class="mui-col-xs-9">
              <div class="item">
                <div class="itemmain">
                  {{item.name}}
                </div>
                <div class="itemsub">
                  {{item.description}}
                </div>
              </div>
              <div class="operation">
                <div class="operationPrice">￥{{item.price}}</div>
                <div class="operationSelect">
                  <span class="mui-icon mui-icon-minus operationMinus" @click="changeQuantity(item,'minus')"></span>
                  <span>{{item.quantity}}</span>
                  <span class="mui-icon mui-icon-plus operationPlus" @click="changeQuantity(item,'plus')"></span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 底部菜单 -->
    <div style="position: relative;top: 0px;" class="mui-bar mui-bar-tab cart-box" id="cart-box">
      <div class="cart-container">
        <div class="cell1">
          <div class="cart-icon-box">
            <span class="cart-icon-glyph">&#128722;</span>
            <span class="mui-badge" id="goodsNum">{{totalQuantity}}</span>
          </div>
        </div>
        <div class="cell12">
          ￥<span id="totalMoney">{{totalPrice.toFixed(2)}}</span>
        </div>
        <div class="cell2" @click="clear">
          <span class="mui-icon mui-icon-trash"></span>
          清空购物车
        </div>
        <div class="cell3" @click="submit">
          <span class="mui-icon mui-icon-checkmarkempty"></span>
          确认下单
        </div>
      </div>
    </div>
  </div>
  </div>
</template>

<script>
import { Toast } from 'mint-ui';
export default {
  name: "Cart",
  data() {
    return {
      totalQuantity:0,
      totalPrice:0,
      menuIndex: 0,
      logo: "../static/logo.jpg",
      data: ''
    }
  },
  created(){
    this.$store.state.index = 1;
    this.$store.state.selectedArray = [];
    const _this = this
    axios.get(this.$store.state.globalhost+'product-service/buyer/product/list').then(function (resp) {
      _this.data = resp.data.data
    })
  },
  methods: {
    //提交
    submit(){
      const _this = this
      if(this.$store.state.selectedArray.length == 0) {
        let instance = Toast('请选择商品');
        setTimeout(() => {
          instance.close();
        }, 1000)
        return
      }
      this.$router.push('/info')
    },
    //清空购物车
    clear(){
      this.totalQuantity=0;
      this.totalPrice=0;
      //菜品的数量设置为0
      for(var i = 0; i < this.data.length;i++){
        var item = this.data[i];
        for(var j=0;j<item.goods.length;j++){
          item.goods[j].quantity = 0;
        }
      }
      //清空已选数组
      this.$store.state.selectedArray=[]
    },
    calcTotal(){
      this.totalQuantity = 0;
      this.totalPrice = 0;
      for(var i = 0; i < this.data.length; i++){
        for(var j = 0; j < this.data[i].goods.length; j++){
          var item = this.data[i].goods[j];
          if(item.quantity > 0){
            this.totalQuantity += item.quantity;
            this.totalPrice += item.price * item.quantity;
          }
        }
      }
      this.totalPrice = Math.round(this.totalPrice * 100) / 100;
    },
    changeQuantity(item,type){
      switch (type) {
        case "minus":
          if(item.quantity == 0) return;
          item.quantity--;
          for(var i=0;i<this.$store.state.selectedArray.length;i++){
            if(item.id == this.$store.state.selectedArray[i].productId){
              this.$store.state.selectedArray[i].productQuantity = item.quantity
              if(item.quantity==0) this.$store.state.selectedArray.splice(i,1)
              break;
            }
          }
          this.calcTotal();
          break;
        case "plus":
          item.quantity++;
          if(item.quantity > item.stock){
            item.quantity = item.stock;
            let instance = Toast(item.name+'已被你抢空！');
            setTimeout(()=>{
              instance.close()
            },1000)
          }
          for(var i=0;i<this.$store.state.selectedArray.length;i++){
            if(item.id == this.$store.state.selectedArray[i].productId){
              this.$store.state.selectedArray[i].productQuantity = item.quantity
              this.calcTotal();
              return;
            }
          }
          this.$store.state.selectedArray.push({
            productId:item.id,
            productQuantity:item.quantity
          });
          this.calcTotal();
          break;
      }
    }
  }
}
</script>

<style scoped>
/* ===== 桌面端样式（无媒体查询，直接应用） ===== */

/* 购物车外层容器 */
.cart-wrapper {
  min-height: 100vh;
  background-color: #f2f4f8;
}
/* 购物车内容区（底部预留：导航栏 12~67px + 购物车栏 84~140px） */
.cart-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
  padding-bottom: 160px !important;
}
/* Logo 隐藏 */
.cart-content > div:first-child {
  display: none;
}
/* 布局重构：菜单顶部横排 + 商品下方网格 */
.cart-content > .mui-row {
  display: flex;
  flex-direction: column;
}
/* 分类菜单：顶部横排 */
.cart-content > .mui-row > .mui-col-xs-3 {
  width: 100% !important;
  flex: none !important;
  max-width: 100% !important;
  height: auto !important;
  border-right: none !important;
  border-bottom: 1px solid #eee;
  background: #ffffff;
  border-radius: 16px 16px 0 0;
  padding: 12px 16px;
  overflow: visible !important;
}
#segmentedControls {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
}
#segmentedControls .mui-control-item {
  width: auto !important;
  line-height: 36px;
  padding: 0 20px;
  border-radius: 30px;
  background: #f5f6fa;
  border: 1px solid transparent;
  font-size: 14px;
  transition: all 0.25s ease;
  margin: 0;
}
#segmentedControls .mui-control-item:hover {
  background-color: #fff0ec;
  border-color: #ff5a3a;
  color: #ff5a3a;
}
/* 当前选中分类（Vue 绑定 menu-active 类）：品牌色填充 + 轻阴影，明确当前位置 */
#segmentedControls .mui-control-item.menu-active,
#segmentedControls .mui-control-item.mui-active {
  background-color: #ff5a3a !important;
  color: #ffffff !important;
  border-color: #ff5a3a !important;
  font-weight: 600;
  box-shadow: 0 4px 12px rgba(255,90,58,0.35);
}
/* 商品列表区域 */
.cart-content > .mui-row > .mui-col-xs-9 {
  width: 100% !important;
  flex: none !important;
  max-width: 100% !important;
  background: #f8f9fb;
  border-radius: 0 0 16px 16px;
  padding: 24px !important;
}
/* 商品网格：一行4列，固定，不响应 */
#segmentedControlContents {
  display: grid !important;
  grid-template-columns: repeat(4, 1fr) !important;
  gap: 20px !important;
  padding: 0 !important;
}
/* 菜单包装层透明化：让商品卡片直接成为网格项 */
/* 注意：不能加 !important，否则会覆盖 v-show 的 display:none 导致所有分类同时显示 */
#segmentedControlContents > div {
  display: contents;
}
/* 每个商品卡片（强制 block，覆盖 MUI .mui-row 的横向布局） */
#segmentedControlContents > div > div {
  display: block !important;
  background: #ffffff;
  border-radius: 16px;
  padding: 0;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}
#segmentedControlContents > div > div:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 30px rgba(0,0,0,0.12);
}
/* 卡片内部：图片在上，信息在下 */
#segmentedControlContents .itembox {
  background: transparent;
  margin: 0;
  padding: 0;
  border-radius: 0;
  border: none;
  display: block !important;
  float: none;
}
#segmentedControlContents .itembox .mui-col-xs-3 {
  width: 100%;
  flex: none;
  max-width: 100%;
  height: auto !important;
  overflow: visible;
  margin-bottom: 0;
  float: none;
}
#segmentedControlContents .itembox .mui-col-xs-3 img {
  width: 100%;
  height: auto;
  aspect-ratio: 1/1;
  object-fit: cover;
  border-radius: 0;
  display: block;
}
#segmentedControlContents .itembox .mui-col-xs-9 {
  width: 100%;
  flex: none;
  max-width: 100%;
  padding: 12px 14px 14px;
  float: none;
}
#segmentedControlContents .item {
  width: 100%;
  height: auto;
  text-align: left;
}
#segmentedControlContents .itemmain {
  font-size: 15px;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
#segmentedControlContents .itemsub {
  font-size: 12px;
  color: #999999;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
#segmentedControlContents .operation {
  width: 100%;
  margin-top: 10px;
  height: auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
#segmentedControlContents .operationPrice {
  font-size: 20px;
  color: #ff5a3a;
  font-weight: 700;
  float: none;
  width: auto;
  line-height: 1;
}
#segmentedControlContents .operationSelect {
  float: none;
  position: static;
  width: auto;
  display: flex;
  align-items: center;
  gap: 8px;
}
/* 加减按钮 - 圆形带边框 */
.operationMinus, .operationPlus {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  border: 1px solid #ddd;
  border-radius: 50%;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.25s ease;
  float: none;
  margin: 0;
  line-height: 28px;
  text-align: center;
}
.operationMinus {
  color: #ff5a3a;
}
.operationMinus:hover {
  background-color: #ff5a3a;
  color: white;
  border-color: #ff5a3a;
}
.operationPlus {
  color: #52c41a;
}
.operationPlus:hover {
  background-color: #52c41a;
  color: white;
  border-color: #52c41a;
}

/* ===== 底部购物车栏 - 毛玻璃胶囊 ===== */
.cart-box {
  max-width: 800px;
  margin: 0 auto;
  left: 0;
  right: 0;
  position: fixed !important;
  /* 关键：覆盖内联 top:0px，否则栏会钉在屏幕顶部被导航栏遮住 */
  top: auto !important;
  /* 上移避让全局底部导航 #bar-tab（占底部 12~67px） */
  bottom: 84px !important;
  background: rgba(255,255,255,0.92);
  -webkit-backdrop-filter: blur(20px);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255,255,255,0.5);
  box-shadow: 0 4px 20px rgba(0,0,0,0.08);
  border-radius: 60px;
  overflow: hidden;
  z-index: 900;
}
.cart-container {
  display: flex;
  align-items: center;
  padding: 0;
  margin: 0;
  height: 56px;
  border-radius: 60px;
  overflow: hidden;
}
/* 购物车图标区 - 固定宽度，保证不被压缩 */
#cart-box .cell1 {
  display: flex !important;
  align-items: center;
  justify-content: center;
  background: transparent !important;
  flex: 0 0 72px !important;
  min-width: 72px !important;
}
/* 图标圆底 - position:relative 供角标定位 */
.cart-icon-box {
  position: relative !important;
  border: 3px solid #ff5a3a;
  background-color: #fff5f3;
  width: 42px;
  height: 42px;
  display: flex !important;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}
/* 购物车图形（Unicode 字符，不依赖 MUI 字体） */
.cart-icon-glyph {
  font-size: 20px;
  line-height: 1;
}
/* 角标 - 强制显示，红色圆形定位右上角 */
.cart-icon-box .mui-badge {
  display: inline-block !important;
  position: absolute !important;
  top: -5px !important;
  right: -5px !important;
  background-color: #ff4d4f !important;
  color: #fff !important;
  font-size: 11px !important;
  font-weight: 700 !important;
  min-width: 18px !important;
  height: 18px !important;
  line-height: 16px !important;
  border-radius: 50% !important;
  text-align: center !important;
  border: 2px solid #fff !important;
  padding: 0 2px !important;
  margin: 0 !important;
}
/* 总价数字 */
.cell12 {
  font-size: 24px;
  font-weight: 700;
  color: #ff5a3a;
  background: transparent;
  flex: 2;
  line-height: 56px;
  text-align: center;
  letter-spacing: 0.5px;
}
/* 清空/下单按钮 - 圆角胶囊 */
.cell2, .cell3 {
  transition: all 0.25s ease;
  cursor: pointer;
  border-radius: 30px;
  margin: 8px 4px;
  padding: 0 20px;
  font-size: 14px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
}
.cell2 {
  background-color: #e8eaed;
  color: #666;
}
.cell2:hover {
  background-color: #d8dadd;
  transform: scale(1.03);
}
.cell3 {
  background-color: #ff5a3a;
  color: #ffffff;
}
.cell3:hover {
  background-color: #e04a2a;
  transform: scale(1.03);
}
</style>
