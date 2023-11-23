package com.wtb.javatool.util;

import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import java.io.*;
import java.security.Key;
import java.security.SecureRandom;

public class EntryptDocUtils {
	/**
	 * 获取密钥对象
	 * @param strKey 密钥（该值先从常量类中获取，按设计是每个帮区都有不用的密钥）
	 * @return
	 */
//	public static Key getKey(String strKey){
//		try {   
//			KeyGenerator _generator = KeyGenerator.getInstance("DES");   
//			_generator.init(new SecureRandom(strKey.getBytes()));   
//			Key key = _generator.generateKey();   
//			_generator = null;  
//			return key;
//		} catch (Exception e) {   
//			throw new RuntimeException("Error initializing SqlMap class. Cause: " + e);   
//		}   
//	}
	public static Key getKey(String strKey){
		try {
			//SecureRandom 实现完全隨操作系统本身的内部状态，
			//除非调用方在调用 getInstance 方法之后又调用了 setSeed 方法；
			//该实现在 windows 上每次生成的 key 都相同，但是在 solaris 或部分 linux 系统上则不同。
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		    random.setSeed(strKey.getBytes());
		    
			KeyGenerator _generator = KeyGenerator.getInstance("DES");   
			_generator.init(random);   
			Key key = _generator.generateKey();   
			_generator = null;  
			return key;
		} catch (Exception e) {   
			throw new RuntimeException("Error initializing SqlMap class. Cause: " + e);   
		}   
	}
	
	/**
	 * 文件加密
	 * @param key 密钥对象
	 * @param ins 待加密文件输入流
	 * @return byte[] 加密后文件内容
	 * @throws Exception
	 */
	public static byte[] encrypt(Key key, InputStream ins) throws Exception{
		CipherInputStream cis = null;
		byte[] buffer = null;
		if(key != null){
			try {
				Cipher cipher = Cipher.getInstance("DES");
				cipher.init(Cipher.ENCRYPT_MODE, key);
				cis = new CipherInputStream(ins, cipher);
				buffer = IOUtils.toByteArray(cis);
			} catch (Exception e) {
				throw e;
			}finally{
				if(cis != null){
					cis.close();  
				}
				if(ins != null){
					ins.close();
				}
			}
		}
		return buffer;
	}
	
	/**
	 * 文件加密
	 * @param key 密钥对象
	 * @param ins 待加密文件输入流
	 * @return byte[] 加密后文件内容
	 * @throws Exception
	 */
	public static byte[] encrypt(Key key, byte[] bytes) throws Exception{
//		InputStream ins = new ByteArrayInputStream(bytes);
		CipherInputStream cis = null;
		byte[] buffer = null;
		if(key != null){
			try {
				Cipher cipher = Cipher.getInstance("DES");
				cipher.init(Cipher.ENCRYPT_MODE, key);
				buffer =  cipher.doFinal(bytes);
//				cis = new CipherInputStream(ins, cipher);
//				buffer = IOUtils.toByteArray(cis);
			} catch (Exception e) {
				throw e;
			}finally{
				if(cis != null){
					cis.close();  
				}
//				if(ins != null){
//					ins.close();
//				}
			}
		} 
		return buffer;
	}
	
	/**
	 * 文件解密
	 * @param key 密钥对象
	 * @param ins 已加密文件输入流
	 * @return byte[] 解密后文件内容
	 * @throws Exception
	 */
	public static byte[] decrypt(Key key, InputStream ins) throws Exception{
		if(key != null){
			Cipher cipher = Cipher.getInstance("DES");   
			cipher.init(Cipher.DECRYPT_MODE, key);
			
			File tempFile = File.createTempFile("fileService", null);
			OutputStream out = new FileOutputStream(tempFile);   
			CipherOutputStream cos = new CipherOutputStream(out, cipher);   
			byte[] buffer = new byte[1024];   
			int r;   
			while ((r = ins.read(buffer)) >= 0) {   
				cos.write(buffer, 0, r);   
			}
			cos.flush();
			//要先关闭CipherOutputStream再重新读取文件，否则读取到的文件内容会是乱码
			cos.close();   
			out.close();   
//			ins.close();
			
			InputStream temp_is = new FileInputStream(tempFile);
			byte[] result = IOUtils.toByteArray(temp_is);
			temp_is.close();
			tempFile.deleteOnExit();
			
			return result;
		} else {
			return null;
		}
	}
	
	/**
	 * 文件解密
	 * @param key 密钥对象
	 * @param ins 已加密文件输入流
	 * @return byte[] 解密后文件内容
	 * @throws Exception
	 */
	public static byte[] decrypt(Key key, byte[] bytes) throws Exception{
		if(key != null){
			Cipher cipher = Cipher.getInstance("DES");   
			cipher.init(Cipher.DECRYPT_MODE, key);
			
			byte[] result = cipher.doFinal(bytes);
			return result;
		} else {
			return null;
		}
	}
	
//	private static void createMyFile(String name, byte[] content){
//		File file1 = new File(name);
//		System.out.println("文件路径为" + name);
//		FileOutputStream fos = null;
//		try {
//			if(!file1.exists()){
//				file1.createNewFile();
//			}
//			fos = new FileOutputStream(file1);
//			fos.write(content);
//			fos.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}finally{
//			if(fos != null){
//				try {
//					fos.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//	
//	private static byte[] inpustreamToByte(InputStream is){
//		ByteArrayOutputStream swapStream = new ByteArrayOutputStream(); 
//		byte[] buff = new byte[100]; //buff用于存放循环读取的临时数据 
//		int rc = 0; 
//		try {
//			while ((rc = is.read(buff, 0, 100)) > 0) { 
//			swapStream.write(buff, 0, rc); 
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
//		byte[] in_b = swapStream.toByteArray();
//		return in_b;
//	}
}
