import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import AddProduct from "../views/AddProduct"
import ProductManage from "../views/ProductManage"
import EditProduct from "../views/EditProduct"
import OrderManage from "../views/OrderManage"
import Bar from "../views/Bar";
import BasicLine from "../views/BasicLine";
import StackedLine from "../views/StackedLine";
import Login from "../views/Login";
import Error from "../views/Error";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: '商品模块',
    component: Home,
    redirect: '/productManage',
    icon: 'el-icon-goods',
    show:true,
    children:[
      {
        path:'/productManage',
        name:'商品管理',
        icon: 'el-icon-s-unfold',
        show:true,
        component: ProductManage
      },
      {
        path:'/addProduct',
        name:'添加商品',
        icon:'el-icon-circle-plus',
        show:true,
        component: AddProduct
      },
      {
        path:'/editProduct',
        name:'修改商品',
        show:false,
        component: EditProduct
      }
    ]
  },
  {
    path: '/',
    name: '订单模块',
    component: Home,
    icon: 'el-icon-s-grid',
    show:true,
    children:[
      {
        path:'/orderManage',
        name:'订单管理',
        icon: 'el-icon-document-copy',
        show:true,
        component: OrderManage
      }
    ]
  },
  {
    path: '/',
    name: '数据统计',
    component: Home,
    icon: 'el-icon-finished',
    show:true,
    children:[
      {
        path:'/bar',
        name:'总销量',
        show:true,
        icon: 'el-icon-s-data',
        component: Bar
      },
      {
        path:'/basicLine',
        name:'日销量',
        show:true,
        icon: 'el-icon-s-marketing',
        component: BasicLine
      },
      {
        path:'/stackedLine',
        name:'销量明细',
        show:true,
        icon: 'el-icon-s-help',
        component: StackedLine
      }
    ]
  },
  {
    path:'/login',
    name:'登录',
    show:false,
    component: Login
  },
  {
    path:'/error',
    name:'登录',
    show:false,
    component: Error
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  if (to.path.startsWith('/login')) {
    window.localStorage.removeItem('access-admin')
    next()
  } else {
    let admin = JSON.parse(window.localStorage.getItem('access-admin'))
    if (!admin) {
      next({path: '/login'})
    } else {
      //判断token有效期
      axios({
        url:'http://localhost:8686/account-service/admin/checkToken',
        method:'get',
        headers:{
          token:admin.token
        }
      }).then((response) => {
        if(response.data.code == -1){
          console.log('校验失败')
          next({path: '/error'})
        }
      })
      next()
    }
  }
})

export default router
