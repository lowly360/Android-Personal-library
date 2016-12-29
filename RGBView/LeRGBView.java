import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.lelight.jmwifi.R;
import cn.lelight.jmwifi.utils.DensityUtils;


/**
 * Copyright 2016 Lelight
 * All right reserved.
 * <p>
 * Created by itLowly
 * <p>
 * on 2016/10/14 09:39
 */

public class LeRGBView extends View {

    private Paint paintBg;
    private Paint paintCir;
    private Paint paintleftRect;
    private Paint paintleftFlagRect;
    private Paint paintShape;

    // RGB 矩形
    private Paint paintRGBRect;

    private Paint commonPaint;

    private Rect leftRect;

    private Context mContext;
    private int width, height;

    private int mCurrColor;
    private Resources res;
    private Shader shader;
    private Shader shaderleftRect;
    private Shader shaderRGBRect;
    private Shader shaderShape;

    private static int bright = 100;
    private int brightTemp = 0;

    private float toughX = -100, toughY = -100;
    private float toughDownX = 0, toughDownY = 0;
    private int controlType = 0;

    private static boolean isAdd = false;

    private float hprogress = 0;

    private int[] mColor = new int[]{
            Color.RED, Color.parseColor("#FFFFFF00"), Color.GREEN, Color.parseColor("#FF00FFFF")
            , Color.BLUE, Color.parseColor("#FFFF00FF"), Color.RED};

    // 两个圆的渐变x坐标
    private int circleX1, circleX2;
    private Paint circlePaint;
    private Paint circlePaint2;
    private Shader circleShader;
    private Shader circleShader2;
    private int circle1R, circle2R;

    // 黑色图层
    private Paint shadePaint;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                invalidate();
            } else if (msg.what == 100) {
                if (isAttach) {
                    circleX1 -= 2;
                    if (circleX1 <= -circle1R) {
                        circleX1 = LeRGBView.this.width + circle1R;
                    }

                    circleX2 -= 2;
                    if (circleX2 <= -circle2R) {
                        circleX2 = LeRGBView.this.width + circle2R;
                    }

                    circleShader = new RadialGradient(circleX1, LeRGBView.this.height / 2, circle1R,
                            new int[]{Color.argb(136, 255, 255, 255), Color.argb(0, 255, 255, 255)},
                            null, Shader.TileMode.CLAMP);
                    circlePaint.setShader(circleShader);

                    circleShader2 = new RadialGradient(circleX2, LeRGBView.this.height / 2 + 60, circle2R,
                            new int[]{Color.argb(126, 255, 255, 255), Color.argb(0, 255, 255, 255)}, null, Shader.TileMode.CLAMP);
                    circlePaint2.setShader(circleShader2);

                    synchronized (whitCricleList) {
                        for (int i = 0; i < whitCricleList.size(); i++) {
                            if (whitCricleList.get(i).r - 1 != 0) {
                                whitCricleList.get(i).r--;
                            } else {
                                whitCricleList.get(i).r = 0;
                            }
                        }
                    }

                    for (int i = 0; i < whitCricleList.size(); i++) {
                        if (whitCricleList.get(i).r == 0) {
                            whitCricleList.remove(i);
                            i--;
                        }
                    }
                    handler.sendEmptyMessage(0);
                    handler.sendEmptyMessageDelayed(100, 40);
                }
            }
        }
    };
    private float progressTemp;
    private Thread alphaThread1;
    private Thread alphaThread2;

    private List<MyWhitCricle> whitCricleList;
    private long lastTime = 0;

    private boolean isAttach = true;

    public LeRGBView(Context context) {
        super(context);
        initView(context);
    }

    public LeRGBView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LeRGBView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setBright(int bright) {
        invalidate();
        LeRGBView.bright = bright;

    }

    private void initView(Context context) {
        this.mContext = context;
        res = getResources();
        mCurrColor = getResources().getColor(R.color.colorPrimary);

        paintCir = new Paint();
        paintCir.setColor(Color.WHITE);
        paintCir.setAntiAlias(true);
        paintCir.setAlpha(0);

        paintleftFlagRect = new Paint();
        paintleftFlagRect.setColor(Color.WHITE);
        paintleftFlagRect.setAntiAlias(true);

        commonPaint = new Paint();
        commonPaint.setColor(Color.WHITE);
        commonPaint.setAntiAlias(true);

        whitCricleList = new ArrayList<>();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            this.width = width;
        } else {
            // 不精确
            this.width = (int) DensityUtils.getMaxWigthPx(mContext);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            this.height = height;
        } else {
            // 不精确
            this.height = (int) DensityUtils.getMaxHeightPx(mContext);
        }

        initData();

        setMeasuredDimension(this.width, this.height);

        // 开始平移
        circleX1 = this.width / 2 + circle1R;
        circleX2 = circleX1 + circle2R;

        handler.sendEmptyMessageDelayed(100, 40);
    }


    private void initData() {
        // -----------------------------------------
        // 渐变白色圆形
        circlePaint = new Paint();
        circlePaint.setColor(Color.WHITE);
        circlePaint.setAntiAlias(true);

        circle1R = (width / 4) * 3;
        circle2R = width / 2;

        circleShader = new RadialGradient(circleX1, LeRGBView.this.height / 2, circle1R,
                new int[]{Color.argb(136, 255, 255, 255), Color.argb(0, 255, 255, 255)},
                null, Shader.TileMode.CLAMP);
        circlePaint.setShader(circleShader);

        circlePaint2 = new Paint();
        circlePaint2.setColor(Color.WHITE);
        circlePaint2.setAntiAlias(true);

        circleShader2 = new RadialGradient(circleX2, LeRGBView.this.height / 2 + 60, circle2R,
                new int[]{Color.argb(126, 255, 255, 255), Color.argb(0, 255, 255, 255)}, null, Shader.TileMode.CLAMP);
        circlePaint2.setShader(circleShader2);

        shadePaint = new Paint();
        shadePaint.setColor(Color.argb(48, 0, 0, 0));
        shadePaint.setAntiAlias(true);
        // -----------------------------------------
        paintBg = new Paint();
        paintBg.setColor(mCurrColor);
        paintBg.setAntiAlias(true);
        shader = new LinearGradient(0, 0,
                0, this.height, new int[]{Color.RED, Color.WHITE, Color.RED}
                , null, Shader.TileMode.REPEAT);
        paintBg.setShader(shader);
        // -----------------------------------------
        leftRect = new Rect(36, this.height / 8, 36 + 20, this.height * 7 / 8);

        paintleftRect = new Paint();
        paintleftRect.setColor(mCurrColor);
        paintleftRect.setAntiAlias(true);
        shaderleftRect = new LinearGradient(0, 0,
                0, this.height, new int[]{mCurrColor, Color.WHITE}
                , null, Shader.TileMode.REPEAT);
        paintleftRect.setShader(shaderleftRect);

        // -----------------------------------------------
        paintShape = new Paint();
        paintShape.setColor(mCurrColor);
        paintShape.setAntiAlias(true);

        // RGB
        paintRGBRect = new Paint();
        paintRGBRect.setColor(Color.WHITE);
        paintRGBRect.setAntiAlias(true);
        shaderRGBRect = new LinearGradient(36, 0,
                this.width - 36, 0, mColor
                , null, Shader.TileMode.REPEAT);
        paintRGBRect.setShader(shaderRGBRect);

        // -------------------------------------------------
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttach = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 是否初始化好了
        canvas.drawRect(0, 0, width, height, paintBg);
        canvas.drawCircle(toughX, toughY, 50, paintCir);

        canvas.drawRect(0, 0, width, height, shadePaint);

        // 画圆
        canvas.drawCircle(circleX1, height / 2, circle1R, circlePaint);
        canvas.drawCircle(circleX2, height / 2 + 60, circle2R, circlePaint2);

        if (paintleftRect.getAlpha() != 0 && controlType == 2) {
            // 控制亮度
            // --------------左边滑条
            int shapeWidth = 3;
            canvas.drawRect(leftRect.left - shapeWidth, leftRect.top - shapeWidth, leftRect.right + shapeWidth, leftRect.bottom + shapeWidth, commonPaint);
            canvas.drawRect(leftRect, paintleftRect);
            //
            float i = leftRect.top + (leftRect.bottom - leftRect.top) * ((100 - bright) / 100.0f);

            Path path = new Path();
            path.moveTo(36 + 20 + 20 + 2 * shapeWidth, i - 10);// 此点为多边形的起点
            path.lineTo(36 + 20 + 2 * shapeWidth, i);
            path.lineTo(36 + 20 + 20 + 2 * shapeWidth, i + 10);
            path.close(); // 使这些点构成封闭的多边形
            canvas.drawPath(path, commonPaint);
        }

        if (paintRGBRect.getAlpha() != 0 && controlType == 1) {
            // ----------------------------- RGB
            int shapeWidth = 3;
            canvas.drawRect(36 - shapeWidth, 72 - shapeWidth, width - 36 + shapeWidth, 72 + 20 + shapeWidth, commonPaint);
            canvas.drawRect(36, 72, width - 36, 72 + 20, paintRGBRect);

            float x = 36 + ((width - 72) * hprogress);

            Path path2 = new Path();
            path2.moveTo(x, 72 + 6 + 20);// 此点为多边形的起点
            path2.lineTo(x - 10, 72 + 6 + 40);
            path2.lineTo(x + 10, 72 + 6 + 40);
            path2.close(); // 使这些点构成封闭的多边形
            canvas.drawPath(path2, commonPaint);
            // -----------------------------
        }

        for (int i = 0; i < whitCricleList.size(); i++) {
            canvas.drawCircle(whitCricleList.get(i).x, whitCricleList.get(i).y, whitCricleList.get(i).r, paintRGBRect);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        toughX = event.getX();
        toughY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                brightTemp = bright;
                progressTemp = hprogress;
                controlType = 0;

                toughDownX = event.getX();
                toughDownY = event.getY();

                commonPaint.setAlpha(0);
                paintleftRect.setAlpha(0);
                paintleftFlagRect.setAlpha(0);
                paintRGBRect.setAlpha(0);
                paintCir.setAlpha(0);

                lastTime = SystemClock.currentThreadTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                // 随机生成气泡
                if (System.currentTimeMillis() - lastTime > 60) {
                    MyWhitCricle newWhiteCricel = new MyWhitCricle();

                    newWhiteCricel.x = (float) (toughX + (Math.random() * 160 - 80));
                    newWhiteCricel.y = (float) (toughY + (Math.random() * 160 - 80));

                    whitCricleList.add(newWhiteCricel);
                    // 最好一次添加后,添加标识
                    lastTime = System.currentTimeMillis();
                }

                if ((Math.abs(toughX - toughDownX) >= 40 && controlType == 0) || controlType == 1) {
                    // 先判断是否进入切换颜色模式
                    float dx = toughX - toughDownX;
                    dx = dx * 3 / 2;
                    float dprogress = dx / width; // 移动了多少
                    hprogress = progressTemp + dprogress;
                    if (hprogress > 1) {
                        hprogress = hprogress - 1;
                    } else if (hprogress < 0) {
                        hprogress = 1 - (-hprogress);
                    }
                    setmCurrBgColor(interpCircleColor(mColor, hprogress));
                    invalidate();
                    if (controlType == 0) {
                        changePainAlpha(1);
                        controlType = 1;
                    }
                    break;
                }

                if ((Math.abs(toughY - toughDownY) > 100 && controlType == 0) || controlType == 2) {
                    float dy = toughY - toughDownY;
                    dy = dy * 3 / 2;
                    int dbright = (int) ((dy / (float) height) * 100);
                    // 改变亮度
                    if (controlType == 0) {
                        changePainAlpha(1);
                        controlType = 2;
                    }
                    bright = brightTemp - dbright;
                    if (bright > 100) {
                        bright = 100;
                    } else if (bright < 0) {
                        bright = 0;
                    }
                    setBrightness();
                    break;
                }
                break;
            case MotionEvent.ACTION_UP:
                changePainAlpha(2);
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 获取颜色
     *
     * @param colors
     * @param unit
     * @return
     */
    private int interpCircleColor(int colors[], float unit) {
        if (unit <= 0) {
            return colors[0];
        }
        if (unit >= 1) {
            return colors[colors.length - 1];
        }

        float p = unit * (colors.length - 1);
        int i = (int) p;
        p -= i;
        int c0 = colors[i];
        int c1 = colors[i + 1];
        int a = ave(Color.alpha(c0), Color.alpha(c1), p);
        int r = ave(Color.red(c0), Color.red(c1), p);
        int g = ave(Color.green(c0), Color.green(c1), p);
        int b = ave(Color.blue(c0), Color.blue(c1), p);

        return Color.argb(a, r, g, b);
    }

    private int ave(int s, int d, float p) {
        return s + Math.round(p * (d - s));
    }

    /**
     * 改变两滑条的透明度
     *
     * @param type
     */
    private void changePainAlpha(int type) {
        final int k = 10;
        if (type == 1) {
            isAdd = true;
            alphaThread1 = new Thread() {
                @Override
                public void run() {
                    int alpha = 0;
                    while (alpha < 255 && isAdd) {
                        if (commonPaint.getAlpha() + k > 255) {
                            commonPaint.setAlpha(255);
                            paintleftRect.setAlpha(255);
                            paintleftFlagRect.setAlpha(255);
                            paintRGBRect.setAlpha(255);
                            paintCir.setAlpha(255);
                            handler.sendEmptyMessage(0);
                            break;
                        }
                        commonPaint.setAlpha(alpha);
                        paintleftRect.setAlpha(alpha);
                        paintleftFlagRect.setAlpha(alpha);
                        paintRGBRect.setAlpha(alpha);
                        paintCir.setAlpha(alpha);
                        alpha += k;

                        SystemClock.sleep(10);
                        handler.sendEmptyMessage(0);
                    }
                }
            };
            if (alphaThread2 != null) {
                if (alphaThread2.isAlive()) {
                    alphaThread2.interrupt();
                }
            }
            alphaThread1.start();
            return;
        }
        if (type == 2) {
            isAdd = false;
            alphaThread2 = new Thread() {
                @Override
                public void run() {
                    int alpha = 255;
                    while (alpha > 0 && !isAdd) {
                        if (alpha < 0) {
                            commonPaint.setAlpha(0);
                            paintleftRect.setAlpha(0);
                            paintleftFlagRect.setAlpha(0);
                            paintCir.setAlpha(0);
                            paintRGBRect.setAlpha(0);
                            controlType = 0;
                            handler.sendEmptyMessage(0);
                            break;
                        }

                        commonPaint.setAlpha(alpha);
                        paintleftRect.setAlpha(alpha);
                        paintleftFlagRect.setAlpha(alpha);
                        paintCir.setAlpha(alpha);
                        paintRGBRect.setAlpha(alpha);
                        alpha -= k;

                        SystemClock.sleep(10);
                        handler.sendEmptyMessage(0);
                    }
                }
            };
            if (alphaThread1 != null) {
                if (alphaThread1.isAlive()) {
                    alphaThread1.interrupt();
                }
            }
            alphaThread2.start();
        }
    }

    /**
     * 设置亮度
     *
     * @return
     */
    private int setBrightness() {
        float proess = (float) bright / 100;
        String s = Integer.toHexString(mCurrColor);
        if (s.length() != 8) {
            for (int i = 0; i < 8 - s.length(); i++) {
                s = "0" + s;
            }
        }
        int color = Color.argb((int) (proess * 255), Integer.valueOf(s.substring(2, 4), 16),
                Integer.valueOf(s.substring(4, 6), 16), Integer.valueOf(s.substring(6, 8), 16));
        // 设置亮度
        shader = new LinearGradient(0, 0,
                0, this.height, new int[]{this.mCurrColor, color, this.mCurrColor}
                , null, Shader.TileMode.REPEAT);
        paintBg.setShader(shader);
        invalidate();
        return color;
    }

    public void setmCurrBgColor(int mCurrColor) {
        this.mCurrColor = mCurrColor;

        shader = new LinearGradient(0, 0,
                0, this.height, new int[]{this.mCurrColor, setBrightness(), this.mCurrColor}
                , null, Shader.TileMode.REPEAT);
        paintBg.setShader(shader);

        shaderleftRect = new LinearGradient(0, 0, 0, this.height, new int[]{this.mCurrColor, Color.WHITE}, null, Shader.TileMode.REPEAT);
        paintleftRect.setShader(shaderleftRect);

        invalidate();
    }

    public int getmCurrColor() {

        return setBrightness();
    }

    class MyWhitCricle {
        public float x, y;
        public int r = DensityUtils.dp2px(getContext(), 8);
    }
}
