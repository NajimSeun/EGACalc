package com.mpit.pristine;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class EGAtDialogPickerFragment extends DialogFragment implements
		OnDateSetListener {

	private String dialogTitle = "Select Date";
	private String requiredCal = null;
	private String from = null;

	@Override
	public Dialog onCreateDialog(Bundle savedInstance) {
		Bundle b = this.getArguments();
		this.requiredCal = b.getString("req");
		this.dialogTitle = b.getString("title");
		this.from = b.getString("from");
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		final DatePickerDialog dpd = new DatePickerDialog(getActivity(), this,
				year, month, day);
		dpd.setTitle(dialogTitle);
		String cancle = this.getActivity().getResources()
				.getString(R.string.close);
		dpd.setButton(DialogInterface.BUTTON_NEGATIVE, cancle,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dpd.cancel();

					}
				});
		String ok = getActivity().getResources().getString(R.string.ok);
		dpd.setButton(DialogInterface.BUTTON_POSITIVE, ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						DatePicker dp = dpd.getDatePicker();
						int year = dp.getYear();
						int month = dp.getMonth();
						int dayOfMonth = dp.getDayOfMonth() - 1;
                      
					}
				});

		return dpd;

	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) { 

	}

	public void myDateSetListener(DatePicker dp, int year, int monthOfYear,
			int dayOfMonth) {

		Calendar selectedDate = Calendar.getInstance();
		selectedDate.set(year, monthOfYear, dayOfMonth);

		Calendar now = Calendar.getInstance();
		Calendar today = this.resetCalendar(now);
		if (this.from.equals(Constants.FLMP)){
			if (today.compareTo(selectedDate) == 0
				|| today.compareTo(selectedDate) == -1) {
			Context context = getActivity().getApplicationContext();
			String wrongLMP = getActivity().getResources().getString(
					R.string.wrongLMP);
			Toast.makeText(context, wrongLMP, Toast.LENGTH_LONG).show();
			return;
		}
			if (this.requiredCal.equals(Constants.EGA)) {
				this.displayEGA(this.convertMSToWeeks(this
						.calculateEGAInMS(selectedDate)));

			} else if (this.requiredCal.equals(Constants.EDD)) {
				Calendar edd = this.calculateEDDFROMLMPNaegele(selectedDate);
				this.displayEDD(edd);
			}
			
		}
		
		else if (this.from.equals(Constants.FUSS)){
			this.displayEGA(this.convertMSToWeeks(this.calculateEGAFromUSS(selectedDate)));
			}
			
		

		
	}

	private void displayEGA(Map <String,String> map) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		ViewGroup vg = (ViewGroup) getActivity().findViewById(
				R.id.popup_root_ega);
		View v = inflater.inflate(R.layout.output_popup_view_ega, vg);
		TextView weeks = (TextView) getActivity().findViewById(R.id.out_pu_tv_ega_week_val);
		TextView days  = (TextView) getActivity().findViewById(R.id.out_pu_tv_ega_day_val);
		weeks.setText(map.get("weeks"));
		days.setText(map.get("days"));
		builder.setTitle((this.requiredCal +" " + this.from));
		builder.setView(v);
		final Dialog dlg = builder.create();
		builder.setPositiveButton(R.id.out_ok_but,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dlg.dismiss();

					}
				});
		dlg.show();
	}

	public void displayEDD(Calendar edd) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		ViewGroup vg = (ViewGroup) getActivity().findViewById(
				R.id.popup_root_edd);
		View v = inflater.inflate(R.layout.output_popup_view_edd, vg);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle((this.requiredCal +" " + this.from));
		builder.setView(v);
		TextView year =  (TextView) getActivity().findViewById(R.id.year);
		TextView month =  (TextView) getActivity().findViewById(R.id.month);
		TextView day_of_month =  (TextView) getActivity().findViewById(R.id.day);
		year.setText(String.valueOf(edd.get(Calendar.YEAR)));
		month.setText(String.valueOf(((edd.get(Calendar.MONTH)) + 1)));
		day_of_month.setText(String.valueOf(edd.get(Calendar.DAY_OF_MONTH)));
		final Dialog dlg = builder.create();
		builder.setPositiveButton(R.id.out_ok_but,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dlg.dismiss();

					}
				});
		dlg.show();
	}

	public String getRequiredCal() {
		return requiredCal;
	}

	public void setRequiredCal(String requiredCal) {
		this.requiredCal = requiredCal;
	}

	public long calculateEGAInMS(Calendar lmp) {
		Calendar now = Calendar.getInstance();
		Calendar today = this.resetCalendar(now);
		return today.getTimeInMillis() - lmp.getTimeInMillis();

	}

	public Calendar calculateEDDFROMLMPNaegele(Calendar lmp) {
		lmp.add(Calendar.DAY_OF_MONTH, 7);
		lmp.add(Calendar.MONTH, 9);
		return lmp;

	}

	private long calculateEGAFromUSS(Calendar ussEDD) { // please normalize
														// ussedd ensure ussedd
														// if today or after
		ussEDD.add(Calendar.DAY_OF_MONTH, -7);
		ussEDD.add(Calendar.MONTH, -9);
		return calculateEGAInMS(ussEDD);

	}

	private Map<String,String> convertMSToWeeks(long egaMS) {
		long weekInMS = 7 * 24 * 60 * 60 * 1000;
		long dayInMS = 1 * 24 * 60 * 60;
		long EGAWeeks = (egaMS / weekInMS);
		long EGADays = (egaMS % weekInMS) / dayInMS;
		 Map <String,String> map = new HashMap<String,String>();
		 map.put("weeks", String.valueOf(EGAWeeks));
		 map.put("days",String.valueOf(EGADays));
		return map ;

	}

	public Calendar resetCalendar(Calendar unset) {
		Calendar set = Calendar.getInstance();
		set.clear();
		set.set(Calendar.YEAR, unset.get(Calendar.YEAR));
		set.set(Calendar.MONTH, unset.get(Calendar.MONTH));
		set.set(Calendar.DAY_OF_MONTH, unset.get(Calendar.DAY_OF_MONTH));
		return set;
	}
}
