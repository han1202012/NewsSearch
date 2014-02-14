package shuliang.han.newssearch;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SearchResultActivity extends Activity {

	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//���ò����ļ�
		setContentView(R.layout.news_search_result);
		//��ʼ�����
		listView = (ListView) findViewById(R.id.lv_search_result);
		//��ȡ��ת����Activity��intent����
		Intent intent = getIntent();
		//��ȡIntent������Я��������
		Bundle bundle = intent.getExtras();
		//��Bundle��ȡ��List<Map<String,String>>����
		@SuppressWarnings("unchecked")
		List<Map<String, String>> list = (List<Map<String, String>>)bundle.getSerializable("news");
		
		SimpleAdapter adapter = new SimpleAdapter(
				getApplicationContext(), 	//�����Ķ���
				list, 						//����Դ
				R.layout.item, 				//List��ʾ����
				new String[]{"tittle", "content"}, //List��map�ļ�ֵ
				new int[]{R.id.tittle, R.id.content});	//��䵽�Ĳ����ļ�
		
		listView.setAdapter(adapter);
	}
	
}
