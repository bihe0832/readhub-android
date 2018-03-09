package com.ottd.libs.framework.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.ottd.libs.framework.R;
import com.ottd.libs.logger.OttdLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: LJM
 * Date&Time: 2017-04-20 & 21:33
 * Describe: Describe Text
 */
public  class SimpleUtils {

    private static final int DEFAULT_WIDTH = 720;

    /**
     * 将 Bitmap 保存到SD卡
     * @param context
     * @param mybitmap
     * @param name
     * @return
     */
    public static boolean saveBitmapToSdCard(Context context, Bitmap mybitmap, String name){
        boolean result = false;
        //创建位图保存目录
        String path = Environment.getExternalStorageDirectory() + "/1000ttt/";
        File sd = new File(path);
        if (!sd.exists()){
            sd.mkdir();
        }

        File file = new File(path+name+".jpg");
        FileOutputStream fileOutputStream = null;
        if (!file.exists()){
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    fileOutputStream = new FileOutputStream(file);
                    mybitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();

                    //update gallery
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(file);
                    intent.setData(uri);
                    context.sendBroadcast(intent);
                    Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
                    result = true;
                }
                else{
                    Toast.makeText(context, "不能读取到SD卡", Toast.LENGTH_SHORT).show();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 手动测量摆放View
     * 对于手动 inflate 或者其他方式代码生成加载的View进行测量，避免该View无尺寸
     * @param v
     * @param width
     * @param height
     */
    public static void layoutView(View v, int width, int height) {
        // validate view.width and view.height
        v.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

        // validate view.measurewidth and view.measureheight
        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    }


    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }



    /**
     * 获取一个 View 的缓存视图
     *  (前提是这个View已经渲染完成显示在页面上)
     * @param view
     * @return
     */
    public static Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }

    /**
     *  对ScrollView进行截图
     * @param scrollView
     * @return
     */
    public static Bitmap shotScrollView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
//            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
        }
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h, Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }


    /**
     * 对ListView进行截图
     * http://stackoverflow.com/questions/12742343/android-get-screenshot-of-all-listview-items
     */
    public static Bitmap shotListView(ListView listview) {

        ListAdapter adapter = listview.getAdapter();
        int itemscount = adapter.getCount();
        int allitemsheight = 0;
        List<Bitmap> bmps = new ArrayList<Bitmap>();

        for (int i = 0; i < itemscount; i++) {

            View childView = adapter.getView(i, null, listview);
            childView.measure(
                    View.MeasureSpec.makeMeasureSpec(listview.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
            childView.setDrawingCacheEnabled(true);
            childView.buildDrawingCache();
            bmps.add(childView.getDrawingCache());
            allitemsheight += childView.getMeasuredHeight();
        }

        Bitmap bigbitmap =
                Bitmap.createBitmap(listview.getMeasuredWidth(), allitemsheight, Bitmap.Config.ARGB_8888);
        Canvas bigcanvas = new Canvas(bigbitmap);

        Paint paint = new Paint();
        int iHeight = 0;

        for (int i = 0; i < bmps.size(); i++) {
            Bitmap bmp = bmps.get(i);
            bigcanvas.drawBitmap(bmp, 0, iHeight, paint);
            iHeight += bmp.getHeight();

            bmp.recycle();
            bmp = null;
        }

        return bigbitmap;
    }


    /**
     * 对RecyclerView进行截图
     * https://gist.github.com/PrashamTrivedi/809d2541776c8c141d9a
     */
    public static Bitmap shotRecyclerView(RecyclerView view) {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(
                        View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(),
                        holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
                height += holder.itemView.getMeasuredHeight();
            }

            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            Drawable lBackground = view.getBackground();
            if (lBackground instanceof ColorDrawable) {
                ColorDrawable lColorDrawable = (ColorDrawable) lBackground;
                int lColor = lColorDrawable.getColor();
                bigCanvas.drawColor(lColor);
            }

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }
        }
        return bigBitmap;
    }

    public static Bitmap getFooter(Context ctx,String id) {
        int destWidth = DEFAULT_WIDTH;   //此处的bitmap已经限定好宽高
        int destHeight = (int)(DEFAULT_WIDTH * 0.388f);
        Bitmap bitmap = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);//初始化画布绘制的图像到icon上
        Rect rectw = new Rect(0,0,destWidth,destHeight);
        Paint rectPaint = new Paint();
        rectPaint.setColor(Color.parseColor("#406D91"));
        rectPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rectw, rectPaint);

        int codeWidth = (int)(destHeight * 2 / 3f);
//        Bitmap temp = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.head);
        Bitmap temp = QRCodeUtil.createQRCodeBitmap("https://readhub.me/topic/" + id, codeWidth, codeWidth);
//        Bitmap tempBitmapT = Bitmap.createScaledBitmap(temp, codeWidth, codeWidth, false);
        Paint mBitPaint = new Paint();
        Rect mSrcRect, mDestRect;
        mSrcRect = new Rect(0, 0, codeWidth, codeWidth);
        // 计算左边位置
        int left = (DEFAULT_WIDTH - codeWidth) / 2;
        // 计算上边位置
        int top = (destHeight - codeWidth) / 4;
        mDestRect = new Rect(left, top, left + codeWidth, top + codeWidth);
        canvas.drawBitmap(temp, mSrcRect, mDestRect, mBitPaint);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        textPaint.setTypeface( font );
        textPaint.setTextSize(24);
        textPaint.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        int textTop = (int)fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        int textBottom = (int)fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom

        int baseLineY = top + codeWidth + (destHeight - top - codeWidth + Math.abs(textTop)  + Math.abs(textBottom)) / 2 ;
        canvas.drawText("长按上方二维码查看话题详情",rectw.centerX(),baseLineY,textPaint);
        return bitmap;

    }

    public static Bitmap getHeader() {
        int destWidth = DEFAULT_WIDTH;
        int destHeight = (int)(DEFAULT_WIDTH * 0.1625f);
        Bitmap bitmap = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);//初始化画布绘制的图像到icon上
        Rect rectw = new Rect(0,0,destWidth,destHeight);
        Paint rectPaint = new Paint();
        rectPaint.setColor(Color.parseColor("#406D91"));
        rectPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rectw, rectPaint);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        textPaint.setTypeface( font );
        textPaint.setTextSize(38);
        textPaint.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom

        int baseLineY = (int) (rectw.centerY() - top/2 - bottom/2);//基线中间点的y轴计算公式

        canvas.drawText("Readhub — 每天几分钟，了解互联网",rectw.centerX(),baseLineY,textPaint);

        return bitmap;

    }

    public static Bitmap mergeBitmap_TB(Bitmap topBitmap, Bitmap bottomBitmap, boolean isBaseMax) {

        if (topBitmap == null || topBitmap.isRecycled()
                || bottomBitmap == null || bottomBitmap.isRecycled()) {
            OttdLog.e("topBitmap=" + topBitmap + ";bottomBitmap=" + bottomBitmap);
            return null;
        }
        int width = 0;
        if (isBaseMax) {
            width = topBitmap.getWidth() > bottomBitmap.getWidth() ? topBitmap.getWidth() : bottomBitmap.getWidth();
        } else {
            width = topBitmap.getWidth() < bottomBitmap.getWidth() ? topBitmap.getWidth() : bottomBitmap.getWidth();
        }
        OttdLog.e("topBitmap:width=" + topBitmap.getWidth() + ";height=" + topBitmap.getHeight());
        OttdLog.e("bottomBitmap:width=" + bottomBitmap.getWidth() + ";height=" + bottomBitmap.getHeight());
        if(width > DEFAULT_WIDTH){
            width = DEFAULT_WIDTH;
        }
        Bitmap tempBitmapT = topBitmap;
        Bitmap tempBitmapB = bottomBitmap;
        OttdLog.e("tempBitmapT:width=" + tempBitmapT.getWidth() + ";height=" + tempBitmapT.getHeight());
        OttdLog.e("tempBitmapB:width=" + tempBitmapB.getWidth() + ";height=" + tempBitmapB.getHeight());
        if (topBitmap.getWidth() != width) {
            tempBitmapT = Bitmap.createScaledBitmap(topBitmap, width, (int)(topBitmap.getHeight()*1f/topBitmap.getWidth()*width), false);
        }

        if (bottomBitmap.getWidth() != width) {
            tempBitmapB = Bitmap.createScaledBitmap(bottomBitmap, width, (int)(bottomBitmap.getHeight()*1f/bottomBitmap.getWidth()*width), false);
        }

        OttdLog.e("topBitmap:width=" + topBitmap.getWidth() + ";height=" + topBitmap.getHeight());
        OttdLog.e("bottomBitmap:width=" + bottomBitmap.getWidth() + ";height=" + bottomBitmap.getHeight());
        OttdLog.e("tempBitmapT:width=" + tempBitmapT.getWidth() + ";height=" + tempBitmapT.getHeight());
        OttdLog.e("tempBitmapB:width=" + tempBitmapB.getWidth() + ";height=" + tempBitmapB.getHeight());
        int height = tempBitmapT.getHeight() + tempBitmapB.getHeight();
        OttdLog.e("width=" + width);
        OttdLog.e("height=" + height);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);

        Rect topRect = new Rect(0, 0, tempBitmapT.getWidth(), tempBitmapT.getHeight());
        canvas.drawBitmap(tempBitmapT, topRect, topRect, null);

        Rect bottomRect  = new Rect(0, 0, tempBitmapB.getWidth(), tempBitmapB.getHeight());
        Rect bottomRectT  = new Rect(0, tempBitmapT.getHeight(), width, height);
        canvas.drawBitmap(tempBitmapB, bottomRect, bottomRectT, null);
        OttdLog.e("bitmap:width=" + bitmap.getWidth() + ";height=" + bitmap.getHeight());

        return bitmap;
    }
}
