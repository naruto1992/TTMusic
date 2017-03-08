package cn.ucai.ttmusic.model.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.model.db.Music;

public class BackgroundUtil {

    //通过资源id获取模糊后的Drawable
    public static Drawable getDrawableByRes(Context context, int musicPicRes) {
        Bitmap bitmap = getBackgroundBitmap(context, musicPicRes);
        return BlurBitmap(context, bitmap);
    }

    //通过歌曲专辑获取模糊后的Drawable
    public static Drawable getDrawableByMusic(Context context, Music music) {
        Bitmap bitmap = MusicUtil.getAlbumImage(context, music.getSongId(), music.getAlbumId());//很可能为空
        if (bitmap == null) {
            bitmap = getBackgroundBitmap(context, R.drawable.aaa); //与轮盘图片一致
        }
        return BlurBitmap(context, bitmap);
    }

    //通过资源id获取并压缩Bitmap
    private static Bitmap getBackgroundBitmap(Context context, int musicPicRes) {
        int screenWidth = PhoneUtil.getScreenWidth(context);
        int screenHeight = PhoneUtil.getScreenHeight(context);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(context.getResources(), musicPicRes, options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;

        if (imageWidth < screenWidth && imageHeight < screenHeight) {
            return BitmapFactory.decodeResource(context.getResources(), musicPicRes);
        }

        int sample = 2;
        int sampleX = imageWidth / PhoneUtil.getScreenWidth(context);
        int sampleY = imageHeight / PhoneUtil.getScreenHeight(context);

        if (sampleX > sampleY && sampleY > 1) {
            sample = sampleX;
        } else if (sampleY > sampleX && sampleX > 1) {
            sample = sampleY;
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = sample;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        return BitmapFactory.decodeResource(context.getResources(), musicPicRes, options);
    }

    //模糊处理
    private static Drawable BlurBitmap(Context context, Bitmap bitmap) {
        /*得到屏幕的宽高比，以便按比例切割图片一部分*/
        final float widthHeightSize = (float) (PhoneUtil.getScreenWidth(context)
                * 1.0 / PhoneUtil.getScreenHeight(context) * 1.0);
        int cropBitmapWidth = (int) (widthHeightSize * bitmap.getHeight());
        int cropBitmapWidthX = (int) ((bitmap.getWidth() - cropBitmapWidth) / 2.0);

        /*切割部分图片*/
        Bitmap cropBitmap = Bitmap.createBitmap(bitmap, cropBitmapWidthX, 0, cropBitmapWidth,
                bitmap.getHeight());
        /*缩小图片*/
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(cropBitmap, bitmap.getWidth() / 50, bitmap
                .getHeight() / 50, false);
        /*模糊化*/
        final Bitmap blurBitmap = FastBlurUtil.doBlur(scaleBitmap, 2, true);//原来是8，但显得太过模糊,2的效果与网易云最接近

        final Drawable foregroundDrawable = new BitmapDrawable(blurBitmap);
        /*加入灰色遮罩层，避免图片过亮影响其他控件*/
        foregroundDrawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        return foregroundDrawable;
    }

}
