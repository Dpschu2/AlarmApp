package deanschulz.alarmapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlarmFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button TimeZoneButton;
    Button setDate;
    Button setTime;
    TextView TimeZoneTextview;
    TextView dateTextField;
    TextView timeTextField;
    EditText message;
    EditText location;
    String calMonth;
    String calDayOfMonth;
    String calYear;
    String calHour;
    String calMinute;
    Switch recursive;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static AlarmFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        AlarmFragment firstFragment = new AlarmFragment();
        firstFragment.setArguments(args);
        return firstFragment;
    }

    public AlarmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlarmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlarmFragment newInstance(String param1, String param2) {
        AlarmFragment fragment = new AlarmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View myFragmentView = inflater.inflate(R.layout.fragment_alarm, container, false);

        TimeZoneButton = (Button) myFragmentView.findViewById(R.id.timeZoneButton);
        TimeZoneTextview = (TextView) myFragmentView.findViewById(R.id.timeZoneField);
        dateTextField = (TextView) myFragmentView.findViewById(R.id.dateTextField);
        timeTextField = (TextView) myFragmentView.findViewById(R.id.timeTextField);
        setDate = (Button) myFragmentView.findViewById(R.id.datePicker);
        setTime = (Button) myFragmentView.findViewById(R.id.timePicker);
        message = (EditText) myFragmentView.findViewById(R.id.alarmMessage);
        location = (EditText) myFragmentView.findViewById(R.id.alarmLocation);
        recursive = (Switch) myFragmentView.findViewById(R.id.recursive);
        TimeZoneButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                TimeZone tz = TimeZone.getDefault();
                TimeZoneTextview.setText("Time Zone:  " + tz.getDisplayName(false,TimeZone.SHORT) + "TimeZone Id: " + tz.getID());
            }
        });

        //Add to others when finished....

        final TextView timeTextField = (TextView) myFragmentView.findViewById(R.id.timeTextField);
        final TextView dateTextField = (TextView) myFragmentView.findViewById(R.id.dateTextField);
        final EditText alarmLocation = (EditText) myFragmentView.findViewById(R.id.alarmLocation);
        final TextView timeZoneField = (TextView) myFragmentView.findViewById(R.id.timeZoneField);
        FloatingActionButton myFab = (FloatingActionButton) myFragmentView.findViewById(R.id.floatingActionButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(timeTextField.getText().length() == 0 || dateTextField.getText().length() == 0 ||
                        alarmLocation.getText().length() == 0 || timeZoneField.getText().length() == 0) {
                    Toast toast = Toast.makeText(getContext(), "Fill blank fields", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    startAlarm();
                    MainActivity main = new MainActivity();
                    String newItem = "Alarm: " + message.getText().toString() + ", " + calMonth + "/" + calDayOfMonth + "/" + calYear + " at " + calHour + ":" + calMinute;
                    main.setArray(newItem);
                    main.setDrawer();
                    clearForm((ViewGroup) myFragmentView.findViewById(R.id.rootAlarm));
                    timeZoneField.setText("");
                    alarmLocation.setText("");
                    dateTextField.setText("");
                    timeTextField.setText("");
                    recursive.setChecked(false);
                }
            }
        });
        setDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                date(v);
            }
        });
        setTime.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                time(v);
            }
        });
        return myFragmentView;
    }
    private void clearForm(ViewGroup group)
    {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
    }
    public void startAlarm(){

        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        cal.set(Calendar.DATE,Integer.parseInt(calDayOfMonth));
        cal.set(Calendar.MONTH,Integer.parseInt(calMonth));
        cal.set(Calendar.YEAR,Integer.parseInt(calYear));
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(calHour));
        cal.set(Calendar.MINUTE, Integer.parseInt(calMinute));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long time = cal.getTimeInMillis();
        Intent intent = new Intent(getContext(), AlarmAct.class);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.putExtra("message", message.getText().toString());
        intent.putExtra("location", location.getText().toString());
        intent.putExtra("selection", 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0,intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        if(!recursive.isChecked())
            am.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        else
            am.setRepeating(AlarmManager.RTC_WAKEUP, time, am.INTERVAL_DAY * 7, pendingIntent);
        String toastText;
        if(recursive.isChecked())
            toastText = "Recursive alarm set";
        else
            toastText = "Alarm set";
        Toast toast = Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT);
        toast.show();
    }
    public void date(View v){
        Calendar call = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    dateTextField.setText("" + month + "/" + dayOfMonth + "/" + year);
                    calMonth = String.valueOf(month);
                    calDayOfMonth = String.valueOf(dayOfMonth);
                    calYear = String.valueOf(year);
                }
            }, call.get(Calendar.YEAR), call.get(Calendar.MONTH), call.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();

    }
    public void time(View v){
        Calendar cal2 = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    timeTextField.setText("" + hourOfDay + ":" + minute);
                    calHour = String.valueOf(hourOfDay);
                    calMinute = String.valueOf(minute);
                }
        }, cal2.get(Calendar.HOUR_OF_DAY), cal2.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    //.......

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //When fragment openned
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
