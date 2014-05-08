package com.codepath.tipyoda.helpers;

import java.util.ArrayList;
import java.util.List;

import com.codepath.tipyoda.models.BillDetails;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class TipDatabaseHandler extends SQLiteOpenHelper {
		
	private Context ctx;
	 private static final int DATABASE_VERSION = 1;
	 
	    // Database Name
	    private static final String DATABASE_NAME = "tipYoda";
	 
	    // Bill Details table name
	    private static final String TABLE_BILL = "billDetails";
	 
	    // Bill Details Table Columns names
	    private static final String KEY_ID = "id";
	    private static final String KEY_DATE = "date";
	    private static final String KEY_BILL = "bill";
	    private static final String KEY_TIP = "tipPercent";
	    private static final String KEY_PEOPLE = "people";
	    private static final String KEY_TIP_AMT = "tipAmount";
	    private static final String KEY_BILL_AMT = "billAmount";
	    private static final String KEY_PER_PERSON = "amtPerPerson";
	    
	public TipDatabaseHandler(Context context) {
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.ctx = context;
		// TODO Auto-generated constructor stub
	}



	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_BILL_TABLE = "CREATE TABLE " + TABLE_BILL + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATE + 
				" TEXT,"+ KEY_BILL + " REAL," + KEY_TIP + " INTEGER," + KEY_PEOPLE + " INTEGER," + KEY_TIP_AMT 
				+ " REAL,"+ KEY_BILL_AMT + " REAL," + KEY_PER_PERSON + " REAL" + ")";
		
		try{
			db.execSQL(CREATE_BILL_TABLE);
			//Toast.makeText(ctx, "DB created",Toast.LENGTH_SHORT).show();
		}catch(SQLException ex){
			//Toast.makeText(ctx, "Exception :"+ ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
		}
	}



	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILL);
 
        // Create tables again
        onCreate(db);
		
	}
	
	public void addBillEntry(BillDetails bill){
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    
	    //values.put(KEY_ID, bill.getId());
	    values.put(KEY_DATE,bill.getDate());
	    values.put(KEY_BILL,bill.getBill());
	    values.put(KEY_TIP,bill.getTipPercent());
	    values.put(KEY_PEOPLE, bill.getPeople());
	    values.put(KEY_TIP_AMT,bill.getTipAmount());
	    values.put(KEY_BILL_AMT,bill.getBillAmount());
	    values.put(KEY_PER_PERSON,bill.getAmtPerPerson());
	    
	    db.insert(TABLE_BILL, null, values);
	    db.close();
	    Toast.makeText(ctx, "Bill saved to archive",Toast.LENGTH_SHORT).show();
	    
	}
	
	public int getBillsCount() {
//        String countQuery = "SELECT  * FROM " + TABLE_BILL;
//        SQLiteDatabase db = this.getWritableDatabase();
//        
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
		List<BillDetails> allBills = getAllBills();
		
        return allBills.size();
    }
	
	public ArrayList<BillDetails> getAllBills() {
	    ArrayList<BillDetails> billList = new ArrayList<BillDetails>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_BILL;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	BillDetails bill = new BillDetails();
	        	bill.setId(cursor.getInt(0));
	        	bill.setDate(cursor.getString(1));
	            bill.setBill(cursor.getDouble(2));
	            bill.setTipPercent(cursor.getInt(3));
	            bill.setPeople(cursor.getInt(4));
	            bill.setTipAmount(cursor.getDouble(5));
	            bill.setBillAmount(cursor.getDouble(6));
	            bill.setAmtPerPerson(cursor.getDouble(7));
	            // Adding contact to list
	            billList.add(bill);
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    return billList;
	}
	
	public void deleteContact(BillDetails bill) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_BILL, KEY_ID + " = ?",
	            new String[] { String.valueOf(bill.getId()) });
	    db.close();
	}
	
	public void deleteContact(int id) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    try{
	    	db.beginTransaction();
	    	 int del = db.delete(TABLE_BILL, KEY_ID + " = ?",
	 	            new String[] { String.valueOf(id) });
	    	 db.setTransactionSuccessful();
	    	 db.endTransaction();
	    	 //Toast.makeText(ctx, ""+del,Toast.LENGTH_SHORT).show();
	    }catch(Exception ex){
	    	//Toast.makeText(ctx, "Exception :"+ ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
	    }
	   
	    db.close();
	}

}
