package opengl_app1.javacodegeeks.com.opengl_app1;



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class FirstActivity extends AppCompatActivity {

    private MyGLSurfaceView openGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openGLView = (MyGLSurfaceView) findViewById(R.id.openGLView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        openGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        openGLView.onPause();
    }
}





