package shuliang.han.newssearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity {

	private NewsSearchDatabaseHelper helper;	//���ݿ������
	private EditText et_tittle;					//�������ű���
	private EditText et_content;				//������������
	private ListView listView;					//��ʾ�����б�
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		helper = new NewsSearchDatabaseHelper(getApplicationContext(), "news", 1);
		
		//��ʼ���ؼ�
		et_tittle = (EditText) findViewById(R.id.et_news_tittle);
		et_content = (EditText) findViewById(R.id.et_news_content);
		listView = (ListView) findViewById(R.id.lv_news);
		
	}

	/*
	 * ��ť����¼�
	 * ͨ���жϱ���������, ִ�в�ͬ�Ĳ���
	 */
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
			case R.id.bt_add_news:
				insertNews();
				break;
			case R.id.bt_query_news:
				queryNews();
				break;
			default:
				break;
		}
	}
	
	/*
	 * ������������
	 * 1. ��EditText����л�ȡ���ŵı��� �� ��������
	 * 2. ��ȡ���ݿⲢ�ӽ� ���ű��� �� ���� ���뵽���ݿ���
	 * 3. ���²�ѯ���ݿ� ���Cursor����
	 * 4. ����cursor���󴴽�SimpleCursorAdapter����
	 * 5. ��SimpleCursorAdapter���ø�ListView, ��ʾ�����б�
	 */
	private void insertNews() {
		String tittle = et_tittle.getText().toString();
		String content = et_content.getText().toString();
		
		helper.getReadableDatabase().execSQL("insert into news_table values(null, ?, ?)", 
				new String[]{tittle, content});
		
		Cursor cursor = helper.getReadableDatabase().rawQuery("select * from news_table", null);
		inflateListView(cursor);
	}
	
	/*
	 * ˢ�����ݿ��б���ʾ
	 * 1. ����SimpleCursorAdapter�����ݿ��, ��ȡ���ݿ���е���������
	 * 2. �����µ�SimpleCursorAdapter���ø�ListView
	 */
	private void inflateListView(Cursor cursor) {
		SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
				getApplicationContext(), 
				R.layout.item, 
				cursor, 
				new String[]{"news_tittle", "news_content"}, 
				new int[]{R.id.tittle, R.id.content});
		
		listView.setAdapter(cursorAdapter);
	}
	
	/*
	 * ��ѯ����
	 * 1. ��ȡҪ��ѯ�����ű��� �� ��������
	 * 2. ��ѯ���ݿ� ��ȡ Cursor, ����Cursorת��ΪList<Map<String, String>>���͵ļ���
	 * 3. �����Ϸ���bundle, Intent������һ��Activity, ��bundle����intent����, ��תActivity
	 * 
	 */
	private void queryNews() {
		String tittle = et_tittle.getText().toString();
		String content = et_content.getText().toString();
		
		Cursor cursor = helper.getReadableDatabase().rawQuery(
				"select * from news_table where news_tittle like ? or news_content like ?", 
				new String[]{"%" + tittle + "%", "%" + content + "%"});
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("news", cursor2list(cursor));
		Intent intent = new Intent(this, SearchResultActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
		
		
	}
	
	/*
	 * ����һ��ArrayList����, ���������ÿ��Ԫ����һ��Map����, ÿ��Map����������Ԫ��
	 * ����Cursor���� : 
	 * 1. cursor��������ƶ�һ��; 
	 * 2. ����һ��HashMap����
	 * 3. ʹ�� cursor.getString(�б��)��ȡ������ĳ��ֵ, �����ֵ����map��
	 * 4. ��Map�������
	 */
	private ArrayList<Map<String, String>> cursor2list(Cursor cursor) {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String,String>>();
		
		//����Cursor
		while(cursor.moveToNext()){
			Map<String, String> map = new HashMap<String, String>();
			map.put("tittle", cursor.getString(1));
			map.put("content", cursor.getString(2));
			list.add(map);
		}
		return list;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//�ͷ����ݿ���Դ
		if(helper !=null)
			helper.close();
	}
}
