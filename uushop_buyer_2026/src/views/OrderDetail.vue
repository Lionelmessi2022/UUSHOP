<template>
  <div class="detail-wrapper">
    <div class="mui-content">
    <div class="mui-card">
      <ul class="mui-table-view" >
        <li class="mui-table-view-cell">订单号<span class="spanright">{{data.orderId}}</span></li>
        <li class="mui-table-view-cell">收货地址<span class="spanright">{{data.buyerAddress}}<br/>{{data.buyerName}} {{data.buyerPhone}}</span></li>
        <li class="mui-table-view-cell">订单价格<span class="spanright order-amount">￥{{data.orderAmount}}</span></li>
        <li class="mui-table-view-cell">下单时间<span class="spanright">{{dateFormat(data.createTime)}}</span></li>
        <li class="mui-table-view-cell">订单状态<span class="spanright"><span :class="'status-dot status-'+data.orderStatus"></span><span class="orderStatus" :class="'status-text-'+data.orderStatus">{{transformOrderStatus(data.orderStatus)}}</span></span></li>
        <li class="mui-table-view-cell">支付状态<span class="spanright"><span :class="'status-dot pay-'+data.payStatus"></span><span class="orderStatus" :class="'pay-text-'+data.payStatus">{{transformPayStatus(data.payStatus)}}</span></span></li>
        <li class="mui-table-view-cell">
          <div style="width: 100%;height: 40px;margin-top: 20px;font-size: 13px" v-for="item in data.orderDetailList" >
            <div style="float:left;width: 40px;height: 40px">
              <img style="height: 40px" :src="item.productIcon"/>
            </div>
            <div style="float:left;height:40px;width: 140px; line-height: 40px; margin-left: 10px">{{item.productName}}</div>
            <div style="float:left;height:40px;width: 40px; line-height: 40px; margin-left: 10px">
              <span class="mui-icon mui-icon-closeempty" style="position: relative;top: 3px;"></span>{{item.productQuantity}}
            </div>
            <div style="float:right;height:40px;width: 60px; line-height: 40px; margin-left: 10px;font-size: 15px;font-weight: 700" class="product-price">￥{{item.productPrice*item.productQuantity}}</div>
          </div>
        </li>
      </ul>
      <button type="button" class="mui-btn mui-btn-block mui-btn-success" v-show="payStatus==0 && orderStatus==0" @click="toPay()">确认付款</button>
      <button type="button" class="mui-btn mui-btn-block mui-btn-warning" v-show="orderStatus==0" @click="cancelOrder()">取消订单</button>
    </div>
  </div>
  </div>
</template>

<script>
import { MessageBox } from 'mint-ui';
import { Toast } from 'mint-ui';
export default {
  name: "orderDetail",
  data(){
    return {
      data:{
        "orderId": "161899085773669363",
        "buyerName": "李四",
        "buyerPhone": "18868877111",
        "buyerAddress": "科技路",
        "buyerOpenid": 1,
        "orderAmount": 18,
        "orderStatus": 0,
        "payStatus": 0,
        "createTime": "2021-06-06T14:56:56",
        "updateTime": "2021-06-06T15:01:27",
        "orderDetailList": [
          {
            "detailId": "78bac05d2716c7535a953c42ec6416df",
            "orderId": "f959ca203b237255ec76926b8c12c6c8",
            "productId": 1,
            "productName": "生鲜大虾",
            "productPrice": 82.6,
            "productQuantity": 10,
            "productIcon": "https://m.360buyimg.com/mobilecms/s750x750_jfs/t1/150370/24/6079/129012/5fa4bebaEb7aea500/0ceaa6b9e0ec073d.jpg!q80.dpg.webp"
          },
          {
            "detailId": "78bac05d2716c7535a953c42ec6416df",
            "orderId": "f959ca203b237255ec76926b8c12c6c8",
            "productId": 1,
            "productName": "生鲜大虾",
            "productPrice": 82.6,
            "productQuantity": 10,
            "productIcon": "https://m.360buyimg.com/mobilecms/s750x750_jfs/t1/150370/24/6079/129012/5fa4bebaEb7aea500/0ceaa6b9e0ec073d.jpg!q80.dpg.webp"
          }
        ]
      },
      selected:'',
      payStatus:0,
      orderStatus:0
    }
  },
  created(){
    this.$store.state.index = 2
    this.fetchOrderDetail()
  },
  beforeRouteEnter(to, from, next){
    next(vm => {
      if(to.query.orderId){
        const _this = vm
        axios.get(vm.$store.state.globalhost+'order-service/buyer/order/detail/'+JSON.parse(window.localStorage.getItem('access-user')).userId+'/'+to.query.orderId).then(function (resp) {
          _this.data = resp.data.data
          _this.payStatus = resp.data.data.payStatus
          _this.orderStatus = resp.data.data.orderStatus
        })
      }
    })
  },
  watch: {
    '$route.query.orderId': function(){
      this.fetchOrderDetail()
    }
  },
  beforeRouteUpdate(to, from, next){
    const _this = this
    axios.get(this.$store.state.globalhost+'order-service/buyer/order/detail/'+JSON.parse(window.localStorage.getItem('access-user')).userId+'/'+to.query.orderId).then(function (resp) {
      _this.data = resp.data.data
      _this.payStatus = resp.data.data.payStatus
      _this.orderStatus = resp.data.data.orderStatus
    })
    next()
  },
  methods:{
    fetchOrderDetail(){
      const _this = this
      axios.get(this.$store.state.globalhost+'order-service/buyer/order/detail/'+JSON.parse(window.localStorage.getItem('access-user')).userId+'/'+this.$route.query.orderId).then(function (resp) {
        _this.data = resp.data.data
        _this.payStatus = resp.data.data.payStatus
        _this.orderStatus = resp.data.data.orderStatus
      })
    },
    transformOrderStatus(status){
      switch (status){
        case 0: return '新订单';break;
        case 1: return '已完成';break;
        case 2: return '已取消';break;
      }
    },
    transformPayStatus(status){
      switch (status){
        case 0: return '未支付';break;
        case 1: return '已支付';break;
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
    },
    cancelOrder(){
      const _this = this
      MessageBox.confirm('确定取消该订单吗?').then(action => {
        axios.put(this.$store.state.globalhost+'order-service/buyer/order/cancel/'+JSON.parse(window.localStorage.getItem('access-user')).userId+'/'+this.$route.query.orderId).then(function (resp) {
          if(resp.data.code == 0){
            let instance = Toast('取消成功');
            setTimeout(() => {
              instance.close();
              window.location.reload()
            }, 500);
          }
        })
      }).catch(() => {});
    },
    toPay(){
      this.$router.push('/pay?orderAmount='+this.data.orderAmount+'&orderId='+this.$route.query.orderId)
    }
  }
}
</script>

<style scoped>
.spanright{
  display: inline-block;
  float: right;
}
.orderStatus{
  font-weight: 600;
  color: #1a1a1a;
}
.order-amount{
  font-size: 20px;
  font-weight: 700;
  color: #ff5a3a;
}
.product-price{
  color: #ff5a3a !important;
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
.pay-0{ background-color: #faad14; }
.pay-1{ background-color: #52c41a; }
.pay-text-0{ color: #faad14; }
.pay-text-1{ color: #52c41a; }
.mui-table-view-cell{
  text-align: left;
}
/* ===== 桌面端样式（无媒体查询） ===== */
.detail-wrapper {
  min-height: 100vh;
  background-color: #f2f4f8;
}
.detail-wrapper .mui-content {
  max-width: 700px;
  margin: 0 auto;
  padding: 24px;
  padding-bottom: 120px;
}
.detail-wrapper .mui-card {
  border-radius: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  overflow: hidden;
}
/* 列表项 hover */
.detail-wrapper .mui-table-view-cell {
  padding: 16px 24px;
  transition: background-color 0.25s ease;
}
.detail-wrapper .mui-table-view-cell:hover {
  background-color: #fafafa;
}
/* 订单金额突出 */
.detail-wrapper .order-amount {
  font-size: 22px;
  font-weight: 700;
  color: #ff5a3a;
}
/* 商品列表对齐 */
.detail-wrapper .mui-table-view-cell div[style] {
  margin-top: 12px;
  padding: 8px 0;
  border-bottom: 1px solid #f5f5f5;
}
.detail-wrapper .product-price {
  font-size: 16px;
  color: #ff5a3a !important;
  font-weight: 700;
}
/* 按钮 - 圆角胶囊 */
.detail-wrapper .mui-btn-block {
  max-width: 300px;
  margin: 12px auto;
  display: block;
  border-radius: 30px;
  transition: all 0.25s ease;
  font-weight: 600;
}
.detail-wrapper .mui-btn-block:hover {
  transform: scale(1.03);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}
.detail-wrapper .mui-btn-success:hover {
  background-color: #45a828;
}
.detail-wrapper .mui-btn-warning:hover {
  background-color: #e0a030;
}
</style>
