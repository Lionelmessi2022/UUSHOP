<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
  import HelloWorld from './components/HelloWorld.vue'

  export default {
    name: 'app',
    components: {
      HelloWorld
    },

    data() {
      return {
      }
    },

    methods:{
      initWebSocket(){
        this.websock = new WebSocket('ws://localhost:8085/webSocket');
        this.websock.onmessage = this.webSocketOnMessage;
        this.websock.onopen = this.webSocketOnOpen;
        this.websock.onerror = this.websocketonerror;
        this.websock.onclose = this.webSocketClose;
      },
      webSocketOnOpen(event){
        console.log('建立连接')
      },
      webSocketOnMessage(event){
        const _this = this
        this.$alert('有新的订单', '消息', {
          confirmButtonText: '确定',
          callback: action => {
            if (this.$route.path !== '/orderManage') {
              _this.$router.push('/orderManage')
            }
          }
        });
      },
      webSocketClose(event){
        console.log('连接关闭');
      }
    },
    created() {
      this.initWebSocket();
    },
    destroyed() {
    }
  }
</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 60px;
  }
</style>
