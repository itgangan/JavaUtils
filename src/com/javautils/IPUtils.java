package com.javautils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class IPUtils {
	// =========================================================================
	// 通过本地IP库获取IP归属地信息
	private static String IP_DATA_PATH = "db/17monipdb.dat";
	private static DataInputStream inputStream = null;
	private static long fileLength = -1;
	private static int dataLength = -1;
	private static Map<String, String> cacheMap = null;
	private static byte[] allData = null;
	static {
		File file = new File(IP_DATA_PATH);
		try {
			inputStream = new DataInputStream(new FileInputStream(file));
			fileLength = file.length();
			cacheMap = new HashMap<String, String>();
			if (fileLength > Integer.MAX_VALUE) {
				throw new Exception("the filelength over 2GB");
			}
			dataLength = (int) fileLength;
			allData = new byte[dataLength];
			inputStream.read(allData, 0, dataLength);
			dataLength = (int) getbytesTolong(allData, 0, 4,
					ByteOrder.BIG_ENDIAN);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static long getbytesTolong(byte[] bytes, int offerSet, int size,
			ByteOrder byteOrder) {
		if ((offerSet + size) > bytes.length || size <= 0) {
			return -1;
		}
		byte[] b = new byte[size];
		for (int i = 0; i < b.length; i++) {
			b[i] = bytes[offerSet + i];
		}
		ByteBuffer byteBuffer = ByteBuffer.wrap(b);
		byteBuffer.order(byteOrder);
		long temp = -1;
		if (byteBuffer.hasRemaining()) {
			temp = byteBuffer.getInt();
		}
		return temp;
	}

	private static long ip2long(String ip) throws UnknownHostException {
		InetAddress address = InetAddress.getByName(ip);
		byte[] bytes = address.getAddress();
		long reslut = getbytesTolong(bytes, 0, 4, ByteOrder.BIG_ENDIAN);
		return reslut;
	}

	private static int getIntByBytes(byte[] b, int offSet) {
		if (b == null || (b.length < (offSet + 3))) {
			return -1;
		}
		byte[] bytes = Arrays.copyOfRange(allData, offSet, offSet + 3);
		byte[] bs = new byte[4];
		bs[3] = 0;
		for (int i = 0; i < 3; i++) {
			bs[i] = bytes[i];
		}
		return (int) getbytesTolong(bs, 0, 4, ByteOrder.LITTLE_ENDIAN);
	}

	/**
	 * 通过本地IP库获取IP归属地信息
	 * 
	 * @param address
	 *            点分十进制表示的IP（比如：118.112.58.221）
	 * @return
	 */
	public static String findGeography(String address) {
		if (StringUtils.isBlank(address)) {
			return "illegal address";
		}
		if (dataLength < 4 || allData == null) {
			return "illegal ip data";
		}
		String ip = "127.0.0.1";
		try {
			ip = Inet4Address.getByName(address).getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		String[] ipArray = StringUtils.split(ip, ".");
		int ipHeadValue = Integer.parseInt(ipArray[0]);
		if (ipArray.length != 4 || ipHeadValue < 0 || ipHeadValue > 255) {
			return "illegal ip";
		}
		if (cacheMap.containsKey(ip)) {
			return cacheMap.get(ip);
		}
		long numIp = 1;
		try {
			numIp = ip2long(address);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		int tempOffSet = ipHeadValue * 4 + 4;
		long start = getbytesTolong(allData, tempOffSet, 4,
				ByteOrder.LITTLE_ENDIAN);
		int max_len = dataLength - 1028;
		long resultOffSet = 0;
		int resultSize = 0;
		for (start = start * 8 + 1024; start < max_len; start += 8) {
			if (getbytesTolong(allData, (int) start + 4, 4,
					ByteOrder.BIG_ENDIAN) >= numIp) {
				resultOffSet = getIntByBytes(allData, (int) (start + 4 + 4));
				resultSize = (char) allData[(int) start + 7 + 4];
				break;
			}
		}
		if (resultOffSet <= 0) {
			return "resultOffSet too small";
		}
		byte[] add = Arrays.copyOfRange(allData, (int) (dataLength
				+ resultOffSet - 1024),
				(int) (dataLength + resultOffSet - 1024 + resultSize));
		try {
			if (add == null) {
				cacheMap.put(ip, new String("no data found!!"));
			} else {
				cacheMap.put(ip, new String(add, "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return cacheMap.get(ip);
	}

	// ================================================================================
	// IP相关

	/**
	 * 获取用户的IP
	 * 
	 * @param request
	 * @return 点分十进制表示的IP
	 */
	public static String getIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");

		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if (ip.equals("0:0:0:0:0:0:0:1")) {
				ip = "127.0.0.1";
			}
		}
		return ip;
	}

	/**
	 * 将十进制表示的IP转换成Long型表示
	 * 
	 * @param ipStr
	 *            点分十进制表示的IP（比如：118.112.58.221）
	 * @return long型表示的IP
	 */
	public static long StringIPToLong(String stringIP) {
		if (StringUtils.isBlank(stringIP)) {
			throw new IllegalArgumentException("ip不能为空！");
		}

		long[] ip = new long[4];
		String[] ipSplit = stringIP.split("[.]");
		for (int i = 0; i < ipSplit.length; i++) {
			ip[i] = Long.parseLong(ipSplit[i]);
		}

		long ipLong = (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3]; // ip1*256*256*256+ip2*256*256+ip3*256+ip4
		return ipLong;
	}

	/**
	 * 将Long型表示的IP转换成点分十进制表示
	 * 
	 * @param longIP
	 *            long型表示的IP
	 * @return 点分十进制表示的IP（比如：118.112.58.221）
	 */
	public static String LongIPToString(long longIP) {
		if (longIP == 0) {
			throw new IllegalArgumentException("ip不能为0！");
		}

		StringBuffer sb = new StringBuffer();
		sb.append(longIP >>> 24);// 右移24位
		sb.append(".");
		sb.append((longIP & 0x00FFFFFF) >>> 16); // 将高8位置0，然后右移16位
		sb.append(".");
		sb.append((longIP & 0x0000FFFF) >>> 8); // 将高16位置0，然后右移8位
		sb.append(".");
		sb.append(longIP & 0x000000FF); // 将高24位置0

		return sb.toString();
	}
	

	// ================================================================================
	// 通过网络地址库获取IP归属地信息
	// taobao
	private static final String TAOBAO_IP_URL = "http://ip.taobao.com/service/getIpInfo.php?ip=";

	/**
	 * 获取IP归属地信息
	 * 
	 * @param ip
	 *            点分十进制表示的IP
	 * @return 归属地信息json串(比如：{"code":0,"data":{"country":"","country_id"
	 *         :"","area":"","area_id":"","region": "" ,"region_id":"","city":""
	 *         ,"city_id":"" ,"county":"","county_id":"","isp":"","isp_id"
	 *         :"","ip":""}})
	 */
	public static String getAreaByIP(String stringIP) {
		String url = TAOBAO_IP_URL + stringIP;

		return FileUtils.getStringByURL(url);
	}

	/**
	 * 获取IP归属地信息
	 * 
	 * @param longIP
	 *            long型表示的IP
	 * @return 归属地信息json串
	 */
	public static String getAreaByIP(long longIP) {
		String stringIP = LongIPToString(longIP);

		return getAreaByIP(stringIP);
	}
	
	public static void main(String[] args) {
		System.out.println(LongIPToString(1969053278L));
		System.out.println(LongIPToString(1969050199L));
		System.out.println(LongIPToString(1928132016L));
	}

}
