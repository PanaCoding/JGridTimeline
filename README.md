GridTimeline
===============
GridTimeline is a timeline SmartSheet similar to others timeline charts but full android native.


Here is a short gif showing the functionality you get with this library:

![alt text](https://github.com/PanaCoding/JGridTimeline/blob/main/demo.gif "Demo gif")


Installing
---------------
### Maven
Add the following maven dependency exchanging `x.x.x` for the latest release.

```XML
<dependency>
    <groupId>com.panacoding.gridtimeline</groupId>
    <artifactId>gridtimeline</artifactId>
    <version>x.x.x</version>
</dependency>
```

### Gradle
Add the following gradle dependency exchanging `x.x.x` for the latest release.
```groovy
dependencies {
    compile 'com.panacoding.gridtimeline:gridtimeline:x.x.x'
}
```

### Cloning
First of all you will have to clone the library.
```shell
git clone https://github.com/PanaCoding/JGridTimeline.git
```

Now that you have the library you will have to import it into Android Studio.
In Android Studio navigate the menus like this.
```
File -> Import Project ...
```
In the following dialog navigate to Gridtimeline which you cloned to your computer in the previous steps and select the `build.gradle`.

Getting Started
---------------
### Basic usage

Ok lets start with your activities or fragments xml file. It might look something like this.
```xml
<com.panacoding.gridtimeline.JTimelineContainer
        android:id="@+id/timeline"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

Now in your activities `onCreate()` or your fragments `onCreateView()` you would want to do something like this
```java
JTimelineContainer timeline = findViewById(R.id.timeline); // Get View timeline

timeline.setyLabelsTitle("Departments"); // Y-Axis Title (optional)

/***
 *
 * Y-Axis Labels (Required)
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

// init timeline
timeline.load();
```

### Styling

You should do this before de load() call
```java

// Grid Stroke color
timeline.setStroke_color_grid("#edf2f4"); // optional
timeline.setInner_line_col_multiplier(2); // optional

// Timeline background color
timeline.setColor_background_timeline("#ffffff"); // optional default white

// Labels text color
timeline.setLabels_text_color("#8d99ae"); // optional

```

### ADD Events to the timeline
```java

ArrayList<JModelEvent> events_list = new ArrayList<>();

JModelEvent event1 = new JModelEvent();

event1.setId("120");
event1.setName("Josue Birthday");
event1.setStart_time("7:25");
event1.setEnd_time("8:25");
event1.setY_label_id("sales"); // id y label
events_list.add(event1);

JModelEvent event2 = new JModelEvent();

event2.setId("122");
event2.setName("Joel Retirement");
event2.setStart_time("10:30");
event2.setEnd_time("11:35");
event2.setY_label_id("executives");
event2.setBackground_color("#000000"); // you can change the color background of the event
event2.setText_color("#f5fc2d"); // you can change the text_color of the event
events_list.add(event2);

JModelEvent event3 = new JModelEvent();

event3.setId("125");
event3.setName("Itzenia Birthday");
event3.setStart_time("7:10");
event3.setEnd_time("9:25");
event3.setY_label_id("it");
events_list.add(event3);

JModelEvent[] arr_events = new JModelEvent[events_list.size()];
events_list.toArray(arr_events);

timeline.setEvents(arr_events); // set events to render on the timeline

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


```


### How to use Dates instead of Hours

First you need to set X-Axis in hour to(false) and add date to start and date to finish before de load() call
```java
timeline.setX_axis_in_hours(false);

// Set X-Axis Start Date in yyyy-MM-dd
timeline.setStart_date("2022-04-01");

// Set X-Axis End Date in yyyy-MM-dd
timeline.setEnd_date("2022-04-30");

// Set the format to show dates default(yyyy-MM-dd)
timeline.setDate_format_to_show("dd/MM/yyyy");
```

Now when you add the events you should add start_date and end_date instead of start_time,end_time like this
```java
ArrayList<JModelEvent> events_list = new ArrayList<>();

JModelEvent event1 = new JModelEvent();

event1.setId("120");
event1.setName("Josue Birthday");
event1.setStart_date("2022-04-04");
event1.setEnd_date("2022-04-06");
event1.setY_label_id("sales");
events_list.add(event1);

JModelEvent[] arr_events = new JModelEvent[events_list.size()];
events_list.toArray(arr_events);

timeline.setEvents(arr_events);
```

