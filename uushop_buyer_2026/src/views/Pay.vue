<template>
  <div class="pay-wrapper">
    <div class="mui-content">
    <div class="mui-card">
      <ul class="mui-table-view">
        <li class="mui-table-view-cell">付款</li>
        <li class="mui-table-view-cell">支付方式<span class="mui-icon mui-icon-weixin spanright"></span></li>
      </ul>
      <form class="mui-input-group">
        <div class="mui-input-row">
          <label>金额</label>
          <input type="text" v-model="orderAmount" readonly/>
        </div>
        <button type="button" class="mui-btn mui-btn-block mui-btn-success" @click="pay()">付款</button>
      </form>
    </div>
  </div>
  </div>
</template>

<script>
import { Toast } from 'mint-ui';
export default {
  name: "Wallet",
  data(){
    return{
      orderAmount: ''
    }
  },
  created(){
    this.orderAmount = this.$route.query.orderAmount
  },
  methods:{
    pay(){
      const _this = this
      axios.put(this.$store.state.globalhost+'order-service/buyer/order/pay/'+JSON.parse(window.localStorage.getItem('access-user')).userId+'/'+this.$route.query.orderId).then(function (resp) {
        let instance = Toast('支付成功');
        setTimeout(() => {
          instance.close();
          _this.$router.replace('/orderDetail?orderId='+_this.$route.query.orderId)
          setTimeout(() => {
            window.location.reload()
          }, 100)
        }, 500);
      })
    }
  }
}
</script>

<style scoped>
.spanright{
  display: inline-block;
  float: right;
}
/* ===== 桌面端样式（无媒体查询） ===== */
.pay-wrapper {
  min-height: 100vh;
  background-color: #f2f4f8;
}
.pay-wrapper .mui-content {
  max-width: 500px;
  margin: 0 auto;
  padding: 24px;
  padding-bottom: 120px;
}
.pay-wrapper .mui-card {
  border-radius: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}
.pay-wrapper .mui-btn-block {
  max-width: 300px;
  margin: 0 auto;
  border-radius: 30px;
  transition: all 0.25s ease;
  font-weight: 600;
}
.pay-wrapper .mui-btn-block:hover {
  transform: scale(1.03);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}
</style>
