package com.sz.dengzh.dandroid_kt.utils.glideUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.util.Util;

import java.security.MessageDigest;


/**
 * Glide 圆形转换器
 * 继承BitmapTransformation ，只能对静态图进行图片变换。
 * 关键是要知道算法换算，顺便了解BitmapShader。
 */
public class GlideCircleTransform extends BitmapTransformation {

    private final String TAG = getClass().getName();

    /**
     * 图像变换最重要的就是transform()方法，这个是我们自定义Transformation的关键方法，我们的处理逻辑都要在这个方法里实现。transform()方法中有四个参数。
     *
     * @param pool         这个是Glide中的BitmapPool缓存池，用于对Bitmap对象进行重用，否则每次图片变换都重新创建Bitmap对象将会非常消耗内存。
     * @param toTransform  这个是原始图片的Bitmap对象，我们就是要对它来进行图片变换。
     * @param outWidth     图片变换后的宽度
     * @param outHeight    图片变换后的高度
     * @return
     */
    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        //举例：宽200、高100情况，size=100，x=50，y=0。也就是(50,0)到(150,100)范围的矩形。
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        //从缓存池取出一个原bitmap
        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }
        //创建一个要转换的bitmap
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);


        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //设置shader，使用位图平铺的渲染效果.
        //CLAMP表示，当所画图形的尺寸大于Bitmap的尺寸的时候，会用Bitmap四边的颜色填充剩余空间
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        //画一个圆
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        return result;
    }

    /**
     * 重写epquals和hashcode方法，确保对象唯一性，以和其他的图片变换做区分
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GlideCircleTransform) {
            return this == obj;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Util.hashCode(TAG.hashCode());
    }

    /**
     * 可通过内部算法 重写此方法自定义图片缓存key
     * @param messageDigest
     */
    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(TAG.getBytes(CHARSET));
    }

}

