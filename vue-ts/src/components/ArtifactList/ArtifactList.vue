<template>
  <div class="artifact-list" style="display: flex;height:100%">
    <div class="btn-group">
      <el-button type="primary" @click="uploadFileVisible = true" size="small" plain>上传项目制品</el-button>
    </div>
    <div class="content">
      <el-table :data="tableData">
        <el-table-column prop="artifactName" label="制品名"/>
        <el-table-column prop="artifactType" label="制品类型"/>
        <el-table-column prop="artifactDoc" label="所属文件"/>
        <el-table-column prop="artifactDate" label="提取日期"/>
      </el-table>
      <el-pagination background layout="prev, pager, next" :total="1000" />
    </div>
  </div>
  <el-dialog v-model="uploadArtifactVisible" title="上传制品" width="30%" align-center>
    <el-form label-position="top" label-width="100px" style="max-width: 460px">
      <el-form-item label="请选择制品类型">
        <el-select v-model="docType" placeholder="解析模板">
          <el-option label="用例" :value="1"/>
          <el-option label="ER图" :value="2"/>
          <el-option label="其他" :value="3"/>
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="uploadArtifactVisible = false">取消</el-button>
        <el-button type="primary" @click="uploadArtifact()">确认</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script lang="ts" setup>
import {HttpClient} from "@/network/HttpClient";
import {ref} from "vue"

const uploadArtifactVisible = ref(false)
const docType = ref(1)
const uploadArtifact = () =>{
  
}
</script>
<style src="./ArtifactList.scss" scoped lang="scss"/>
