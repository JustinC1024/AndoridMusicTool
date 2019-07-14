package com.example.justin.myapplication.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleImageView extends ImageView {

    private Paint mPaint; //画笔

    private int mRadius; //圆形图片的半径

    private float mScale; //图片的缩放比例

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //因为是圆形图片，所以应该让宽高保持一致
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mRadius = size / 2;

        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint = new Paint();
        Bitmap bitmap = drawableToBitmap(getDrawable());

        //初始化BitmapShader，传入bitmap对象
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        //计算缩放比例
        mScale = (mRadius * 2.0f) / Math.min(bitmap.getHeight(), bitmap.getWidth());

        Matrix matrix = new Matrix();
        matrix.setScale(mScale, mScale);
        bitmapShader.setLocalMatrix(matrix);


        mPaint.setShader(bitmapShader);

        //画圆形，指定好中心点坐标、半径、画笔
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);


        /*
        * 边框
        * */
        int roundPx = 25;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE); //这里的颜色决定了边缘的颜色

        Drawable drawable = getDrawable();
        if(drawable == null){
            return ;
        }
        if(getWidth() == 0 || getHeight() == 0){
            return ;
        }

        Bitmap b = ((BitmapDrawable)drawable).getBitmap();
        Bitmap bitmap1 = b.copy(Bitmap.Config.ARGB_8888, true);

        int w = getWidth() ;
        int h = getHeight() ;
        RectF rectF = new RectF(0, 0, w, w);

        Bitmap roundBitmap = getCroppedBitmap(bitmap1, w, roundPx);

        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawBitmap(roundBitmap, 0, 0, null);

    }

    //写一个drawble转BitMap的方法
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /*
    * 边框
    * */
    public static Bitmap getCroppedBitmap(Bitmap bmp, int length,int roundPx) {

        Bitmap sbmp;
        if (bmp.getWidth() != length || bmp.getHeight() != length)
            sbmp = Bitmap.createScaledBitmap(bmp, length, length, false);
        else
            sbmp = bmp;

        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
        final RectF rectF = new RectF(6, 6, sbmp.getWidth() - 6, sbmp.getHeight() - 6);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.WHITE);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }
}
