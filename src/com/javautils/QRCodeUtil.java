package com.javautils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.swetake.util.Qrcode;

public class QRCodeUtil {

	// 二维码容量配置
	private static JSONObject capaticyConfig = null; // {version:{L,M,Q,H}...}
	static {
		String configStr = "{\"1\":[17,14,11,7],\"2\":[32,26,20,14],\"3\":[53,42,32,24],\"4\":[78,62,46,34],\"5\":[106,84,60,44],\"6\":[134,106,74,58],\"7\":[154,122,86,64],\"8\":[192,152,108,84],\"9\":[230,180,130,98],\"10\":[271,213,151,119],\"11\":[321,251,177,137],\"12\":[367,287,203,155],\"13\":[425,331,241,177],\"14\":[458,362,258,194],\"15\":[520,412,292,220],\"16\":[586,450,322,250],\"17\":[644,504,364,280],\"18\":[718,560,394,310],\"19\":[792,624,442,338],\"20\":[858,666,482,382],\"21\":[929,711,509,403],\"22\":[1003,779,565,439],\"23\":[1091,857,611,461],\"24\":[1171,911,661,511],\"25\":[1273,997,715,535],\"26\":[1367,1059,751,593],\"27\":[1465,1125,805,625],\"28\":[1528,1190,868,658],\"29\":[1628,1264,908,698],\"30\":[1732,1370,982,742],\"31\":[1840,1452,1030,790],\"32\":[1952,1538,1112,842],\"33\":[2068,1628,1168,898],\"34\":[2188,1722,1228,958],\"35\":[2303,1809,1283,983],\"36\":[2431,1911,1351,1051],\"37\":[2563,1989,1423,1093],\"38\":[2699,2099,1499,1139],\"39\":[2809,2213,1579,1219],\"40\":[2953,2331,1663,1273]}";
		capaticyConfig = JSON.parseObject(configStr);
	}

	/**
	 * 生成二维码
	 * 
	 * @param content
	 *            二维码中的内容
	 * @return
	 */
	public static BufferedImage createQRCode(String content) {
		return createQRCode(content, null, 7, 3, 3);
	}

	/**
	 * 生成带logo的二维码
	 * 
	 * @param content
	 *            二维码中的内容
	 * @param img
	 *            二维码中心的logo图片
	 * @return
	 */
	public static BufferedImage createQRCode(String content, Image img) {
		return createQRCode(content, img, 7, 6, 6);
	}

	/**
	 * 生成带logo的二维码
	 * 
	 * @param content
	 *            二维码中的内容
	 * @param img
	 *            二维码中心的logo图片
	 * @param version
	 *            二维码版本号(1-40)
	 * @param matrixSize
	 *            每一个矩阵点绘几个像素
	 * @param pixoff
	 *            设置二维码图片的白边宽度
	 * @return
	 */
	public static BufferedImage createQRCode(String content, Image img, int version, int matrixSize, int pixoff) {
		try {
			// 整个二维码的图片大小
			int qrcodeSize = ((21 + 4 * (version - 1))) * matrixSize + (pixoff * 2);
			Qrcode qrcodeHandler = new Qrcode();
			qrcodeHandler.setQrcodeErrorCorrect('M');
			qrcodeHandler.setQrcodeEncodeMode('B');
			qrcodeHandler.setQrcodeVersion(version);
			byte[] contentBytes = content.getBytes("UTF-8");
			BufferedImage bufImg = new BufferedImage(qrcodeSize, qrcodeSize, BufferedImage.TYPE_INT_RGB);
			Graphics2D gs = bufImg.createGraphics();
			gs.setBackground(Color.WHITE);
			gs.clearRect(0, 0, qrcodeSize, qrcodeSize);
			// 设定图像颜色 > BLACK
			gs.setColor(Color.BLACK);
			JSONArray jsonArray = capaticyConfig.getJSONArray(String.valueOf(version));
			int maxLength = jsonArray.getIntValue(1);
			// 输出内容 > 二维码
			if (contentBytes.length > 0 && contentBytes.length < maxLength) {
				boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
				for (int i = 0; i < codeOut.length; i++) {
					for (int j = 0; j < codeOut.length; j++) {
						if (codeOut[j][i]) {
							gs.fillRect(j * matrixSize + pixoff, i * matrixSize + pixoff, matrixSize, matrixSize);
						}
					}
				}
			} else {
				return null;
			}

			// 中间图标所在二维码中的位置坐标
			if (img != null) {
				int denominator = 4; // 最大也只能为整个二维码的几分之一
				int imgWidth = img.getWidth(null);
				int imgHeight = img.getHeight(null);
				imgWidth = imgWidth > qrcodeSize / denominator ? qrcodeSize / denominator : imgWidth;
				imgHeight = imgHeight > qrcodeSize / denominator ? qrcodeSize / denominator : imgHeight;
				int imgOffx = (qrcodeSize - imgWidth) / 2;
				int imgOffy = (qrcodeSize - imgHeight) / 2;
				gs.drawImage(img, imgOffx, imgOffy, imgWidth, imgHeight, null);
			}

			gs.dispose();
			bufImg.flush();

			// 生成二维码QRCode图片
			return bufImg;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) throws IOException {
		String str = "http://img.xunbao178.com/download/html5/20160303/t_2200000041_1092334_o1b9j6_555_559.jpg";
		URL url = new URL(str);
		Image img = ImageIO.read(url);
		ImageIO.write(createQRCode("http://www.baidu.com", img), "png", new File("F:/hehe.jpg"));
	}
}
