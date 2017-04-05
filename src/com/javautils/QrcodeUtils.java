package com.javautils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrcodeUtils {

	public static void createQrcode(String content) throws IOException, WriterException {
		QRCodeWriter writer = new QRCodeWriter();
		Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 230, 230, hints);
		FileOutputStream file = new FileOutputStream("D:/hehe.jpg");
		MatrixToImageWriter.writeToStream(matrix, "jpg", file);
		// 以下是将Matrix转换为BufferedImage
//		BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);
//		ImageIO.write(bufferedImage, "jpg", file);
	}

	public static void main(String[] args) throws IOException, WriterException {
		String content = new String("我是中国人，我为中国人自豪，不知道你是不是也为身为中国人自豪！");
		createQrcode(content);
		//createQrcode("I am Chinese, I am proud with China!");
	}
}
