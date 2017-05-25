package com.zhangmiao.mykeyboardview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: zhangmiao
 * Date: 2017/5/24
 */
public class MyKeyboardView extends ViewGroup {

    private static final String TAG = MyKeyboardView.class.getSimpleName();

    private int defaultWidth = 953;
    private int defaultHeight = 296;

    private List<TextView> textViewList;
    private List<ImageView> imageViewList;

    char[] chars;

    private EditText editText;
    ImageView delete;

    private LinearLayout linearLayout;

    private List<View> viewList;

    private boolean isCapital = false;

    public MyKeyboardView(Context context) {
        super(context);
        init(context);
    }

    public MyKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {

        setBackgroundColor(Color.parseColor("#00000000"));
        chars = new char[]{'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm'};
        int[] imageId = {R.drawable.keyboard_sensitivity_default, R.drawable.space_selector};

        editText = new EditText(context);
        editText.setText("");
        editText.setBackgroundColor(Color.parseColor("#00000000"));
        InputFilter[] filters = {new InputFilter.LengthFilter(66)};
        editText.setFilters(filters);
        editText.setTextSize(30);
        editText.setInputType(InputType.TYPE_NULL);

        delete = new ImageView(context);
        delete.setImageResource(R.drawable.keyboard_delete_selector);
        delete.setPadding(0,10,0,0);
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().length() > 0) {
                    editText.setText(editText.getText().toString().substring(0, editText.getText().length() - 1));
                    editText.setSelection(editText.getText().length());
                }
            }
        });

        LinearLayout.LayoutParams editTextLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        editTextLayoutParams.weight = 1;
        editTextLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        LinearLayout.LayoutParams deleteLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        deleteLayoutParams.topMargin = 12;
        deleteLayoutParams.rightMargin = 36;

        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundResource(R.drawable.enter_text_image);
        linearLayout.addView(editText, editTextLayoutParams);
        linearLayout.addView(delete, deleteLayoutParams);

        addView(linearLayout);

        viewList = new ArrayList<>();
        textViewList = new ArrayList<>();
        imageViewList = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            final TextView textView = new TextView(context);
            textView.setText(chars[i] + "");
            textView.setBackgroundResource(R.drawable.keyboard_selector);
            textView.setHeight(78);
            textView.setWidth(78);
            textView.setTextSize(32);
            textView.setTextColor(Color.parseColor("#999999"));
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editText != null) {
                        editText.setText(editText.getText().toString() + textView.getText());
                        editText.setSelection(editText.getText().length());
                    }
                }
            });
            textViewList.add(textView);
        }
        ImageView sensitivityImageView = new ImageView(context);
        sensitivityImageView.setImageResource(imageId[0]);
        //sensitivityImageView.setMinimumWidth(78);
        //sensitivityImageView.setMinimumHeight(77);
        sensitivityImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < chars.length; i++) {
                    if (isCapital) {
                        chars[i] += 32;
                    } else {
                        chars[i] -= 32;
                    }
                }
                isCapital = !isCapital;
                invalidate();
            }
        });
        imageViewList.add(sensitivityImageView);

        ImageView spaceImageView = new ImageView(context);
        spaceImageView.setImageResource(imageId[1]);
        //spaceImageView.setMinimumWidth(135);
        //spaceImageView.setMinimumHeight(77);
        spaceImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText != null) {
                    editText.setText(editText.getText().toString() + " ");
                    editText.setSelection(editText.getText().length());
                }
            }
        });
        imageViewList.add(spaceImageView);

        int n = 0;
        for (int i = 0; i < textViewList.size() + imageViewList.size(); i++) {
            if (i == 19) {
                viewList.add(imageViewList.get(0));
            } else if (i == 27) {
                viewList.add(imageViewList.get(1));
            } else {
                viewList.add(textViewList.get(n));
                n++;
            }
            addView(viewList.get(i));
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged");
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //UNSPECIFIED, EXACTLY, AT_MOST
        Log.d(TAG, "UNSPECIFIED = " + MeasureSpec.UNSPECIFIED);
        Log.d(TAG, "EXACTLY = " + MeasureSpec.EXACTLY);
        Log.d(TAG, "AT_MOST = " + MeasureSpec.AT_MOST);

        Log.d(TAG, "widthMode = " + widthMode + ",widthSize = " + widthSize);
        Log.d(TAG, "heightMode = " + heightMode + ",heightSize = " + heightSize);

        widthSize = 953;
        heightSize = 380;

        setMeasuredDimension(widthSize, heightSize);

        int childCount = getChildCount();
        Log.d(TAG, "onMeasure childCount = " + childCount);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        Log.d(TAG, "onLayout");
        int layoutWidth = linearLayout.getMeasuredWidth();
        int layoutHeight = linearLayout.getMeasuredHeight();

        linearLayout.layout(0, 0, layoutWidth, layoutHeight);

        int offsetX = 0;
        int offsetY = layoutHeight + 12;

        for (int n = 0; n < 10; n++) {
            View scrap = viewList.get(n);

            int width = scrap.getMeasuredWidth();
            int height = scrap.getMeasuredHeight();

            int top = offsetY;
            if (n == 0) {
                offsetX += 24;
            } else {
                offsetX += 14;
            }

            top += 20;
            scrap.layout(offsetX, top, offsetX + width, top + height);
            offsetX += width;
        }
        offsetX = 0;

        for (int n = 10; n < 19; n++) {
            View scrap = viewList.get(n);

            int width = scrap.getMeasuredWidth();
            int height = scrap.getMeasuredHeight();

            offsetY = height + 20 + 12 + layoutHeight + 12;

            if (n == 10) {
                offsetX += 68;
            } else {
                offsetX += 14;
            }
            scrap.layout(offsetX, offsetY, offsetX + width, offsetY + height);
            offsetX += width;
        }

        offsetX = 0;

        for (int n = 19; n < 28; n++) {
            View scrap = viewList.get(n);

            int width = scrap.getMeasuredWidth();
            int height = scrap.getMeasuredHeight();

            offsetY = height * 2 + 20 + 12 * 2 + layoutHeight + 12;


            if (n == 19) {
                offsetX += 68;
                Log.d(TAG,"n = 19, height = "+height);
                Log.d(TAG,"n = 19, left = "+offsetX+",top = "+offsetY+",right = "+(offsetX + width)+",bottom = "+(offsetY + height));
            } else {
                offsetX += 14;
                if(n == 20){
                    Log.d(TAG,"n = 20, height = "+height);
                    Log.d(TAG,"n = 20, left = "+offsetX+",top = "+offsetY+",right = "+(offsetX + width)+",bottom = "+(offsetY + height));
                }
            }
            scrap.layout(offsetX, offsetY, offsetX + width, offsetY + height);
            offsetX += width;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw");

        Resources resources = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.keyboard_background);

        Paint paint = new Paint();

        canvas.drawBitmap(bitmap, 0, linearLayout.getMeasuredHeight() + 12, paint);

        for (int i = 0; i < textViewList.size(); i++) {
            TextView textView = textViewList.get(i);
            textView.setText(chars[i] + "");
        }
        ImageView imageView = imageViewList.get(0);
        if (isCapital) {
            imageView.setImageResource(R.drawable.keyboard_sensitivity_press);
        } else {
            imageView.setImageResource(R.drawable.keyboard_sensitivity_default);
        }
        super.onDraw(canvas);
    }

    public void clearKeyboard(){
        if(isCapital){
            for(int i = 0;i<chars.length;i++){
                chars[i] += 32;
            }
            isCapital = !isCapital;
        }
        editText.setText("");
        editText.setSelection(editText.getText().length());
        invalidate();
    }

    public void clearInputString(){
        editText.setText("");
        editText.setSelection(editText.getText().length());
    }

    public String getInputString(){
        return editText.getText().toString();
    }

}
