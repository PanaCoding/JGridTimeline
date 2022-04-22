package com.panacoding.gridtimeline;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
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

        timeline.setyLabelsTitle("Departments"); // Y-Axis Title (optional)

        /***
         *
         * Y-Axis Labels Required
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

            JModelYLabel yLabel = new JModelYLabel();

            yLabel.setId(key);
            yLabel.setName(value);

            list_y_labels.add(yLabel);
        }

        timeline.setyLabels(list_y_labels);



        // Date (Required)
        timeline.setDate("2022-04-18");

        // Set initial hour 1-24 default 5am (optional)
        timeline.setStart_hour_24(7);

        // Set final hour 1-24 default 24 (optional)
        timeline.setEnd_hour_24(23);

        // Set intervals in minutes default 60mins (optional)
        timeline.setInterval_mins(30);

        // Show actual hour indicator (default: false) (optional)
        timeline.setShow_actual_hour_indicator(true);

        // Set row labels width in Pixels default 200px (optional)
        timeline.setRows_labels_width(300);

        // Grid Stroke color
        timeline.setStroke_color_grid("#edf2f4"); // optional
        timeline.setInner_line_col_multiplier(2); // optional

        // Timeline background color
        timeline.setColor_background_timeline("#ffffff"); // optional default white

        // Labels text color
        timeline.setLabels_text_color("#8d99ae"); // optional

        // ======================== IN DATE ============================ //

        // X-Axis in hour(true) ::: in days set (true)
        /*timeline.setX_axis_in_hours(false);

        // Set X-Axis Start Date in yyyy-MM-dd
        timeline.setStart_date("2022-04-01");

        // Set X-Axis End Date in yyyy-MM-dd
        timeline.setEnd_date("2022-04-30");

        // Set the format to show dates default(yyyy-MM-dd)
        timeline.setDate_format_to_show("dd/MM/yyyy");*/

        // ======================== IN DATE ============================ //

        // Load event
        timeline.load();


        // ================ ADD Events ======================= //
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

        timeline.setEvents(arr_events);

        // Add event click event (Optional)
        timeline.setOnEventClick(new JTimelineContainer.OnEventClick() {
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


    }
}