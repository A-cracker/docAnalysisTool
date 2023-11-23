package com.wtb.javatool.constant;

import com.gpu.GpuTaskPool;

/**
 * 文件相关常量，如文件类型、文件转换状态
 * @author hongweiquan
 *
 */
public class FileConstant {
	
	public static GpuTaskPool gpuTashPool = new GpuTaskPool();

	// ****************文件类型（消息附件或者帮区资料）******************
	public final static String MESSAGE_FILE = "messageFile";

	public final static String BAND_DOCUMENT = "bandDocument";

	/**
	 * 将纯文本文件转码为UTF-8,防止乱码
	 */
	public final static String PLAIN_TEXT_CHANGE_CODE = "UTF-8";

	// ****************MS office文档******************
	// MS office 2003
	public final static String DOC_EXTENSION = "doc";
	public final static String PPT_EXTENSION = "ppt";
	public final static String XLS_EXTENSION = "xls";
	public final static String VSD_EXTENSION = "vsd";

	// MS office里的visio和project不支持转为pdf

	// MS office 2007/2010
	public final static String DOCX_EXTENSION = "docx";
	public final static String PPTX_EXTENSION = "pptx";
	public final static String XLSX_EXTENSION = "xlsx";
	public final static String VSDX_EXTENSION = "vsdx";

	// ****************open office文档【暂时只知道这么多个******************
	// 文字(odt ott sxw sdw vor)
	public final static String ODT_EXTENSION = "odt";
	public final static String OTT_EXTENSION = "ott";
	public final static String SXW_EXTENSION = "sxw";
	public final static String SDW_EXTENSION = "sdw";
	public final static String VOR_EXTENSION = "vor";

	// 电子表格(ods ots sxc stc dif dbf xlt sdc vor slk csv)
	public final static String ODS_EXTENSION = "ods";
	public final static String OTS_EXTENSION = "ots";
	public final static String SXC_EXTENSION = "sxc";
	public final static String STC_EXTENSION = "stc";
	public final static String DIF_EXTENSION = "dif";
	public final static String DBF_EXTENSION = "dbf";
	public final static String XLT_EXTENSION = "xlt";
	public final static String SDC_EXTENSION = "sdc";
	public final static String SLK_EXTENSION = "slk";
	public final static String CSV_EXTENSION = "csv";

	// 幻灯片(odg odp otp sda sdd sti sxd sxi vor)
	public final static String ODP_EXTENSION = "odp";
	public final static String OTP_EXTENSION = "otp";
	public final static String STI_EXTENSION = "sti";
	public final static String SXI_EXTENSION = "sxi";

	// 图形文件(odg otg sxd std sda vor sdd)
	public final static String ODG_EXTENSION = "odg";
	public final static String OTG_EXTENSION = "otg";
	public final static String SXD_EXTENSION = "sxd";
	public final static String STD_EXTENSION = "std";
	public final static String SDA_EXTENSION = "sda";
	public final static String SDD_EXTENSION = "sdd";

	// ****************wps office文档******************

	// 文字(wps，wpt，dot，rtf)
	public final static String WPS_EXTENSION = "wps";
	public final static String WPT_EXTENSION = "wpt";
	public final static String DOT_EXTENSION = "dot";

	// 电子表格(et,ett)
	public final static String ET_EXTENSION = "et";
	public final static String ETT_EXTENSION = "ett";

	// 幻灯(dps，dpt)
	public final static String DPS_EXTENSION = "dps";
	public final static String DPT_EXTENSION = "dpt";

	// 图片(gif,jpg,png,tif,bmp,wmf,emf)
	public final static String GIF_EXTENSION = "gif";
	public final static String JPG_EXTENSION = "jpg";
	public final static String PNG_EXTENSION = "png";
	public final static String TIF_EXTENSION = "tif";
	public final static String BMP_EXTENSION = "bmp";
	public final static String WMF_EXTENSION = "wmf";
	public final static String EMF_EXTENSION = "emf";

	public final static String RTF_EXTENSION = "rtf";

	// ****************纯文本文*****************
	public final static String TXT_EXTENSION = "txt";
	public final static String LOG_EXTENSION = "log";
	public final static String XML_EXTENSION = "xml";
	public final static String MXML_EXTENSION = "mxml";// flex文件
	public final static String JAVA_EXTENSION = "java";
	public final static String CPP_EXTENSION = "cpp";
	public final static String C_EXTENSION = "c";
	public final static String H_EXTENSION = "h";// 头文�?
	public final static String PROPERTIES_EXTENSION = "properties";
	public final static String CSS_EXTENSION = "css";
	public final static String JSP_EXTENSION = "jsp";
	public final static String ASP_EXTENSION = "asp";

	// *************直接转换*************
	public final static String HTML_EXTENSION = "html";
	public final static String HTM_EXTENSION = "htm";
	public final static String JS_EXTENSION = "js";// java script

	// ***********下面是支持文件解压缩的压缩格�?*********
	public final static String RAR_EXTENSION = "rar";
	public final static String ZIP_EXTENSION = "zip";
	public final static String JAR_EXTENSION = "jar";

	/**
	 * pdf文档
	 */
	public final static String PDF_EXTENSION = "pdf";

	/**
	 * swf文档，就不用传过来转换了，可以直接在线浏览
	 */
	public final static String SWF_EXTENSION = "swf";

	/**
	 * MicroSoft文件中转为pdf，再转为swf
	 */
	public final static String MICROSOFT_OFFICE_FILE = "MS";

	/**
	 * openoffice文件中转为pdf，再转为swf
	 */
	public final static String OPEN_OFFICE_FILE = "OO";

	/**
	 * wps office文件中转为pdf，再转为swf
	 */
	public final static String WPS_OFFICE_FILE = "WPS";

	/**
	 * 纯文本文件转为pdf，再转为swf
	 */
	public final static String PLAIN_TEXT_FILE = "Plain_Text";

	/**
	 * 图片文件直接浏览
	 */
	public final static String IMG_FILE = "IMG";

	/**
		 * 
		 */
	public final static String RAR_FILE = "RAR";
	/**
	 * PDF文件
	 */
	public final static String PDF_FILE = "PDF";

	/**
	 * SWF文件
	 */
	public final static String SWF_FILE = "SWF";

	/**
	 * 将纯文本文件转为系统对中文支持最好的编码格式(windos:GBK linux:UTF-8)的文件
	 */
	public final static String PLAIN_TEXT_CHINESE_FILE = "Plain_Text_Chinese";

	/**
	 * 不可识别的文件，不些文件不作转换处理
	 */
	public final static String UNRECOGNIZED_FILE = "Unrecognized";
	
	// ****************文件转换状态******************
	/**
	 * 文件转换状态:转换中
	 */
	public final static Integer FC_CONVERTING = 0;
	/**
	 * 文件转换失败
	 */
	public final static Integer FC_CONVERT_FAIL = 1;
	/**
	 * 文件转换成功 
	 */
	public final static Integer FC_CONVERT_SUCCESS = 2;
	/**
	 * 没有找到转换记录
	 */
	public final static Integer FC_NOT_FOUND = -1;
	
	// ****************文件转换处理状态******************
	/**
	 * 参数错误
	 */
	public final static Integer FCM_PARAM_ERROR = 0;
	/**
	 * 无法或无需转换的类型
	 */
	public final static Integer FCM_CANNOT_COVERT_TYPE = 1;
	/**
	 * 文件已转换过
	 */
	public final static Integer FCM_HAS_BEEN_CONVERTED = 2;
	/**
	 * 文件正在转换中
	 */
	public final static Integer FCM_CONVERTING = 3;
	/**
	 * 源文件不存在
	 */
	public final static Integer FCM_NOT_EXISTS = 4;
	/**
	 * 文件转换失败
	 */
	public final static Integer FCM_CONVERT_ERROR = 5;
	/**
	 * 文件转换成功
	 */
	public final static Integer FCM_CONVERT_SUCCESS = 6;
	
	// ****************文件操作权限******************
	/**
	 * 没有任何权限
	 */
	public final static Integer NO_PERMISSION = 0;
	
	/**
	 * 有预览权限
	 */
	public final static Integer PREVIEW_PERMISSION = 1;
	
	/**
	 * 有下载权限
	 */
	public final static Integer DOWNLOAD_PERMISSION = 2;
	
	// ****************转换后的文件类型******************
	/**
	 * 转换后的html文件内容
	 */
	public final static String HTML = "htmlContent";
	/**
	 * 转换后的pdf文件内容
	 */
	public final static String PDF = "pdfContent";
	/**
	 * 其它文件内容(压缩文件的无需转换的子文件（如txt、img）)
	 */
	public final static String OTHER_FILE_CONTENT = "otherFileContent";
	
	/**
	 * 最大转换失败次数（转换失败次数大于等于这个数字则不再进行重新转换）
	 */
	public final static Integer MAX_CONVERT_DEFEAT_TIMES = 6;
	
	public final static long NEXT_CONVERT_DATE = 6 * 60 * 60 * 1000;
}
