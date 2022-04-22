GridTimeline
===============
GridTimeline is a timeline SmartSheet similar to others timeline charts but full android native.


Here is a short gif showing the functionality you get with this library:

![alt text](https://github.com/PanaCoding/JGridTimeline/blob/main/demo.gif "Demo gif")


Installing
---------------
###Maven
Add the following maven dependency exchanging `x.x.x` for the latest release.
```XML
<dependency>
    <groupId>com.panacoding.gridtimeline</groupId>
    <artifactId>gridtimeline</artifactId>
    <version>x.x.x</version>
</dependency>
```

###Gradle
Add the following gradle dependency exchanging `x.x.x` for the latest release.
```groovy
dependencies {
    compile 'com.panacoding.gridtimeline:gridtimeline:x.x.x'
}
```

###Cloning
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
###Base usage

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