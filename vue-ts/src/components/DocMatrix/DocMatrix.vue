<template>
  <div class="doc-matrix">
    <div class="tool-bar">
      <el-button type="primary" plain size="small">新增对比文件</el-button>
      <el-button type="default" plain size="small">新增跟踪链</el-button>
      <el-input placeholder="搜索对比内容" size="small" :suffix-icon="Search"></el-input>
      <el-popover placement="bottom" :width="230" trigger="hover">
        <template #reference>
          <el-icon color="grey"><QuestionFilled /></el-icon>
        </template>
        <div class="tip">
          <el-tag type="primary">功能分析点</el-tag>
          <el-tag type="success" style="margin-left: 5px">非功能分析点</el-tag>
        </div>
      </el-popover>
    </div>
    <div class="matrix">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="init" label="批复的立项方案"/>
      </el-table>
    </div>
  </div>
</template>
<script lang="ts" setup>

import {onMounted} from 'vue'
import {QuestionFilled, Search} from "@element-plus/icons-vue";

onMounted(()=>{

})
const tableData = [
  {
    id:1,//追溯链id
    init: {
        analysisPointId:1,
        analysisPontName:"hh",
        type:"text",
        content:"登录"
    },
    newDoc:[
      {
        analysisPointId:2,
        type:"表格",
        analysisPontName:"hh",
        content:"表格json数据"
      },
      {
        analysisPointId:3,
        analysisPontName:"hh",
        type:"img",
        content:"图片源地址"
      },
    ]
  },
]
</script>
<style src="./DocMatrix.scss" scoped lang="scss"/>
