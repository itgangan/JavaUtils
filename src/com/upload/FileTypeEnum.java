package com.upload;

/**
 * 文件类型枚举
 * @author ganxiangyong
 * @date 2017-03-30
 */
public enum FileTypeEnum {
	// 文本
	TXT,
	// 图片
	JPG, JPEG, PNG, GIF, BMP,
	// excel
	XLS, XLSX,
	// 文档
	PDF, DOC,
	// 视
	RM, RMVB, AVI, MKV,
	// 音频
	MP3, WAV, WMA, FLV;

	public static FileTypeEnum getFileTypeEnum(String suffix) {
		for (FileTypeEnum f : FileTypeEnum.values()) {
			suffix = suffix == null ? "" : suffix.toUpperCase();
			if (f.name().equals(suffix)) {
				return f;
			}
		}
		return null;
	}
}
