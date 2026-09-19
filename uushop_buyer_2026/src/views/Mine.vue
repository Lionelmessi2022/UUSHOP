<template>
  <div class="mine-wrapper">
    <div class="mui-page-content">
    <div>
      <ul class="mui-table-view" >
        <li class="mui-table-view-cell">
          <span class="mui-icon mui-icon-contact"></span><p class='mui-ellipsis'>{{user.mobile}}</p>
        </li>
      </ul>
      <br/>

      <ul class="mui-table-view" >
        <li class="mui-table-view-cell">
          <router-link to="order" class="mui-navigate-right" >我的订单</router-link>
        </li>
        <li class="mui-table-view-cell">
          <router-link to="about" class="mui-navigate-right" >关于点餐平台</router-link>
        </li>
      </ul>
      <br/><br/><br/>
      <button style="background-color: #ffb248;border: 1px solid #ffb248;" class="mui-btn mui-btn-danger mui-btn-block quit" @click="logout">退出登录</button>
    </div>
  </div>
  </div>

</template>

<script>
import { MessageBox } from 'mint-ui';
export default {
  name: "Mine",
  data(){
    return {
      user:{
        mobile:''
      }
    }
  },
  methods:{
    logout(){
      MessageBox.confirm('确定退出当前账号吗?').then(action => {
        localStorage.removeItem('access-user')
        this.$router.replace({path: '/login'})
      }).catch(() => {})
    }
  },
  created() {
    this.$store.state.index = 3
    // 读取当前登录用户信息，显示真实手机号
    try {
      const stored = JSON.parse(window.localStorage.getItem('access-user'))
      if(stored && stored.mobile) this.user = stored
    } catch(e) {}
  }
}
</script>

<style scoped>
.mui-page-content{
  background-color: #efeff4;
}
.quit{
  margin: 0 auto;
  width: 95%;

}

a:after,a:link,a:active,a:visited{
  text-decoration:none;
}
/* ===== 桌面端样式（无媒体查询） ===== */
.mine-wrapper {
  min-height: 100vh;
  background-color: #f2f4f8;
}
.mine-wrapper .mui-page-content {
  max-width: 600px;
  margin: 0 auto;
  padding: 24px;
  padding-bottom: 120px;
}
.mine-wrapper .mui-table-view {
  background: #ffffff;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}
/* 列表项 hover */
.mine-wrapper .mui-table-view-cell {
  padding: 16px 20px;
  transition: background-color 0.25s ease;
}
.mine-wrapper .mui-table-view-cell:hover {
  background-color: #f8f9fa;
}
.mine-wrapper .quit {
  max-width: 300px;
  margin: 20px auto;
  border-radius: 30px;
  transition: all 0.25s ease;
  font-weight: 600;
}
.mine-wrapper .quit:hover {
  transform: scale(1.03);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}
</style>
