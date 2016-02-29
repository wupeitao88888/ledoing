/*******************************************************************************
 * Copyright (c) 2014 by ehoo Corporation all right reserved.
 * 2014-7-15 
 * 
 *******************************************************************************/
package cn.ledoing.db;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import cn.ledoing.bean.IsPraise;
import cn.ledoing.utils.L;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2014-7-15
 * 编写人员:	 吴培涛
 * 
 * 历史记录
 * 1、修改日期:
 *    修改人:
 *    修改内容:
 * </pre>
 */
public class Dbhelper {
	@SuppressWarnings("unused")
	private LCDbHelper contrastedDbHelper;

	public Dbhelper(LCDbHelper ContrastedDbHelper) {
		super();
		// 20141124 修改构造方法
		// this.contrastedDbHelper = ContrastedDbHelper;
		DatabaseManager.initializeInstance(ContrastedDbHelper);
	}

	public void add(IsPraise praise) throws Exception{



		SQLiteDatabase writableDatabase = DatabaseManager.getInstance()
				.openDatabase();
		writableDatabase
				.execSQL(
						"insert into ispraise(groupsid,prise,userid)values(?,?,?)",
						new Object[] { praise.getGroupsid(),praise.getPrise(),praise.getUserid() });

		// 20141124注
//		writableDatabase.close();
	}
	public boolean checkedPraise(IsPraise praise) {


        L.e(praise.getGroupsid()+"/"+praise.getUserid());
		/*
		 * 20141124改 SQLiteDatabase readableDatabase = contrastedDbHelper
		 * .getReadableDatabase();
		 */
		SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
				.readDatabase();
		Cursor cursor = readableDatabase.rawQuery(
				"select * from ispraise where groupsid=? and userid=?",
				new String[] { praise.getGroupsid(),praise.getUserid() });

		boolean b = false;
		while (cursor.moveToNext()) {
			String uid = null;
            String groupsid=null;
			uid = cursor.getString(cursor.getColumnIndex("userid"));
            groupsid = cursor.getString(cursor.getColumnIndex("groupsid"));
            L.e(uid+"/"+groupsid+"++++++"+praise.getGroupsid()+"/"+praise.getUserid());
            if (uid != null) {
				b = true;
				break;
			} else {
				b = false;
				break;
			}
		}
		// 20141124注
		// readableDatabase.close();
		cursor.close();

		return b;
	}


    //	public int selectChat() {
//
		/*
		 * 20141124改 SQLiteDatabase readableDatabase = contrastedDbHelper
		 * .getReadableDatabase();
		 */
//		SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
//				.readDatabase();
//		Cursor cursor = readableDatabase.rawQuery("select * from listchat",
//				new String[] {});
//		int h = 0;
//		// int g = 0;
//		while (cursor.moveToNext()) {
//
//			h += cursor.getInt(cursor.getColumnIndex("total"));
//			// g = g + h;
//		}
//
//		// 20141124注
//		// readableDatabase.close();
//		cursor.close();
//		return h;
//
//
//	public List<ChatMsgEntity> select(String kfid, String uid) {
//
//		/*
//		 * 20141124 修改 SQLiteDatabase readableDatabase = contrastedDbHelper
//		 * .getReadableDatabase();
//		 */
//		SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
//				.readDatabase();
//		Cursor cursor = readableDatabase
//				.rawQuery(
//						"select * from listservice where settingid=? and userid=? order by mdate asc",
//						new String[] { kfid, uid });
//		List<ChatMsgEntity> list = new ArrayList<ChatMsgEntity>();
//		while (cursor.moveToNext()) {
//			String siteid = cursor.getString(cursor.getColumnIndex("siteid"));
//			String sellerid = cursor.getString(cursor
//					.getColumnIndex("sellerid"));
//			String userid = cursor.getString(cursor.getColumnIndex("userid"));
//			String msgtype = cursor.getString(cursor.getColumnIndex("msgtype"));
//			String settingid = cursor.getString(cursor
//					.getColumnIndex("settingid"));
//			String url = cursor.getString(cursor.getColumnIndex("url"));
//			String sourceurl = cursor.getString(cursor
//					.getColumnIndex("sourceurl"));
//			// int position = cursor.getInt(cursor.getColumnIndex("format"));
//
//			String format = cursor.getString(cursor.getColumnIndex("format"));
//
//			String voiceurl = cursor.getString(cursor
//					.getColumnIndex("voiceurl"));
//			String length = cursor.getString(cursor.getColumnIndex("length"));
//			String remark = cursor.getString(cursor.getColumnIndex("remark"));
//			String msgid = cursor.getString(cursor.getColumnIndex("msgid"));
//			String sendstatus = cursor.getString(cursor
//					.getColumnIndex("sendstatus"));
//			String username = cursor.getString(cursor
//					.getColumnIndex("username"));
//			String group = cursor.getString(cursor.getColumnIndex("mgroup"));
//			String date = cursor.getString(cursor.getColumnIndex("mdate"));
//			String text = cursor.getString(cursor.getColumnIndex("text"));
//			String isComMeg = cursor.getString(cursor
//					.getColumnIndex("isComMeg"));
//			String localimage = cursor.getString(cursor
//					.getColumnIndex("localimage"));
//			String total = cursor.getString(cursor.getColumnIndex("total"));
//			ChatMsgEntity entity = new ChatMsgEntity(Long.parseLong(date),
//					text, isComMeg, siteid, sellerid, userid, msgtype,
//					settingid, url, sourceurl, format, voiceurl, length,
//					remark, msgid, sendstatus, username, group, total);
//			entity.setLocalimage(localimage);
//			list.add(entity);
//		}
//
//		// 20141124注
//		// readableDatabase.close();
//		cursor.close();
//		return list;
//	}
//
//	// 修改单个消息总数
//	public void update(String stingid, int q, String uid) {
//		// 20141124改
//		// SQLiteDatabase db = contrastedDbHelper.getWritableDatabase();
//		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//		ContentValues values = new ContentValues();
//		values.put("total", q);// key为字段名，value为值
//		db.update("listservice", values, "settingid=? and userid=?",
//				new String[] { stingid, uid });
//		// 20141124注
//		// db.close();
//	}
//
//	public int selectChat(String stingid) {
//
//		/*
//		 * 20141124改 SQLiteDatabase readableDatabase = contrastedDbHelper
//		 * .getReadableDatabase();
//		 */
//		SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
//				.readDatabase();
//		Cursor cursor = readableDatabase.rawQuery(
//				"select * from listservice where settingid=?",
//				new String[] { stingid });
//		int h = 0;
//		// int g = 0;
//		while (cursor.moveToNext()) {
//
//			h += cursor.getInt(cursor.getColumnIndex("total"));
//			// g = g + h;
//		}
//		// 20141124注
//		// readableDatabase.close();
//		cursor.close();
//		return h;
//
//	}
//
//	public boolean checkednewlist(String setid, String msgid) {
//
//		/*
//		 * 20141124改 SQLiteDatabase readableDatabase = contrastedDbHelper
//		 * .getReadableDatabase();
//		 */
//		SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
//				.readDatabase();
//		Cursor cursor = readableDatabase.rawQuery(
//				"select * from listservice where settingid=? and msgid=? ",
//				new String[] { setid, msgid });
//
//		String uid = null;
//		boolean b = true;
//		while (cursor.moveToLast()) {
//			uid = cursor.getString(cursor.getColumnIndex("msgid"));
//
//			if (uid != null) {
//
//				b = false;
//
//				break;
//			} else {
//				b = true;
//
//				break;
//			}
//		}
//		// 20141124注
//		// readableDatabase.close();
//		cursor.close();
//
//		return b;
//	}
//
//	// 以下是列表
//	public void add(ChatGrouplist nlistmsg) {
//		/*
//		 * 20141124改 SQLiteDatabase writableDatabase = contrastedDbHelper
//		 * .getWritableDatabase();
//		 */
//		SQLiteDatabase writableDatabase = DatabaseManager.getInstance()
//				.openDatabase();
//		writableDatabase
//				.execSQL(
//						"insert into listchat(siteid,sellerid,userid,msgtype,settingid,url,mgroup,mid,imageurl,currency,price,stock,category,name,username,total)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
//						new Object[] { nlistmsg.getSiteid(),
//								nlistmsg.getSellerid(), nlistmsg.getUserid(),
//								nlistmsg.getMsgtype(), nlistmsg.getSettingid(),
//								nlistmsg.getUrl(), nlistmsg.getMgroup(),
//								nlistmsg.getId(), nlistmsg.getImageurl(),
//								nlistmsg.getCurrency(), nlistmsg.getPrice(),
//								nlistmsg.getStock(), nlistmsg.getCategory(),
//								nlistmsg.getName(), nlistmsg.getUsername(),
//								nlistmsg.getTotal() });
//		// 20141124注
//		// writableDatabase.close();
//	}
//
//	public List<ChatGrouplist> selectChatGroup(String uid) {
//
//		/*
//		 * 20141124改 SQLiteDatabase readableDatabase = contrastedDbHelper
//		 * .getReadableDatabase();
//		 */
//		SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
//				.readDatabase();
//		Cursor cursor = readableDatabase.rawQuery(
//				"select * from listchat where userid=?", new String[] { uid });
//		List<ChatGrouplist> list = new ArrayList<ChatGrouplist>();
//		while (cursor.moveToNext()) {
//			String siteid = cursor.getString(cursor.getColumnIndex("siteid"));
//			String sellerid = cursor.getString(cursor
//					.getColumnIndex("sellerid"));
//			String userid = cursor.getString(cursor.getColumnIndex("userid"));
//			String msgtype = cursor.getString(cursor.getColumnIndex("msgtype"));
//			String settingid = cursor.getString(cursor
//					.getColumnIndex("settingid"));
//			String url = cursor.getString(cursor.getColumnIndex("url"));
//			String mgroup = cursor.getString(cursor.getColumnIndex("mgroup"));
//			String id = cursor.getString(cursor.getColumnIndex("mid"));
//			String imageurl = cursor.getString(cursor
//					.getColumnIndex("imageurl"));
//			String currency = cursor.getString(cursor
//					.getColumnIndex("currency"));
//			String price = cursor.getString(cursor.getColumnIndex("price"));
//			String category = cursor.getString(cursor
//					.getColumnIndex("category"));
//			String username = cursor.getString(cursor
//					.getColumnIndex("username"));
//			int stock = cursor.getInt(cursor.getColumnIndex("stock"));
//			String name = cursor.getString(cursor.getColumnIndex("name"));
//			int total = cursor.getInt(cursor.getColumnIndex("total"));
//			ChatGrouplist chatG = new ChatGrouplist(siteid, sellerid, userid,
//					msgtype, settingid, url, mgroup, total);
//			chatG.setId(id);
//			chatG.setImageurl(imageurl);
//			chatG.setPrice(price);
//			chatG.setCategory(category);
//			chatG.setCurrency(currency);
//			chatG.setStock(stock);
//			chatG.setName(name);
//			chatG.setUsername(username);
//			list.add(chatG);
//		}
//
//		// 20141124注
//		// readableDatabase.close();
//		cursor.close();
//		return list;
//	}
//
//	public boolean checkedchatG(String setid, String userid) {
//
//		/*
//		 * 20141124改 SQLiteDatabase readableDatabase = contrastedDbHelper
//		 * .getReadableDatabase();
//		 */
//		SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
//				.readDatabase();
//		Cursor cursor = readableDatabase.rawQuery(
//				"select * from listchat where settingid=? and userid=?",
//				new String[] { setid, userid });
//
//		boolean b = true;
//		while (cursor.moveToNext()) {
//			String uid = null;
//			uid = cursor.getString(cursor.getColumnIndex("settingid"));
//
//			if (uid != null) {
//				b = false;
//				break;
//			} else {
//				b = true;
//				break;
//			}
//		}
//		// 20141124注
//		// readableDatabase.close();
//		cursor.close();
//
//		return b;
//	}
//
//	// 修改单个消息总数
//	public void updatet(String userid, String stingid, int q) {
//		// 20141124改
//		// SQLiteDatabase db = contrastedDbHelper.getWritableDatabase();
//		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//		ContentValues values = new ContentValues();
//		values.put("total", q);// key为字段名，value为值
//		db.update("listchat", values, "settingid=? and userid=?", new String[] {
//				stingid, userid });
//		// 20141124注
//		// db.close();
//	}
//
//	public int selectChat() {
//
//		/*
//		 * 20141124改 SQLiteDatabase readableDatabase = contrastedDbHelper
//		 * .getReadableDatabase();
//		 */
//		SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
//				.readDatabase();
//		Cursor cursor = readableDatabase.rawQuery("select * from listchat",
//				new String[] {});
//		int h = 0;
//		// int g = 0;
//		while (cursor.moveToNext()) {
//
//			h += cursor.getInt(cursor.getColumnIndex("total"));
//			// g = g + h;
//		}
//
//		// 20141124注
//		// readableDatabase.close();
//		cursor.close();
//		return h;
//
//	}

}
