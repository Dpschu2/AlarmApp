package deanschulz.alarmapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

/**
 * Created by Dean Schulz on 11/26/2017.
 */

public class DFragment extends DialogFragment {

    Handler mHandler = new Handler();
    String last;
    String cur;
    AlarmManager alarmManager = AlarmManager.getInstance();
    Context mContext;

    public DFragment(){
    }
    public static DFragment newInstance(String key) {
        DFragment dialog = new DFragment();
        Bundle args = new Bundle();
        args.putString("key", key);
        dialog.setArguments(args);
        return dialog;
    }
    public void setContext(Context context){
        mContext = context;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
    @Override
    public void onDismiss(final DialogInterface dialog){
        super.onDismiss(dialog);
        ((DialogInterface.OnDismissListener)getActivity()).onDismiss(dialog);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialogfragment, container,
                false);
        getDialog().setTitle("Location Alarm");

        last = alarmManager.getLastLocation();
        if(!alarmManager.isLocationChanged()) {
//
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity mainActivity = new MainActivity();
                    mainActivity.resetLocation();
                    cur = alarmManager.getLocation();
                    if(!cur.equals(last)){
                        dismiss();
                    }
                    last = cur;
                    mHandler.postDelayed(this, 1000);
                }},1000);

        }
        return rootView;
    }
}
