package pcg.yzc.tactilefilmtouch;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends Activity {

    TextView textView_touchInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView_touchInfo = (TextView) findViewById(R.id.textView_touchInfo);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        StringBuilder strB = new StringBuilder();
        String text = "";
        for (int i = 0; i < e.getPointerCount(); ++i) {
            int id = e.getPointerId(i);
            int x = (int) e.getX(i);
            int y = (int) e.getY(i);
            float p = e.getPressure(i);
            float s = e.getSize(i);
            strB.append("Id:" + id + " X:" + x + " Y:" + y);
            strB.append(" S:" + String.format("%.2f", s) + " P:" + String.format("%.2f", p) + "\n");
        }
        if (e.getAction() == MotionEvent.ACTION_UP)
            textView_touchInfo.setText("");
        else
            textView_touchInfo.setText(strB.toString());
        return super.dispatchTouchEvent(e);
    }
}
