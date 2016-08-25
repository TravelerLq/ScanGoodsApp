package com.example.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TempData {
	// ɨ�赽������sn
	public static ArrayList<String> templist = new ArrayList<String>();
	// ��������ļ�code.txt�еĺϷ�ǰ׺
	public static ArrayList<String> prelist = new ArrayList<String>();
	// ¼����Ʒ����
	public static int count = 0;
	// ����������
	public static String name;
	// ��ΪExpandableListView�е�Group
	public static List<String> parent = new ArrayList<String>();
	// ��ΪExpandableListView�е�Child
	public static Map<String, List<String>> map = new HashMap<String, List<String>>();
	
	// ��sn����
	public static void sort(String sn) {
		String shortsn="";
		for(String pre : prelist){
			if(sn.startsWith(pre)){
				shortsn = pre;
				break;
			}
		}
		

		if (!parent.contains(shortsn)) {
			parent.add(shortsn);
		}

		if (!map.keySet().contains(shortsn)) {
			List<String> childlist = new ArrayList<String>();
			childlist.add(sn);
			map.put(shortsn, childlist);
		} else {
			map.get(shortsn).add(sn);
		}

	}

	public static void add(String resultString) {
		TempData.count++;
		TempData.templist.add(resultString);
		TempData.sort(resultString);
	}

	// ɾ��һ̨����
	public static void deleteBySn(String s, int groupPosition, int childPosition) {
		templist.remove(s);
		count--;
		String key = parent.get(groupPosition);
		map.get(key).remove(childPosition);
		if (map.get(key).isEmpty()) {
			map.remove(key);
			parent.remove(key);
		}

	}

	// ɾ��ͬһ���ͺ�
	public static void deleteByType(String s) {
		Iterator<String> ittemp = templist.iterator();
		while (ittemp.hasNext()) {
			String e = ittemp.next();
			if (e.contains(s)) {
				ittemp.remove();
				count--;
			}
		}

		map.remove(s);
		parent.remove(s);
	}

	// �������
	public static void clean() {
		TempData.count = 0;
		TempData.map.clear();
		TempData.parent.clear();
		TempData.templist.clear();
		TempData.name = null;
	}

}
