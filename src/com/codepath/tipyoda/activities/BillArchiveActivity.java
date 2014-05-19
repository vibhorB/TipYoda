package com.codepath.tipyoda.activities;

import java.util.ArrayList;
import java.util.List;

import com.codepath.tipcalculator.R;
import com.codepath.tipcalculator.R.id;
import com.codepath.tipcalculator.R.layout;
import com.codepath.tipyoda.adapters.BillAdapter;
import com.codepath.tipyoda.helpers.TipDatabaseHandler;
import com.codepath.tipyoda.models.BillDetails;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
		//sendToAdapter.add(0, null);
		allBills = dbHandler.getAllBills();
		sendToAdapter.addAll(allBills);
		adapter = new BillAdapter(this, sendToAdapter, dbHandler);
		lvBills.setAdapter(adapter);
		
		 setUpListViewListener();
		Crouton.makeText(this, "Click on any item to view details", Style.INFO).show();
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
					//if(pos != 0){
					    confirmAndDeleteBill(pos);
					//}
					//saveItems();
					return true;
				}
			});
		 
		lvBills.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> view, View item, int pos,
					long id) {
				// TODO Auto-generated method stub
				//if(pos != 0){
					BillDetails bill = sendToAdapter.get(pos);
					final Dialog detailsDialog = new Dialog(context);
					detailsDialog.setContentView(R.layout.details_dialog);
					detailsDialog.setTitle("Bill Record for "+ bill.getBillName());
					
					TextView tvBill = (TextView) detailsDialog.findViewById(R.id.tvDiaBill);
					TextView tvTipP = (TextView) detailsDialog.findViewById(R.id.tvDiaTipPercent);
					TextView tvSplit = (TextView) detailsDialog.findViewById(R.id.tvDiaSplit);
					TextView tvTipAmt = (TextView) detailsDialog.findViewById(R.id.tvDiaTipAmt);
					TextView tvBillAmt = (TextView) detailsDialog.findViewById(R.id.tvDiaBillAmt);
					TextView tvPp = (TextView) detailsDialog.findViewById(R.id.tvDiaPPAmt);
					TextView tvBillName = (TextView) detailsDialog.findViewById(R.id.tvDiaBillName);
					
					tvBillName.setText("Bill Date :" + bill.getDate());
					tvBill.setText("Bill Amount : $"+bill.getBill());
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
				
				
			//}
		});
	 }
	 
	 private void confirmAndDeleteBill(final int pos){
	     new AlertDialog.Builder(this)
	     .setTitle("Delete")
	     .setMessage("Do you really want to delete?")
	     .setIcon(android.R.drawable.ic_dialog_alert)
	     .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

	         public void onClick(DialogInterface dialog, int whichButton) {
	             deleteBill(pos);
	         }})
	      .setNegativeButton(android.R.string.no, null).show();
	 }
	 
	 private void deleteBill(int pos){
	     BillDetails billItemToDelete = allBills.get(pos);
         sendToAdapter.remove(pos);
         adapter.notifyDataSetChanged();
         dbHandler.deleteContact(billItemToDelete);
         
         Crouton.makeText(this, "Record Deleted", Style.INFO).show();
	 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

}
