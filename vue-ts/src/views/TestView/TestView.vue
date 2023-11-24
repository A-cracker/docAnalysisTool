<template>
  <div class="tree-view box">
    <div class="tree left">
      <div class="tree-wrap" style="height: 100%;">
        <div class="search-box">
          <span>搜索：</span>
          <input type="text" v-model="keyword" placeholder="请输入搜索内容" @keydown.enter="filter" />
          <div style="margin: 0 2px;display: inline-block"><el-button @click="filter" :icon="Search"></el-button></div>
          <div>
            <el-button @click="extractionDialogVisible = true">解析文件</el-button>
          </div>
        </div>
        <div style="flex-grow: 1" v-loading="loading" element-loading-text="正在解析文件...">
          <vue3-tree-org
              ref="tree"
              :data="data"
              :horizontal="horizontal"
              :collapsable="collapsable"
              :label-style="style"
              :node-draggable="true"
              :only-one-node="onlyOneNode"
              :selected-class-name="selectedClassName"
              :selected-key="focusNode"
              :default-expand-level="1"
              :filter-node-method="filterNodeMethod"
              :clone-node-drag="cloneNodeDrag"
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
          <el-col v-for="content in contents">
            <div class="card" v-if="content.type==2">
              <el-card shadow="hover">
                <div class="analysis">
                  <div class="analysis-content" :id=content.id>
                    <p class="text">{{content.content}}</p>
                  </div>
                  <div class="card-tag">
                    <el-tag class="ml-2">文本</el-tag>
                  </div>
                </div>
              </el-card>
            </div>
            <div class="card" v-if="content.type==0">
              <el-card shadow="hover">
                <div class="analysis">
                  <div class="analysis-content" style="flex-direction: column" :id=content.id>
                    <el-table :show-header="false" :data="parseTable(content.content)" style="width: 100%" height="200px">
                      <el-table-column v-for="header in parseHeader(content.content)" :prop=header :label=header />
                    </el-table>
                  </div>
                  <div class="card-tag">
                    <el-tag type=success class="ml-2">表格</el-tag>
                  </div>
                </div>
              </el-card>
            </div>
            <div class="card" v-if="content.type==1">
              <el-card shadow="hover">
                <div class="analysis">
                  <div class="analysis-content" style="flex-direction: column" :id=content.id>
                    <img :src=content.content class="img"/>
                  </div>
                  <div class="card-tag">
                    <el-tag type="warning" class="ml-2">图片</el-tag>
                  </div>
                </div>
              </el-card>
            </div>
          </el-col>
        </el-scrollbar>
      </div>
    </div>
  </div>
  <el-dialog v-model="extractionDialogVisible" title="解析文件" width="30%" align-center>
    <el-form label-position="top" label-width="100px" style="max-width: 460px">
      <el-form-item label="请选择待解析文件">
        <el-select v-model="fileChosen" value-key="objID" placeholder="帮区文件">
          <el-option v-for="file in filterFile(fileLists)" :key=file.objID :label="file.name+'.'+file.extension" :value="file"/>
        </el-select>
      </el-form-item>
      <el-form-item label="请选择解析模板">
        <el-select v-model="templateType" placeholder="解析模板">
          <el-option label="立项方案模板" value="1"/>
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
    <span>该文件已被解析过，是否需要重新解析？</span>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click=useHistoryVersion>采用历史解析版本</el-button>
        <el-button type="primary" @click="reExtraction">
          重新解析
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {ref, onMounted, getCurrentInstance, ComponentInternalInstance, inject} from 'vue';
import {ElMessage} from "element-plus";
import {Search} from "@element-plus/icons-vue";
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
const onlyOneNode = ref(true);
const cloneNodeDrag = ref(true);
//文本解析对话框
const extractionDialogVisible = ref(false)
//是否重新解析对话框
const checkDialogVisible = ref(false)
// 文档树DOM
const tree = ref(null);
// 解析的文档id
const fid = ref(0);
// 当前文档树的根节点id
const rootId = ref(0);
// 相应分析点的内容
const contents = ref([]);
const runToolParam = inject<RunToolParam>("runToolParam");
onMounted(async () => {
  const dragControllerDiv = () => {
    const resize = document.getElementsByClassName('resize') as HTMLCollectionOf<HTMLElement>;
    const left = document.getElementsByClassName('left') as HTMLCollectionOf<HTMLElement>;
    const mid = document.getElementsByClassName('mid') as HTMLCollectionOf<HTMLElement>;
    const box = document.getElementsByClassName('box') as HTMLCollectionOf<HTMLElement>;

    for (let i = 0; i < resize.length; i++) {
      resize[i].onmousedown = function (e) {
        resize[i].style.background = '#818181';
        const startX = e.clientX;
        // @ts-ignore
        resize[i].left = resize[i].offsetLeft;

        document.onmousemove = function (e) {
          const endX = e.clientX;
          // @ts-ignore
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
          // @ts-ignore
          resize[i].releaseCapture && resize[i].releaseCapture();
        };
        // @ts-ignore
        resize[i].setCapture && resize[i].setCapture();
        return false;
      };
    }
  }
  dragControllerDiv(); // 调用函数
  // //构建文档树
  // await HttpClient.post("buildTree",{root:1,fid:"document_1698668284283"}).then(res => {
  //   if(res.result!=null) {
  //     delete res.result[0].pid;
  //     data.value = res.result[0];
  //   }
  // })
  //获取帮区资料柜文件
  await HttpClient.post("getDocumentsInfo",{bandId: runToolParam?.bandId}).then(res => {
    if(res.result.rows.length!=0) {
      fileLists.value = res.result.rows
    }
  })
});
//选择的模板
const templateType =ref(null)
//内容检索词
const searchWord = ref("")
const style = {
  background: "#fff",
  color: "#5e6d82",
}

const onMenus = ({ node, command }) => {
  console.log(node, command);
}

const restore = () => {
  console.log('restore')
}

const filter = () => {
  const treeElement:any = tree.value
  treeElement.filter(keyword.value)
}

const filterNodeMethod = (value, data) => {
  console.log(value, data)
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
}

const onNodeClick = async (e, data) => {
  focusNode.value =data.id;
  ElMessage({message:data.label,type:'success'});
  const focus =focusNode.value;
  await HttpClient.post("getContentById",{id:focus}).then(res => {
    contents.value = res.result
  });
}
//选择的文件
const fileChosen =ref({
  currDocumentVersion:{
    storageID:""
  },
  name:"",
  extension:"",
  objID:""
})

const fileLists = ref([])
//过滤文件
const filterFile = (fileLists) =>{
  let filteredTLists = fileLists.filter(file => file.extension === 'doc' || file.extension === 'docx');
  return filteredTLists
}
//控制加载
const loading = ref(false)
//解析文件
const extraction = async() => {
  if(templateType.value==null){
    ElMessage({message:"请选择解析模板",type:'warning'});
  }else if(fileChosen.value.objID = ""){
    ElMessage({message:"请选择要解析的文件",type:'warning'});
  }else {
    let storageID: String = fileChosen.value.currDocumentVersion.storageID
    let fileName: String = fileChosen.value.name
    let extension: String = fileChosen.value.extension
    let hasCheck: boolean = false
    await HttpClient.post("checkExist", {storageID: storageID}).then(res => {
      hasCheck = res.result
    })
    if (hasCheck) {
      checkDialogVisible.value = true
    } else {
      extractionDialogVisible.value = false
      loading.value = true
      await HttpClient.post("extraction", {storageID: storageID, fileName: fileName, extension: extension}).then(res => {
        fid.value = res.result.fid
        rootId.value = res.result.root
      })
      let temp_f = fid.value;
      let temp_r = rootId.value;
      await HttpClient.post("buildTree", {root: temp_r, fid: temp_f}).then(res => {
        delete res.result[0].pid;
        store.commit('tree/setTreeData', res.result[0])
        data.value = store.getters["tree/getTreeData"] || {};
        loading.value = false
        // data.value = res.result[0];
      })
    }
  }
}
const useHistoryVersion = async() =>{
  checkDialogVisible.value = false
  extractionDialogVisible.value = false
  let storageID: String = fileChosen.value.currDocumentVersion.storageID
  let root = 0
  await HttpClient.post("getRoot", {storageID: storageID}).then(res => {
    root = res.result
  })
  loading.value = true
  await HttpClient.post("buildTree", {root: root, fid: storageID}).then(res => {
    delete res.result[0].pid;
    store.commit('tree/setTreeData', res.result[0])
    data.value = store.getters["tree/getTreeData"] || {};
    loading.value = false
  })
}

const reExtraction = async() =>{
  checkDialogVisible.value = false
  extractionDialogVisible.value = false
  let storageID: String = fileChosen.value.currDocumentVersion.storageID
  let fileName: String = fileChosen.value.name
  let extension: String = fileChosen.value.extension
  await HttpClient.post("deleteTree", {storageID: storageID}).then(res => {
    console.log(res.result)
  })
  loading.value = true
  await HttpClient.post("extraction", {storageID: storageID, fileName: fileName, extension: extension}).then(res => {
    fid.value = res.result.fid
    rootId.value = res.result.root
  })
  let temp_f = fid.value;
  let temp_r = rootId.value;
  await HttpClient.post("buildTree", {root: temp_r, fid: temp_f}).then(res => {
    delete res.result[0].pid;
    store.commit('tree/setTreeData', res.result[0])
    data.value = store.getters["tree/getTreeData"] || {};
    loading.value = false
    // data.value = res.result[0];
  })
}

const parseHeader = (content) =>{
  const data = JSON.parse(content.replace(/'/g, '"'))
  const array:string[] = new Array()
  for(let i = 0;i<data[0].length;i++){
    array.push(i.toString())
  }
  const headers = array;
  return headers
}
const parseTable = (content)=>{
  const data = JSON.parse(content.replace(/'/g, '"'))
  const array:string[] = new Array()
  for(let i = 0;i<data[0].length;i++){
    array.push(i.toString())
  }
  const headers = array;
  const rows = data

  const tableData = rows.map(row => {
    const rowData = {};
    headers.forEach((header, index) => {
      rowData[header] = row[index];
    });
    return rowData;
  });
  return tableData;
}

</script>
<style lang="scss" scoped>
.tree-wrap {
  position: relative;
  padding-top: 52px;
}
.tree-org-node__text {
  text-align: left;
  font-size: 14px;
  .custom-content {
    padding-bottom: 8px;
    margin-bottom: 8px;
    border-bottom: 1px solid currentColor;
  }
}
.detail{
  background: #f1f0f0;
  display: flex;
  flex-direction: column;
  .search {
    padding:1px 2px;
  }
  .content-card {
    background: #ffffff;
    margin: 0 2px 2px 2px;
    flex-grow: 1;
  }
}
.card{
  margin: 3px 3px;
}
.analysis{
  display: flex;
  justify-content: space-between;
  .card-tag{
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 4px;
  }
  .analysis-content{
    flex-grow: 1;
    display: flex;
    overflow-y: auto;
    .text{
      text-align: start;
      font-size: 14px;
      padding:2px;
    }
    .img{
      width: 100%;
    }
    .table{
      width: 100%;
    }
  }
}
@media(min-width: 768px) {
  .left {
    width: calc(32% - 10px);  /*左侧初始化宽度*/
    height: 100%;
    background: #FFFFFF;
    float: left;
  }
  /*拖拽区div样式*/
  .resize {
    cursor: col-resize;
    float: left;
    position: relative;
    top: 45%;
    background-color: #d6d6d6;
    border-radius: 5px;
    margin-top: -10px;
    width: 8px;
    height: 50px;
    background-size: cover;
    background-position: center;
    /*z-index: 99999;*/
    font-size: 28px;
    color: white;
  }
  /*拖拽区鼠标悬停样式*/
  .resize:hover {
    color: #444444;
  }
  /*右侧div'样式*/
  .mid {
    float: left;
    width: 100%;   /*右侧初始化宽度*/
    height: 100%;
    background: #fff;
    box-shadow: -1px 4px 5px 3px rgba(0, 0, 0, 0.11);
  }
  .tree-view {
    height: 100%;
    display: flex;
    .tree {
      min-width: 35%;
      height: 100%;
    }
    .detail {
      min-width: 30%;
      height: 100%;
    }
  }
}
@media(max-width: 768px) {
  .tree-view {
    height: 100%;
    display: flex;
    flex-direction: column;
    .tree {
      width: 100%;
      height: 50%;
    }
    .detail {
      width: 100%;
      height: 50%;
    }
    .resize{
      display: none;
    }
  }
}
.tree-wrap {
  display: flex;
  flex-direction: column;
  position: relative;
  padding-top: 52px;
  .zm-tree-org{
    padding: 0;
  }
}
.search-box {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  padding: 8px 15px;
  position: absolute;
  top: 0;
  left: 0;
  input {
    width: 200px;
    height: 32px;
    border:1px solid #ddd;
    outline: none;
    border-radius: 5px;
    padding-left: 10px;
  }
}
.tree-org-node__text {
  text-align: left;
  font-size: 14px;
  .custom-content {
    padding-bottom: 8px;
    margin-bottom: 8px;
    border-bottom: 1px solid currentColor;
  }
}
</style>
<style>
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