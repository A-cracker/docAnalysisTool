package com.wtb.javatool.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * java执行python的工具类
 *  使用条件，python代码中有一个main方法，其他代码都在main方法中执行
 * @author hjw
 */

@Slf4j
public class RunPython {

    /**
     * 使用jython运行py代码，缺点：一旦引用第三方库容易报错，而即便手动设置第三方库，也有可能出现错误
     * @param file python文件名称
     * @param params python代码中的参数
     * @return
     */
//    public static Map<String,Object> runPythonByJython(String file, String fun, String params){
//        Map<String,Object> rtnMap = new HashMap<>();
//
//        PythonInterpreter interpreter = new PythonInterpreter();
//        PySystemState sys = interpreter.getSystemState();
//        sys.path.add("D:\\dev\\anaconda\\envs\\pytorch\\Lib");
//
//        try {
//            interpreter.execfile(file);
//            // 假设python有一个main方法，包含所有实现需求的代码。换言之，传来的python代码只需要执行main方法就能完成需求
//            PyFunction function = interpreter.get(fun,PyFunction.class);
//            // 将报文代入并执行python进行解析
//            PyObject o = params == null ? function.__call__(Py.None) : function.__call__(Py.newString(params));
//            rtnMap.put("result",o);
//            interpreter.cleanup();
//            interpreter.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            rtnMap.put("error",e);
//        }
//        return rtnMap;
//    }
    
    /**
     * 使用Runtime.getRuntime().exec()解析运行python
     * @param command 解析的python代码，即py文件的路径
     * @param params python代码中的参数
     * @param charset 码表
     * @return
     */
    public static Map<String,Object> runPythonByRuntime(String command, String params, String charset) throws IOException, InterruptedException {
        Map<String,Object> rtnMap = new HashMap<>();
        String line;
        StringBuffer rtnSb = new StringBuffer();
        try {
            /* 注意：cmd的格式：“python py文件的路径 参数...”
             *  注意2：参数是字符串的时候，必须在首尾手动添加双引号（单引号都不行）
             *  则下面的cmd=python E:/test/pythontest/Demo.py “params” */
//            String cmd = String.format("python %s \"%s\"", command, params);
            // 也可以用String[]，但是params传入前也需要手动在字符串前后加双引号
            String[] cmd = new String[]{"python",command,params};
            Process process = Runtime.getRuntime().exec(cmd);
            // error的要单独开一个线程处理。其实最好分成两个子线程处理标准输出流和错误输出流
            ProcessStream stderr = new ProcessStream(process.getErrorStream(), "ERROR", charset);
            stderr.start();
            // 获取标准输出流的内容
            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream(), charset));
            while ((line = stdout.readLine()) != null) {
                rtnSb.append(line).append("\r\n");
            }
            rtnMap.put("result",rtnSb.toString());
            rtnMap.put("error",stderr.getContent());
            //关闭流
            stdout.close();
            int status = process.waitFor();
            if (status != 0) {
                System.out.println("return value:"+status);
                log.info("event:{}", "RunExeForWindows",process.exitValue());
            }
            process.destroy();
        } catch (Exception e) {
            throw e;
        }
        return rtnMap;
    }


    public static Map<String,Object> runPythonByRuntime(String cmd) throws Exception {
        String charset = "\\".equals(File.separator) ? "gbk" : "utf-8";
        System.out.println(cmd);
        System.out.println(charset);
        Map<String,Object> rtnMap = new HashMap<>();
        String line;
        ArrayList<String> resLines = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            // error的要单独开一个线程处理。其实最好分成两个子线程处理标准输出流和错误输出流
            ProcessStream stderr = new ProcessStream(process.getErrorStream(), "ERROR", charset);
            stderr.start();
            // 获取标准输出流的内容
            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream(), charset));
            while ((line = stdout.readLine()) != null) {
                resLines.add(line);
            }
            System.out.println("resLines: " + resLines.size());
            rtnMap.put("result", resLines);
            rtnMap.put("error",stderr.getContent());
            //关闭流
            stdout.close();
            int status = process.waitFor();
            if (status != 0) {
                System.out.println("return value:"+status);
                log.info("event:{}", "RunExeForWindows",process.exitValue());
            }
            process.destroy();
        } catch (Exception e) {
            throw e;
        }
        return rtnMap;
    }
}
