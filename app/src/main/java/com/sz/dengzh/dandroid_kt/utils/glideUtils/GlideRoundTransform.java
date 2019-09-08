package com.sz.dengzh.dandroid_kt.utils.glideUtils;

import android.content.res.Resources;
import android.graphics.*;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.util.Util;

import java.security.MessageDigest;

/**
 * Glide 圆角转换器
 * 图形变换，用第三方库好些
 * https://github.com/wasabeef/glide-transformations
 */
public class GlideRoundTransform extends BitmapTransformation {

    private final String TAG = getClass().getName();
    private static float radius = 0f;


    public GlideRoundTransform(int dp) {
        this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
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