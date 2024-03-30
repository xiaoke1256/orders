import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import Home from "@/views/Home.vue"
import HomeIndex from "@/views/home/Index.vue"

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "Index",
    redirect: "/login",
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("@/views/Login.vue"),
  },
  {
    path: "/about",
    name: "about",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  },
  {
    path: '/home',
    name: 'Home',
    component: Home,
    children: [
      {
        path: 'index',
        name: 'HomeIndex',
        component: HomeIndex
      },
    ]
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
