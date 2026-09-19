<template>
  <div class="order-wrapper">
    <div class="app-container">
    <ul class="mui-table-view mui-table-view-chevron">
      <li class="mui-table-view-cell mui-media" v-for="item in data">
        <router-link class="mui-navigate-right" :to="'/orderDetail?orderId='+item.orderId"  >
          <div class="order-row">
            <div class="order-left">
              <div class="order-id">订单号：{{item.orderId}}</div>
              <div class="order-time">{{dateFormat(item.createTime)}}</div>
            </div>
            <div class="order-right">
              <div class="order-amount">￥ <span class="price">{{item.orderAmount}}</span></div>
              <div class="order-status-row">
                <span :class="'status-dot status-'+item.orderStatus"></span>
                <span class="orderStatus" :class="'status-text-'+item.orderStatus">{{transformOrderStatus(item.orderStatus)}}</span>
              </div>
            </div>
          </div>
        </router-link>
      </li>
    </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: "Order",
  data(){
    return {
      data:''
    }
  },
  created(){
    this.$store.state.index = 2;
    this.fetchOrderList()
  },
  beforeRouteEnter(to, from, next){
    next(vm => {
      vm.fetchOrderList()
    })
  },
  beforeRouteUpdate(to, from, next){
    this.fetchOrderList()
    next()
  },
  methods:{
    fetchOrderList(){
      const _this = this
      axios.get(this.$store.state.globalhost+'order-service/buyer/order/list/'+JSON.parse(window.localStorage.getItem('access-user')).userId+'/1/100').then(function (resp) {
        _this.data = resp.data.data
      })
    },
    transformOrderStatus(status){
      switch (status){
        case 0: return '新订单';break;
        case 1: return '已完成';break;
        case 2: return '已取消';break;
      }
    },
    dateFormat(time) {
      var date = new Date(time);
      var year=date.getFullYear();
      var month= date.getMonth()+1<10 ? "0"+(date.getMonth()+1) : date.getMonth()+1;
      var day=date.getDate()<10 ? "0"+date.getDate() : date.getDate();
      var hours=date.getHours()<10 ? "0"+date.getHours() : date.getHours();
      var minutes=date.getMinutes()<10 ? "0"+date.getMinutes() : date.getMinutes();
      var seconds=date.getSeconds()<10 ? "0"+date.getSeconds() : date.getSeconds();
      return year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds;
    }
  }
}
</script>

<style scoped>
.app-container{

}
.mui-media-body{
  font-size: 14px;
}
.mui-ellipsis{
  font-size: 12px;
}
.price{
  color: #ff5a3a;
  font-size: 18px;
  font-weight: 700;
}
a:after,a:link,a:active,a:visited{
  text-decoration:none;
}
.orderTime{
  color: #c8cbcf;
  font-size: 12px;
}
.head-img{
  width: 60px;
  height: 58px;
}
.order-row {
  width: 100%;
}
.order-left, .order-right {
  width: 100%;
}
.order-id {
  font-size: 14px;
  color: #1a1a1a;
}
.order-time {
  font-size: 12px;
  color: #c8cbcf;
  margin-top: 6px;
}
.order-amount {
  text-align: right;
}
.order-status-row {
  text-align: right;
  margin-top: 6px;
}
.orderStatus{
  display: inline-block;
  font-weight: 600;
}
.status-dot{
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 6px;
  vertical-align: middle;
}
.status-0{ background-color: #faad14; }
.status-1{ background-color: #52c41a; }
.status-2{ background-color: #d9d9d9; }
.status-text-0{ color: #faad14; }
.status-text-1{ color: #52c41a; }
.status-text-2{ color: #999; }
/* ===== 桌面端样式（无媒体查询） ===== */
.order-wrapper {
  min-height: 100vh;
  background-color: #f2f4f8;
}
.order-wrapper .app-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 24px;
  padding-bottom: 120px;
}
.order-wrapper .mui-table-view {
  background: transparent;
  border-radius: 0;
  box-shadow: none;
}
/* 每个订单卡片独立 + 圆角 + 阴影 */
.order-wrapper .mui-table-view-cell {
  padding: 18px 24px;
  margin-bottom: 16px;
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}
.order-wrapper .mui-table-view-cell:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.1);
  background-color: #ffffff;
}
/* 桌面端两栏布局 */
.order-row {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}
.order-left {
  flex: 1;
}
.order-right {
  flex-shrink: 0;
  text-align: right;
}
.order-id {
  font-size: 15px;
  font-weight: 600;
  color: #1a1a1a;
}
.order-time {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
}
/* 价格 */
.order-wrapper .price {
  font-size: 20px;
  font-weight: 700;
  color: #ff5a3a;
}
.order-amount {
  margin-bottom: 8px;
}
.order-status-row {
  margin-top: 0;
}
</style>
