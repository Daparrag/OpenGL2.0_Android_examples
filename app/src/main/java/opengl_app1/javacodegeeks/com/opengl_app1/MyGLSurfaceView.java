package opengl_app1.javacodegeeks.com.opengl_app1;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;


public class MyGLSurfaceView extends GLSurfaceView {

    //private final MyGLRenderer mRenderer;
    public MyGLSurfaceView(Context context){
        super (context);
        init();

    }

    public MyGLSurfaceView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    private void init(){
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
        setRenderer(new MyGLRenderer());
    }
}