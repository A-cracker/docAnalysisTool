import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect:'/extraction'
  },
  {
    path: '/extraction',
    name: 'extraction',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/DocView/DocView.vue')
  },
  {
    path: '/fileRelation',
    name: 'fileRelation',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/DocCompareView/DocCompareView.vue')
  },
  {
    path: '/fileCode',
    name: 'fileCode',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/FileCodeView/FileCodeView.vue')
  }
];

const router = createRouter({
  history: createWebHashHistory(), // 最好使用WebHashHistory方式；如果使用WebHistory会非常麻烦，不建议使用这种方式
  routes
});

export default router;
