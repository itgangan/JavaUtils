package com.upload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 通用文件上传
 * 
 * @author ganxiangyong
 * @date 2017-03-30
 *
 */
public abstract class FileUpload {

	// private static final Logger logger =
	// LoggerFactory.getLogger(AbstractFileUpload.class);

	/**
	 * 允许上传的文件类型
	 * 
	 * @return 默认允许所有的文件类型
	 */
	protected Set<FileTypeEnum> setPermitFileType() {
		return null;
	};

	/**
	 * 上传的文件大小
	 * 
	 * @return 默认不限制大小
	 */
	protected long setFileSizeMax() {
		return 0;
	}

	/**
	 * 文件要上传到服务器的位置
	 * 
	 * @param filename
	 *            上传原文件的名称
	 * @return
	 */
	protected abstract File destination(String filename);

	/**
	 * 通用的文件上传
	 * 
	 * @param request
	 */
	public void upload(HttpServletRequest request) {
		if (ServletFileUpload.isMultipartContent(request)) {
			DiskFileItemFactory factory = new DiskFileItemFactory();

			ServletFileUpload upload = new ServletFileUpload(factory);
			if (setFileSizeMax() > 0) {
				upload.setSizeMax(setFileSizeMax());
			}

			List<FileItem> items = new ArrayList<FileItem>();
			try {
				items = upload.parseRequest(request);
			} catch (FileUploadException e) {
				throw new RequestException("解析请求出错");
			}

			for (FileItem item : items) {
				if (item.isFormField()) {
					continue;
				}

				if (setFileSizeMax() > 0 && item.getSize() > setFileSizeMax()) {
					throw new RequestException("文件大小超过限制");
				}

				String filename = item.getName();
				if (setPermitFileType() != null) {
					String suffix = getSuffix(filename);
					FileTypeEnum fileType = FileTypeEnum.getFileTypeEnum(suffix);
					if (!setPermitFileType().contains(fileType)) {
						throw new FileTypeException("文件类型不合法:" + filename);
					}
				}

				File destination = destination(filename);
				if (destination == null) {
					throw new RuntimeException("文件目的地为null:" + filename);
				}

				File directory = destination.getParentFile();
				if (directory != null && !directory.exists()) {
					directory.mkdirs();
				}

				try {
					item.write(destination);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("文件上传失败:" + filename);
				}
			}

		} else {
			throw new RequestException("不是Multipart请求");
		}
	}

	protected String getSuffix(String fileName) {
		int index = fileName.lastIndexOf(".");
		if (index > -1) {
			return fileName.substring(index + 1);
		}
		return "";
	}
}
