<template>
  <div id="app">
    <!-- 桌面端顶部导航栏 -->
    <div class="desktop-topnav" v-show="showHeader">
      <div class="topnav-inner">
        <div class="topnav-left">
          <div class="topnav-back" v-show="showBack" @click="back" title="返回上一页">&#8592;</div>
          <div class="topnav-logo">UU优选</div>
        </div>
        <div class="topnav-user">{{userName}}</div>
      </div>
    </div>
    <mt-header v-show="showHeader" title="UU优选" id="head">
      <mt-button v-show="showBack" icon="back" slot="left" @click="back">返回</mt-button>
    </mt-header>
    <router-view/>
    <nav class="mui-bar mui-bar-tab" id="bar-tab" v-show="showNav">
      <div class="mui-tab-item" @click="navigateTo('/cart', 1)">
        <span class="mui-icon mui-icon-compose" :class="{'mui-active':$store.state.index==1}"></span>
        <span class="mui-tab-label" :class="{'mui-text-active':$store.state.index==1}">购买</span>
      </div>
      <div class="mui-tab-item" @click="navigateTo('/order', 2)">
        <span class="mui-icon mui-icon-bars" :class="{'mui-active':$store.state.index==2}"></span>
        <span class="mui-tab-label" :class="{'mui-text-active':$store.state.index==2}">订单</span>
      </div>
      <div class="mui-tab-item" @click="navigateTo('/mine', 3)">
        <span class="mui-icon mui-icon-person" :class="{'mui-active':$store.state.index==3}"><span class="mui-badge">1</span></span>
        <span class="mui-tab-label" :class="{'mui-text-active':$store.state.index==3}">我的</span>
      </div>
    </nav>
  </div>
</template>

<script>
export default {
  name: 'App',
  data(){
    return {
      isSelected: '1',
      isActive:false,
      value:''
      // index:2
    }
  },
  methods: {
    back() {
      history.go(-1)
    },
    navigateTo(path, index){
      this.$store.state.index = index
      if(this.$route.path !== path){
        this.$router.push(path).catch(() => {})
      }
    }
  },
  computed: {
    currentPath(){
      return this.$route.path
    },
    showHeader(){
      return !['/login','/register'].includes(this.currentPath)
    },
    showBack(){
      return !['/','/cart'].includes(this.currentPath)
    },
    showNav(){
      return !['/login','/register'].includes(this.currentPath)
    },
    userName(){
      try {
        var user = JSON.parse(localStorage.getItem('access-user'))
        return user ? user.mobile || '用户' : ''
      } catch(e) { return '' }
    }
  }
}
</script>

<style>

/* ===== 桌面端全局样式（无媒体查询） ===== */

body {
  background-color: #f2f4f8;
  padding-bottom: 0;
}

#head {
  display: none !important;
}

#app {
  font-family: 'Inter', 'PingFang SC', 'system-ui', -apple-system, 'Helvetica Neue', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  padding-top: 56px;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}

/* 桌面端顶部导航栏 */
.desktop-topnav {
  display: block;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 999;
  background: #ffffff;
  box-shadow: 0 1px 8px rgba(0,0,0,0.06);
  height: 56px;
}
.topnav-inner {
  max-width: 1200px;
  margin: 0 auto;
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
}
.topnav-left {
  display: flex;
  align-items: center;
  gap: 16px;
}
/* 返回按钮 - 圆形，悬停变品牌色 */
.topnav-back {
  width: 34px;
  height: 34px;
  border-radius: 50%;
  background: #f5f6fa;
  color: #333;
  font-size: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.25s ease;
  user-select: none;
}
.topnav-back:hover {
  background: #ff5a3a;
  color: #ffffff;
  transform: translateX(-2px);
}
.topnav-logo {
  font-size: 20px;
  font-weight: 700;
  color: #ff5a3a;
  letter-spacing: 1px;
}
.topnav-user {
  font-size: 14px;
  color: #666;
  font-weight: 500;
  padding: 6px 16px;
  background: #f5f5f5;
  border-radius: 20px;
}

/* 底部导航栏 - 毛玻璃胶囊 */
#bar-tab {
  max-width: 420px;
  margin: 0 auto;
  left: 0;
  right: 0;
  bottom: 12px;
  background: rgba(255,255,255,0.85);
  -webkit-backdrop-filter: blur(20px);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255,255,255,0.6);
  border-radius: 60px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.08);
  overflow: hidden;
}
#bar-tab .mui-tab-item {
  border-radius: 20px;
  transition: background-color 0.25s ease;
}
#bar-tab .mui-tab-item:hover {
  background-color: rgba(0,0,0,0.04);
}
.mui-text-active {
  color: #ff5a3a;
}

/* 全局底部间距 */
#app > .mui-content,
#app > div > .mui-content,
.cart-wrapper .cart-content,
.order-wrapper,
.detail-wrapper,
.info-wrapper,
.pay-wrapper,
.mine-wrapper {
  padding-bottom: 120px;
}

/* ===== Mint UI MessageBox 桌面适配（弹窗挂载在 body，必须全局样式） ===== */
/* 注意：mint-ui/lib/style.css 在 main.js 中于 App.vue 之后注入，同优先级会反覆盖，故全部加 !important */
.mint-msgbox {
  width: 340px !important;
  max-width: 90% !important;
  border-radius: 16px !important;
  box-shadow: 0 12px 40px rgba(0,0,0,0.18) !important;
  overflow: hidden;
  background-color: #ffffff !important;
}
.mint-msgbox-header {
  padding: 22px 0 0 !important;
}
.mint-msgbox-title {
  font-size: 17px !important;
  font-weight: 600 !important;
  color: #1a1a1a !important;
}
.mint-msgbox-content {
  padding: 16px 28px 22px !important;
  border-bottom: none !important;
}
.mint-msgbox-message {
  font-size: 15px !important;
  color: #666 !important;
  line-height: 1.6 !important;
}
.mint-msgbox-btns {
  height: 50px !important;
  border-top: 1px solid #f0f0f0 !important;
  display: flex;
}
.mint-msgbox-btn {
  font-size: 15px !important;
  line-height: 50px !important;
  transition: background-color 0.25s ease;
  flex: 1;
  border: none;
  background-color: #ffffff !important;
  cursor: pointer;
}
.mint-msgbox-btn:hover {
  background-color: #f8f9fa !important;
}
.mint-msgbox-cancel {
  color: #666 !important;
  border-right: 1px solid #f0f0f0 !important;
}
.mint-msgbox-confirm {
  color: #ff5a3a !important;
  font-weight: 600 !important;
}
</style>
