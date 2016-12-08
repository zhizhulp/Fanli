package com.ascba.rebate.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ascba.rebate.beans.City;

/**
 * 提供的工具类方法
 * 1.构造方法:名称,版本编号
 * 2.getReadableDatabase打开数据库
 * 3.onCreate:数据表的创建 (才会调用) ,以后添加新的表只能改版本号
 * 4.onUpGade:更新数据库的操作
 * 5.对数据表增删改查的封装:sql语句
 *   查询:rawquery(sql) Cursor转化成实际的List<JavaBean>
 * @author Jimmy
 *
 */
public class MySqliteOpenHelper extends SQLiteOpenHelper {
	//内部可以对数据库的名称和版本标号进行统一管理
	private static final String DB_NAME = "city.db";
	private static int DB_VERSION = 1;
	//定义全局的SQLiteDatabase
	private SQLiteDatabase db;
	
	/**
	 * SQListOpenHelper工具类的构造方法必须要复写
	 * 作用:创建数据库文件
	 * @param context 上下文 (和包名有关的数据存储都依赖于上下文)
	 * 给数据库起的名字(文件的名字db后缀名)
	 * 构建数据库的工厂类,影响数据库查询的返回结果(Cursor),对系统默认返回Cursor结果不满意,可以对CursorFactory进行处理
	 * 数据库的版本编号,目的以后通过版本编号可以更新数据库
	 */
	public MySqliteOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
		//连接的处理
		getConn();
	}
	
	/**
	 *  进行数据库的连接
	 */
	public void getConn(){
		//getWritableDatabase和getReadableDatabase
		//1.共同点:都是对数据库进行可读写的操作:
		//    a.如果第一次创建数据库,会调用onCreate和onOpen的方法
	    //    b.如果不是第一次创建数据库,直接调用onOpen的方法
		//    c.如果数据库版本发生改变的时候会调用onUpgrade和onOpen的方法
		//2.不同点:getWritableDatabase磁盘满的时候在录入数据会报错,直接throw抛错,数据会被加锁
		//        getReadableDatabase磁盘满的时候录入失败,直接结束 
		//db = getWritableDatabase(); //打开连接的方法
		db = getReadableDatabase(); //打开连接的方法 (推荐使用readable)
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
	}

	/**
	 * 创建数据库内数据表,执行创建表的语句
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//通过SQLiteDatabase工具类可以执行sql语句 
		//字符串填写sql语句就可以了 sql全称: structure query language (结构化查询语言)
		// 创建表的结构:create table if not exists 表名(字段名字 数据类型 限制条件)
		// sqlite中的支持的五种数据格式:integer(整型),text(文本类型),float(浮点类型),blob(二进制类型),null(空)
		// 如果字段的数据类型没写或写错默认的缺省类型就是text类型
		// 可以通过android的sdk工具中sqlite3.exe模拟数据库的操作(内存空间的操作)
		// 我们执行的sql语句是真是保存到db数据库文件中的
		String words=new String("create table if not exists city (city_id integer,city_name text,city_level integer,city_pid integer,city_initial text,city_cascade_id,city_cascade_name);");
		try {
			String sql=new String(words.getBytes(),"utf-8");
			db.execSQL(sql); //可以创建多张表
			//db.execSQL(sql2);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 对数据表的添加 (针对student表的操作)
	 */
	public void insertIntoCity(City city){
		//通过sql语句录入
		if(db != null){
			//怎么把student引入进来
			String sql = "insert into city ('city_id','city_name','city_level','city_pid','city_initial','city_cascade_id','city_cascade_name') values (?,?,?,?,?,?,?);";
			//db.execSQL(sql);
			//数据填写的数据的次序和?产生的先后次序必须要一一对应
			Object[] obj = new Object[]{city.getCityId(),city.getCityName(),city.getCityLevel(),city.getCityPid(),city.getCityInitial(),city.getCascade_id(),city.getCascade_name()};
			/**
			 * 第二个参数Object[]实际需要填写的值
			 */
			db.execSQL(sql, obj);
		}
	}
	/**
	 * 对数据表的查询 (针对student表的操作)
	 * 查询所有数据
	 * 不推荐把Cursor作为全局变量,操作起来麻烦.如果定义了全局,一定要注意位置的处理
	 */
	public List<City> getCity(){
		
		String sql = "select * from city;";
		//查新调用的方法
		//rawQuery中String[]的含义和execSQL中Object[]的作用一样
		//没有?就用null填写
		//Cursor就是返回结果,接口 cursor(中文翻译就是游标的意思)
		Cursor c = db.rawQuery(sql, null);  //c携带数据的结构和链表的类似
		//游标的概念.每次cursor移动以后,要知道cursor在什么位置
		if(c.getCount() == 0){ //没有数据的处理方式
			return null;
		}else {
			List<City> list = new ArrayList<City>();
			//不moveToFirst直接通过while移动cursor就可以了
			c.moveToFirst(); //移动到数据源的头部
			do {
				//获取内容 (先定数据类型,再定义字段名字)
				int cityId = c.getInt(c.getColumnIndex("city_id"));
				String cityName = c.getString(c.getColumnIndex("city_name"));
				int cityLevel = c.getInt(c.getColumnIndex("city_level"));
				int cityPid = c.getInt(c.getColumnIndex("city_pid"));
				String cityInitial = c.getString(c.getColumnIndex("city_initial"));
				String city_cascade_id = c.getString(c.getColumnIndex("city_cascade_id"));
				String city_cascade_name = c.getString(c.getColumnIndex("city_cascade_name"));
				City city = new City(cityId,cityName,cityLevel,cityPid,cityInitial,city_cascade_id,city_cascade_name);
				list.add(city);
			} while (c.moveToNext()); //获取数据后移动,移动到false就截数据了
			c.close();
			return list;
		}
		//db.execSQL(sql); //这个方法不可以使用,没有返回没有意义
	}

	
	/**
	 * 更新数据库(通过版本号的不同,执行数据库的更新)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if(newVersion > oldVersion){
			//添加表,删除表,删除数据等等都可以
//			String sql = "drop table words";
//			String sql3 = "drop database test";
//			String sql = "create table if not exists words (_id integer primary key autoincrement,word text, detail text);";
//			db.execSQL(sql);
//			db.execSQL(sql2);
		}
	}
	
	/**
	 * 关闭数据库的连接
	 */
	public void closeDB(){
		if(db != null){
			//关闭连接的方法
			db.close();
		}
	}

}
