package cn.ucai.ttmusic.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;

import java.lang.reflect.Field;

public class MyPlayViewPager extends ViewPager {

    public MyPlayViewPager(Context context) {
        super(context);
        init(context);
    }

    public MyPlayViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            SpeedScroller scroller = new SpeedScroller(this.getContext(),
                    new AccelerateInterpolator());
            mScroller.set(this, scroller);
            scroller.setmDuration(500);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {
        }
    }

}
