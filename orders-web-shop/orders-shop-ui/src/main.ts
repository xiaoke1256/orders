import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import viewUIPlus from 'view-ui-plus'
import './assets/style/public.css';
import './assets/style/login.css';
import './assets/style/home.css';
import './assets/style/style.css';
import 'view-ui-plus/dist/styles/viewuiplus.css'

createApp(App)
    .use(store)
    .use(router)
    .use(viewUIPlus)
    .mount("#app");
