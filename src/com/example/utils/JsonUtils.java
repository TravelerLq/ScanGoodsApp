package com.example.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

	public static List<String> list = new ArrayList<String>();

	/* ����������JSON��ʽ���� */
	public static void parseJSONWithJSONObject(String jsondata)
			 {
		try {
			list.clear();
			JSONArray jsonArray = new JSONArray(jsondata);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String name = jsonObject.getString("name");
				list.add(name);
				// System.out.println(DataUtils.list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/* ����������JSON��ʽ���� */
	public static JSONObject buildJSONObject(String name, List<String> parent,
			Map<String, List<String>> map) {
		JSONObject object = new JSONObject();// ����
		JSONArray jsonArray1 = new JSONArray(); // produce��array

		for (int i = 0; i < parent.size(); i++) {
			JSONObject obj = new JSONObject();
			JSONArray jsonArraysn = new JSONArray();
			String device = parent.get(i);
			try {
				List<String> snlist = map.get(device);

				for (String s : snlist) {
					jsonArraysn.put(s);
				}
				obj.put("device", device);
				obj.put("sn", jsonArraysn);
				jsonArray1.put(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		try {
			object.put("name", name);
			object.put("produce", jsonArray1);
		} catch (JSONException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}

		return object;

	}
}
