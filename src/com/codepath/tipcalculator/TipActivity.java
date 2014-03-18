package com.codepath.tipcalculator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.codepath.database.BillDetails;
import com.codepath.database.TipDatabaseHandler;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class TipActivity extends Activity implements OnItemSelectedListener{

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * 
	 * Settinsg file - spinner limit, split default, tip% default, save or no default
	 */
	
	private double billAmount = 0;
	private int tipPercent = 0;
	private double tipAmount = 0;
	private int people = 1;
	private double totalBill = 0;
	private double perPerson = 0;
	private String roundOff = "none"; 
	
	private EditText bill;
	private SeekBar tipSeekbar;
	private SeekBar pplSeekbar;
	private TextView tipSeekVal;
	private TextView pplSeekVal;
	private Spinner round;
	private TextView tipAmt;
	private TextView billAmt;
	private TextView ppAmt;
	private TextView ppLabel;
	
	int defaultTip = 0;
	int defaultSplit = 0;
	String defaultRoundOff = "";
	boolean defaultSave = false;
	private final int REQUEST_CODE = 1;
	TipDatabaseHandler tipDB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip);
		tipDB = new TipDatabaseHandler(this);
		init();
		addRoundOffToSpinner();
		initFromSettings();
		setupBillAmtListener();
		setupSeekbars();
		
	}
	
	private void setupBillAmtListener() {
		// TODO Auto-generated method stub
		bill.addTextChangedListener(new TextWatcher() {

	        @Override
	        public void afterTextChanged(Editable s) {
	        }

	        @Override
	        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	        }

	        @Override
	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	        	if(s.toString().equals("")){
	        		billAmount = 0;
	        		clearCalculationValues();
	        	}else{
	        		billAmount = Double.parseDouble(s.toString());
		        	updateCalculation();
	        	}
	        }
	    });
		
	}

	private void setupSeekbars() {
		tipSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser){
				tipPercent = progress;
				tipSeekVal.setText(""+tipPercent);
				updateCalculation();
			 }
			public void onStartTrackingTouch(SeekBar seekBar){
			}
			public void onStopTrackingTouch(SeekBar seekBar){
			}
		});
		
		pplSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser){
				if(progress == 0 || progress ==1){
					progress = 1; // to set minimum split of 1
					ppAmt.setVisibility(View.GONE);
					ppLabel.setVisibility(View.GONE);
				}else{
					ppAmt.setVisibility(View.VISIBLE);
					ppLabel.setVisibility(View.VISIBLE);
				}
				people = progress;
				pplSeekVal.setText(""+people);
				updateCalculation();
			 }
			public void onStartTrackingTouch(SeekBar seekBar){
			}
			public void onStopTrackingTouch(SeekBar seekBar){
			}
		});
		
	}

	private void init(){
		bill = (EditText) findViewById(R.id.etBill);
		bill.setInputType(InputType.TYPE_CLASS_NUMBER 
                 | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		tipSeekbar = (SeekBar) findViewById(R.id.sbtip);
		pplSeekbar = (SeekBar) findViewById(R.id.sbPeople);
		tipSeekVal = (TextView) findViewById(R.id.tvPercent);
		pplSeekVal = (TextView) findViewById(R.id.tvPeople);
		round = (Spinner) findViewById(R.id.spRound);
		tipAmt = (TextView) findViewById(R.id.tvFinalTip);
		billAmt = (TextView) findViewById(R.id.tvFinalBill);
		ppAmt = (TextView) findViewById(R.id.tvFinalPP);
		ppLabel = (TextView) findViewById(R.id.tvPerPErson);
		
		ppAmt.setVisibility(View.GONE);
		ppLabel.setVisibility(View.GONE);
	}
	
	private void initFromSettings(){
		loadSettings();
		
		tipPercent = defaultTip;
		people = defaultSplit;
		
		tipSeekbar.setProgress(defaultTip);
		pplSeekbar.setProgress(defaultSplit);
		round.setSelection(getRoundOffPosition(defaultRoundOff));
		
		tipSeekVal.setText(""+tipPercent);
		pplSeekVal.setText(""+people);

	}
	
	private int getRoundOffPosition(String val){
		if(val.equals("Tip")){
			return 0;
		}
		if(val.equals("Total")){
			return 1;
		}
		if(val.equals("None")){
			return 2;
		}
		return 0;
	}

	private void addRoundOffToSpinner() {
		// TODO Auto-generated method stub
		List<String> roundOff = new ArrayList<String>();
		
		roundOff.add("Tip");
		roundOff.add("Total");
		roundOff.add("None");
		ArrayAdapter<String> tipAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roundOff);
		tipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		round.setAdapter(tipAdapter);
		
		addListenerOnSpinnerItemSelection();
	}

	private void addListenerOnSpinnerItemSelection() {
		Spinner tipSpinner = (Spinner) findViewById(R.id.spRound);
		tipSpinner.setOnItemSelectedListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tip, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            openSettings();
	            return true;
	            
	        case R.id.refresh:
	        	clear();
	        	initFromSettings();
	        	return true;
	        case R.id.save:
	        	saveToDB();
	        	return true;
	        case R.id.archieve:
	        	startArchiveActivity();
	        	return true;
	        case R.id.about:
	        	openAboutDialog();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void openAboutDialog() {
		final Dialog aboutDialog = new Dialog(this);
		aboutDialog.setContentView(R.layout.about);
		aboutDialog.setTitle("About TipYoda");
		
		String aboutApp = 	"Version 1.0.1" + System.getProperty ("line.separator")
							+ System.getProperty ("line.separator")
							+ System.getProperty ("line.separator")
							+ "What all this app can do :"+ System.getProperty ("line.separator")
							+ System.getProperty ("line.separator")
							+ "1. Calculate tip"+ System.getProperty ("line.separator")
							+ "2. Split bills"+ System.getProperty ("line.separator")
							+ "3. Round-off tip or total"+ System.getProperty ("line.separator")
							+ "4. Save time - set default tip, round off and split in Settings"+ System.getProperty ("line.separator")
							+ "5. Clear all, reset from settings - click on reset icon"+ System.getProperty ("line.separator")
							+ "6. Save your expenses - click the save icon"+ System.getProperty ("line.separator")
							+ "7. Check history - click on Folder icon"+ System.getProperty ("line.separator")
							+ "8. Single tap history item to view more details"+ System.getProperty ("line.separator")
							+ "9. Long press to delete history item"+ System.getProperty ("line.separator")
							+ ""+ System.getProperty ("line.separator")
							+ System.getProperty ("line.separator")
							+ "Coming Soon :"+System.getProperty ("line.separator")
							+ System.getProperty ("line.separator")
							+ "1. Store original receipt"+ System.getProperty ("line.separator")
							+ "2. Add restaurant name"+ System.getProperty ("line.separator")
							+ "3. Share expenses with friends"+ System.getProperty ("line.separator")
							+ System.getProperty ("line.separator")
							+ "Enjoy !!!";
		
		TextView tvABout = (TextView) aboutDialog.findViewById(R.id.tvAbout);
		tvABout.setText(aboutApp);
		
		aboutDialog.show();	
	}

	private void startArchiveActivity() {
		Intent archiveIntent = new Intent(TipActivity.this, BillArchiveActivity.class);
		startActivity(archiveIntent);
		
	}

	private void saveToDB() {
		if(billAmount == 0 && totalBill == 0){
			Toast.makeText(this, "Nothing to add",Toast.LENGTH_SHORT).show();
			return;
		}
		
		Time now = new Time();
		now.setToNow();
		String time = now.format3339(true);
		//Toast.makeText(this, "Time:"+ time,Toast.LENGTH_SHORT).show();
		BillDetails bill = new BillDetails(time, billAmount, tipPercent, people, tipAmount, totalBill, perPerson);
		tipDB.addBillEntry(bill);
		//int records = tipDB.getBillsCount();
		//Toast.makeText(this, "Records:"+ records,Toast.LENGTH_SHORT).show();
	}

	private void clear() {
		billAmount = 0;
		tipAmount = 0;
		totalBill = 0;
		perPerson = 0;
		bill.setText("");		
	}

	private void openSettings() {
		Intent setingIntent = new Intent(TipActivity.this, UserSettingActivity.class);
		startActivityForResult(setingIntent, REQUEST_CODE);
		
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      // REQUEST_CODE is defined above
      if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
         initFromSettings();
      }
    }
	
	private void loadSettings(){
		SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
		
		defaultTip = Integer.parseInt(sharedPrefs.getString("tipPref", "0"));
		defaultSplit = Integer.parseInt(sharedPrefs.getString("splitPref", "1"));
		defaultRoundOff = sharedPrefs.getString("roundOffPref", "None");
		defaultSave = sharedPrefs.getBoolean("savePref", false);
		//Toast.makeText(this, "Your choice :" + sharedPrefs.getString("tipPref", "default"),Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int pos,
			long arg3) {
		Spinner spin = (Spinner)parent;
        
        if(spin.getId() == R.id.spRound)
        {
            //Toast.makeText(this, "Your choice :" + parent.getItemAtPosition(pos).toString(),Toast.LENGTH_SHORT).show();
        	roundOff = parent.getItemAtPosition(pos).toString();
        	updateCalculation();
        	loadSettings();
        }
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	private void updateCalculation(){
		
		if(billAmount == 0){
			clearCalculationValues();
		}else{
			calculateTip();
			calculateTotal();
			if(people > 1){
				ppAmt.setVisibility(View.VISIBLE);
				ppLabel.setVisibility(View.VISIBLE);
			}
			calculatePerPerson();
			updateValuesOnUI();
		}
		
	}
	
	
	private void updateValuesOnUI() {
		tipAmt.setText(TipCalcConstants.DOLLAR_SIGN+tipAmount);
		billAmt.setText(TipCalcConstants.DOLLAR_SIGN+totalBill);
		ppAmt.setText(TipCalcConstants.DOLLAR_SIGN+perPerson);		
	}
	
	private void clearCalculationValues(){
		billAmount = 0;
		tipAmount = 0;
		totalBill = 0;
		perPerson = 0;
		tipAmt.setText("");
		billAmt.setText("");
		ppAmt.setText("");
	}

	private void calculateTip(){
		tipAmount = (billAmount * tipPercent) / 100;
		tipAmount = roundTo2Decimals(tipAmount);
		if(roundOff.equals("Tip")){
			tipAmount = Math.ceil(tipAmount);
		}
	}
	
	private void calculateTotal(){
		totalBill = billAmount + tipAmount;
		totalBill = roundTo2Decimals(totalBill);
		if(roundOff.equals("Total") && people ==1){
			totalBill = Math.ceil(totalBill);
		}
	}
	
	private void calculatePerPerson(){
		perPerson = totalBill / people;
		perPerson = roundTo2Decimals(perPerson);
		if(roundOff.equals("Total")){
			perPerson = Math.ceil(perPerson);
		}
		
	}
	
	double roundTo2Decimals(double val) {
        DecimalFormat df2 = new DecimalFormat("#####.##");
    return Double.valueOf(df2.format(val));
}

}
