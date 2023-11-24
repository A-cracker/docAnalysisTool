package com.wtb.javatool.action;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fy.basejar.tool.ActionToolBase;
import com.fy.javanode.declarative.annotation.EnableHttpApi;
import com.fy.toolhelper.tool.ActionTool;
import com.fy.toolhelper.util.RequestUtils;
import com.wtb.javatool.httpApi.CoreApi;
import com.wtb.javatool.romote.service.CoreRemoteService;
import com.wtb.javatool.service.AnalysisService;
import com.wtb.javatool.service.DocumentService;
import com.wtb.javatool.util.Node;
import com.wtb.javatool.util.R;
import com.wtb.javatool.util.RunPython;
import com.wtb.javatool.vo.AnalysisContent;
import com.wtb.javatool.vo.AnalysisPoint;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;

import javax.json.Json;
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
//		String base = getRealPath("extraction.py");
//		String path = "E:\\lab\\file-extraction\\项目文件\\案例 (1)\\案例\\批复的立项方案_测试.docx";
//		String cmd = String.format("python %s --path=\"%s\"", base, path);
//		Map<String, Object> map = RunPython.runPythonByRuntime(cmd);
//		System.out.println(map);
//		System.out.println("更新过的test");
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
        List<Node> result = analysisService.bulidTree(root, fid);
        Integer layer = analysisService.getTreeLayer(fid);
        Map<String,Object> res= new HashMap<>();
        res.put("tree",result);
        res.put("layer",layer);
        return R.ok().MAP().PRINT().result(res);
    }

    /**
     * 获取相应分析点的内容ti
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
//		String storageId = "document_1698668284283";
        String storageId = RequestUtils.getStringParameter(request, "storageID");
        String fn = RequestUtils.getStringParameter(request, "fileName");
        String extension = RequestUtils.getStringParameter(request, "extension");
        // FACILITY_TYPE_ID:4487639
        // PUBLIC_BIG_FILE_SERVER_URL: ""
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

        //暂时没用上
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
        System.out.println("base:" + base);
        System.out.println(filePath);
//        String cmd = String.format("python3 %s --path=\"%s\" --name=\"%s\"", base, filePath, fileName);
        //本地
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
        int root = analysisService.extration(docContent, storageId);

        result.put("root", root);
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
        String fid = RequestUtils.getStringParameter(request, "storageID");
        Integer count = analysisService.checkExist(fid);
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
        Integer root = analysisService.getRootByFid(fid);
        if(root == null){
            root = 0;
        }
        return R.ok().MAP().PRINT().result(root);
    }

    /**
     * 删除分析点树
     */
    @Action
    public R deleteTree(HttpServletRequest request) throws Exception {
        String fid = RequestUtils.getStringParameter(request, "storageID");
        analysisService.deleteTree(fid);
        return R.ok().MAP().PRINT().result("删除分析点树成功");
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
        AnalysisPoint ap = analysisService.addAnalysisPoint(pid,fid,name);
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
        Map<String, String> res = new HashMap<>();
        if(analysisService.deleteAnalysisPoint(id,fid,root,type)){
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
}