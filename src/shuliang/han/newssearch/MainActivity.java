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

	private NewsSearchDatabaseHelper helper;	//数据库帮助类
	private EditText et_tittle;					//输入新闻标题
	private EditText et_content;				//输入新闻内容
	private ListView listView;					//显示新闻列表
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		helper = new NewsSearchDatabaseHelper(getApplicationContext(), "news", 1);
		
		//初始化控件
		et_tittle = (EditText) findViewById(R.id.et_news_tittle);
		et_content = (EditText) findViewById(R.id.et_news_content);
		listView = (ListView) findViewById(R.id.lv_news);
		
	}

	/*
	 * 按钮点击事件
	 * 通过判断被点击的组件, 执行不同的操作
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
	 * 插入新闻数据
	 * 1. 从EditText组件中获取新闻的标题 和 新闻内容
	 * 2. 获取数据库并从将 新闻标题 和 内容 插入到数据库中
	 * 3. 重新查询数据库 获得Cursor对象
	 * 4. 根据cursor对象创建SimpleCursorAdapter对象
	 * 5. 将SimpleCursorAdapter设置给ListView, 显示新闻列表
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
	 * 刷新数据库列表显示
	 * 1. 关联SimpleCursorAdapter与数据库表, 获取数据库表中的最新数据
	 * 2. 将最新的SimpleCursorAdapter设置给ListView
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
	 * 查询新闻
	 * 1. 获取要查询的新闻标题 和 新闻内容
	 * 2. 查询数据库 获取 Cursor, 并将Cursor转化为List<Map<String, String>>类型的集合
	 * 3. 将集合放入bundle, Intent开启另一个Activity, 将bundle放入intent对象, 跳转Activity
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
	 * 返回一个ArrayList集合, 这个集合中每个元素是一个Map集合, 每个Map集合有两个元素
	 * 解析Cursor对象 : 
	 * 1. cursor光标向下移动一格; 
	 * 2. 创建一个HashMap对象
	 * 3. 使用 cursor.getString(列标号)获取该行中某列值, 将这个值放入map中
	 * 4. 将Map对象放入
	 */
	private ArrayList<Map<String, String>> cursor2list(Cursor cursor) {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String,String>>();
		
		//遍历Cursor
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
		//释放数据库资源
		if(helper !=null)
			helper.close();
	}
}
