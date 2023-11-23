/**
 * 
 */
package com.wtb.javatool.constant;

import java.io.File;

/**
 * @author yangzhuoheng
 */
public class Constants {
	public static String SYSTEM_TEMP_DIR;
	public static Long FILE_DELETE_TOOLID = null;
	public static String TOOL_CONFIG = null;
	public static String WTB_CORE_URL = null;
	public static String WTB_WX_URL = null;
	public static String AID = null;

	/**
	 * 普通文件服务器设施区类型ID
	 */
	public static String FACILITY_TYPE_ID = null;
	public static String FACILITY_PROPERTY_NAME = "fileServerUrl";
	public static String PUBLIC_FILE_SERVER_URL = null;

	/**
	 * 大文件服务器设施区类型ID
	 */
	public static String BIG_FILE_SERVER_FACILITY_TYPE_ID;

	/**
	 * 文件处理结果：文件处理失败
	 */
	public static final String FILE_MANAGE_FAIL = "-1";
	/**
	 * 文件处理结果：文件处理成功
	 */
	public static final String FILE_MANAGE_SUCCESS = "0";
	/***********上传的文件类型**************/
	/**上传资料**/
	public static final String UPLOAD_DOCUMENT = "0";
	/**上传版本**/
	public static final String UPLOAD_VERSION = "1";
	/**替换版本**/
	public static final String REPLACE_VERSION = "2";
	
	static{
		SYSTEM_TEMP_DIR = System.getProperty("java.io.tmpdir") + File.separator;
	}

	/**
	 * 普通文件storageID前缀
	 */
	public static final String NORMAL_FILE_PREFIX = "document_";

	/**
	 * 大文件storageID前缀
	 */
	public static final String BIG_FILE_PREFIX = "big_file_";
	
	public static void main(String[] args) {
		System.out.println(System.getProperty("java.io.tmpdir"));
	}
}

