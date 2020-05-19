package com.ruanyun.australianews.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ruanyun.australianews.R;
import com.ruanyun.australianews.util.StringUtil;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * @author hdl
 * @description
 * @date 2019/9/9
 */
public class SelectDateTimeDialog {

    public static final int MODEL_DATE_TIME = 1;
    public static final int MODEL_DATE = 2;
    public static final int MODEL_TIME = 3;

    private Context mContext;
    private TextView tvDate;
    private TextView tvTime;
    private DatePicker datePicker;
    private TimePicker timePicker;

    private int model;//类型1选择日期时间、2选择日期、3选择时间


    private AlertDialog dialog;

    private int Year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    public SelectDateTimeDialog(Context context, int model) {
        mContext = context;
        this.model = model;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_select_date_time, null);
        tvDate = view.findViewById(R.id.tvDate);
        tvTime = view.findViewById(R.id.tvTime);
        datePicker = view.findViewById(R.id.datePicker);
        timePicker = view.findViewById(R.id.timePicker);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        TextView tvConfirm = view.findViewById(R.id.tvConfirm);

        //获取年月日分秒信息
        Calendar cal = Calendar.getInstance();
        Year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;     //月份从0开始的 所以要加1
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);

        if (model == MODEL_DATE_TIME || model == MODEL_DATE) {
            setDatePickerDividerColor(datePicker, context);
            updateDate();
            //datePicker初始化 设置初始日期
            datePicker.init(Year, cal.get(Calendar.MONTH), day, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Year = year;
                    month = monthOfYear + 1;
                    day = dayOfMonth;
                    updateDate();
                }
            });
        }
        if (model == MODEL_DATE_TIME || model == MODEL_TIME) {
            setTimePickerDividerColor(timePicker, context);
            updateTime();
            timePicker.setIs24HourView(true);   //设置时间显示为24小时
            timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {  //获取当前选择的时间
                @Override
                public void onTimeChanged(TimePicker view, int hour1, int minute1) {
                    hour = hour1;
                    minute = minute1;
                    updateTime();
                }
            });
        }

        if (model == MODEL_DATE) {
            tvDate.setVisibility(View.VISIBLE);
            datePicker.setVisibility(View.VISIBLE);
            tvTime.setVisibility(View.GONE);
            timePicker.setVisibility(View.GONE);
        } else if (model == MODEL_TIME) {
            tvDate.setVisibility(View.GONE);
            datePicker.setVisibility(View.GONE);
            tvTime.setVisibility(View.VISIBLE);
            timePicker.setVisibility(View.VISIBLE);
        } else {
            tvDate.setVisibility(View.VISIBLE);
            datePicker.setVisibility(View.VISIBLE);
            tvTime.setVisibility(View.VISIBLE);
            timePicker.setVisibility(View.GONE);
            tvDate.setSelected(true);
            tvDate.setOnClickListener(v -> {
                datePicker.setVisibility(View.VISIBLE);
                timePicker.setVisibility(View.GONE);
                tvDate.setSelected(true);
                tvTime.setSelected(false);
            });
            tvTime.setOnClickListener(v -> {
                datePicker.setVisibility(View.GONE);
                timePicker.setVisibility(View.VISIBLE);
                tvDate.setSelected(false);
                tvTime.setSelected(true);
            });
        }


        tvCancel.setOnClickListener(v -> dismiss());
        tvConfirm.setOnClickListener(v -> {
            dismiss();
            if (mOnSelectDateTimeListener != null) {
                String dateTime;
                String format;
                String oldFormat;
                switch (model) {
                    case MODEL_DATE_TIME:
                        format = "yyyy-MM-dd HH:mm";
                        oldFormat = "yyyy年MM月dd日HH:mm";
                        dateTime = tvDate.getText().toString() + tvTime.getText().toString();
                        break;
                    case MODEL_DATE:
                        format = "yyyy-MM-dd";
                        oldFormat = "yyyy年MM月dd日";
                        dateTime = tvDate.getText().toString();
                        break;
                    case MODEL_TIME:
                        format = "HH:mm";
                        oldFormat = "HH:mm";
                        dateTime = tvTime.getText().toString();
                        break;
                    default:
                        return;
                }
                mOnSelectDateTimeListener.onSelectDateTime(StringUtil.getFormatStrFromFormatStr(format, oldFormat, dateTime));
            }
        });


        builder.setView(view);
        dialog = builder.create();
    }

    private void updateDate() {
        String monthStr = String.valueOf(month);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }
        String dayStr = String.valueOf(day);
        if (dayStr.length() == 1) {
            dayStr = "0" + dayStr;
        }
        tvDate.setText(String.format("%s年%s月%s日", Year, monthStr, dayStr));
    }

    private void updateTime() {
        String hourStr = String.valueOf(hour);
        if (hourStr.length() == 1) {
            hourStr = "0" + hourStr;
        }
        String minuteStr = String.valueOf(minute);
        if (minuteStr.length() == 1) {
            minuteStr = "0" + minuteStr;
        }
        tvTime.setText(String.format("%s:%s", hourStr, minuteStr));
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }

//        Window window = dialog.getWindow();
//        if (window != null) {
//            window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
//            WindowManager.LayoutParams params = window.getAttributes();
//            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
//            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            params.gravity = Gravity.CENTER;
//            window.setAttributes(params);
//        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    private OnSelectDateTimeListener mOnSelectDateTimeListener;

    public interface OnSelectDateTimeListener {
        void onSelectDateTime(String dateTime);
    }

    public void setOnSelectDateTimeListener(OnSelectDateTimeListener OnSelectDateTimeListener) {
        this.mOnSelectDateTimeListener = OnSelectDateTimeListener;
    }

    /**
     * 设置日期选择器的分割线颜色
     */
    public void setDatePickerDividerColor(DatePicker datePicker, Context context) {
        Resources systemResources = Resources.getSystem();
        int yearNumberPickerId = systemResources.getIdentifier("year", "id", "android");
        int monthNumberPickerId = systemResources.getIdentifier("month", "id", "android");
        int dayNumberPickerId = systemResources.getIdentifier("day", "id", "android");
        NumberPicker yearNumberPicker = (NumberPicker) datePicker.findViewById(yearNumberPickerId);
        NumberPicker monthNumberPicker = (NumberPicker) datePicker.findViewById(monthNumberPickerId);
        NumberPicker dayNumberPicker = (NumberPicker) datePicker.findViewById(dayNumberPickerId);
        setNumberPickerDivider(yearNumberPicker, context);
        setNumberPickerDivider(monthNumberPicker, context);
        setNumberPickerDivider(dayNumberPicker, context);
    }

    /**
     * 设置时间选择器的分割线颜色
     */
    public void setTimePickerDividerColor(TimePicker timePicker, Context context) {
        Resources systemResources = Resources.getSystem();
        int hourNumberPickerId = systemResources.getIdentifier("hour", "id", "android");
        int minuteNumberPickerId = systemResources.getIdentifier("minute", "id", "android");
        NumberPicker hourNumberPicker = (NumberPicker) timePicker.findViewById(hourNumberPickerId);
        NumberPicker minuteNumberPicker = (NumberPicker) timePicker.findViewById(minuteNumberPickerId);
        setNumberPickerDivider(hourNumberPicker, context);
        setNumberPickerDivider(minuteNumberPicker, context);
    }


    private void setNumberPickerDivider(NumberPicker numberPicker, Context context) {
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            try {
                Field dividerField = numberPicker.getClass().getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
                ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.theme_color));
                dividerField.set(numberPicker, colorDrawable);
                numberPicker.invalidate();
            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

}
