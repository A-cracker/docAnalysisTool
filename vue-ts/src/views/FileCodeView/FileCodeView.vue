<template>
  <div class="code-view">
    <div style="padding: 8px 0;display: flex;" class="nav">
      <div class="nav-left">
        <el-input v-model="searchText" placeholder="请输入代码源件信息" :suffix-icon="Search"/>
        <el-tooltip content="新增源件"  placement="bottom" effect="light">
          <el-button type="primary" :icon="Plus" @click="addCodeVisible = true" size="small" plain/>
        </el-tooltip>
        <el-popconfirm width="auto" hide-icon confirm-button-text="确认删除" cancel-button-text="取消" title="是否删除选中的源件（注意，该操作不可撤销）?" @confirm="deleteCode()">
          <template #reference>
            <el-button type="danger" :icon="Delete" size="small"/>
          </template>
        </el-popconfirm>
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
          lazy
          style="width: 100%;"
          v-loading="loading"
          row-key="id"
          empty-text="暂无相关源件">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column type="expand">
          <template #default="props">
            <div m="4">
              <p m="t-0 b-2"><el-tag>开发者:</el-tag> {{ props.row.codeAuthor }}</p>
              <p m="t-0 b-2"><el-tag type="info">开发时间:</el-tag> {{ props.row.codeDate }}</p>
              <p m="t-0 b-2"><el-tag type="success">源件属性:</el-tag><el-button style="margin-left: 10px" size="small" plain type="primary" @click="addPropClick(props.row.id)">新增属性</el-button></p>
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
              <p m="t-0 b-2"><el-tag type="success">源件函数:</el-tag><el-button style="margin-left: 10px" size="small" plain type="primary" @click="addFunctionClick(props.row.id)">新增函数</el-button></p>
              <el-table :data="props.row.codeFunction" border empty-text="该源件暂无函数">
                <el-table-column label="函数原型" prop="functionProto" />
                <el-table-column label="函数注释" prop="functionDescription" />
                <el-table-column label="操作">
                  <template #default="scope">
                    <el-button size="small" :icon="Edit" @click="editFunctionClick(props.row.id,scope.row)"></el-button>
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

  <el-dialog v-model="addPropVisible" title="新增源件属性" align-center width="auto">
    <el-form label-position="top" label-width="100px">
      <el-form-item label="属性原型">
        <el-input v-model="propAdd.propName" placeholder="如:String name"/>
      </el-form-item>
      <el-form-item label="属性描述">
        <el-input v-model="propAdd.propDescription" type="textarea" placeholder="请输入属性描述"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="addPropVisible = false">取消</el-button>
        <el-button type="primary" @click="addPropEvent(addPropId)">确认添加</el-button>
      </span>
    </template>
  </el-dialog>

  <el-dialog v-model="addFVisible" title="新增源件函数" align-center width="auto">
    <el-form label-position="top" label-width="100px">
      <el-form-item label="函数原型">
        <el-input v-model="fAdd.functionProto" placeholder="如:public String getItem(int id)"/>
      </el-form-item>
      <el-form-item label="函数注释">
        <el-input v-model="fAdd.functionDescription" type="textarea" placeholder="请输入函数描述"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="addFVisible = false">取消</el-button>
        <el-button type="primary" @click="addFEvent(addFId)">确认添加</el-button>
      </span>
    </template>
  </el-dialog>
  <el-dialog v-model="editFVisible" title="修改函数" align-center width="auto">
    <el-form label-position="top" label-width="100px">
      <el-form-item label="函数原型">
        <el-input v-model="fEdit.functionProto" placeholder="如:public String getItem(int id)"/>
      </el-form-item>
      <el-form-item label="函数注释">
        <el-input v-model="fEdit.functionDescription" type="textarea" placeholder="请输入函数描述"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="editFVisible = false">取消</el-button>
        <el-button type="primary" @click="editFEvent(editCodeId)">确认修改</el-button>
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
  console.log(chosenCode)
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
  })
  multipleSelectedRow.value.sort((a, b) => {return b - a})
  for (let row of multipleSelectedRow.value) {
    tableData.value.splice(row, 1);
  }
}
//新增属性
const addPropVisible = ref(false)
const addPropId = ref(null)
const addPropClick = (id) =>{
  addPropVisible.value = true
  addPropId.value = id
}
const propAdd = ref({
  propName:"",
  propDescription:""
})
const addPropEvent = async (id) =>{
  addPropVisible.value = true
  const props = tableData.value.find(obj => obj.id === id).codeProps;
  props.push(cloneDeep(propAdd.value))
  await HttpClient.post("updatePropById",{id:id,prop:JSON.stringify(props)}).then(res => {
    ElMessage({message:"新增属性成功",type:'success'});
    addPropVisible.value = false
  })
}
//新增函数
const addFVisible = ref(false)
const addFId = ref(null)
const addFunctionClick = (id) =>{
  addFVisible.value = true
  addFId.value = id
}
const fAdd = ref({
  functionProto:"",
  functionDescription:""
})
const addFEvent = async (id) =>{
  addFVisible.value = true
  const functions = tableData.value.find(obj => obj.id === id).codeFunction;
  functions.push(cloneDeep(fAdd.value))
  await HttpClient.post("updateFuncById",{id:id,functions:JSON.stringify(functions)}).then(res => {
    ElMessage({message:"新增函数成功",type:'success'});
    addFVisible.value = false
  })
}
//修改函数
const editFVisible = ref(false)
const editCodeId = ref(null)
const fEdit = ref({
  functionProto:"",
  functionDescription:""
})
let temp = {}
const editFunctionClick = (id,chosenFunction) =>{
  editFVisible.value = true
  editCodeId.value = id
  fEdit.value = cloneDeep(chosenFunction)
  temp = chosenFunction
}
const editFEvent = async (id) =>{
  editFVisible.value = false
  temp.functionProto = fEdit.value.functionProto
  temp.functionDescription = fEdit.value.functionDescription

  //
}
//新增被调用源件
//新增调用源件
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
