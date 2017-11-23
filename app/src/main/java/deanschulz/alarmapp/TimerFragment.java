package deanschulz.alarmapp;

import android.app.*;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText calMinute;
    EditText calDays;
    TextView timeTextField;
    EditText message;
    EditText location;
    TextView countdown;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static TimerFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        TimerFragment firstFragment = new TimerFragment();
        firstFragment.setArguments(args);
        return firstFragment;
    }

    public TimerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimerFragment newInstance(String param1, String param2) {
        TimerFragment fragment = new TimerFragment();
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
        final View myFragmentView = inflater.inflate(R.layout.fragment_timer, container, false);

        calDays = (EditText) myFragmentView.findViewById(R.id.days);
        calMinute = (EditText) myFragmentView.findViewById(R.id.minutes);
        message = (EditText) myFragmentView.findViewById(R.id.timerMessage);
        location = (EditText) myFragmentView.findViewById(R.id.timerLocation);
        countdown = (TextView) myFragmentView.findViewById(R.id.countdown);

        FloatingActionButton myFab = (FloatingActionButton) myFragmentView.findViewById(R.id.floatingActionButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(calDays.getText().length() == 0 || calMinute.getText().length() == 0 ||
                        location.getText().toString().length() == 0) {
                    Toast toast = Toast.makeText(getContext(), "Fill blank fields", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    startAlarm(message.getText().toString(), location.getText().toString());
                    MainActivity main = new MainActivity();
                    String newItem = "Timer: " + message.getText().toString() + ", Days: " + calDays.getText().toString() + ", Minutes: " + calMinute.getText().toString();
                    main.setArray(newItem);
                    main.setDrawer();
                    clearForm((ViewGroup) myFragmentView.findViewById(R.id.timerAlarm));
                    location.setText("");
                    calMinute.setText("");
                    calDays.setText("");
                }
            }
        });
        return myFragmentView;
    }
    public void startAlarm(final String message2, final String location2){
        long duration = Integer.parseInt(calDays.getText().toString())*24*60*60*1000 + Integer.parseInt(calMinute.getText().toString())*60*1000;
        new CountDownTimer(duration, 1000) {

            public void onTick(long millisUntilFinished) {
                countdown.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                Intent intent = new Intent(getContext(), AlarmAct.class);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.putExtra("message", message2);
                intent.putExtra("location", location2);
                intent.putExtra("selection", 2);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0,intent, PendingIntent.FLAG_CANCEL_CURRENT);
                android.app.AlarmManager am = (android.app.AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                am.setExact(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
                countdown.setText("");
            }

        }.start();
        Toast toast = Toast.makeText(getContext(), "Timer set", Toast.LENGTH_SHORT);
        toast.show();
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
