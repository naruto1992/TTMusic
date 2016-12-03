package cn.ucai.ttmusic;

public interface I {

    interface PlayState {
        int IS_INIT = 0;
        int IS_PLAY = 1;
        int IS_PAUSE = 2;
    }

    interface PlayMode {
        int MODE_NORMAL = 0;
        int MODE_SINGLE = 1;
        int MODE_SHUFFLE = 2;
    }


}
