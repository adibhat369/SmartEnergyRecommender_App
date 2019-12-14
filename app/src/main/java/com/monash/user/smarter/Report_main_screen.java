package com.monash.user.smarter;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Report_main_screen extends Fragment {

    View vReports;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vReports = inflater.inflate(R.layout.reports_main_screen, container, false);
        getActivity().setTitle("REPORTS");

        final RadioButton radio_pie = (RadioButton) vReports.findViewById(R.id.pie_chart);
        final RadioButton radio_bar = (RadioButton) vReports.findViewById(R.id.bar_graph);
        final RadioButton radio_line = (RadioButton) vReports.findViewById(R.id.line_graph);
        final TextView invalid_view = (TextView) vReports.findViewById(R.id.invalid_radio);
        final RadioGroup rdgrp = (RadioGroup) vReports.findViewById(R.id.radio_grp);

        final String invalid_msg = getActivity().getString(R.string.invalid_radio_selection);
        Button generate_button = (Button) vReports.findViewById(R.id.generate_button);
        generate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            int selected_id = rdgrp.getCheckedRadioButtonId();
            RadioButton rdbtn =(RadioButton) vReports.findViewById(selected_id);
                if(selected_id == radio_pie.getId()){
                    Intent i = new Intent(getActivity(),Piechart.class);
                    getActivity().startActivity(i);
                }
                else if(radio_bar.isChecked()){
                    Intent i = new Intent(getActivity(),Bargraph.class);
                    getActivity().startActivity(i);

                }
                else if(radio_line.isChecked()){
                    Intent i = new Intent(getActivity(),LineGraph.class);
                    getActivity().startActivity(i);

                }
                else
                    invalid_view.setText(invalid_msg);


            }
        });

        return vReports;
    }
   /* public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

    }*/
}
