package com.panacoding.gridtimeline;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class JTimeLine{
    private Activity activity;
    private String date;
    private JTimelineContainer container;

    private int start_hour_24;
    private int end_hour_24;
    private int interval_mins;
    private int interval_days;
    private boolean show_actual_hour_indicator;
    private String start_date;
    private String end_date;
    private String date_format_to_show;
    private int rows_labels_width;
    private boolean x_axis_in_hours;
    private int inner_line_col_multiplier;
    private String stroke_color_grid;
    private String color_background_timeline;
    private String labels_text_color;
    private LayoutInflater inflater;
    List<JModelYLabel> yLabels;
    private JModelEvent[] events;
    private OnEventClick onEventClick;
    private String yLabelsTitle;

    /*** internal views **/
    private LinearLayoutCompat ll_rows_labels;
    private LinearLayoutCompat ll_columns_labels;
    private LinearLayoutCompat indicador_actual_time;
    private FrameLayout fl_info_plane;
    private JGridDraw jGridDraw;
    private HorizontalScrollView hs_hours;
    private View sc_main_timeline;
    float cell_width;
    float cell_height;
    private ArrayList<Float> hours_x_label = new ArrayList<>();
    private ArrayList<Long> date_x_label = new ArrayList<>();

    private JTimeLine(JTimelineBuilder builder) {
        this.activity = builder.activity;
        this.container = builder.container;
        this.date = builder.date;
        this.start_hour_24 = builder.start_hour_24;
        this.end_hour_24 = builder.end_hour_24;
        this.interval_mins = builder.interval_mins;
        this.interval_days = builder.interval_days;
        this.show_actual_hour_indicator = builder.show_actual_hour_indicator;
        this.start_date = builder.start_date;
        this.end_date = builder.end_date;
        this.date_format_to_show = builder.date_format_to_show;
        this.rows_labels_width = builder.rows_labels_width;
        this.x_axis_in_hours = builder.x_axis_in_hours;
        this.inner_line_col_multiplier = builder.inner_line_col_multiplier;
        this.stroke_color_grid = builder.stroke_color_grid;
        this.color_background_timeline = builder.color_background_timeline;
        this.labels_text_color= builder.labels_text_color;
        this.yLabels = builder.yLabels;
        this.yLabelsTitle = builder.yLabelsTitle;
        this.events = builder.events;
        this.onEventClick = builder.onEventClick;
    }

    private void init(){
        inflater = activity.getLayoutInflater();

        View jtimeline = inflater.inflate(R.layout.layout_jtimelinecontainer,container,false);

        sc_main_timeline = jtimeline.findViewById(R.id.sc_main_timeline);
        ll_rows_labels = jtimeline.findViewById(R.id.ll_rows_labels);
        ll_columns_labels = jtimeline.findViewById(R.id.ll_columns_labels);
        fl_info_plane = jtimeline.findViewById(R.id.fl_info_plane);
        jGridDraw = jtimeline.findViewById(R.id.jGridDraw);
        indicador_actual_time = jtimeline.findViewById(R.id.indicador_actual_time);
        hs_hours = jtimeline.findViewById(R.id.hs_hours);

        container.addView(jtimeline);
    }

    public void load(){
        init();

        // clear everything
        ll_rows_labels.removeAllViews();
        ll_columns_labels.removeAllViews();
        fl_info_plane.removeAllViews();
        fl_info_plane.requestLayout();
        cell_width = 0;
        cell_height = 0;

        // Background timeline
        sc_main_timeline.setBackgroundColor(Color.parseColor(color_background_timeline));

        // row labels width
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_rows_labels.getLayoutParams();
        params.width = rows_labels_width;
        ll_rows_labels.setLayoutParams(params);

        LayerDrawable layer_border_bottom_gridsheet = (LayerDrawable) ContextCompat.getDrawable(activity,R.drawable.border_bottom_gridsheet);
        GradientDrawable d_border_bottom_gridsheet = (GradientDrawable) layer_border_bottom_gridsheet.findDrawableByLayerId(R.id.box);
        d_border_bottom_gridsheet.setStroke(1,Color.parseColor(stroke_color_grid));

        // Title y labels
        TextView tw_Ytitle = (TextView) inflater.inflate(R.layout.layout_jtimeline_y_row_label,container,false);

        tw_Ytitle.setBackground(d_border_bottom_gridsheet);
        tw_Ytitle.setTextColor(Color.parseColor(labels_text_color));

        if(yLabelsTitle!=null && !yLabelsTitle.isEmpty()) {
            tw_Ytitle.setText(yLabelsTitle);
        }else{
            tw_Ytitle.setText("");
        }

        ll_rows_labels.addView(tw_Ytitle);


        // Y Labels
        if( yLabels!=null && !yLabels.isEmpty() ){

            for(JModelYLabel jModelYLabel : yLabels){
                TextView tw_y_row_label = (TextView) inflater.inflate(R.layout.layout_jtimeline_y_row_label,container,false);
                tw_y_row_label.setText(jModelYLabel.getName());
                tw_y_row_label.setBackground(d_border_bottom_gridsheet);
                tw_y_row_label.setTextColor(Color.parseColor(labels_text_color));

                ll_rows_labels.addView(tw_y_row_label);
            }
        }

        LayerDrawable layer_border_right_gridsheet = (LayerDrawable) ContextCompat.getDrawable(activity,R.drawable.border_right_gridsheet);
        GradientDrawable d_border_right_gridsheet = (GradientDrawable) layer_border_right_gridsheet.findDrawableByLayerId(R.id.box);
        d_border_right_gridsheet.setStroke(1,Color.parseColor(stroke_color_grid));

        int cols_count = 0;
        if(x_axis_in_hours) {

            float hour_start_x = start_hour_24;
            SimpleDateFormat format_hour = new SimpleDateFormat("h:mm a");
            hours_x_label = new ArrayList<>();

            while (hour_start_x <= end_hour_24) {
                int next_hour = (int) hour_start_x;
                int next_min = (int) ((hour_start_x * 60) % 60);
                Calendar c_hour_date = JUtils.getCalendarFromDateString(date + " " + next_hour + ":" + next_min, "yyyy-MM-dd HH:mm");
                String hour_formatted = format_hour.format(c_hour_date.getTime());

                TextView tw_x_col_label = (TextView) inflater.inflate(R.layout.layout_jtimeline_x_col_label, container, false);
                tw_x_col_label.setText(hour_formatted);
                tw_x_col_label.setBackground(d_border_right_gridsheet);
                tw_x_col_label.setTextColor(Color.parseColor(labels_text_color));

                ll_columns_labels.addView(tw_x_col_label);

                hours_x_label.add(hour_start_x);
                hour_start_x += (interval_mins / 60f);

                cols_count++;
            }

        }else{
            SimpleDateFormat format_date = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format_date_to_show = new SimpleDateFormat(date_format_to_show);

            if( start_date==null || end_date==null || start_date.isEmpty() || end_date.isEmpty() ){
                start_date = format_date.format(Calendar.getInstance().getTime());
                end_date = format_date.format(Calendar.getInstance().getTime());
            }

            Calendar c_start_date = JUtils.getCalendarFromDateString(start_date,"yyyy-MM-dd");
            Calendar c_end_date = JUtils.getCalendarFromDateString(end_date,"yyyy-MM-dd");

            c_start_date.set(Calendar.MILLISECOND, 0);
            c_start_date.set(Calendar.MINUTE, 0);
            c_start_date.set(Calendar.HOUR, 0);

            c_end_date.set(Calendar.MILLISECOND, 0);
            c_end_date.set(Calendar.MINUTE, 0);
            c_end_date.set(Calendar.HOUR, 0);

            while( c_start_date.getTimeInMillis() <= c_end_date.getTimeInMillis() ){
                String date_formatted = format_date_to_show.format(c_start_date.getTime());

                TextView tw_x_col_label = (TextView) inflater.inflate(R.layout.layout_jtimeline_x_col_label, container, false);
                tw_x_col_label.setText(date_formatted);
                tw_x_col_label.setBackground(d_border_right_gridsheet);
                tw_x_col_label.setTextColor(Color.parseColor(labels_text_color));

                ll_columns_labels.addView(tw_x_col_label);

                date_x_label.add(c_start_date.getTimeInMillis());
                c_start_date.add(Calendar.DATE,interval_days);

                cols_count++;
            }

        }

        // grid draw
        if( yLabels!=null && !yLabels.isEmpty() ) {
            jGridDraw.setRowsCount(yLabels.size());
        }

        jGridDraw.setColumnsCount(cols_count);
        jGridDraw.setCol_multiplier(inner_line_col_multiplier);
        jGridDraw.setStroke_color(stroke_color_grid);
        jGridDraw.invalidate();

        jGridDraw.setOnCellSizeChange(new JGridDraw.OnCellSizeChange() {
            @Override
            public void onCellSizeChange(float cell_width, float cell_height) {
                JTimeLine.this.cell_width = cell_width;
                JTimeLine.this.cell_height = cell_height;

                onLoadComplete();
            }
        });
    }

    private void onLoadComplete(){
        populateActualHourIndicator();
        populateEvents();
    }

    private int find_y_position_label(String label_id){
        int position = 0;
        int i=0;

        for (JModelYLabel label : yLabels){
            if(label.getId().equals(label_id)){
                position = i;
                break;
            }
            i++;
        }

        return position;
    }

    private float find_x_position_label(int start_hour,int start_hour_minutes){
        float position = 0;
        int i=0;

        float mins_percent = start_hour_minutes / (float)interval_mins;

        for (Float label_x : hours_x_label){
            if( label_x == start_hour ){
                position = i;
                position += mins_percent;
                break;
            }
            i++;
        }

        return position;
    }

    private float find_x_position_label_for_dates(long start_time){
        float position = 0;
        int i=0;

        for (long label_x : date_x_label ){
            if( label_x == start_time ){
                position = i;
                break;
            }
            i++;
        }

        return position;
    }

    public boolean is_actual_hour_position_find(int start_hour,int start_hour_minutes){
        float position = 0;
        int i=0;
        boolean position_find = false;

        float mins_percent = start_hour_minutes / (float)interval_mins;

        for (Float label_x : hours_x_label){
            if( label_x == start_hour ){
                position = i;
                position += mins_percent;
                position_find = true;
                break;
            }
            i++;
        }

        return position_find;
    }

    private void populateEvents(){
        if(events!=null && events.length>0){


            int ie = 0;
            for(JModelEvent event : events){
                View layout_event = inflater.inflate(R.layout.layout_timeline_event,fl_info_plane,false);
                TextView tw_event_name = layout_event.findViewById(R.id.tw_event_name);

                tw_event_name.setText(event.getName());
                tw_event_name.setTextColor(Color.parseColor(event.getText_color()));

                float width_event_px = 0f;
                float position_label_x = 0f;

                /**
                 *
                 * Duration and Width For hours X-Axis
                 *
                 */
                if(x_axis_in_hours) {

                    String[] start_hour_parts = event.getStart_time().split(":");
                    String[] end_hour_parts = event.getEnd_time().split(":");

                    try {
                        int start_hour = Integer.parseInt(start_hour_parts[0]);
                        int start_hour_minutes = Integer.parseInt(start_hour_parts[1]);

                        int end_hour = Integer.parseInt(end_hour_parts[0]);
                        int end_hour_minutes = Integer.parseInt(end_hour_parts[1]);


                        int duration_in_hours = Math.abs(end_hour - start_hour);
                        int duration_in_minutes = end_hour_minutes - start_hour_minutes;
                        int total_duration_minutes = (duration_in_hours * 60) + duration_in_minutes;

                        width_event_px = (total_duration_minutes * cell_width) / (float) interval_mins;

                        // Posicion en X
                        position_label_x = find_x_position_label(start_hour,start_hour_minutes);

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                }


                /**
                 *
                 * Duration and Width For dates X-Axis
                 *
                 */
                if(!x_axis_in_hours) {

                    SimpleDateFormat format_date = new SimpleDateFormat("yyyy-MM-dd");
                    String start_date = event.getStart_date();
                    String end_date = event.getEnd_date();

                    if (start_date == null || end_date == null || start_date.isEmpty() || end_date.isEmpty()) {
                        start_date = format_date.format(Calendar.getInstance().getTime());
                        end_date = format_date.format(Calendar.getInstance().getTime());
                    }

                    Calendar c_start_date = JUtils.getCalendarFromDateString(start_date, "yyyy-MM-dd");
                    Calendar c_end_date = JUtils.getCalendarFromDateString(end_date, "yyyy-MM-dd");

                    c_start_date.set(Calendar.MILLISECOND, 0);
                    c_start_date.set(Calendar.MINUTE, 0);
                    c_start_date.set(Calendar.HOUR, 0);

                    c_end_date.set(Calendar.MILLISECOND, 0);
                    c_end_date.set(Calendar.MINUTE, 0);
                    c_end_date.set(Calendar.HOUR, 0);

                    long total_duration_in_days = JUtils.getDiffDays(c_start_date.getTimeInMillis(), c_end_date.getTimeInMillis());
                    total_duration_in_days++;

                    width_event_px = (total_duration_in_days * cell_width) / (float) interval_days;

                    // Posicion en X
                    position_label_x = find_x_position_label_for_dates(c_start_date.getTimeInMillis());
                }



                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.width = (int)width_event_px;

                int event_margin_top = (int)JUtils.convertDpToPixel(10,activity);
                int event_height = (int)(cell_height - event_margin_top);

                params.height = event_height;


                // Posicion en Y
                int position_label_y = find_y_position_label(event.getY_label_id());
                int y_pos = (int)((position_label_y * cell_height) + (event_margin_top/2));
                layout_event.setTranslationY(y_pos);


                float x_pos = position_label_x * cell_width;
                layout_event.setTranslationX(x_pos);

                // Background del evento
                layout_event.setBackground(getEventBackground(event.getBackground_color()));

                layout_event.setLayoutParams(params);

                int finalIe = ie;
                layout_event.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onEventClick!=null) {
                            onEventClick.onEventClick(event);
                        }
                    }
                });

                fl_info_plane.addView(layout_event);

                ie++;
            }


        }
    }

    private void populateActualHourIndicator(){
        Calendar actual_hour = Calendar.getInstance();

        int hour_s = actual_hour.get(Calendar.HOUR_OF_DAY);
        int hour_mins = actual_hour.get(Calendar.MINUTE);

        if( is_actual_hour_position_find(hour_s,hour_mins) && show_actual_hour_indicator ) {
            indicador_actual_time.setVisibility(View.VISIBLE);

            float position_label_x = find_x_position_label(hour_s, hour_mins);

            float x_pos = position_label_x * cell_width;
            x_pos -= (indicador_actual_time.getWidth() / 2f);

            indicador_actual_time.setX(x_pos);

            int x_position_show = (int) x_pos - 180;
            hs_hours.scrollTo(x_position_show, 0);

        }else{
            indicador_actual_time.setVisibility(View.GONE);
        }
    }

    private Drawable getEventBackground(String color_hex){
        Drawable background = ContextCompat.getDrawable(activity,R.drawable.box_timeline_event);

        if(background!=null) background.setTint(Color.parseColor(color_hex));

        return background;
    }

    public static class JTimelineBuilder{

        /** required parameters **/
        private Activity activity;
        private String date;
        private JTimelineContainer container;

        /** optional **/
        private int start_hour_24 = 5;
        private int end_hour_24 = 24;
        private int interval_mins = 60;
        private int interval_days = 1;
        private boolean show_actual_hour_indicator = false;
        private String start_date;
        private String end_date;
        private String date_format_to_show = "yyyy-MM-dd";
        private int rows_labels_width = 200; // in px
        private boolean x_axis_in_hours = true;
        private int inner_line_col_multiplier = 4;
        private String stroke_color_grid = "#edf2f4";
        private String color_background_timeline = "#ffffff";
        private String labels_text_color = "#8d99ae"; // Text colors
        List<JModelYLabel> yLabels = new ArrayList<JModelYLabel>(){
            {
                add(new JModelYLabel("1", "y1"));
                add(new JModelYLabel("2", "y2"));
                add(new JModelYLabel("3", "y3"));
            }
        };
        private String yLabelsTitle;
        private JModelEvent[] events;
        private OnEventClick onEventClick;

        public JTimelineBuilder(Activity activity,JTimelineContainer container,String date){
            this.container=container;
            this.date=date;
            this.activity=activity;
        }

        public JTimelineBuilder setDateFormatToShow(String date_format_to_show) {
            this.date_format_to_show = date_format_to_show;
            return this;
        }

        public JTimelineBuilder setEvents(JModelEvent[] events) {
            this.events = events;
            return this;
        }

        public JTimelineBuilder setOnEventClick(OnEventClick onEventClick) {
            this.onEventClick = onEventClick;
            return this;
        }

        public JTimelineBuilder setYLabelsTitle(String yLabelsTitle) {
            this.yLabelsTitle = yLabelsTitle;
            return this;
        }

        public JTimelineBuilder setYLabels(List<JModelYLabel> yLabels) {
            this.yLabels = yLabels;
            return this;
        }

        public JTimelineBuilder setStartHour24(int start_hour_24) {
            this.start_hour_24 = start_hour_24;
            return this;
        }

        public JTimelineBuilder setEndHour24(int end_hour_24) {
            this.end_hour_24 = end_hour_24;
            return this;
        }

        public JTimelineBuilder setLabelsTextColor(String labels_text_color) {
            this.labels_text_color = labels_text_color;
            return this;
        }

        public JTimelineBuilder setColorBackgroundTimeline(String color_background_timeline) {
            this.color_background_timeline = color_background_timeline;
            return this;
        }

        public JTimelineBuilder setInnerLineColMultiplier(int inner_line_col_multiplier) {
            this.inner_line_col_multiplier = inner_line_col_multiplier;
            return this;
        }

        public JTimelineBuilder setStrokeColorGrid(String stroke_color_grid) {
            this.stroke_color_grid = stroke_color_grid;
            return this;
        }

        public JTimelineBuilder setIntervalDays(int interval_days) {
            this.interval_days = interval_days;
            return this;
        }

        public JTimelineBuilder setStartDate(String start_date) {
            this.start_date = start_date;
            return this;
        }

        public JTimelineBuilder setEndDate(String end_date) {
            this.end_date = end_date;
            return this;
        }

        public JTimelineBuilder setXAxisInHours(boolean x_axis_in_hours) {
            this.x_axis_in_hours = x_axis_in_hours;
            return this;
        }

        public JTimelineBuilder setShowActualHourIndicator(boolean show_actual_hour_indicator) {
            this.show_actual_hour_indicator = show_actual_hour_indicator;
            return this;
        }

        public JTimelineBuilder setIntervalMins(int interval_mins) {
            this.interval_mins = interval_mins;
            return this;
        }

        public JTimelineBuilder setRowsLabelsWidth(int rows_labels_width) {
            this.rows_labels_width = rows_labels_width;
            return this;
        }

        public JTimeLine build(){
            return new JTimeLine(this);
        }
    }
}
