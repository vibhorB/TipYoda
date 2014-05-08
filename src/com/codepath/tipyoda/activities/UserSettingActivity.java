package com.codepath.tipyoda.activities;

import com.codepath.tipcalculator.R;
import com.codepath.tipcalculator.R.xml;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.KeyEvent;
import android.view.MenuItem;

public class UserSettingActivity extends PreferenceActivity {
	
	  @Override
      public void onCreate(Bundle savedInstanceState) 
      {
              super.onCreate(savedInstanceState);
              addPreferencesFromResource(R.xml.user_settings);
              getActionBar().setDisplayHomeAsUpEnabled(true);
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
	  
	  @Override
	  public void onBackPressed() {
		  setResult(RESULT_OK);
          this.finish();
	  }

}
