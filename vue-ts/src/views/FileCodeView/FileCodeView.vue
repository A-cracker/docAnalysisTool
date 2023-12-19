<template>
  <div class="code-view">
    <div style="padding: 8px 0;display: flex;" class="nav">
      <div class="nav-left">
        <el-input v-model="searchText" placeholder="请输入代码源件信息" :suffix-icon="Search"/>
        <el-tooltip content="新增源件"  placement="bottom" effect="light">
          <el-button type="primary" :icon="Plus" @click="addCodeVisible = true"/>
        </el-tooltip>
        <el-button type="danger" :icon="Delete" @click="deleteCodeVisible = true"/>
      </div>
      <div class = "nav-right">
        <el-tooltip content="上传要分析的代码包"  placement="bottom" effect="light">
          <el-link type="primary">上传源件包</el-link>
        </el-tooltip>
        <el-tooltip content="导出代码分析文件"  placement="bottom" effect="light">
          <el-link type="primary">导出文件</el-link>
        </el-tooltip>
      </div>
    </div>
    <div class="table-view">
      <el-table
          @selection-change="handleSelectionChange"
          :data="filterTableData"
          border
          stripe
          style="width: 100%;"
          v-loading="loading"
          empty-text="暂无相关源件">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column type="expand">
          <template #default="props">
            <div m="4">
              <p m="t-0 b-2"><el-tag>开发者:</el-tag> {{ props.row.codeAuthor }}</p>
              <p m="t-0 b-2"><el-tag type="info">开发时间:</el-tag> {{ props.row.codeDate }}</p>
              <p m="t-0 b-2"><el-tag type="success">源件属性:</el-tag><el-button style="margin-left: 10px" size="small" plain type="primary">新增属性</el-button></p>
              <el-table :data="props.row.codeProps" border empty-text="该源件暂无属性">
                <el-table-column label="属性原型" prop="propName" />
                <el-table-column label="属性注释" prop="propDescription" />
                <el-table-column label="操作">
                  <template #default="scope">
                    <el-button size="small" :icon="Edit"></el-button>
                    <el-button size="small" type="danger" :icon="Delete"></el-button>
                  </template>
                </el-table-column>
              </el-table>
              <p m="t-0 b-2"><el-tag type="success">源件函数:</el-tag><el-button style="margin-left: 10px" size="small" plain type="primary">新增函数</el-button></p>
              <el-table :data="props.row.codeFunction" border empty-text="该源件暂无函数">
                <el-table-column label="函数原型" prop="functionProto" />
                <el-table-column label="函数注释" prop="functionDescription" />
                <el-table-column label="操作">
                  <template #default="scope">
                    <el-button size="small" :icon="Edit"></el-button>
                    <el-button size="small" type="danger" :icon="Delete"></el-button>
                  </template>
                </el-table-column>
              </el-table>
              <p m="t-0 b-2"><el-tag type="success">被调用源件：</el-tag><el-button style="margin-left: 10px" size="small" plain type="primary">新增源件</el-button></p>
              <el-table :data="props.row.codeCallee" border empty-text="该源件暂无调用其他源件">
                <el-table-column label="源件名" prop="callee" />
                <el-table-column label="源件注释" prop="calleeDescription" />
                <el-table-column label="调用类型" prop="calleeType" />
                <el-table-column label="操作">
                  <template #default="scope">
                    <el-button size="small" :icon="Edit"></el-button>
                    <el-button size="small" type="danger" :icon="Delete"></el-button>
                  </template>
                </el-table-column>
              </el-table>
              <p m="t-0 b-2"><el-tag type="success">调用源件：</el-tag><el-button style="margin-left: 10px" size="small" plain type="primary">新增源件</el-button></p>
              <el-table :data="props.row.codeCaller" border empty-text="暂无被任何源件调用">
                <el-table-column label="源件名" prop="caller" />
                <el-table-column label="源件注释" prop="callerDescription" />
                <el-table-column label="调用类型" prop="callerType" />
                <el-table-column label="操作">
                  <template #default="scope">
                    <el-button size="small" :icon="Edit"></el-button>
                    <el-button size="small" type="danger" :icon="Delete"></el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="代码源件名称" prop="codeName" />
        <el-table-column label="源件类型">
          <template #default="scope">
            <el-tag v-if="scope.row.codeType == 0" type="warning">Class</el-tag>
            <el-tag v-if="scope.row.codeType == 1" type="success">Interface</el-tag>
            <el-tag v-if="scope.row.codeType == 2" type="info">Enum</el-tag>
            <el-tag v-if="scope.row.codeType == 3" type="danger">Annotation</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="源件注释" prop="codeComment" />
        <el-table-column label="操作">
          <template #default="scope">
            <div class="btns">
              <el-tooltip content="编辑源件信息"  placement="bottom" effect="light">
                <el-button size="small" :icon="Edit" @click="editCodeEvent(scope.row,scope.$index)"></el-button>
              </el-tooltip>
              <el-tooltip content="导出源件JSON文件"  placement="bottom" effect="light">
                <el-button size="small" :icon="Download" @click="downloadCodeJson(scope.row)"></el-button>
              </el-tooltip>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
  <el-dialog v-model="editCodeVisible" title="编辑源件" align-center>
    <el-form label-position="top" label-width="100px">
      <el-form-item label="源件名称">
        <el-input v-model="codeEdit.codeName" placeholder="请输入代码源件名称"/>
      </el-form-item>
      <el-form-item label="源件开发者">
        <el-input v-model="codeEdit.codeAuthor" placeholder="请输入开发者名称"/>
      </el-form-item>
      <el-form-item label="源件类型">
        <el-select v-model="codeEdit.codeType" placeholder="请选择源件类型">
          <el-option label="Class" :value='0'/>
          <el-option label="Interface" :value='1'/>
          <el-option label="Enum" :value='2'/>
          <el-option label="Annotation" :value='3'/>
        </el-select>
      </el-form-item>
      <el-form-item label="源件开发时间">
        <el-input v-model="codeEdit.codeDate" placeholder="请输入代码源件开发时间"/>
      </el-form-item>
      <el-form-item label="源件描述">
        <el-input v-model="codeEdit.codeComment" type="textarea" placeholder="请输入代码源件描述"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="editCodeVisible = false">取消</el-button>
        <el-button type="primary" @click="editCode(codeEdit.id)">确认修改</el-button>
      </span>
    </template>
  </el-dialog>
  <el-dialog v-model="addCodeVisible" title="新增源件" align-center>
    <el-form label-position="top" label-width="100px">
      <el-form-item label="源件名称">
        <el-input v-model="codeAdd.codeName" placeholder="请输入代码源件名称"/>
      </el-form-item>
      <el-form-item label="源件开发者">
        <el-input v-model="codeAdd.codeAuthor" placeholder="请输入开发者名称"/>
      </el-form-item>
      <el-form-item label="源件类型">
        <el-select v-model="codeAdd.codeType" placeholder="请选择源件类型">
          <el-option label="Class" :value='0'/>
          <el-option label="Interface" :value='1'/>
          <el-option label="Enum" :value='2'/>
          <el-option label="Annotation" :value='3'/>
        </el-select>
      </el-form-item>
      <el-form-item label="源件开发时间">
        <el-input v-model="codeAdd.codeDate" placeholder="请输入代码源件开发时间"/>
      </el-form-item>
      <el-form-item label="源件描述">
        <el-input v-model="codeAdd.codeComment" type="textarea" placeholder="请输入代码源件描述"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="editCodeVisible = false">取消</el-button>
        <el-button type="primary" @click="addCode()">确认添加</el-button>
      </span>
    </template>
  </el-dialog>
  <el-dialog v-model="deleteCodeVisible" title="删除源件" align-center width="auto">
    <p>是否删除选中的源件（注意，该操作不可撤回）</p>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="deleteCodeVisible = false">取消</el-button>
        <el-button type="danger" @click="deleteCode()">确认删除</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped src="./FileCodeView.scss"/>
<script lang="ts" setup>
import {Delete, Download, Edit, Plus, Search} from "@element-plus/icons-vue";
import {ref,computed,onMounted} from 'vue'
import {HttpClient} from "@/network/HttpClient";
import {ElMessage} from "element-plus";
import {cloneDeep} from "lodash";

const loading = ref(false)
const searchText = ref("")
const editCodeVisible = ref(false)
const addCodeVisible = ref(false)
const deleteCodeVisible = ref(false)
const codeEdit = ref({
  id:null,
  codeName: '',
  codeType: null,
  codeComment: '',
  codeAuthor: '',
  codeProps:[],
  codeDate:'',
  codeCaller: [],
  codeFunction: [],
  codeCallee: []
})
const codeAdd = ref({
  codeName: '',
  codeType: null,
  codeComment: '',
  codeAuthor: '',
  codeProps:[],
  codeDate:'',
  codeCaller: [],
  codeFunction: [],
  codeCallee: []
})

onMounted(async ()=> {
  loading.value = true
  await getAllCodes();
})
const getAllCodes = async () =>{
  await HttpClient.post("getAllCodes").then(res => {
    console.log(res.result)
    tableData.value = res.result
    loading.value = false
  })
}
const editRowIndex = ref(null)
const editCodeEvent = (chosenCode,rowIndex)=>{
  editRowIndex.value = rowIndex
  editCodeVisible.value = true
  codeEdit.value = cloneDeep(chosenCode)
}
const editCode = async (id)=>{
  let name = codeEdit.value.codeName
  let author = codeEdit.value.codeAuthor
  let comment = codeEdit.value.codeComment
  let type = codeEdit.value.codeType
  let date = codeEdit.value.codeDate
  await HttpClient.post("editCode", {id:id,name:name,author:author,comment:comment,date:date,type:type}).then(res => {
    if(res.result){
      let index = editRowIndex.value
      ElMessage({message:"修改源件成功",type:'success'});
      tableData.value[index].codeName = name
      tableData.value[index].codeAuthor = author
      tableData.value[index].codeComment = comment
      tableData.value[index].codeType = type
      tableData.value[index].codeDate = date
      editCodeVisible.value = false
    }
  })
}
const addCode = async () =>{
  let name = codeAdd.value.codeName
  let author = codeAdd.value.codeAuthor
  let comment = codeAdd.value.codeComment
  let type = codeAdd.value.codeType
  let date = codeAdd.value.codeDate
  if(name == ''){
    ElMessage({message:"源件名不得为空",type:'warning'});
  }else if(type==null||type==undefined){
    ElMessage({message:"请选择源件类型",type:'warning'});
  }else if(!date){
    ElMessage({message:"请输入开发时间",type:'warning'});
  }else{
    await HttpClient.post("addCode", {name:name,author:author,comment:comment,date:date,type:type}).then(async res => {
        ElMessage({message:"添加源件成功",type:'success'});
        console.log(res.result)
        let jo = {}
        jo.codeName =name
        jo.codeAuthor = author
        jo.codeComment = comment
        jo.id = res.result
        jo.codeType = type
        jo.codeDate = date
        jo.codeProps = []
        jo.codeFunction = []
        jo.codeCaller = []
        jo.codeCallee = []
        tableData.value.push(jo)
        addCodeVisible.value = false
    })
  }
}
//删除源件
const multipleSelection = ref([])
const multipleSelectedRow = ref([])
const handleSelectionChange = (val) => {
  multipleSelection.value = []
  for(let v of val){
    multipleSelection.value.push(v.id)
  }
  multipleSelectedRow.value = val.map(row => tableData.value.indexOf(row));
}
const deleteCode = async () =>{
  await HttpClient.post("deleteCodes",{ids:JSON.stringify(multipleSelection.value)}).then(res => {
    ElMessage({message:"删除源件成功",type:'success'});
    deleteCodeVisible.value = false
  })
  multipleSelectedRow.value.sort((a, b) => {return b - a})
  for (let row of multipleSelectedRow.value) {
    tableData.value.splice(row, 1);
  }
}
//下载Json
const downloadCodeJson = (jsonData) => {
  const jsonString = JSON.stringify(jsonData, null, 2);
  const blob = new Blob([jsonString], { type: 'text/plain' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = jsonData.codeName + '.txt';
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  URL.revokeObjectURL(url);
}
const filterTableData = computed(() =>
    tableData.value.filter(
        (data) => !searchText.value || data.codeName.toLowerCase().includes(searchText.value.toLowerCase()) || data.codeComment.toLowerCase().includes(searchText.value.toLowerCase()) || data.codeAuthor.toLowerCase().includes(searchText.value.toLowerCase())
    )
)
const tableData:any = ref([
  // {
  //   id:1,
  //   codeName: 'AnalysisPoint',
  //   codeType: 0,
  //   codeComment: '用于描述分析点名',
  //   codeAuthor: '梁沚诺',
  //   codeProps:[
  //     {
  //       propName:"id",
  //       propDescription:"分析点id"
  //     }
  //   ],
  //   codeDate:'2023-12-1',
  //   codeCaller: [
  //     {
  //       caller:"AnalysisPointDaoImpl",
  //       callerType:"创建",
  //       callerDescription:"分析点数据库实现类"
  //     },
  //   ],
  //   codeFunction: [
  //     {
  //       functionProto:"void setName(String name)",
  //       functionDescription:"设置分析点名"
  //     },
  //   ],
  //   codeCallee: [
  //   ],
  // },
])
</script>
<style>
.el-dialog__body{
  padding-bottom: 10px !important;
  padding-top: 10px !important;
}
.el-dialog__header{
  display: flex !important;
}
</style>
