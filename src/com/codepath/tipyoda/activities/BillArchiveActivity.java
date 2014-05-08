package com.codepath.tipyoda.activities;

import java.util.ArrayList;
import java.util.List;

import com.codepath.tipcalculator.R;
import com.codepath.tipcalculator.R.id;
import com.codepath.tipcalculator.R.layout;
import com.codepath.tipyoda.adapters.BillAdapter;
import com.codepath.tipyoda.helpers.TipDatabaseHandler;
import com.codepath.tipyoda.models.BillDetails;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class BillArchiveActivity extends Activity{
	
	private TipDatabaseHandler dbHandler;
	private ListView lvBills;
	private ArrayList<BillDetails> sendToAdapter;
	private BillAdapter adapter;
	private ArrayList<BillDetails> allBills;
	
	final Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		dbHandler = new TipDatabaseHandler(this);
		
		lvBills = (ListView) findViewById(R.id.lvBills);
		
		sendToAdapter = new ArrayList<BillDetails>();
		sendToAdapter.add(0, null);
		allBills = dbHandler.getAllBills();
		sendToAdapter.addAll(allBills);
		adapter = new BillAdapter(this, sendToAdapter, dbHandler);
		lvBills.setAdapter(adapter);
		
		 setUpListViewListener();
		
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	setResult(RESULT_OK);
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	 private void setUpListViewListener() {
		 lvBills.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> aView, View item,
						int pos, long id) {
					if(pos != 0){
						BillDetails billItemToDelete = allBills.get(pos-1);
						sendToAdapter.remove(pos);
						adapter.notifyDataSetChanged();
						dbHandler.deleteContact(billItemToDelete);
					}
					//saveItems();
					return true;
				}
			});
		 
		lvBills.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> view, View item, int pos,
					long id) {
				// TODO Auto-generated method stub
				if(pos != 0){
					BillDetails bill = sendToAdapter.get(pos);
					final Dialog detailsDialog = new Dialog(context);
					detailsDialog.setContentView(R.layout.details_dialog);
					detailsDialog.setTitle("Bill Record for "+ bill.getDate());
					
					TextView tvBill = (TextView) detailsDialog.findViewById(R.id.tvDiaBill);
					TextView tvTipP = (TextView) detailsDialog.findViewById(R.id.tvDiaTipPercent);
					TextView tvSplit = (TextView) detailsDialog.findViewById(R.id.tvDiaSplit);
					TextView tvTipAmt = (TextView) detailsDialog.findViewById(R.id.tvDiaTipAmt);
					TextView tvBillAmt = (TextView) detailsDialog.findViewById(R.id.tvDiaBillAmt);
					TextView tvPp = (TextView) detailsDialog.findViewById(R.id.tvDiaPPAmt);
					
					tvBill.setText("Bill : $"+bill.getBill());
					tvTipP.setText("Tip % : "+ bill.getTipPercent());
					tvSplit.setText("Split among : "+ bill.getPeople());
					tvTipAmt.setText("Tip Amount : $"+ bill.getTipAmount());
					tvBillAmt.setText("Total Bill : $"+ bill.getBillAmount());
					if(bill.getPeople() > 1){
						tvPp.setText("Per head : $"+ bill.getAmtPerPerson());
					}else{
						tvPp.setVisibility(View.GONE);
					}
					
					Button dialogButton = (Button) detailsDialog.findViewById(R.id.dialogButtonOK);
					// if button is clicked, close the custom dialog
					dialogButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							detailsDialog.dismiss();
						}
					});
		 
					detailsDialog.show();
				}
				
				
			}
		});
	 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.tip, menu);
		//return true;
		return super.onCreateOptionsMenu(menu);
	}

}
