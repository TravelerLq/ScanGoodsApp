package com.example.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Data.GlobalConstants;
import com.example.Data.TempData;
import com.example.Receiver.MReceiver;
import com.example.myscanapp.R;
import com.example.utils.CharacterParser;
import com.example.utils.ClearEditText;
import com.example.utils.JsonUtils;
import com.example.utils.PinyinComparator;
import com.example.utils.SideBar;
import com.example.utils.SortAdapter;
import com.example.utils.SortModel;
import com.example.utils.SideBar.OnTouchingLetterChangedListener;

/**
 * ������ѡ�����
 * 
 * @author dell
 * 
 */
public class MainActivity extends Activity {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	private MReceiver mReceive;
	private ImageView refreshImageView;
	private Animation anim;
	private RequestQueue mQueue;

	/**
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * ����ƴ��������ListView�����������
	 */
	private PinyinComparator pinyinComparator;

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// ��������������������������������
				sendRequest();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initRefreshImage();
		anim = AnimationUtils.loadAnimation(MainActivity.this,
				R.anim.round_loading);
		mQueue = Volley.newRequestQueue(this);

		// ע���������״̬�ı�㲥������
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		mReceive = new MReceiver(this);
		registerReceiver(mReceive, intentFilter);
		
	}

	// ˢ����ť��ʼ��
	private void initRefreshImage() {

		refreshImageView = (ImageView) findViewById(R.id.refresh);
		refreshImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				refreshImageView.startAnimation(anim);
				sendRequest();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceive);
	}

	/**
	 * ������������(��������ģ������Դ,ԭ������Ϊע�ʹ���)
	 */
	private void sendRequest(){
		 String[] NAMES = new String[] { "�ν�", "¬����", "����",
			"����ʤ", "��ʤ", "�ֳ�", "����", "������", "����", "���", "��Ӧ", "����", "³����",
			"����", "��ƽ", "����", "��־", "����", "����", "����", "����", "����", "ʷ��", "�º�",
			"�׺�", "�", "��С��", "�ź�", "��С��", " ��˳", "��С��", "����", "ʯ��", "����",
			" �ⱦ", "����", "����", "����", "����", "����", "��˼��", "����", "��^", "��͢��",
			"κ����", "����", "����", "ŷ��", "�˷�", " ��˳", "����", "����", "����", "����",
			"�� ʢ", "����ȫ", "�ʸ���", "��Ӣ", "������", "����", "����", "����", "����", "���",
			"����", "����", "����", "ͯ��", "ͯ��", "�Ͽ�", "�", "�´�", "�", "֣����",
			"������", "����", "�ֺ�", "����", "������", "�´�", "����", "����", "��Ǩ", "Ѧ��", "ʩ��",
			 };
		 JSONArray array=new JSONArray();
		for(int i=0;i<NAMES.length;i++){
			JSONObject object = new JSONObject();
			try {
				object.put("name", NAMES[i]);
				array.put(object);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		System.out.println(array.toString());
		JsonUtils.parseJSONWithJSONObject(array.toString());
		refreshImageView.clearAnimation();
		refreshImageView.setVisibility(View.INVISIBLE);
		initViews();
		
		
		
	}
	/*private void sendRequest() {
		String url = GlobalConstants.Agency_URL;
		StringRequest stringRequest = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					// �ɹ���Ӧ
					public void onResponse(String response) {

						JsonUtils.parseJSONWithJSONObject(response);
						refreshImageView.clearAnimation();
						refreshImageView.setVisibility(View.INVISIBLE);
						initViews();

					}
				}, new Response.ErrorListener() {
					@Override
					// ��Ӧʧ��
					public void onErrorResponse(VolleyError error) {

						Toast.makeText(MainActivity.this, "��ȡ����ʧ��", 1).show();
						refreshImageView.setVisibility(View.VISIBLE);
						refreshImageView.clearAnimation();
					}
				});
		mQueue.add(stringRequest);
		
	}*/

	/**
	 * ��ʼ����ͼ
	 */
	private void initViews() {
		// ʵ��������תƴ����
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// �����Ҳഥ������
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// ����ĸ�״γ��ֵ�λ��
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		// ���õ���¼�
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���
				String name = ((SortModel) adapter.getItem(position)).getName();
				// �����ִ���TempData����
				TempData.name = name;
				Intent intent = new Intent(MainActivity.this,
						CaptureActivity.class);
				startActivity(intent);
				finish();
			}
		});
		// �õ�JsonUtils��ľ������б�����ת��Ϊ����
		String[] arr = JsonUtils.list
				.toArray(new String[JsonUtils.list.size()]);
		// ������Ϊ����Դ
		SourceDateList = filledData(arr);

		// ����a-z��������Դ����
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);

		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

		// �������������ֵ�ĸı�����������
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// ������������ֵΪ�գ�����Ϊԭ�����б�����Ϊ���������б�
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * ΪListView�������
	 * 
	 * @param date
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	private List<SortModel> filledData(String[] date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < date.length; i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			// ����ת����ƴ��
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	/**
	 * ����������е�ֵ���������ݲ�����ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}
		// ����a-z��������
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

}
