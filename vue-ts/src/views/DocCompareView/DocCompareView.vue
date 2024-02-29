<template>
  <div class="root">
    <el-menu :default-active="defaultActive" class="el-menu-vertical-demo">
      <el-menu-item index="1" v-if="mode" @click="page=0"><el-icon><document /></el-icon><span>文档列表</span></el-menu-item>
      <el-menu-item index="2" v-if="mode" @click="page=1"><el-icon><setting /></el-icon><span>制品列表</span></el-menu-item>
      <el-menu-item index="3" v-if="mode" @click="page=2"><el-icon><Menu /></el-icon><span>分析矩阵</span></el-menu-item>
      <el-menu-item index="4" v-if="!mode" @click="page=3"><el-icon><List /></el-icon><span>代码列表</span></el-menu-item>
      <el-menu-item index="5" v-if="!mode" @click="page=4"><el-icon><Menu /></el-icon><span>文码矩阵</span></el-menu-item>
      <div class="switch">
        <el-tooltip :content="tip" placement="bottom">
          <el-link @click="changeMode()">{{content}}<el-icon><Switch /></el-icon></el-link>
        </el-tooltip>
      </div>
    </el-menu>
    <doc-list v-if="page==0"/>
    <artifact-list v-if="page == 1"/>
    <doc-matrix v-if="page == 2"></doc-matrix>
    <code-list v-if="page == 3"></code-list>
    <code-matrix v-if="page == 4"></code-matrix>
  </div>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
import ArtifactList from "@/components/ArtifactList/ArtifactList"
import DocList from "@/components/DocList/DocList.vue"
import DocMatrix from "@/components/DocMatrix/DocMatrix"
import CodeMatrix from "@/components/CodeMatrix/CodeMatrix"
import CodeList from "@/components/CodeList/CodeList"

const mode = ref(true)
const tip = ref("切换为文码一致性")
const content = ref("文档一致性")
const defaultActive = ref("1")
const changeMode = () => {
  mode.value = !mode.value
  if(mode.value){
    tip.value = "切换为文码一致性"
    content.value = "文档一致性"
    defaultActive.value ="1"
    page.value = 0
  }else{
    tip.value = "切换为文档一致性"
    content.value = "文码一致性"
    defaultActive.value ="4"
    page.value = 3
  }
}
const page = ref(0)
</script>
<style lang="scss" scoped src="./DocCompare.scss">

</style>