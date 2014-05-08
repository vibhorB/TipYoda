package com.codepath.tipyoda.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codepath.tipcalculator.R;
import com.codepath.tipcalculator.R.id;
import com.codepath.tipcalculator.R.layout;
import com.codepath.tipyoda.helpers.TipDatabaseHandler;
import com.codepath.tipyoda.models.BillDetails;

public class BillAdapter extends ArrayAdapter<BillDetails>{
	
	private TipDatabaseHandler dbHandler;

	public BillAdapter(Context context, ArrayList<BillDetails> bills, TipDatabaseHandler dbHandler) {
		super(context, R.layout.item_bill, bills);
		this.dbHandler = dbHandler;
		// TODO Auto-generated constructor stub
	}
	
	 @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	       // Get the data item for this position
		 final BillDetails bill = getItem(position);   
		 
	       // Check if an existing view is being reused, otherwise inflate the view
	       if (convertView == null) {
	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_bill, null);
	       }
	       // Lookup view for data population
	       TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
//	       TextView tvBill = (TextView) convertView.findViewById(R.id.tvBillList);
//	       TextView tvTip = (TextView) convertView.findViewById(R.id.tvTipList);
	       TextView tvPp = (TextView) convertView.findViewById(R.id.tvPerPersonList);
	       //ImageButton ibDel = (ImageButton) convertView.findViewById(R.id.btnRemove);
	       // Populate the data into the template view using the data object
	       
	       if(bill == null){
	    	   tvDate.setText("     Date   ");
	    	   //tvBill.setText("       Bill");
	    	   //tvTip.setText("    Tip   ");
	    	   tvPp.setText("   You Paid");
	    	   //ibDel.setVisibility(View.GONE);
	       }else{
	    	   tvDate.setText(bill.getDate());
		       //tvBill.setText(""+bill.getBill());
		       //tvTip.setText(""+bill.getTipAmount());
		       tvPp.setText(""+bill.getAmtPerPerson());
	       }
	       // Return the completed view to render on screen
	       return convertView;
	   }
}


