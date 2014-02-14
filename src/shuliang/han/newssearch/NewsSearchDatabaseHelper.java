package shuliang.han.newssearch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsSearchDatabaseHelper extends SQLiteOpenHelper {

	final String SQL_CREATE_TABLE = "create table news_table (" +
			"_id integer primary key autoincrement, " +
			"news_tittle varchar(50), " +
			"news_content varchar(5000))";
	
	/*
	 * 构造方法 : 
	 * 参数介绍 : 
	 * 参数① : 上下文对象
	 * 参数② : 数据库名称
	 * 参数③ : 数据库版本号
	 */
	public NewsSearchDatabaseHelper(Context context, String name, int version) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("call update");
	}

}
