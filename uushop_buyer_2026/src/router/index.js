import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Cart from "../views/Cart";
import Info from "../views/Info";
import OrderDetail from "../views/OrderDetail";
import Pay from "../views/Pay";
import Order from "../views/Order";
import Mine from "../views/Mine";
import Login from "../views/Login";
import Register from "../views/Register";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Cart
  },
  {
    path: '/cart',
    name: '购买',
    component: Cart
  },
  {
    path: '/info',
    name: '用户信息',
    component: Info
  },
  {
    path: '/orderDetail',
    name: '订单详情',
    component: OrderDetail
  },
  {
    path: '/pay',
    name: '支付页面',
    component: Pay
  },
  {
    path: '/order',
    name: '订单列表',
    component: Order
  },
  {
    path: '/mine',
    name: '我的模块',
    component: Mine
  },
  {
    path: '/login',
    name: '登录',
    component: Login
  },
  {
    path: '/register',
    name: '注册',
    component: Register
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  if (to.path.startsWith('/login') || to.path.startsWith('/register')) {
    if (to.path.startsWith('/login')) {
      window.localStorage.removeItem('access-user')
    }
    next()
  } else {
    let user = JSON.parse(window.localStorage.getItem('access-user'))
    if (!user) {
      next({path: '/login'})
    } else {
      axios({
        url:'http://localhost:8686/account-service/user/checkToken',
        method:'get',
        headers:{
          token:user.token
        }
      }).then((response) => {
        if(response.data.code == -1){
          let instance = Toast('登录超时！请重新登录！');
          setTimeout(() => {
            instance.close();
          }, 2000)
          window.localStorage.removeItem('access-user')
          next({path: '/login'})
        } else {
          next()
        }
      }).catch(() => {
        window.localStorage.removeItem('access-user')
        next({path: '/login'})
      })
    }
  }
})

export default router
