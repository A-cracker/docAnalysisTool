<template>
  <div id="root">
    <el-menu
        default-active="/tree"
        class="el-menu-demo"
        mode="horizontal"
        router="router"
    >
      <div style="display: flex;align-items: center;margin: 10px 20px;font-size: 20px;">文档分析工具</div>
      <el-menu-item index="/extraction">模板分析</el-menu-item>
      <el-menu-item index="/fileRelation">一致性分析</el-menu-item>
      <el-menu-item index="/fileCode">代码分析</el-menu-item>
    </el-menu>
    <div class="content">
      <RouterView/>
    </div>
  </div>
</template>
<script setup lang="ts">
import { getRtParam } from './base/constant';
import { onBeforeMount, provide, ref } from 'vue';
const debounce = (callback: (...args: any[]) => void, delay: number) => {
  let tid: any
  return function (...args: any[]) {
    const ctx = self
    tid && clearTimeout(tid)
    tid = setTimeout(() => {
      callback.apply(ctx, args)
    }, delay)
  }
}

const _ = (window as any).ResizeObserver;
(window as any).ResizeObserver = class ResizeObserver extends _ {
  constructor(callback: (...args: any[]) => void) {
    callback = debounce(callback, 20)
    super(callback)
  }
}

const runToolParam = ref<string>('');
onBeforeMount(() => {
  runToolParam.value = getRtParam() || '';
  if (/^\{.*\}$/.test(runToolParam.value)) {
    const runToolParamJson = JSON.parse(runToolParam.value);
    provide('runToolParam', {
      bandId: runToolParamJson.bandId,
      storageId:runToolParamJson.storageId,
      objID:runToolParamJson.objID
    });
  }
});
</script>
<style scoped>
#root{
  display:flex;
  flex-direction: column;
  height: 100vh;
}
.content{
  flex-grow: 1;
  max-height:calc(100vh - 58.67px);
}
</style>
<style>
#app {
  font-family: Helvetica,Avenir,Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  height: 100%;
}
</style>