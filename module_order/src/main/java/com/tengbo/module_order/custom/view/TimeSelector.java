package com.tengbo.module_order.custom.view;


import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.tengbo.module_order.R;
import com.tengbo.module_order.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by WangChenchen on 2016/12/29.
 */
public class TimeSelector extends LinearLayout {

    private final String TAG = "TimeSelectorDialog";

    public interface ResultHandler {
        void handle(String time);
    }

    private ResultHandler handler;

    public enum SCROLLTYPE {

        HOUR(1),
        MINUTE(2),
        SECOND(4);

        private SCROLLTYPE(int value) {
            this.value = value;
        }

        public int value;

    }

    public enum MODE {

        YMD(1),
        YMDHMS(2),
        HMS(3),
        HM(4);


        private MODE(int value) {
            this.value = value;
        }

        public int value;

    }


    private int scrollUnits = SCROLLTYPE.HOUR.value + SCROLLTYPE.MINUTE.value + SCROLLTYPE.SECOND.value;
    private Context context;
    private final String FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

    private PickerView year_pv;
    private PickerView month_pv;
    private PickerView day_pv;
//    private PickerView hour_pv;
//    private PickerView minute_pv;
//    private PickerView second_pv;

    private final int MAXSECOND = 59;
    private final int MAXMINUTE = 59;
    private int MAXHOUR = 23;
    private final int MINSECOND = 0;
    private final int MINMINUTE = 0;
    private int MINHOUR = 0;
    private final int MAXMONTH = 12;

    private ArrayList<String> year, month, day, hour, minute, second;
    private int startYear, startMonth, startDay, startHour, startMinute, startSecond, endYear, endMonth, endDay, endHour, endMinute, endSecond, second_workStart, second_workEnd, minute_workStart, minute_workEnd, hour_workStart, hour_workEnd;
    private boolean spanYear, spanMon, spanDay, spanHour, spanMin, spanSecond;
    private Calendar selectedCalender = Calendar.getInstance();
    private final long ANIMATORDELAY = 200L;
    private final long CHANGEDELAY = 90L;
    private Calendar startCalendar;
    private Calendar endCalendar;
//    private TextView hour_text;
//    private TextView minute_text;
//    private TextView second_text;
    private TextView year_text;
    private TextView month_text;
    private TextView day_text;

    private MODE mode;

    private int layoutId;

    public TimeSelector(Context context) {
        this(context, null);
    }

    public TimeSelector(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STR);
        long currentMillis = System.currentTimeMillis();
        String startDate = "1900-01-01 00:00:00";
        String endDate = "2100-12-31 00:00:00";
//        String endDate = sdf.format(currentMillis);

        startCalendar.setTime(TimeUtils.string2Date(startDate, sdf));
        endCalendar.setTime(TimeUtils.string2Date(endDate, sdf));
        this.layoutId = R.layout.layout_time_selector;
        initView();
        initParameter();
        initTimer();
        addListener();
    }

    public void setDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(millis);
        String[] split = date.split("-");
        year_pv.setSelected(split[0]);
        month_pv.setSelected(split[1]);
        day_pv.setSelected(split[2]);

        selectedCalender.setTimeInMillis(millis);
        Log.d(TAG, Arrays.toString(split));
    }

    public String getSelectedDate() {
        return DateUtils.format(selectedCalender.getTime(), "yyyy-MM-dd");
    }


    private void initView() {
        View view = LayoutInflater.from(context).inflate(layoutId, null);
        addView(view);
        year_pv = view.findViewById(R.id.year_pv);
        month_pv = view.findViewById(R.id.month_pv);
        day_pv = view.findViewById(R.id.day_pv);
//        hour_pv = view.findViewById(R.id.hour_pv);
//        minute_pv = view.findViewById(R.id.minute_pv);
//        second_pv = view.findViewById(R.id.second_pv);
//        hour_text = view.findViewById(R.id.hour_text);
//        minute_text = view.findViewById(R.id.minute_text);
//        second_text = view.findViewById(R.id.second_text);
        year_text = view.findViewById(R.id.year_text);
        month_text = view.findViewById(R.id.month_text);
        day_text = view.findViewById(R.id.day_text);
        setMode(MODE.YMD);

//        tv_cancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                seletorDialog.dismiss();
//            }
//        });
//        tv_select.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                boolean isChinese = (layoutId == R.layout.layout_time_selector);
//                if(mode == MODE.YMD) {
//                    if(isChinese)
//                        handler.handle(DateUtils.format(selectedCalender.getTime(), "yyyy年MM月dd日"));
//                    else
//                        handler.handle(DateUtils.format(selectedCalender.getTime(), "yyyy-MM-dd"));
//                }
//                else if(mode == MODE.YMDHMS) {
//                    if(isChinese)
//                        handler.handle(DateUtils.format(selectedCalender.getTime(), "yyyy年MM月dd日 HH时mm分ss秒"));
//                    else
//                        handler.handle(DateUtils.format(selectedCalender.getTime(), FORMAT_STR));
//                }
//                else if(mode == MODE.HMS) {
//                    if(isChinese)
//                        handler.handle(DateUtils.format(selectedCalender.getTime(), "HH时mm分ss秒"));
//                    else
//                        handler.handle(DateUtils.format(selectedCalender.getTime(), "HH:mm:ss"));
//                }
//                else if(mode == MODE.HM) {
//                    if(isChinese)
//                        handler.handle(DateUtils.format(selectedCalender.getTime(), "HH时mm分"));
//                    else
//                        handler.handle(DateUtils.format(selectedCalender.getTime(), "HH:mm"));
//                }
//
//                seletorDialog.dismiss();
//            }
//        });

    }

    private void initParameter() {
        startYear = startCalendar.get(Calendar.YEAR);
        startMonth = startCalendar.get(Calendar.MONTH) + 1;
        startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
        startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
        startMinute = startCalendar.get(Calendar.MINUTE);
        startSecond = startCalendar.get(Calendar.SECOND);
        endYear = endCalendar.get(Calendar.YEAR);
        endMonth = endCalendar.get(Calendar.MONTH) + 1;
        endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
        endHour = endCalendar.get(Calendar.HOUR_OF_DAY);
        endMinute = endCalendar.get(Calendar.MINUTE);
        endSecond = endCalendar.get(Calendar.SECOND);
        spanYear = startYear != endYear;
        spanMon = (!spanYear) && (startMonth != endMonth);
        spanDay = (!spanMon) && (startDay != endDay);
        spanHour = (!spanDay) && (startHour != endHour);
        spanMin = (!spanHour) && (startMinute != endMinute);
        spanSecond = (!spanMin) && (startSecond != endSecond);
        selectedCalender.setTime(startCalendar.getTime());

        Log.d(TAG, "initParameter " + toString());
    }

    @Override
    public String toString() {
        return "TimeSelectorDialog{" +
                "startYear=" + startYear +
                ", startMonth=" + startMonth +
                ", startDay=" + startDay +
                ", startHour=" + startHour +
                ", startMinute=" + startMinute +
                ", startSecond=" + startSecond +
                ", endYear=" + endYear +
                ", endMonth=" + endMonth +
                ", endDay=" + endDay +
                ", endHour=" + endHour +
                ", endMinute=" + endMinute +
                ", endSecond=" + endSecond +
                '}';
    }

    private void initTimer() {
        initArrayList();

        if (spanYear) {
            for (int i = startYear; i <= endYear; i++) {
                year.add(String.valueOf(i));
            }
            for (int i = startMonth; i <= MAXMONTH; i++) {
                month.add(fomatTimeUnit(i));
            }
            for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(fomatTimeUnit(i));
            }
            if ((scrollUnits & SCROLLTYPE.HOUR.value) != SCROLLTYPE.HOUR.value) {
                hour.add(fomatTimeUnit(startHour));
            } else {
                for (int i = startHour; i <= MAXHOUR; i++) {
                    hour.add(fomatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLLTYPE.MINUTE.value) != SCROLLTYPE.MINUTE.value) {
                minute.add(fomatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= MAXMINUTE; i++) {
                    minute.add(fomatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLLTYPE.SECOND.value) != SCROLLTYPE.SECOND.value) {
                second.add(fomatTimeUnit(startSecond));
            } else {
                for (int i = startSecond; i <= MAXSECOND; i++) {
                    second.add(fomatTimeUnit(i));
                }
            }

        } else if (spanMon) {
            year.add(String.valueOf(startYear));
            for (int i = startMonth; i <= endMonth; i++) {
                month.add(fomatTimeUnit(i));
            }
            for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(fomatTimeUnit(i));
            }
            if ((scrollUnits & SCROLLTYPE.HOUR.value) != SCROLLTYPE.HOUR.value) {
                hour.add(fomatTimeUnit(startHour));
            } else {
                for (int i = startHour; i <= MAXHOUR; i++) {
                    hour.add(fomatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLLTYPE.MINUTE.value) != SCROLLTYPE.MINUTE.value) {
                minute.add(fomatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= MAXMINUTE; i++) {
                    minute.add(fomatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLLTYPE.SECOND.value) != SCROLLTYPE.SECOND.value) {
                second.add(fomatTimeUnit(startSecond));
            } else {
                for (int i = startSecond; i <= MAXSECOND; i++) {
                    second.add(fomatTimeUnit(i));
                }
            }
        } else if (spanDay) {
            year.add(String.valueOf(startYear));
            month.add(fomatTimeUnit(startMonth));
            for (int i = startDay; i <= endDay; i++) {
                day.add(fomatTimeUnit(i));
            }
            if ((scrollUnits & SCROLLTYPE.HOUR.value) != SCROLLTYPE.HOUR.value) {
                hour.add(fomatTimeUnit(startHour));
            } else {
                for (int i = startHour; i <= MAXHOUR; i++) {
                    hour.add(fomatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLLTYPE.MINUTE.value) != SCROLLTYPE.MINUTE.value) {
                minute.add(fomatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= MAXMINUTE; i++) {
                    minute.add(fomatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLLTYPE.SECOND.value) != SCROLLTYPE.SECOND.value) {
                second.add(fomatTimeUnit(startSecond));
            } else {
                for (int i = startSecond; i <= MAXSECOND; i++) {
                    second.add(fomatTimeUnit(i));
                }
            }

        } else if (spanHour) {
            year.add(String.valueOf(startYear));
            month.add(fomatTimeUnit(startMonth));
            day.add(fomatTimeUnit(startDay));

            if ((scrollUnits & SCROLLTYPE.HOUR.value) != SCROLLTYPE.HOUR.value) {
                hour.add(fomatTimeUnit(startHour));
            } else {
                for (int i = startHour; i <= endHour; i++) {
                    hour.add(fomatTimeUnit(i));
                }

            }

            if ((scrollUnits & SCROLLTYPE.MINUTE.value) != SCROLLTYPE.MINUTE.value) {
                minute.add(fomatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= MAXMINUTE; i++) {
                    minute.add(fomatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLLTYPE.SECOND.value) != SCROLLTYPE.SECOND.value) {
                second.add(fomatTimeUnit(startSecond));
            } else {
                for (int i = startSecond; i <= MAXSECOND; i++) {
                    second.add(fomatTimeUnit(i));
                }
            }


        } else if (spanMin) {
            year.add(String.valueOf(startYear));
            month.add(fomatTimeUnit(startMonth));
            day.add(fomatTimeUnit(startDay));
            hour.add(fomatTimeUnit(startHour));


            if ((scrollUnits & SCROLLTYPE.MINUTE.value) != SCROLLTYPE.MINUTE.value) {
                minute.add(fomatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= endMinute; i++) {
                    minute.add(fomatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLLTYPE.SECOND.value) != SCROLLTYPE.SECOND.value) {
                second.add(fomatTimeUnit(startSecond));
            } else {
                for (int i = startSecond; i <= MAXSECOND; i++) {
                    second.add(fomatTimeUnit(i));
                }
            }
        } else if (spanSecond) {
            year.add(String.valueOf(startYear));
            month.add(fomatTimeUnit(startMonth));
            day.add(fomatTimeUnit(startDay));
            hour.add(fomatTimeUnit(startHour));
            minute.add(fomatTimeUnit(startMinute));


            if ((scrollUnits & SCROLLTYPE.SECOND.value) != SCROLLTYPE.SECOND.value) {
                second.add(fomatTimeUnit(startSecond));
            } else {
                for (int i = startSecond; i <= endSecond; i++) {
                    second.add(fomatTimeUnit(i));
                }
            }
        }

        loadComponent();

    }

    private String fomatTimeUnit(int unit) {
        return unit < 10 ? "0" + String.valueOf(unit) : String.valueOf(unit);
    }

    private void initArrayList() {
        if (year == null) year = new ArrayList<>();
        if (month == null) month = new ArrayList<>();
        if (day == null) day = new ArrayList<>();
        if (hour == null) hour = new ArrayList<>();
        if (minute == null) minute = new ArrayList<>();
        if (second == null) second = new ArrayList<>();
        year.clear();
        month.clear();
        day.clear();
        hour.clear();
        minute.clear();
        second.clear();
    }


    private void addListener() {
        year_pv.setOnSelectListener(text -> {
            selectedCalender.set(Calendar.YEAR, Integer.parseInt(text));
//            monthChange();


        });
        month_pv.setOnSelectListener(text -> {
            selectedCalender.set(Calendar.DAY_OF_MONTH, 1);
            selectedCalender.set(Calendar.MONTH, Integer.parseInt(text) - 1);
//            dayChange();


        });
        day_pv.setOnSelectListener(text -> {
            selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(text));
//            hourChange();

        });
//        hour_pv.setOnSelectListener(text -> {
//            selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(text));
//            minuteChange();
//
//
//        });
//        minute_pv.setOnSelectListener(text -> {
//            selectedCalender.set(Calendar.MINUTE, Integer.parseInt(text));
//            secondChange();
//
//        });
//        second_pv.setOnSelectListener(text -> selectedCalender.set(Calendar.SECOND, Integer.parseInt(text)));

    }

    private void loadComponent() {
        year_pv.setData(year);
        month_pv.setData(month);
        day_pv.setData(day);
//        hour_pv.setData(hour);
//        minute_pv.setData(minute);
//        second_pv.setData(second);
//
        year_pv.setSelected(0);
        month_pv.setSelected(0);
        day_pv.setSelected(0);
//        hour_pv.setSelected(0);
//        minute_pv.setSelected(0);
//        second_pv.setSelected(0);
        excuteScroll();
    }

    private void excuteScroll() {
        year_pv.setCanScroll(year.size() > 1);
        month_pv.setCanScroll(month.size() > 1);
        day_pv.setCanScroll(day.size() > 1);
//        hour_pv.setCanScroll(hour.size() > 1 && (scrollUnits & SCROLLTYPE.HOUR.value) == SCROLLTYPE.HOUR.value);
//        minute_pv.setCanScroll(minute.size() > 1 && (scrollUnits & SCROLLTYPE.MINUTE.value) == SCROLLTYPE.MINUTE.value);
//        second_pv.setCanScroll(second.size() > 1 && (scrollUnits & SCROLLTYPE.SECOND.value) == SCROLLTYPE.SECOND.value);
    }

    private void monthChange() {

        month.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        if (selectedYear == startYear) {
            for (int i = startMonth; i <= MAXMONTH; i++) {
                month.add(fomatTimeUnit(i));
            }
        } else if (selectedYear == endYear) {
            for (int i = 1; i <= endMonth; i++) {
                month.add(fomatTimeUnit(i));
            }
        } else {
            for (int i = 1; i <= MAXMONTH; i++) {
                month.add(fomatTimeUnit(i));
            }
        }
        selectedCalender.set(Calendar.MONTH, Integer.parseInt(month.get(0)) - 1);
        month_pv.setData(month);
        month_pv.setSelected(0);
        excuteAnimator(ANIMATORDELAY, month_pv);

        month_pv.postDelayed(new Runnable() {
            @Override
            public void run() {
                dayChange();
            }
        }, CHANGEDELAY);

    }

    private void dayChange() {

        day.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
        if (selectedYear == startYear && selectedMonth == startMonth) {
            for (int i = startDay; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(fomatTimeUnit(i));
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth) {
            for (int i = 1; i <= endDay; i++) {
                day.add(fomatTimeUnit(i));
            }
        } else {
            for (int i = 1; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(fomatTimeUnit(i));
            }
        }
        selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day.get(0)));
        day_pv.setData(day);
        day_pv.setSelected(0);
        excuteAnimator(ANIMATORDELAY, day_pv);

//        day_pv.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                hourChange();
//            }
//        }, CHANGEDELAY);
    }

//    private void hourChange() {
//        if ((scrollUnits & SCROLLTYPE.HOUR.value) == SCROLLTYPE.HOUR.value) {
//            hour.clear();
//            int selectedYear = selectedCalender.get(Calendar.YEAR);
//            int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
//            int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
//
//            if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay) {
//                for (int i = startHour; i <= MAXHOUR; i++) {
//                    hour.add(fomatTimeUnit(i));
//                }
//            } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay) {
//                for (int i = MINHOUR; i <= endHour; i++) {
//                    hour.add(fomatTimeUnit(i));
//                }
//            } else {
//
//                for (int i = MINHOUR; i <= MAXHOUR; i++) {
//                    hour.add(fomatTimeUnit(i));
//                }
//
//            }
//            selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour.get(0)));
//            hour_pv.setData(hour);
//            hour_pv.setSelected(0);
//            excuteAnimator(ANIMATORDELAY, hour_pv);
//        }
//        hour_pv.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                minuteChange();
//            }
//        }, CHANGEDELAY);
//
//    }

//    private void minuteChange() {
//        if ((scrollUnits & SCROLLTYPE.MINUTE.value) == SCROLLTYPE.MINUTE.value) {
//            minute.clear();
//            int selectedYear = selectedCalender.get(Calendar.YEAR);
//            int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
//            int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
//            int selectedHour = selectedCalender.get(Calendar.HOUR_OF_DAY);
//
//            if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay && selectedHour == startHour) {
//                for (int i = startMinute; i <= MAXMINUTE; i++) {
//                    minute.add(fomatTimeUnit(i));
//                }
//            } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay && selectedHour == endHour) {
//                for (int i = MINMINUTE; i <= endMinute; i++) {
//                    minute.add(fomatTimeUnit(i));
//                }
//            } else if (selectedHour == hour_workStart) {
//                for (int i = minute_workStart; i <= MAXMINUTE; i++) {
//                    minute.add(fomatTimeUnit(i));
//                }
//            } else if (selectedHour == hour_workEnd) {
//                for (int i = MINMINUTE; i <= minute_workEnd; i++) {
//                    minute.add(fomatTimeUnit(i));
//                }
//            } else {
//                for (int i = MINMINUTE; i <= MAXMINUTE; i++) {
//                    minute.add(fomatTimeUnit(i));
//                }
//            }
//            selectedCalender.set(Calendar.MINUTE, Integer.parseInt(minute.get(0)));
//            minute_pv.setData(minute);
//            minute_pv.setSelected(0);
//            excuteAnimator(ANIMATORDELAY, minute_pv);
//
//        }
//        minute_pv.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                secondChange();
//            }
//        }, CHANGEDELAY);
//
//
//    }

//    private void secondChange() {
//        if ((scrollUnits & SCROLLTYPE.SECOND.value) == SCROLLTYPE.SECOND.value) {
//            second.clear();
//            int selectedYear = selectedCalender.get(Calendar.YEAR);
//            int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
//            int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
//            int selectedHour = selectedCalender.get(Calendar.HOUR_OF_DAY);
//            int selectedMinute = selectedCalender.get(Calendar.MINUTE);
//
//            if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay && selectedHour == startHour && selectedMinute == startMinute) {
//                for (int i = startSecond; i <= MAXSECOND; i++) {
//                    second.add(fomatTimeUnit(i));
//                }
//            } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay && selectedHour == endHour && selectedMinute == endMinute) {
//                for (int i = MINSECOND; i <= endSecond; i++) {
//                    second.add(fomatTimeUnit(i));
//                }
//            } else if (selectedHour == hour_workStart) {
//                for (int i = second_workStart; i <= MAXSECOND; i++) {
//                    second.add(fomatTimeUnit(i));
//                }
//            } else if (selectedHour == hour_workEnd) {
//                for (int i = MINSECOND; i <= second_workEnd; i++) {
//                    second.add(fomatTimeUnit(i));
//                }
//            } else {
//                for (int i = MINSECOND; i <= MAXSECOND; i++) {
//                    second.add(fomatTimeUnit(i));
//                }
//            }
//            selectedCalender.set(Calendar.SECOND, Integer.parseInt(second.get(0)));
//            second_pv.setData(second);
//            second_pv.setSelected(0);
//            excuteAnimator(ANIMATORDELAY, second_pv);
//
//        }
//        excuteScroll();
//
//
//    }

    private void excuteAnimator(long ANIMATORDELAY, View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f,
                0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f,
                1.3f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f,
                1.3f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(ANIMATORDELAY).start();
    }


    public int disScrollUnit(SCROLLTYPE... scrolltypes) {
        if (scrolltypes == null || scrolltypes.length == 0)
            scrollUnits = SCROLLTYPE.HOUR.value + SCROLLTYPE.MINUTE.value + SCROLLTYPE.SECOND.value;
        for (SCROLLTYPE scrolltype : scrolltypes) {
            scrollUnits ^= scrolltype.value;
        }
        return scrollUnits;
    }

    public void setMode(MODE mode) {
        this.mode = mode;
        switch (mode.value) {
            case 1:
                disScrollUnit(SCROLLTYPE.HOUR, SCROLLTYPE.MINUTE, SCROLLTYPE.SECOND);
//                hour_pv.setVisibility(View.GONE);
//                minute_pv.setVisibility(View.GONE);
//                second_pv.setVisibility(View.GONE);
//                hour_text.setVisibility(View.GONE);
//                minute_text.setVisibility(View.GONE);
//                second_text.setVisibility(View.GONE);
                break;
            case 2:
                disScrollUnit();
//                hour_pv.setVisibility(View.VISIBLE);
//                minute_pv.setVisibility(View.VISIBLE);
//                second_pv.setVisibility(View.VISIBLE);
//                hour_text.setVisibility(View.VISIBLE);
//                minute_text.setVisibility(View.VISIBLE);
//                second_text.setVisibility(View.VISIBLE);
                break;
            case 3:
                year_pv.setVisibility(View.GONE);
                month_pv.setVisibility(View.GONE);
                day_pv.setVisibility(View.GONE);
                year_text.setVisibility(View.GONE);
                month_text.setVisibility(View.GONE);
                day_text.setVisibility(View.GONE);
                break;

            case 4:
                disScrollUnit(SCROLLTYPE.SECOND);
                year_pv.setVisibility(View.GONE);
                month_pv.setVisibility(View.GONE);
                day_pv.setVisibility(View.GONE);
                year_text.setVisibility(View.GONE);
                month_text.setVisibility(View.GONE);
                day_text.setVisibility(View.GONE);
//                if (!minute_text.getText().toString().trim().equals("分"))
//                    minute_text.setVisibility(View.GONE);
//                second_pv.setVisibility(View.GONE);
//                second_text.setVisibility(View.GONE);
        }
    }

    public void setIsLoop(boolean isLoop) {
        this.year_pv.setIsLoop(isLoop);
        this.month_pv.setIsLoop(isLoop);
        this.day_pv.setIsLoop(isLoop);
//        this.hour_pv.setIsLoop(isLoop);
//        this.minute_pv.setIsLoop(isLoop);
//        this.second_pv.setIsLoop(isLoop);
    }
}

