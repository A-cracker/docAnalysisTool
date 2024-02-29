<template>
  <div class="tree-view box">
    <div class="tree left">
      <div class="tree-wrap" style="height: 100%;">
        <div class="search-box">
          <span class="search-text">搜索：</span>
          <input type="text" v-model="keyword" placeholder="请输入搜索内容" @keydown.enter="filter" />
          <div style="margin: 0 2px;display: inline-block"><el-button @click="filter" :icon="Search"></el-button></div>
          <div style="margin-right: 2px">
            <el-button @click="extractionDialogVisible = true" plain type="primary">解析文件</el-button>
          </div>
          <div>
            <el-select v-model="version" placeholder="版本选择">
              <el-option
                  v-for="version in versionList"
                  :key="version.id"
                  :label="version.name"
                  :value="version.id"
                  @click="useHistoryVersion"
              >
                <span style="float: left">{{ version.name }}</span>
                <a style="float: right;color: var(--el-text-color-secondary);font-size: 13px;" @click.native.stop="getVersionDetail(version.description,version.name,version.id)">详情</a>
              </el-option>
            </el-select>
          </div>
        </div>
        <div style="flex-grow: 1" v-loading="loading" element-loading-text="正在解析文件...">
          <vue3-tree-org
              ref="tree"
              :data="data"
              :horizontal="horizontal"
              :collapsable="collapsable"
              :label-style="style"
              :node-draggable="false"
              :default-expand-level="level"
              :only-one-node="onlyOneNode"
              :selected-class-name="selectedClassName"
              :selected-key="focusNode"
              :filter-node-method="filterNodeMethod"
              :define-menus="defineMenus"
              :node-add="NodeAdd"
              :node-delete="NodeDelete"
              :node-edit="NodeEdit"
              @on-restore="restore"
              @on-contextmenu="onMenus"
              @on-node-click="onNodeClick"
          />
        </div>
      </div>
    </div>
    <div class="resize">⋮</div>
    <div class="detail mid">
      <div class="search">
        <el-input
            v-model="searchWord"
            class="w-50 m-2"
            placeholder="请输入检索词"
            :prefix-icon="Search"
        />
      </div>
      <div class="content-card">
        <el-scrollbar height="calc(100vh - 100px)">
          <el-col v-for="(content,index) in filteredContents" :key="index">
            <div class="card" v-if="content.type==2">
              <el-card shadow="hover">
                <div class="analysis">
                  <div class="analysis-content" :id=content.index>
                    <p class="text" v-html="highlightText(content.content)"></p>
                  </div>
                  <div class="card-tag">
                    <el-tag class="ml-2">文</el-tag>
                  </div>
                </div>
              </el-card>
            </div>
            <div class="card" v-if="content.type==0">
              <el-card shadow="hover">
                <div class="analysis">
                  <div class="analysis-content" style="flex-direction: column" :id=content.index>
                    <el-table border :data="parseTable(content.content)" style="width: 100%" height="500px" :span-method="createSpanMethod(content.content)">
                      <!--                      <el-table-column v-for="header in parseHeader(content.content)" :prop=header :label=header />-->
                      <template #default="{ row }">
                        <el-table-column align="center" v-for="header in parseHeader(content.content)" :key="header" :prop="header" :label="header">
                          <template #default="{ row }">
                            <span v-html="highlightTable(row[header], searchWord)"></span>
                          </template>
                        </el-table-column>
                      </template>
                    </el-table>
                  </div>
                  <div class="card-tag">
                    <el-tag type=success class="ml-2">表</el-tag>
                  </div>
                </div>
              </el-card>
            </div>
            <div class="card" v-if="content.type==1">
              <el-card shadow="hover">
                <div class="analysis">
                  <div class="analysis-content" style="flex-direction: column" :id=content.index>
                    <img :src=content.content class="img"/>
                  </div>
                  <div class="card-tag">
                    <el-tag type="warning" class="ml-2">图</el-tag>
                  </div>
                </div>
              </el-card>
            </div>
          </el-col>
        </el-scrollbar>
      </div>
      <el-button class="float-button" circle type="primary" :icon="Plus"></el-button>
    </div>
  </div>
  <el-dialog v-model="extractionDialogVisible" title="解析文件" width="30%" align-center>
    <el-form label-position="top" label-width="100px" style="max-width: 460px">
      <el-form-item label="请选择解析模板">
        <el-select v-model="templateType" placeholder="解析模板">
          <el-option label="立项方案模板" value="1"/>
          <el-option label="需求规格模板" value="2"/>
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="extractionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="extraction">确认</el-button>
      </span>
    </template>
  </el-dialog>
  <el-dialog
      v-model="checkDialogVisible"
      title="提示"
      width="30%"
      align-center
  >
    <span>若重新解析文件，会覆盖当前解析版本，是否另存为新版本？</span>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click=useHistoryVersion>取消</el-button>
        <el-button plain type="primary" @click="updateVersion">覆盖当前版本</el-button>
        <el-button type="primary" @click="reExtraction('new')" plain>
          另存为新版本
        </el-button>
      </span>
    </template>
  </el-dialog>
  <el-dialog
      v-model="addAnalysisPointDialog"
      title="增加分析点"
      width="30%"
      align-center
  >
    <el-form label-position="top" label-width="100px" style="max-width: 460px">
      <el-form-item label="分析点名：">
        <el-input placeholder="请输入新的分析点名" v-model="analysisName"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="addAnalysisPointDialog = false">取消</el-button>
        <el-button type="primary" @click="addAnalysisPoint">
          确认添加
        </el-button>
      </span>
    </template>
  </el-dialog>
  <el-dialog
      v-model="deleteAnalysisPointDialog"
      title="删除分析点"
      width="30%"
      align-center
  >
    如果该分析点下有其余分析点，是否将其另存到新的分析节点？
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="deleteAnalysisPoint(0)" type="primary" plain>另存分析点</el-button>
        <el-button @click="deleteAnalysisPoint(1)" type="danger" plain>
          一律删除
        </el-button>
      </span>
    </template>
  </el-dialog>
  <el-dialog
      v-model="editAnalysisPointDialog"
      title="修改名称"
      width="30%"
      align-center
  >
    <el-form label-position="top" label-width="100px" style="max-width: 460px">
      <el-form-item label="分析点名：">
        <el-input placeholder="请输入分析点名" v-model="analysisEditedName"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="editAnalysisPointDialog = false">取消</el-button>
        <el-button type="primary" @click="editAnalysisPoint">
          确认修改
        </el-button>
      </span>
    </template>
  </el-dialog>
  <el-dialog
      v-model="editVersionDialog"
      title="修改版本信息"
      width="30%"
      align-center
  >
    <el-form label-position="top" label-width="100px" style="max-width: 460px">
      <el-form-item label="*版本名：">
        <el-input placeholder="请输入版本名" v-model="versionName"></el-input>
      </el-form-item>
      <el-form-item label="版本描述：">
        <el-input placeholder="请输入版本描述" v-model="versionDescription"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="editVersionDialog = false">取消</el-button>
        <el-button type="danger" @click="deleteVersion">删除版本</el-button>
        <el-button type="primary" @click="editVersion">
          确认修改
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {ref, onMounted, inject, computed, onUnmounted} from 'vue';
import {ElMessage} from "element-plus";
import {Search,Plus} from "@element-plus/icons-vue";
import {HttpClient} from "@/network/HttpClient";
import { useStore } from 'vuex'
import { key } from '@/store'
import {RunToolParam} from "@/type";

const store = useStore(key)
const data = ref( store.getters["tree/getTreeData"] || {});

const selectedClassName = ref('selectedNode');
const focusNode = ref('');
//文档树搜索词
const keyword = ref('');
const horizontal = ref(false);
const collapsable = ref(true);
const onlyOneNode = ref(false);
const cloneNodeDrag = ref(true);
const level = ref(0);

//文本解析对话框
const extractionDialogVisible = ref(false)
//是否重新解析对话框
const checkDialogVisible = ref(false)
//修改版本信息
const editVersionDialog = ref(false)
const versionName = ref('')
const versionDescription = ref('')
const editVersion =async ()=>{
  if(versionName.value==''){
    ElMessage({message:"请输入版本名",type:'warning'});
  }else{
    editVersionDialog.value = false
    await HttpClient.post("editVersion", {versionId: versionEdit.value,name:versionName.value,description:versionDescription.value}).then(res => {
      if(res.result){
        //获取文档版本信息
        HttpClient.post("getVersionsByFid", {fid: fileChosen.value.currDocumentVersion.storageID}).then(res => {
          versionList.value = res.result
        })
        ElMessage({message:"修改版本信息成功",type:'success'});
      }else{
        ElMessage({message:"修改版本信息失败",type:'error'});
      }
    })
  }
}
const deleteVersion = async() =>{
  await HttpClient.post("deleteVersion", {versionId: versionEdit.value}).then(res => {
    if(res.result){
      HttpClient.post("getVersionsByFid", {fid: fileChosen.value.currDocumentVersion.storageID}).then(res => {
        versionList.value = res.result
      })
      HttpClient.post("getLatestVersion", {fid: fileChosen.value.currDocumentVersion.storageID}).then(res => {
        // version.value = res.result
        if(res.result == 0){//如果该文档已经没有默认解析版本，解析文档
          templateType.value = "需求规格文档"
          reExtraction('new')
        }else{
          useHistoryVersion()
        }
      })
      ElMessage({message:"版本删除成功",type:'success'});
    }else{
      ElMessage({message:"版本删除失败",type:'error'});
    }
  })
  editVersionDialog.value = false
}
// 文档树DOM
const tree = ref(null);
// 解析的文档id
const fid = ref(0);
const version = ref(null);
const versionList:any = ref([]);
const versionEdit:any = ref(null);
const getVersionDetail = (description,name,id)=>{
  editVersionDialog.value = true;
  versionDescription.value=description;
  versionName.value=name;
  versionEdit.value=id
}
// 当前文档树的根节点id
const rootId = ref(0);
// 相应分析点的内容
const contents:any = ref([]);

const filteredContents = computed(() => {
  if (!searchWord.value || !contents.value.length) {
    return contents.value;
  }
  const regex = new RegExp(searchWord.value, 'gi');
  return contents.value.filter((content) => content.content && content.content.match(regex));
});
//表格高亮
const highlightTable = (text:string, searchTerm:string) =>{
  if (!searchTerm || !text) {
    return text;
  }
  const regex = new RegExp(searchTerm, 'gi');
  return text.replace(regex, '<span class="highlight">$&</span>');
}
//文本高亮
const highlightText = (text: string) => {
  if (!searchWord.value) {
    return text;
  }
  const regex = new RegExp(searchWord.value, 'gi');
  return text.replace(regex, (match) => `<span class="highlight">${match}</span>`);
};

const runToolParam = inject<RunToolParam>("runToolParam");

onMounted(async () => {
  const dragControllerDiv = () => {
    const resize:any = document.getElementsByClassName('resize') as HTMLCollectionOf<HTMLElement>;
    const left = document.getElementsByClassName('left') as HTMLCollectionOf<HTMLElement>;
    const mid = document.getElementsByClassName('mid') as HTMLCollectionOf<HTMLElement>;
    const box = document.getElementsByClassName('box') as HTMLCollectionOf<HTMLElement>;

    for (let i = 0; i < resize.length; i++) {
      resize[i].onmousedown = function (e) {
        resize[i].style.background = '#818181';
        const startX = e.clientX;

        resize[i].left = resize[i].offsetLeft;

        document.onmousemove = function (e) {
          const endX = e.clientX;

          let moveLen = resize[i].left + (endX - startX);
          const maxT = box[i].clientWidth - resize[i].offsetWidth;

          moveLen = moveLen < 50 ? 50 : moveLen;
          moveLen = moveLen > (maxT - 150) ? maxT - 150 : moveLen;

          resize[i].style.left = moveLen;

          for (let j = 0; j < left.length; j++) {
            left[j].style.width = moveLen + 'px';
            mid[j].style.width = (box[i].clientWidth - moveLen - 10) + 'px';
          }
        }

        document.onmouseup = function (evt) {
          resize[i].style.background = '#d6d6d6';
          document.onmousemove = null;
          document.onmouseup = null;

          resize[i].releaseCapture && resize[i].releaseCapture();
        };

        resize[i].setCapture && resize[i].setCapture();
        return false;
      };
    }
  }
  dragControllerDiv(); // 调用函数
  await HttpClient.post("getDocumentInfoById", {bandId: runToolParam?.bandId,objID: runToolParam?.objID}).then(res => {
    fileChosen.value.currDocumentVersion.storageID = res.result.rows[0].currDocumentVersion.storageID
    fileChosen.value.name = res.result.rows[0].name
    fileChosen.value.extension = res.result.rows[0].extension
    fileChosen.value.objID = res.result.rows[0].objID
  })
  //获取文档版本信息
  await HttpClient.post("getVersionsByFid", {fid: fileChosen.value.currDocumentVersion.storageID}).then(res => {
    versionList.value = res.result
  })
  await HttpClient.post("getLatestVersion", {fid: fileChosen.value.currDocumentVersion.storageID}).then(async res => {
    if (res.result == 0) {//如果该文档还没有默认解析版本，解析文档
      templateType.value = "需求规格文档"
      await reExtraction('new')
    } else {
      version.value = res.result
      //使用历史版本
      await useHistoryVersion();
    }
  })
  await getCurrentVersion()
  versionEdit.value = version.value
});
//选择的模板
const templateType:any =ref(null)
//内容检索词
const searchWord = ref("")
const style = {
  background: "#fff",
  color: "#5e6d82",
}

const defineMenus = ref([
  { name:'新建分析点', command: 'add' },
  { name:'删除分析点', command: 'delete' },
  { name:'修改名称', command: 'edit' },
])
//在原先的功能上增加功能

const onMenus = ({ node, command }) => {
  focusNode.value = node.id;
  switch(command){
    case 'add' :
      break
    case 'delete':
      break
    case 'edit':
      break
    default:
      break
  }
}

//覆盖原来的编辑
const editAnalysisPointDialog = ref(false)
const analysisEditedName =ref("")

const NodeEdit = (node) =>{
  focusNode.value = node.id;
  editAnalysisPointDialog.value = true
}
const editAnalysisPoint = async () =>{
  if(analysisEditedName.value == ""){
    ElMessage({message:"分析点名不得为空",type:'warning'});
  }else {
    await HttpClient.post("editAnalysisPoint", {id: focusNode.value, name: analysisEditedName.value}).then(async res => {
      if (res.result) {
        await buildTree(rootId.value,fileChosen.value.currDocumentVersion.storageID)
        editAnalysisPointDialog.value = false
        ElMessage({message: "修改成功", type: 'success'});
      }else{
        ElMessage({message: "修改失败", type: 'error'});
      }
    });
  }
}
//覆盖原来的新增
const addAnalysisPointDialog = ref(false)
const analysisName = ref('')
const NodeAdd = (node) =>{
  focusNode.value = node.id;
  addAnalysisPointDialog.value = true
}
const getCurrentVersion = async ()=>{
  await HttpClient.post("getVersionById", {versionId:version.value}).then(res => {
    versionName.value = res.result.name
    versionDescription.value = res.result.description
  })
}
const addAnalysisPoint = async () => {
  let fid = fileChosen.value.currDocumentVersion.storageID
  if(analysisName.value == ""){
    ElMessage({message:"分析点名不得为空",type:'warning'});
  }else{
    await HttpClient.post("addAnalysisPoint",{pid:focusNode.value,fid:fid,name:analysisName.value,versionId:version.value}).then(res => {
      console.log(res)
    });
    addAnalysisPointDialog.value = false
    await useHistoryVersion()
  }
}
//覆盖原来的删除
const deleteAnalysisPointDialog = ref(false)
const NodeDelete = (node) =>{
  focusNode.value = node.id;
  deleteAnalysisPointDialog.value = true
}
const deleteAnalysisPoint = async (type) => {
  let fid = fileChosen.value.currDocumentVersion.storageID
  await HttpClient.post("deleteAnalysisPoint",{id:focusNode.value,fid:fid,root:rootId.value,type:type,versionId:version.value}).then(async res => {
    deleteAnalysisPointDialog.value = false
    await useHistoryVersion()
    if(res.result.type == "success"){
      ElMessage({message:res.result.msg,type:'success'});
    }else{
      ElMessage({message:res.result.msg,type:'error'});
    }
  });
}

const restore = () => {
  console.log('restore')
}

const filter = () => {
  const treeElement:any = tree.value
  treeElement.filter(keyword.value)
}

const filterNodeMethod = (value, data) => {
  if (!value) return true;//如果输入为空，全展示
  return data.label.indexOf(value) !== -1;
}

const onNodeClick = async (e, data) => {
  focusNode.value =data.id;
  ElMessage({message:data.label});
  await HttpClient.post("getContentById",{id:focusNode.value}).then(res => {
    contents.value = res.result
  });
}
//进入的文件
const fileChosen =ref({
  currDocumentVersion:{
    storageID:""
  },
  name:"",
  extension:"",
  objID:""
})
const fileLists = ref([])
//过滤出doc和docx文件
const filterFile = (fileLists) =>{
  let filteredTLists = fileLists.filter(file => file.extension === 'doc' || file.extension === 'docx');
  return filteredTLists
}
//控制加载
const loading = ref(false)
const buildTree = async (root,fid)=>{
  await HttpClient.post("buildTree", {root: root, fid: fid,versionId:version.value}).then(res => {
    delete res.result.tree[0].pid;
    store.commit('tree/setTreeData', res.result.tree[0])
    data.value = store.getters["tree/getTreeData"] || {};
    level.value = res.result.layer - 1
    loading.value = false
    // data.value = res.result[0];
  })
}
//解析文件
const extraction = async() => {
  if(templateType.value==null){
    ElMessage({message:"请选择解析模板",type:'warning'});
  }else {
    extractionDialogVisible.value = false
    checkDialogVisible.value = true
  }
}
const useHistoryVersion = async() =>{
  checkDialogVisible.value = false
  extractionDialogVisible.value = false
  let storageID: String = fileChosen.value.currDocumentVersion.storageID
  let root = 0
  await HttpClient.post("getRoot", {storageID: storageID,versionId:version.value}).then(res => {
    root = res.result
    rootId.value = res.result
  })
  if(root!=0) {
    loading.value = true
    await buildTree(root,storageID)
  }
}
const updateVersion = async() =>{
  await HttpClient.post("deleteHistoryVersion", {versionId: version.value}).then(res => {
      if(res.result){
        reExtraction('update')
      }
  })
}
const reExtraction = async(type) =>{
  checkDialogVisible.value = false
  extractionDialogVisible.value = false
  let storageID: String = fileChosen.value.currDocumentVersion.storageID
  let fileName: String = fileChosen.value.name
  let extension: String = fileChosen.value.extension
  loading.value = true
  let vid: number|null = version.value
  if(vid == null){
    vid = 0
  }
  await HttpClient.post("extraction", {storageID: storageID, fileName: fileName, extension: extension,type:type,versionId:vid}).then(res => {
    fid.value = res.result.fid
    rootId.value = res.result.root
    version.value = res.result.versionId
    if(type=="new"){
      versionList.value.push({id:res.result.versionId,name:"新建版本",description:""})
    }
  })
  let temp_f = fid.value;
  let temp_r = rootId.value;
  await buildTree(temp_r,temp_f)
}

const parseHeader = (content) =>{
  const data = JSON.parse(content.replace(/'/g, '"'))
  const array:string[] = new Array()
  for(let i = 0;i<data[0].length;i++){
    // array.push(i.toString())
    array.push(data[0][i].toString())
  }
  const headers = array;
  return headers
}

const parseTable = (content)=>{
  const data = JSON.parse(content.replace(/'/g, '"'))
  const array:string[] = new Array()
  for(let i = 0;i<data[0].length;i++){
    // array.push(i.toString())
    array.push(data[0][i].toString())
  }
  const headers = array;
  const rows = data.slice(1)
  // 数字做表头的时候
  // const rows = data
  const tableData = rows.map(row => {
    const rowData = {};
    headers.forEach((header, index) => {
      rowData[header] = row[index];
    });
    return rowData;
  });
  return tableData;
}
//合并单元格
const createSpanMethod = (content) =>{
  let tableData = parseTable(content)
  let tableHeaders = parseHeader(content)
  const rows: number = tableData.length; // 你可以根据需要更改行数
  const cols: number = tableHeaders.length; // 你可以根据需要更改列数
  //初始化单元格span信息
  const cellSpan: any[][] = new Array(rows)
      .fill(null)
      .map(() => new Array(cols).fill([1,1]));
  const areCellsEqual = (row1, col1, row2, col2) => {
    // return tableData[row1][col1] === tableData[row2][col2];
    return tableData[row1][tableHeaders[col1]] === tableData[row2][tableHeaders[col2]];
  };
  // Iterate through the table to merge cells with the same content
  for (let row = 0; row < rows; row++) {
    for (let col = 0; col < cols; col++) {
      // Skip cells that have already been merged
      if (cellSpan[row][col][0] === 0 && cellSpan[row][col][1] === 0) {
        continue;
      }
      // Check if the current cell can be part of a merged group
      let rowspan = 1;
      let colspan = 1;
      // Check for rowspan
      while (
          row + rowspan < rows &&
          areCellsEqual(row, col, row + rowspan, col)
          ) {
        rowspan++;
      }
      // Check for colspan
      while (
          col + colspan < cols &&
          areCellsEqual(row, col, row, col + colspan)
          ) {
        colspan++;
      }
      // Merge cells in the group
      for (let i = 0; i < rowspan; i++) {
        for (let j = 0; j < colspan; j++) {
          cellSpan[row + i][col + j] = [0, 0];
        }
      }
      // Set the span values for the first cell in the group
      cellSpan[row][col] = [rowspan, colspan];
    }
  }
  return ({ row, column, rowIndex, columnIndex }) => {
    return {
      rowspan: cellSpan[rowIndex][columnIndex][0],
      colspan: cellSpan[rowIndex][columnIndex][1]
    };
  }
}
</script>
<style http scoped src="./DocView.scss" lang="scss"/>
<style>
/*搜索高亮*/
.highlight {
  background-color: #108ffe;
  color: white;
}
/*插件样式*/
.zm-tree-contextmenu{
  padding: 4px 0;
}
.selectedNode{
  background: #108ffe !important;
  color:#fff !important;
}
.el-card__body{
  padding: 4px !important;
}
.el-dialog__header{
  text-align: justify;
}
.content .el-select{
  width: 100%;
}
</style>