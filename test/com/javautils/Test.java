package com.javautils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Test {
	public static void main(String[] args) throws IOException {
		String content = FileUtils.readFile("E://zones.txt");
		Set<String> allZones = getZones(content);
		//printZone(allZones);
		generateFile(allZones, "E://zones_formant.txt");
		
//		content = FileUtils.readFile("F://zones2.txt");
//		Set<String> iosZones = getZones(content);
	//	printZone(iosZones);
		
//		Set<String> andoridZones = getDeduct(allZones, iosZones);
//		printZone(andoridZones);
	}

	private static Set<String> getZones(String content) {
		Set<String> allZones = new HashSet<String>();

		JSONObject json = JSON.parseObject(content);
		JSONArray array = json.getJSONObject("data").getJSONArray("serverList");
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = array.getJSONObject(i);
			allZones.add(obj.getString("name"));
		}
		return allZones;
	}
	
	private static Set<String> getDeduct(Set<String> allZones, Set<String> willDeduct){
		Set<String> allZoneSnapshot = new HashSet<String>(allZones);
		Set<String> removeSet = new HashSet<String>();
		for (String zone : allZoneSnapshot){
			if (willDeduct.contains(zone)){
				removeSet.add(zone);
			}
		}
		allZoneSnapshot.removeAll(removeSet);
		return allZoneSnapshot;
	}
	
	private static void printZone(Set<String> zones){
		for (String zone : zones){
			System.out.println(zone);
		}
	}
	
	private static void generateFile(Set<String> zones, String destPath){
		StringBuilder sb = new StringBuilder();
		for (String zone : zones){
			sb.append(zone + "\r\n");
		}
		FileUtils.genFile(destPath, sb.toString());
		System.out.println("generate destPath success! size:" + zones.size());
	}

}
