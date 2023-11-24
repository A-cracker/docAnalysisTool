import './assets/main.css';
import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import { store, key } from './store'


// import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import vue3TreeOrg from 'vue3-tree-org';
import "vue3-tree-org/lib/vue3-tree-org.css";
import {Key} from "@element-plus/icons-vue";

const init = () => {
  const app = createApp(App)
  for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
  }
  app.use(store, key)
  app.use(vue3TreeOrg).use(router)
  app.mount('#app')
};

if (process.env.NODE_ENV === 'development') {
  // 本地开发需要等待全局变量初始化
  setTimeout(() => {
    init();
  }, 2000);
} else {
  console.log('初始化app');
  init();
}
