<template>
  <div class="docList" style="display: flex;height:100%">
      <div class="btn-group">
        <el-button type="primary" @click="uploadFileVisible = true" size="small" plain>上传项目工件</el-button>
        <el-button @click="testParser" size="small">测试doc</el-button>
      </div>
      <div class="content">
        <el-table :data="tableData">
          <el-table-column prop="docName" label="工件名"/>
          <el-table-column prop="docDate" label="工件日期"/>
          <el-table-column prop="docType" label="工件类型"/>
          <el-table-column prop="docOwner" label="创建者"/>
          <el-table-column prop="op" label="操作"/>
        </el-table>
        <el-pagination background layout="prev, pager, next" :total="1000" />
      </div>
  </div>
  <el-dialog v-model="uploadFileVisible" title="上传工件" width="30%" align-center>
    <el-form label-position="top" label-width="100px" style="max-width: 460px">
      <el-form-item label="请选择工件类型">
        <el-select v-model="docType" placeholder="解析模板">
          <el-option label="立项方案文档" :value="1"/>
          <el-option label="需求规格说明书" :value="2"/>
          <el-option label="概要设计说明书" :value="3"/>
          <el-option label="详细设计说明书" :value="4"/>
          <el-option label="三方测试文档" :value="5"/>
          <el-option label="验收审核模板" :value="6"/>
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="uploadFileVisible = false">取消</el-button>
        <el-button type="primary" @click="uploadFile()">确认</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script lang="ts" setup>
import {HttpClient} from "@/network/HttpClient";
import {ref} from "vue"

const uploadFileVisible = ref(false)
const docType = ref(1)
const uploadFile = () =>{

}
const testParser = async () =>{
  HttpClient.post("testParser").then(res => {
    console.log(res.result)
  })
}
</script>
<style src="./DocList.scss" scoped lang="scss"/>
