package com.example.restimplementation.ui.send;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.archit.calendardaterangepicker.customviews.CalendarListener;
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.example.restimplementation.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SendFragment extends Fragment {
    String[] country = { "India", "USA", "China", "Japan", "Other"};
    Button button;
    String from_date_string;
    String to_date_string;
    Date from_date,to_date;
    CardView otherInfo;
    Spinner spinner_id;
    TextView datesTextView,daysTextView,sat_sunTextView,annualLeavesTextView;
private DateRangeCalendarView calendar;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_send, container, false);
        //Initialize calendar
        spinner_id=root.findViewById(R.id.spinner3);
        spinner_id.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this.getContext());

        calendar =  root.findViewById(R.id.calendar);
        button=root.findViewById(R.id.submitButtonId);
        otherInfo=root.findViewById(R.id.cd_otherInfo);
        datesTextView=root.findViewById(R.id.tv_dateSelected);
        daysTextView=root.findViewById(R.id.tv_dayNumber);
        sat_sunTextView=root.findViewById(R.id.tv_numberofNonWorkingDays);
        annualLeavesTextView=root.findViewById(R.id.tv_annualLeaves);




        final Date today=new Date();
        int month=Calendar.DAY_OF_MONTH;
        int start_month=month+1;
        start_month=start_month-2;
        final int end_month=14-month;

        Calendar startMonth = Calendar.getInstance();

        startMonth.add(Calendar.MONTH, -start_month);
        Calendar endMonth = Calendar.getInstance();
        endMonth.add(Calendar.MONTH, end_month);
        Calendar current = Calendar.getInstance();

        calendar.setVisibleMonthRange(startMonth,endMonth);
        calendar.setCurrentMonth(current);
        Calendar startSelectionDate = Calendar.getInstance();
        startSelectionDate.add(Calendar.DATE, 0);
        Calendar endSelectionDate = (Calendar) startSelectionDate.clone();
        endSelectionDate.add(Calendar.DATE, 365);
        calendar.setSelectableDateRange(startSelectionDate, endSelectionDate);
        calendar.setCalendarListener(new CalendarListener() {
            @Override
            public void onFirstDateSelected(Calendar startDate) {
              //  Toast.makeText(getActivity(), "Start Date: " + startDate.getTime().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {

                SimpleDateFormat dates = new SimpleDateFormat("dd/MM/yyyy");
                from_date_string = dates.format(startDate.getTime());
                to_date_string=dates.format(endDate.getTime());
                try {
                    from_date = dates.parse(from_date_string);
                    to_date=dates.parse(to_date_string);
                }
                catch (Exception e)
                {

                }

            }

        });


        //

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long difference = Math.abs(from_date.getTime() - to_date.getTime());
                long differenceDates = difference / (24 * 60 * 60 * 1000);
                differenceDates=differenceDates+1;
                Toast.makeText(getActivity(), "Start Date: " + from_date_string+ " End date: " + to_date_string+" diff "+differenceDates, Toast.LENGTH_SHORT).show();

                Calendar startCal = Calendar.getInstance();
                startCal.setTime(from_date);

                Calendar endCal = Calendar.getInstance();
                endCal.setTime(to_date);

                int sat_sun = 0;
               do{
                    if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        ++sat_sun;
                    }
                    startCal.add(Calendar.DAY_OF_MONTH, 1);
                } while (startCal.getTimeInMillis() <= endCal.getTimeInMillis());




                otherInfo.setVisibility(View.VISIBLE);
                datesTextView.setText(from_date_string+"-"+to_date_string);
                daysTextView.setText(""+differenceDates);
                sat_sunTextView.setText(""+sat_sun);
                annualLeavesTextView.setText("1");

            }
        });


        ///
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this.getContext(),android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_id.setAdapter(aa);





        ///
        return root;
    }
    //Performing action onItemSelected and onNothing selected

    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getActivity(),country[position] , Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}