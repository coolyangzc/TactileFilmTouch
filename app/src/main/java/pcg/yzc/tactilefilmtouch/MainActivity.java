package pcg.yzc.tactilefilmtouch;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends Activity {

    TextView textView_touchInfo;
    public HistoryValueContainer pHistory = new HistoryValueContainer(1000, 0.5);
    public HistoryValueContainer yHistory = new HistoryValueContainer(1000, 0.5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView_touchInfo = (TextView) findViewById(R.id.textView_touchInfo);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        String text = "";
        boolean updated = false;
        for (int i = 0; i < e.getPointerCount(); ++i) {
            int id = e.getPointerId(i);
            int x = (int) e.getX(i);
            int y = (int) e.getY(i);
            float p = e.getPressure(i);
            float s = e.getSize(i);
            text += "Id:" + id + " X:" + x + " Y:" + y;
            text += " S:" + String.format("%.2f", s) + " P:" + String.format("%.2f", p) + "\n";
            if (!updated && x < 150 || x > 930) {
                updated = true;
                long curTime = SystemClock.uptimeMillis();
                pHistory.update(p, curTime);
                yHistory.update(y, curTime);
            }
        }
        String textH = "pHistory: " + String.format("%.2f %.2f\n", pHistory.getDelta(), pHistory.getValue());
        textH += "yHistory: " + String.format("%.2f %.2f\n", yHistory.getDelta(), yHistory.getValue());
        if (e.getAction() == MotionEvent.ACTION_UP)
            text = textH;
        else
            text = textH + text;
        textView_touchInfo.setText(text);
        return super.dispatchTouchEvent(e);
    }
}
