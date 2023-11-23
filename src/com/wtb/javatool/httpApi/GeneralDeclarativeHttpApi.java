package com.wtb.javatool.httpApi;

import com.fy.javanode.declarative.annotation.*;
import com.wtb.javatool.vo.Entity;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.Map;


/**
 * 普通声明式调用接口,@HttpApi的urlAlias为URL别名,在config.xml中配置
 * <br>
 * 例如:
 * <br>
 * {@code <UrlAlias name="abc">http://127.0.0.1:8080</UrlAlias>}
 * <br>
 * 也可以把@HttpApi的urlAlias省略掉,如:@HttpApi("abc")
 * 
 * 
 * @Author linjintian
 * @Date 2019年3月4日
 */
@HttpApi(urlAlias = "abc",version = HttpApi.VERSION_2)
@Param(name = "global", value="val") //全局参数，支持占位符
@AccessToken(name = "access_token")  //URL带上accessToken、bandID、gid、toolID、userID,且accessToken的key用access_token
@BandID 
@Gid 
@ToolID
@UserID
@Header(name = "user-agent", value = "user") //全局header
@Config(socketTimeout = 10, connectionTimeout = 10) //配置全局socketTimeout和connectionTimeout
public interface GeneralDeclarativeHttpApi{
	/*
	 *	请求  /entity/|id|  接口,该接口返回{"id":123,"name":""}, 用不同的类型包装返回的数据
	 */
	
	/**
	 * Rest风格的Get请求,一个请求参数,返回String
	 * @param id
	 * @return
	 * 
	 * @Author linjintian
	 */
	@Get("/entity/|id|")
	String getEntityByIDAndReturnString(@Mount("id") int id);
	
	/**
	 * Rest风格的Get请求,一个请求参数,返回实体类
	 * @param id
	 * @return
	 * 
	 * @Author linjintian
	 */
	@Get("/entity/|id|")
	Entity getEntityByIDAndReturnEntityObject(@Mount("id") int id);
	
	/**
	 * Rest风格的Get请求,一个请求参数,返回Map
	 * @param id
	 * @return
	 * 
	 * @Author linjintian
	 */
	@Get("/entity/|id|")
	Map<String,Object> getEntityByIDAndReturnMap(@Mount("id") int id);
	
	/**
	 * Rest风格的Get请求,一个请求参数,返回由ResponseEntity包装过的Entity,可以从ResponseEntity中获取请求状态码; Entity也可以换成String
	 * @param id
	 * @return
	 * 
	 * @Author linjintian
	 */
	@Get("/entity/|id|")
	ResponseEntity<Entity> getEntityByIDAndReturnResponseEntity(@Mount("id") int id);
	
	
	
	
	/*
		请求 entity/|id|/|name|  接口,该接口返回{"id":123,"name":""},用不同的方式进行传参
	*/
	
	/**
	 * Rest风格的Get请求,两个请求参数,返回实体类
	 * @param id
	 * @param name
	 * @return
	 * 
	 * @Author linjintian
	 */
	@Get("entity/|id|/|name|")
	Entity getEntityByIDAndNameAndReturnEntityObject(@Mount("id") int id,@Mount("name") String name);
	
	
	
	/**
	 * Post请求,两个参数用Entity进行包装,并用@Body注明请求体返回实体类
	 * @param id
	 * @param name
	 * @return
	 * 
	 * @Author linjintian
	 */
	@Post("testPostAndReturnObject")
	Entity testEntityPostMethodAndReturnEntity(@Body Entity entity);
	
	
	/**
	 * 请求体@Body用在方法上
	 * @param id
	 * @param name
	 * @return
	 */
	@Post("testPostAndReturnObject")
	@Body("{\"id\":|id|,\"name\":\"|name|\"}")
	@Header(name = "content-type",value="application/json")
	String testBody(@Mount("id") Integer id,@Mount("name") String name);
	
	
	/**
	 * 提交表单,表单带文件
	 * @return
	 */
	@Post("testPostForm")
	@FormField(name = "fileName" ,value = "|name|")
	@FormField(name = "file", value = "|file|" ,type = FormField.FILE)	//提交文件时要修改type，且要用FileSystemResource来封装File
	String testForm(@Mount("name") String name, @Mount("file") FileSystemResource file);
	
	
	
	@Delete("testDelete/|id|")
	boolean testDeleteMethod(@Mount("id") int id);
	
	
	
	
	@Put("testPut")
	boolean testPutMethod(@Body Entity entity);
	
	
	
	
	
	
	/**
	 * 获取文件流
	 * @return
	 * 
	 * @Author linjintian
	 */
	@Get("getStream")
	byte[] getFileByteArr();
	
	
	/**
	 * 上传文件
	 * @param entity
	 * @return
	 * 
	 * @Author linjintian
	 */
	@Post("postFile")
	boolean postFile(@Body MultiValueMap<String, Object> entity);
	
}
