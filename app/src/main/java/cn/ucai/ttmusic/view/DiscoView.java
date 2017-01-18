package cn.ucai.ttmusic.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import cn.ucai.ttmusic.R;
import cn.ucai.ttmusic.model.db.Music;
import cn.ucai.ttmusic.model.utils.MusicUtil;

public class DiscoView extends View {

    Context mContext;
    Bitmap mBitmap;
    ObjectAnimator discObjectAnimator;
    boolean started = false;

    public DiscoView(Context context) {
        super(context);
        this.mContext = context;
    }

    public DiscoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    private void setView(Bitmap bitmap) {
        initAnimation();
        //最外部的半透明边线
        OvalShape ovalShape0 = new OvalShape();
        ShapeDrawable drawable0 = new ShapeDrawable(ovalShape0);
        drawable0.getPaint().setColor(0x10000000);
        drawable0.getPaint().setStyle(Paint.Style.FILL);
        drawable0.getPaint().setAntiAlias(true);

        //黑色唱片边框
        RoundedBitmapDrawable drawable1 = RoundedBitmapDrawableFactory.create(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.icon_play_disc));
        drawable1.setCircular(true);
        drawable1.setAntiAlias(true);

        //内层黑色边线
        OvalShape ovalShape2 = new OvalShape();
        ShapeDrawable drawable2 = new ShapeDrawable(ovalShape2);
        drawable2.getPaint().setColor(Color.BLACK);
        drawable2.getPaint().setStyle(Paint.Style.FILL);
        drawable2.getPaint().setAntiAlias(true);

        //最里面的图像
        if (bitmap == null) {
            //默认图片
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aaa);
        }
        RoundedBitmapDrawable drawable3 = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        drawable3.setCircular(true);
        drawable3.setAntiAlias(true);

        Drawable[] layers = new Drawable[4];
        layers[0] = drawable0;
        layers[1] = drawable1;
        layers[2] = drawable2;
        layers[3] = drawable3;

        LayerDrawable layerDrawable = new LayerDrawable(layers);

        int width = 10;
        //针对每一个图层进行填充，使得各个圆环之间相互有间隔，否则就重合成一个了。
        //轮盘的宽度经过测量，约为70px，因此：
        // （L3边距-L2边距）-（L2边距-L1边距）>=70
        layerDrawable.setLayerInset(0, 0, 0, 0, 0);
        layerDrawable.setLayerInset(1, width, width, width, width);
        layerDrawable.setLayerInset(2, width * 9, width * 9, width * 9, width * 9);
        layerDrawable.setLayerInset(3, width * 10, width * 10, width * 10, width * 10);

        this.setBackground(layerDrawable);
    }

    private void initAnimation() {
        discObjectAnimator = ObjectAnimator.ofFloat(this, "rotation", 0, 360);
        discObjectAnimator.setDuration(20000);
        //使ObjectAnimator动画匀速平滑旋转
        discObjectAnimator.setInterpolator(new LinearInterpolator());
        //无限循环旋转
        discObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        discObjectAnimator.setRepeatMode(ValueAnimator.RESTART);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    public void initByBitmap(Bitmap bitmap) {
        release();
        this.mBitmap = bitmap;
        setView(mBitmap);
    }

    public void initByMusic(Music music) {
        release();
        started = false;
        this.mBitmap = MusicUtil.getAlbumImage(mContext, music.getSongId(), music.getAlbumId());
        setView(mBitmap);
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    public void start() {
        discObjectAnimator.start();
        started = true;
    }

    public void reStart() {
        if (discObjectAnimator.isPaused()) {
            discObjectAnimator.resume();
        }
    }

    public void pause() {
        discObjectAnimator.pause();
    }

    public boolean isStarted() {
        return started;
    }

    public void release() {
        if (mBitmap != null) {
            mBitmap.recycle();
        }
        if (discObjectAnimator != null) {
            discObjectAnimator.cancel();
        }
    }

}
