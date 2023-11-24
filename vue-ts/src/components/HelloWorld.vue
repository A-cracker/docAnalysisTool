<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <h1>响应结果：{{ res }}</h1>
    <el-button @click="handleClick">点击</el-button>
    <el-button @click="handleClick2">点击</el-button>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import { HttpClient } from '@/network/HttpClient';

export default defineComponent({
  name: 'HelloWorld',
  props: {
    msg: String,
  },
  data() {
    return {
      res: ""
    };
  },
  methods: {
    async handleClick() {
      const res = await HttpClient.post("Test", {});
      this.res = res;
    },
    async handleClick2() {
      const res = await HttpClient.post("getKeywordTree", {});
      this.res = res;
    }
  },
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
h3 {
  margin: 40px 0 0;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}
</style>
