package com.wtb.javatool.httpApi;

import com.fy.javanode.declarative.annotation.*;
import com.wtb.javatool.vo.Entity;
import org.springframework.core.io.FileSystemResource;

/**
 * 工具声明式调用接口,@HttpApi(toolID = 123)中的toolID在callToolConfig.xml中配置
 * 
 * @Author linjintian
 * @Date 2019年3月4日
 */
@HttpApi(toolID = 12345, version = HttpApi.VERSION_2)
public interface ToolDeclarativeHttpApi {

	/**
	 * 调用工具的server_getEntity操作,并返回{"id":123,"name":""}
	 * @param action
	 * @return
	 * 
	 * @Author linjintian
	 */
	@Get(params="action=getEntity2")
	Entity getEntity(@Param(name = "name") String name);


	/**
	 * 测试@RunToolParam注解
	 * 调用工具的server_testParamOfRunToolParam操作,并返回{"id":123,"name":""}
	 * @param action
	 * @return
	 * 
	 * @Author linjintian
	 */
	@Get(params="action=server_testParamOfRunToolParam")
	String testParamOfRunToolParam(@RunToolParam String param);

	@Get(params= {"action=server_testParamOfRunToolParamTargetMethod"})
	@RunToolParam("{\"name\":\"|name|\"}")
	String testParamOfRunToolParamTargetMethod(@Mount("name") String name);

	/**
	 * 调用工具的server_postFile操作进行上传文件,并返回{"id":123,"name":""}
	 * @param action
	 * @return
	 * 
	 * @Author linjintian
	 */
	@Post(params= {"action=server_postFile"})
	@Param(name = "p1",value="|p1|")
	@FormField(name = "file" ,value = "|file|",type = FormField.FILE)
	@FormField(name = "name" ,value = "|name|")
	Entity postFile(@Mount("p1") String p1,@Mount("file") FileSystemResource file,@Mount("name") String name);
	
	
}
