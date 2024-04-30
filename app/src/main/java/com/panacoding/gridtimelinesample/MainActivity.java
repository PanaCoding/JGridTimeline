package com.panacoding.gridtimelinesample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.panacoding.gridtimeline.JModelEvent;
import com.panacoding.gridtimeline.JModelYLabel;
import com.panacoding.gridtimeline.JTimeLine;
import com.panacoding.gridtimeline.JTimelineContainer;
import com.panacoding.gridtimeline.OnEventClick;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JTimelineContainer timeline = findViewById(R.id.timeline); // Get View timeline

        /**
         *
         * Y-Axis Labels
         *
         */
        List<JModelYLabel> list_y_labels = new ArrayList<>();

        HashMap<String,String> office_departments = new HashMap<>();
        office_departments.put("it","IT");
        office_departments.put("accounting","Accounting");
        office_departments.put("human_resources","Human Resources");
        office_departments.put("sales","Sales");
        office_departments.put("executives","Executives");
        office_departments.put("warehouse","Warehouse");
        office_departments.put("marketing","Marketing");
        office_departments.put("factory","Factory");
        office_departments.put("research","Research");

        for (Map.Entry<String, String> department : office_departments.entrySet()) {
            String key = department.getKey();
            String value = department.getValue();

            JModelYLabel yLabel = new JModelYLabel(key,value);

            list_y_labels.add(yLabel);
        }


        /** ================ ADD Events ======================= **/
        ArrayList<JModelEvent> events_list = new ArrayList<>();

        JModelEvent event1 = new JModelEvent();

        event1.setId("120");
        event1.setName("Josue Birthday");
        event1.setStart_date("2022-04-04");
        event1.setEnd_date("2022-04-06");
        event1.setStart_time("7:25");
        event1.setEnd_time("8:25");
        event1.setY_label_id("sales");
        events_list.add(event1);

        JModelEvent event2 = new JModelEvent();

        event2.setId("122");
        event2.setName("Joel Retirement");
        event2.setStart_date("2022-04-08");
        event2.setEnd_date("2022-04-08");
        event2.setStart_time("10:30");
        event2.setEnd_time("11:35");
        event2.setY_label_id("executives");
        event2.setBackground_color("#000000");
        event2.setText_color("#f5fc2d");
        events_list.add(event2);


        JModelEvent event3 = new JModelEvent();
        event3.setId("125");
        event3.setName("Itzenia Birthday");
        event3.setStart_date("2022-04-04");
        event3.setEnd_date("2022-04-06");
        event3.setStart_time("7:10");
        event3.setEnd_time("9:25");
        event3.setY_label_id("it");
        events_list.add(event3);

        JModelEvent[] arr_events = new JModelEvent[events_list.size()];
        events_list.toArray(arr_events);



        /*** BUILDER **/
        JTimeLine.JTimelineBuilder builder = new JTimeLine.JTimelineBuilder(this,timeline,"2022-04-18")
            .setYLabelsTitle("Departments") // Y-Axis Title (optional)
            .setYLabels(list_y_labels)
            .setStartHour24(7) // Set initial hour 1-24 default 5am (optional)
            .setEndHour24(23) // Set final hour 1-24 default 24 (optional)
            .setIntervalMins(30)// Set intervals in minutes default 60mins (optional)
            .setShowActualHourIndicator(true) // Show actual hour indicator (default: false) (optional)
            .setRowsLabelsWidth(300) // Set row labels width in Pixels default 200px (optional)
            .setStrokeColorGrid("#edf2f4") // Grid Stroke color optional
            .setInnerLineColMultiplier(2) // optional
            .setColorBackgroundTimeline("#ffffff") // Timeline background color optional default white
            .setLabelsTextColor("#8d99ae")
            .setEvents(arr_events); // Labels text color optional

        /** Add event click event (Optional) **/
        builder.setOnEventClick(new OnEventClick() {
            @Override
            public void onEventClick(JModelEvent event) {
                /**
                 *
                 *
                 *
                 */
                Toast.makeText(MainActivity.this, "event clicked "+event.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        // ======================== IN DATE ============================ //

        // X-Axis in hour(true) ::: in days set (true)
        //builder.setXAxisInHours(false);

        // Set X-Axis Start Date in yyyy-MM-dd
        //builder.setStartDate("2022-04-01");

        // Set X-Axis End Date in yyyy-MM-dd
        //builder.setEndDate("2022-04-30");

        // Set the format to show dates default(yyyy-MM-dd)
        //builder.setDateFormatToShow("dd/MM/yyyy");

        // ======================== IN DATE ============================ //


        JTimeLine jTimeLine = builder.build();
        jTimeLine.load();


    }
}