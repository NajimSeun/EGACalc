package com.mpit.pristine;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class EGAtMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.egat_main);
		Button cegaBut = (Button)findViewById(R.id.cega);
		Button ceddBut = (Button)findViewById(R.id.cedd);
		Button cegaUssBut = (Button)findViewById(R.id.cega_uss);
		Button about = (Button)findViewById(R.id.)
		cegaBut.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			EGAtDialogPickerFragment frag =	new EGAtDialogPickerFragment();
			Bundle bundle = new Bundle();
			bundle.putString("title", (Constants.CALC + "  " + Constants.EGA + "  " + Constants.FLMP));
			bundle.putString("req", Constants.EGA);
			bundle.putString("from", Constants.FLMP);
		    frag.setArguments(bundle);
		    String fragTag = Constants.EGA + "tag" ;
		    
		    frag.show(frag.getFragmentManager(),fragTag );
			}
		});
		
		ceddBut.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				EGAtDialogPickerFragment frag =	new EGAtDialogPickerFragment();
				Bundle bundle = new Bundle();
				bundle.putString("title", (Constants.CALC + "  " + Constants.EDD + "  " + Constants.FLMP));
				bundle.putString("req", Constants.EDD);
				bundle.putString("from", Constants.FLMP);
			    frag.setArguments(bundle);
			    String fragTag = Constants.EDD + "tag" ;
			    
			    frag.show(frag.getFragmentManager(),fragTag );
				
			}});
		
		cegaUssBut.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EGAtDialogPickerFragment frag =	new EGAtDialogPickerFragment();
				Bundle bundle = new Bundle();
				bundle.putString("title", (Constants.CALC + "  " + Constants.EGA + "  " + Constants.FUSS));
				bundle.putString("req", Constants.EGA);
				bundle.putString("from", Constants.FUSS);
			    frag.setArguments(bundle);
			    String fragTag = Constants.EGA + Constants.FUSS + "tag" ;
			    
			    frag.show(frag.getFragmentManager(),fragTag );
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.egat_main, menu);
		return true;
	}

}
