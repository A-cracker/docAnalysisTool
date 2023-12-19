package com.wtb.javatool.action;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fy.basejar.tool.ActionToolBase;
import com.fy.javanode.declarative.annotation.EnableHttpApi;
import com.fy.toolhelper.tool.ActionTool;
import com.fy.toolhelper.util.RequestUtils;
import com.wtb.javatool.httpApi.CoreApi;
import com.wtb.javatool.romote.service.CoreRemoteService;
import com.wtb.javatool.service.AnalysisService;
import com.wtb.javatool.service.CodeService;
import com.wtb.javatool.service.DocumentService;
import com.wtb.javatool.service.VersionService;
import com.wtb.javatool.util.Node;
import com.wtb.javatool.util.R;
import com.wtb.javatool.util.RunPython;
import com.wtb.javatool.vo.AnalysisContent;
import com.wtb.javatool.vo.AnalysisPoint;
import com.wtb.javatool.vo.Code;
import com.wtb.javatool.vo.Version;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Api(value = "javatool", description = "java工具")
@Scope(SCOPE_PROTOTYPE) //务必使用多实例模式
@ComponentScan("com.wtb.javatool")
@EnableHttpApi(packages = "com.wtb.javatool") //启用声明式调用
public class JavaTool extends ActionToolBase {
    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private VersionService versionService;

    @Autowired
    private CoreRemoteService coreRemoteService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CoreApi api;

    private static Logger log = LoggerFactory.getLogger(JavaTool.class);

    /**
     * 获取python文件路径
     */
    public String getRealPath(String file) {
        String path = this.getResourcePath();
        System.out.println("getRealPath path = " + path);
        //本地调试添加
        path = path.replace("\\resource\\", "WEB-INF\\");
        path = path + "other" + File.separator + file;
        return path;
    }

    /**
     * 测试接口
     */
    @Action
    public R test(HttpServletRequest request) {
        try {
            Long bandId = getBandID();
            JSONObject result = coreRemoteService.getDocumentsInBand(bandId);
            // 在这里处理正常返回的结果
            JSONArray rows = result.getJSONArray("rows");
            return R.ok().MAP().PRINT().result(rows);
        } catch (Exception e) {
            // 在这里处理异常
            e.printStackTrace(); // 或者进行其他处理，比如记录日志
            return R.error().STR().PRINT().result(e);
        }
    }
    /**
     * 提取信息接口（调试本地文件）
     */
//	@Action
//	public R extractionLocal(HttpServletRequest request) {
//		String base = getRealPath("extraction.py");
//		//获取文件url
//		String path = "E:\\lab\\file-extraction\\项目文件\\案例 (1)\\案例\\批复的立项方案_测试.docx";
//		// 创建 File 对象
//		File file = new File(path);
//		FileVo f= new FileVo();
//		f.setTitle(file.getName());
//		f.setUrl(path);
//		fileService.uploadFile(f);
//		String cmd = String.format("python %s --path=\"%s\"", base, path);
//		Map<String, Object> map = RunPython.runPythonByRuntime(cmd);
//		Object value = map.get("result");
//		ArrayList<String> docContent = null;
//		if (value instanceof ArrayList) {
//			docContent = (ArrayList<String>) value;
//		} else {
//			System.out.println("值不是 ArrayList<String> 类型");
//		}
//		int root = analysisService.extration(docContent,f.getId());
//		Map<String, Object> result = new HashMap<>();
//		result.put("root",root);
//		result.put("fid",f.getId());
//		result.put("msg","信息提取成功");
//		return R.ok().MAP().PRINT().result(result);
//
//	}

    /**
     * 构建文档树接口
     */
    @Action
    public R buildTree(HttpServletRequest request) {
        int root = RequestUtils.getIntegerParameter(request, "root");
        String fid = RequestUtils.getStringParameter(request, "fid");
        int versionId = RequestUtils.getIntegerParameter(request, "versionId");
        List<Node> result = analysisService.bulidTree(root, fid,versionId);
        Integer layer = analysisService.getTreeLayer(fid);
        Map<String,Object> res= new HashMap<>();
        res.put("tree",result);
        res.put("layer",layer);
        return R.ok().MAP().PRINT().result(res);
    }

    /**
     * 获取相应分析点的内容
     */
    @Action
    public R getContentById(HttpServletRequest request) {
        int id = RequestUtils.getIntegerParameter(request, "id");
        List<AnalysisContent> result = analysisService.getContentById(id);
        return R.ok().MAP().PRINT().result(result);
    }

    /**
     * 获取帮区资料柜文件解析接口（PUBLIC_FILE_SERVER_URL要改成线上）
     */
    @Action
    public R extraction(HttpServletRequest request) throws Exception {
        String storageId = RequestUtils.getStringParameter(request, "storageID");
        String fn = RequestUtils.getStringParameter(request, "fileName");
        String extension = RequestUtils.getStringParameter(request, "extension");
        int versionId = RequestUtils.getIntegerParameter(request, "versionId");
        String type = RequestUtils.getStringParameter(request, "type");
        String FACILITY_TYPE_ID = "4487639";
        String PUBLIC_FILE_SERVER_URL = "https://192.168.88.47:10060/servlet/FileManageServlet";
        String fileServiceUrl = documentService.getFileServerByFacilityTypeID(FACILITY_TYPE_ID,
                PUBLIC_FILE_SERVER_URL, this);
        Map<String, Object> params = new HashMap<>();
        params.put("storageID", storageId);
        params.put("accessToken", getAccessToken());
        params.put("action", "getFiles");
        HttpResponse response = HttpRequest.post(fileServiceUrl)
                .header("wtbauthc-trust", "true")
                .form(params)
                .execute();
        System.out.println("fileServiceUrl : " + fileServiceUrl);
        System.out.println("accessToken : " + getAccessToken());

        InputStream inputStream = response.bodyStream();
        // 存储到Tomcat临时目录
        String tempDir = System.getProperty("catalina.base");
        String fileName = fn + '.' + extension;
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        String filePath = tempDir + File.separator + "temp" + File.separator + uniqueFileName;
        Map<String, Object> result = new HashMap<>();
		File file = new File(filePath);
		if (!file.exists()) {
			try{
				if(file.createNewFile()) {
                    System.out.println("创建文件");
                    // 将byte数据写入文件
                    try {
                        FileOutputStream fos = new FileOutputStream(filePath);
                        // 创建一个缓冲区
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        // 读取二进制数据并写入文件
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                            fos.flush();
                        }
                        inputStream.close();
                        fos.close();
                    } catch (IOException e) {
                        throw e;
                    }
                }
        //测试解析
        String base = getRealPath("extraction.py");
        String cmd = String.format("python %s --path=\"%s\" --name=\"%s\"", base, filePath, fileName);
        Map<String, Object> map = RunPython.runPythonByRuntime(cmd);
        Object value = map.get("result");
        result.put("map", map.get("error").toString());
        ArrayList<String> docContent = null;
        if (value instanceof ArrayList) {
            docContent = (ArrayList<String>) value;
        } else {
            System.out.println("值不是 ArrayList<String> 类型");
        }
        Map<String,Integer> res = analysisService.extraction(docContent, storageId,type,versionId);
        result.put("versionId",res.get("versionId"));
        result.put("root", res.get("root"));
        result.put("msg", "信息提取成功");
			}catch (IOException e) {
				throw e;
			}
		}
        //释放文件
		if (file.exists()) {
			file.delete();
		}
        result.put("fid", storageId);
        result.put("msg", "信息提取成功");
        return R.ok().MAP().PRINT().result(result);
    }

    /**
     * 获取文件列表
     */
    @Action
    public R getDocumentsInfo(HttpServletRequest request) throws Exception {
        long bandId = RequestUtils.getLongParameter(request, "bandId");
        JSONObject jo = coreRemoteService.getDocumentsInBand(bandId);
        return R.ok().MAP().PRINT().result(jo);
    }

    /**
     * 检查文件是否被解析过
     */
    @Action
    public R checkExist(HttpServletRequest request) throws Exception {
        int versionId = RequestUtils.getIntegerParameter(request, "versionId");
        Integer count = analysisService.checkExist(versionId);
        if (count != 0) {
            return R.ok().MAP().PRINT().result(true);
        } else {
            return R.ok().MAP().PRINT().result(false);
        }
    }

    /**
     * 返回根节点
     */
    @Action
    public R getRoot(HttpServletRequest request) throws Exception {
        String fid = RequestUtils.getStringParameter(request, "storageID");
        int versionId = RequestUtils.getIntegerParameter(request, "versionId");
        Integer root = analysisService.getRootByFid(fid,versionId);
        if(root == null){
            root = 0;
        }
        return R.ok().MAP().PRINT().result(root);
    }
    /**
    * 获取文件树最新版本id
    */
    @Action
    public R getLatestVersion(HttpServletRequest request) throws Exception {
        String fid = RequestUtils.getStringParameter(request, "fid");
        Integer latestVersion = versionService.getLatestVersionByFid(fid);
        if(latestVersion == null){
            latestVersion = 0;
        }
        return R.ok().MAP().PRINT().result(latestVersion);
    }
    /**
     * 删除分析点树
     */
    @Action
    public R deleteVersion(HttpServletRequest request) throws Exception {
        int versionId = RequestUtils.getIntegerParameter(request, "versionId");
        boolean flag = versionService.deleteVersion(versionId);
        return R.ok().MAP().PRINT().result(flag);
    }
    /**
     * 根据文档objID获取文件信息
     */
    @Action
    public R getDocumentInfoById(HttpServletRequest request) throws Exception {
        long bandId = RequestUtils.getLongParameter(request, "bandId");
        String objID = RequestUtils.getStringParameter(request, "objID");
        JSONObject jo = coreRemoteService.getDocumentInfoById(objID,bandId);
        return R.ok().MAP().PRINT().result(jo);
    }
    /**
     * 新增分析点
     */
    @Action
    public R addAnalysisPoint(HttpServletRequest request) throws Exception {
        int pid = RequestUtils.getIntegerParameter(request, "pid");
        String name = RequestUtils.getStringParameter(request, "name");
        String fid = RequestUtils.getStringParameter(request, "fid");
        int versionId = RequestUtils.getIntegerParameter(request, "versionId");
        AnalysisPoint ap = analysisService.addAnalysisPoint(pid,fid,name,versionId);
        return R.ok().MAP().PRINT().result(ap);
    }
    /**
     * 删除分析点
     */
    @Action
    public R deleteAnalysisPoint(HttpServletRequest request) throws Exception {
        int id = RequestUtils.getIntegerParameter(request, "id");
        String fid = RequestUtils.getStringParameter(request, "fid");
        int root = RequestUtils.getIntegerParameter(request, "root");
        int type = RequestUtils.getIntegerParameter(request, "type");
        int versionId = RequestUtils.getIntegerParameter(request, "versionId");
        Map<String, String> res = new HashMap<>();
        if(analysisService.deleteAnalysisPoint(id,fid,root,type,versionId)){
            res.put("type","success");
            res.put("msg","删除节点成功");
        }else{
            res.put("type","error");
            res.put("msg","删除失败,不可删除根节点");
        }
        return R.ok().MAP().PRINT().result(res);
    }
    /**
     * 修改分析点名
     */
    @Action
    public R editAnalysisPoint(HttpServletRequest request) throws Exception {
        int id = RequestUtils.getIntegerParameter(request, "id");
        String name = RequestUtils.getStringParameter(request, "name");
        analysisService.editAnalysisName(id,name);
        return R.ok().MAP().PRINT().result(true);
    }
    /**
     * 获取所有文档版本
     */
    @Action
    public R getVersionsByFid(HttpServletRequest request) throws Exception {
        String fid = RequestUtils.getStringParameter(request, "fid");
        List<Version> list = versionService.getVersionsByFid(fid);
        return R.ok().MAP().PRINT().result(list);
    }
    /**
     * 修改版本信息
     */
    @Action
    public R editVersion(HttpServletRequest request) throws Exception {
        int versionId = RequestUtils.getIntegerParameter(request, "versionId");
        String name = RequestUtils.getStringParameter(request, "name");
        String description = RequestUtils.getStringParameter(request, "description");
        Boolean res = versionService.editVersion(versionId,name,description);
        return R.ok().MAP().PRINT().result(res);
    }
    /**
     * 根据id获取版本信息
     */
    @Action
    public R getVersionById(HttpServletRequest request) throws Exception {
        int versionId = RequestUtils.getIntegerParameter(request, "versionId");
        Version version = versionService.getVersionById(versionId);
        return R.ok().MAP().PRINT().result(version);
    }
    /**
     * 更新覆盖版本树
     */
    @Action
    public R deleteHistoryVersion(HttpServletRequest request) throws Exception {
        int versionId = RequestUtils.getIntegerParameter(request, "versionId");
        boolean flag = versionService.deleteHistoryVersion(versionId);
        return R.ok().MAP().PRINT().result(flag);
    }
    @Autowired
    CodeService codeService;
    /**
     * 新增源件
     */
    @Action
    public R addCode(HttpServletRequest request) throws Exception {
        String name = RequestUtils.getStringParameter(request, "name");
        Integer type = RequestUtils.getIntegerParameter(request, "type");
        String comment = RequestUtils.getStringParameter(request, "comment");
        String date = RequestUtils.getStringParameter(request, "date");
        String author = RequestUtils.getStringParameter(request, "author");
        Integer id = codeService.addCode(name,author,type,date,comment);
        return R.ok().MAP().PRINT().result(id);
    }
    @Action
    public R editCode(HttpServletRequest request) throws Exception {
        Integer id = RequestUtils.getIntegerParameter(request, "id");
        String name = RequestUtils.getStringParameter(request, "name");
        Integer type = RequestUtils.getIntegerParameter(request, "type");
        String comment = RequestUtils.getStringParameter(request, "comment");
        String date = RequestUtils.getStringParameter(request, "date");
        String author = RequestUtils.getStringParameter(request, "author");
        boolean flag = codeService.editCode(id,name,author,type,date,comment);
        return R.ok().MAP().PRINT().result(flag);
    }
    @Action
    public R getAllCodes(HttpServletRequest request) throws Exception {
        List<Code> codes= codeService.getAllCodes();
        JSONArray ja = new JSONArray();
        for(Code code : codes){
            JSONObject jo = new JSONObject();
            jo.put("codeName",code.getName());
            jo.put("id",code.getId());
            jo.put("codeComment",code.getComment());
            jo.put("codeProps", JSON.parse(code.getProps()));
            jo.put("codeFunctions",JSON.parse(code.getFunctions()));
            jo.put("codeCaller",JSON.parse(code.getCallers()));
            jo.put("codeCallee",JSON.parse(code.getCallees()));
            jo.put("codeType",code.getType());
            jo.put("codeAuthor",code.getAuthor());
            jo.put("codeDate",code.getDate());
            ja.add(jo);
        }
        return R.ok().MAP().PRINT().result(ja);
    }
    @Action
    public R deleteCodes(HttpServletRequest request) throws Exception {
        List<String> arr = RequestUtils.getJsonArrayParameter(request, "ids");
        codeService.deleteCodesByIdArr(arr);
        return R.ok().MAP().PRINT().result("删除成功");
    }
}