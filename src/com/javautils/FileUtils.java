package com.javautils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author ganxiangyong
 * @date 2014年9月17日 下午4:05:15
 */
public class FileUtils {

	public static void main(String[] args) throws IOException {
		String aa = readFile("F://seq.txt");
		String[] b = aa.split(",");
		Set<String> duOrders = new HashSet<String>();
		for (String str : b) {
			duOrders.add(str.trim());
		}
		System.out.println("duOrder length:" + duOrders.size());
		
		Set<String> chenOrders = new HashSet<String>();
		BufferedReader fr = new BufferedReader(new FileReader(new File("F://chen.txt")));
		while (true){
			String cc = fr.readLine();
			if (cc != null && !"".equals(cc)){
				chenOrders.add(cc.trim());
			}else{
				break;
			}
		}
		fr.close();
		System.out.println("chenOrder length:" + chenOrders.size());
		
		int a = 0;
		for (String du : duOrders){
			if (!chenOrders.contains(du)){
				a++;
				System.out.println(du + ",") ;
			}
		}
		System.out.println(a);
		
		for (String chen : chenOrders){
			if ("1041559929172533".equals(chen.trim())){
				System.out.println("1041559929172533该订单存在总订单中");
			}
		}
		
		Set<String> ganOrders = new HashSet<String>();
		BufferedReader fr1 = new BufferedReader(new FileReader(new File("F://gan.txt")));
		while (true){
			String cc = fr1.readLine();
			if (cc != null && !"".equals(cc)){
				ganOrders.add(cc.trim());
			}else{
				break;
			}
		}
		fr1.close();
		System.out.println("ganOrder length:" + ganOrders.size());
		System.out.println("甘的订单与杜峰的订单差集：");
		for (String gan : ganOrders){
			if (!duOrders.contains(gan)){
				System.out.println(gan);
			}
		}
		System.out.println("---------------------");
		System.out.println("杜峰的订单与甘的订单差集：");
		for (String du : duOrders){
			if (!ganOrders.contains(du)){
				System.out.println(du);
			}
		}
		
		Set<String> allOrders = new HashSet<String>();
		BufferedReader fr3 = new BufferedReader(new FileReader(new File("F://allRefundOrders548.txt")));
		while (true){
			String cc = fr3.readLine();
			if (cc != null && !"".equals(cc)){
				boolean addFail = !allOrders.add(cc);
				if (addFail){
					System.out.println(cc);
				}
			}else{
				break;
			}
		}
		fr3.close();
		System.out.println("allOrders length:" + allOrders.size());
		
	}

	// ////////////////////////本地相关//////////////////////////////////

	/**
	 * 读取一个文本文件
	 * 
	 * @param srcPath
	 * @return
	 */
	public static String readFile(final String srcPath) {
		try {
			FileReader reader = new FileReader(new File(srcPath));
			BufferedReader br = new BufferedReader(reader);

			String line = null;
			StringBuffer strBuffer = new StringBuffer();
			while ((line = br.readLine()) != null) {
				strBuffer.append(line);
			}

			br.close();
			reader.close();

			return strBuffer.toString();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 将字符内容生成文件
	 * 
	 * @param destPath
	 *            生成的文件路径
	 * @param content
	 *            文件内容
	 * @return
	 */
	public static boolean genFile(final String destPath, String content) {
		if (content == null || content.length() == 0) {
			throw new IllegalArgumentException("内容不能为空！");
		}

		FileWriter fw = null;
		try {
			fw = new FileWriter(destPath);
			fw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	/**
	 * copyFile:拷贝文件
	 * 
	 * @param src
	 *            原文件路径
	 * @param dest
	 *            目标文件路径
	 * @return
	 */
	public static boolean copyFile(final String srcPath, final String destPath) {
		InputStream is = null;
		OutputStream os = null;

		try {
			is = new FileInputStream(srcPath);
			os = new FileOutputStream(destPath);

			int length = 0;
			byte[] b = new byte[1024];
			while (-1 != (length = is.read(b))) {
				os.write(b, 0, length);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	/**
	 * deleteFile：删除指定文件
	 * 
	 * @param src
	 *            要删除的文件路径
	 * @return
	 */
	public static boolean deleteFile(final String srcPath) {
		File file = new File(srcPath);
		if (file.exists() && file.isFile()) {
			return file.delete();
		}

		return false;
	}

	/**
	 * 将指定的文件对象删除（适用于文件处理中所产生的临时文件，比如下载时生成的临时文件）
	 * 
	 * @param files
	 *            要删除的文件对象
	 */
	public static void deleteFiles(List<File> files) {
		if (files != null) {
			for (File file : files) {
				if (file.exists() && file.isFile()) {
					file.delete();
				}
			}
		}
	}

	// /////////////////////////下载相关////////////////////////////////

	/**
	 * 下载文本文件
	 * 
	 * @param response
	 * @param encoding
	 * @param file
	 */
	public static void downloadTextFile(HttpServletResponse response, String encoding, File file) {
		if (file != null) {
			response.setContentType("text/plain");
			response.setCharacterEncoding(encoding);
			response.addHeader("Content-Disposition", "attachment;filename=" + file.getName());

			ServletOutputStream out = null;
			InputStream fis = null;

			try {
				out = response.getOutputStream();
				fis = new BufferedInputStream(new FileInputStream(file.getPath()));
				int lenth;
				byte[] buffer = new byte[1024];
				while (-1 != (lenth = fis.read(buffer))) {
					out.write(buffer, 0, lenth);
				}
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (out != null) {
						out.close();
					}
					if (fis != null) {
						fis.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * excel文件下载
	 * 
	 * @param response
	 * @param wb
	 * @param fileName
	 */
	public static void downloadExcelFile(HttpServletResponse response, Workbook wb, final String fileName) {
		if (wb != null) {
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			ServletOutputStream out = null;
			try {
				out = response.getOutputStream();
				wb.write(out);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (out != null) {
						out.close();
					}
					if (wb != null) {
						wb.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 下载zip文件
	 * 
	 * @param response
	 * @param zipFile
	 */
	public static void downloadZipFile(HttpServletResponse response, File zipFile) {
		if (zipFile != null) {
			response.setContentType("application/zip");
			response.addHeader("Content-Disposition", "attachment;filename=" + zipFile.getName());

			ServletOutputStream out = null;
			InputStream fis = null;

			try {
				out = response.getOutputStream();
				fis = new BufferedInputStream(new FileInputStream(zipFile.getPath()));
				int lenth;
				byte[] buffer = new byte[1024];
				while (-1 != (lenth = fis.read(buffer))) {
					out.write(buffer, 0, lenth);
				}
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (out != null) {
						out.close();
					}
					if (fis != null) {
						fis.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// //////////////////////////////////文件处理相关///////////////////////////////////

	/**
	 * 根据多个文件生成zip压缩文件
	 * 
	 * @param files
	 *            要生成zip文件的文件列表
	 */
	public static void genZipFile(List<File> files, File zipFile) {
		if (files == null) {
			throw new IllegalArgumentException("文件列表不能为空!");
		}
		FileOutputStream out = null;
		ZipOutputStream zos = null;
		InputStream is = null;
		try {
			out = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(out);

			for (int i = 0; i < files.size(); i++) {
				File file = files.get(i);
				is = new FileInputStream(file);
				zos.putNextEntry(new ZipEntry(file.getName()));

				int length;
				byte[] buffer = new byte[1024];
				while (-1 != (length = is.read(buffer))) {
					zos.write(buffer, 0, length);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (zos != null) {
					zos.closeEntry();
					zos.close();
				}
				if (out != null) {
					out.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将多个Workbook对象生成对应的文件对象
	 * 
	 * @param wbs
	 * @param fileNames
	 * @return 生成的文件列表
	 */
	public static List<File> genFilebyWorkbooks(List<Workbook> wbs, List<String> fileNames) {
		if (wbs == null || fileNames == null) {
			throw new IllegalArgumentException("excel列表为空或者文件列名表为空！");
		}
		if (wbs.size() != fileNames.size()) {
			throw new IllegalArgumentException("wbs数量与文件名数量不相等！");
		}

		List<File> files = new ArrayList<File>();
		OutputStream out = null;

		try {
			for (int i = 0; i < wbs.size(); i++) {
				Workbook wb = wbs.get(i);
				File file = new File(fileNames.get(i));
				out = new FileOutputStream(file);
				wb.write(out);
				out.flush();
				out.close();
				files.add(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return files;
	}

	// /////////////////////////与网络文件相关///////////////////////////////

	/**
	 * 从internet上获取文本内容
	 * 
	 * @param _url
	 *            要获取文本的url
	 * @return 该url所返回的字符串
	 */
	public static String getStringByURL(final String _url) {
		InputStream is = null;
		try {
			URL url = new URL(_url);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(1000 * 30);
			conn.setReadTimeout(1000 * 60);
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.connect();

			is = conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			StringBuilder sb = new StringBuilder();
			int len;
			char[] c = new char[1024];
			while (-1 != (len = isr.read(c, 0, 1024))) {
				sb.append(c, 0, len);
			}

			return sb.toString();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 从internet上获取文件并保存到destPath
	 * 
	 * @param _url
	 *            要下载的文件的url
	 * @param destPath
	 *            保存文件的路径
	 */
	public static boolean getFileByURL(final String _url, final String destPath) {
		InputStream is = null;
		OutputStream os = null;
		URL url = null;
		URLConnection conn = null;

		try {
			url = new URL(_url);
			conn = url.openConnection();
			conn.setConnectTimeout(1000 * 30); // 连接超时：30秒
			conn.setReadTimeout(1000 * 60); // 读取超时：1分钟
			conn.connect();

			is = conn.getInputStream();
			os = new FileOutputStream(destPath);

			int length;
			byte[] b = new byte[1024];
			while (-1 != (length = is.read(b))) {
				os.write(b, 0, length);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

}
