package com.javautils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

	/**
	 * 通用导出
	 * 
	 * @param list
	 *            要导出的对象列表
	 * @param type
	 *            VO类型
	 * @param title
	 *            文件总标题
	 * @param titles
	 *            文件中文标题
	 * @param isXLSX
	 *            true:创建xlsx格式的文件，false:创建xls格式的文件
	 * @return
	 * @throws Exception
	 */
	public static <T> Workbook genWorkbookByVO(List<T> list, Class<T> type,
			String titleName, String sheetName, String[] titles, boolean isXLSX)
			throws Exception {
		// check VO对象列表
		if (list == null) {
			throw new RuntimeException("导出的对象列表为null");
		}

		// 创建Workbook和Sheet
		Workbook wb = createWorkbook(isXLSX);
		Sheet sheet = createSheet(wb, sheetName);

		// 创建将要使用的一些局部变量
		Field[] fields = type.getDeclaredFields();
		int rowIndex = 0;
		Row row = null;
		Cell cell = null;

		// 文件总标题
		if (StringUtils.isNotBlank(titleName)) {
			row = sheet.createRow(rowIndex++);
			cell = row.createCell(0);
			cell.setCellValue(titleName);
			// 设置字体
			setFont(16, true, true, null, cell, wb);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,
					fields.length - 1)); // 合并单元格
		}

		// 填充标题中文名
		if (titles != null && titles.length > 0) {
			row = sheet.createRow(rowIndex++);
			for (int i = 0; i < titles.length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(titles[i]);
				setFont(0, false, true, null, cell, wb);
			}
		}

		// 填充VO属性名
		row = sheet.createRow(rowIndex++);
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			cell = row.createCell(i);
			cell.setCellValue(fields[i].getName());
			setFont(0, false, true, null, cell, wb);
		}

		// 将每个对象填充到每一行
		for (T t : list) {
			row = sheet.createRow(rowIndex++);
			fillCellByVO(fields, t, row, wb);
		}

		// 调整列宽，使其自适应
		for (int i = 0; i < fields.length; i++) {
			sheet.autoSizeColumn(i);
		}

		return wb;
	}

	/**
	 * 将VO对象的各种属性值填充到cell
	 * 
	 * @param <T>
	 * @param fields
	 *            将要填充的属性数组
	 * @param t
	 *            将要填充的对象
	 * @param row
	 *            要将属性填充到的Row
	 * @param wb
	 *            Workbook对象
	 * @throws Exception
	 */
	private static <T> void fillCellByVO(Field[] fields, T t, Row row,
			Workbook wb) throws Exception {
		Cell cell = null;
		for (int i = 0; i < fields.length; i++) {
			@SuppressWarnings("rawtypes")
			Class c = fields[i].getType(); // 属性类型
			Object value = fields[i].get(t); // 属性值
			// excel只会识别6种类型，常用的有4种boolean,double, String, date
			// byte,short,int,long,float,double在cell.setCellValue()时都作为Double处理
			if (c == Byte.class) {
				row.createCell(i).setCellValue((Integer) value);
			} else if (c == Short.class) {
				row.createCell(i).setCellValue((Integer) value);
			} else if (c == Integer.TYPE) {
				row.createCell(i).setCellValue((Integer) value);
			} else if (c == Long.TYPE) {
				row.createCell(i).setCellValue((Long) value);
			} else if (c == Float.TYPE) {
				cell = row.createCell(i);
				cell.setCellValue((Float) value);
				setFont(0, false, false, "0.00", cell, wb);
			} else if (c == Double.TYPE) {
				cell = row.createCell(i);
				cell.setCellValue((Double) value);
				setFont(0, false, false, "0.00", cell, wb);
			} else if (c == Character.TYPE) {
				row.createCell(i).setCellValue((Integer) value);
			} else if (c == Boolean.TYPE) {
				row.createCell(i).setCellValue((Boolean) value);
			} else if (c == Date.class) {
				cell = row.createCell(i);
				cell.setCellValue((Date) fields[i].get(t));
				setFont(0, false, false, "yyyy-MM-dd HH:mm:ss", cell, wb);
			} else {
				row.createCell(i).setCellValue((String) value);
			}
		}
	}

	/**
	 * 设置单元格字体
	 * 
	 * @param fontPoint
	 *            字符大小（例：16）
	 * @param isBold
	 *            是否加粗
	 * @param isCenter
	 *            是否居中
	 * @param formatPattern
	 *            格式化模式（例如：0.00保留两位小数，yyyy-MM-dd格式化时间）
	 * @param cell
	 *            要设置的单元格
	 * @param wb
	 *            要设置的WorkBook对象
	 * @return
	 */
	public static void setFont(int fontPoint, boolean isBold, boolean isCenter,
			String formatPattern, Cell cell, Workbook wb) {
		CellStyle cs = wb.createCellStyle();
		Font font = wb.createFont();
		if (isBold) { // 加粗
			font.setBold(true);
		}
		if (fontPoint != 0) { // 字号
			font.setFontHeightInPoints((short) 16);
		}
		cs.setFont(font);

		if (isCenter) { // 居中
			cs.setAlignment(CellStyle.ALIGN_CENTER);
		}
		if (formatPattern != null && !"".equals(formatPattern)) { // 格式模式
			DataFormat df = wb.createDataFormat();
			short format = df.getFormat(formatPattern);
			cs.setDataFormat(format);
		}
		cell.setCellStyle(cs);
	}

	/**
	 * 创建Workbook对象
	 * 
	 * @param isXLSX
	 *            true:创建excel2007以上格式的excel,后缀名为xlsx,false:创建xls格式的Workbook
	 * @return
	 */
	public static Workbook createWorkbook(boolean isXLSX) {
		Workbook wb = null;
		if (isXLSX == true) {
			wb = new XSSFWorkbook();
		} else {
			wb = new HSSFWorkbook();
		}
		return wb;
	}

	/**
	 * 创建Sheet
	 * 
	 * @param wb
	 *            Workbook对象
	 * @param name
	 *            Sheet名称，如果不指定则Sheet以Sheet的默认规则命名
	 * @return
	 */
	public static Sheet createSheet(Workbook wb, String name) {
		Sheet sheet = null;
		if (StringUtils.isNotBlank(name)) {
			sheet = wb.createSheet(name);
		} else {
			sheet = wb.createSheet();
		}
		return sheet;
	}

}
