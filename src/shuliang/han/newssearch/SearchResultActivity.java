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
		
		//设置布局文件
		setContentView(R.layout.news_search_result);
		//初始化组件
		listView = (ListView) findViewById(R.id.lv_search_result);
		//获取跳转到该Activity的intent对象
		Intent intent = getIntent();
		//获取Intent对象所携带的数据
		Bundle bundle = intent.getExtras();
		//从Bundle中取出List<Map<String,String>>数据
		@SuppressWarnings("unchecked")
		List<Map<String, String>> list = (List<Map<String, String>>)bundle.getSerializable("news");
		
		SimpleAdapter adapter = new SimpleAdapter(
				getApplicationContext(), 	//上下文对象
				list, 						//数据源
				R.layout.item, 				//List显示布局
				new String[]{"tittle", "content"}, //List中map的键值
				new int[]{R.id.tittle, R.id.content});	//填充到的布局文件
		
		listView.setAdapter(adapter);
	}
	
}
