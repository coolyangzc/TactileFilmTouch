package pcg.yzc.tactilefilmtouch;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends Activity {

    final int tUpPixel = 1450, tDownPixel = 2240;
    TextView textView_touchInfo, textView_resultInfo;
    public HistoryValueContainer pHistory = new HistoryValueContainer(1000, 0.5);
    public HistoryValueContainer yHistory = new HistoryValueContainer(1000, 0.5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView_touchInfo = (TextView) findViewById(R.id.textView_touchInfo);
        textView_resultInfo = (TextView) findViewById(R.id.textView_resultInfo);
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
            text += String.format(Locale.US," S:%.2f P:%.2f", s, p) + "\n";
            if (!updated && x < 150 || x > 930) {
                updated = true;
                long curTime = SystemClock.uptimeMillis();
                pHistory.update(p, curTime);
                yHistory.update(y, curTime);
            }
        }
        String textH = String.format(Locale.US, "pHistory: %.2f %.2f\n", pHistory.getDelta(), pHistory.getValue());
        textH += String.format(Locale.US,"yHistory: %.2f %.2f\n", yHistory.getDelta(), yHistory.getValue());
        if (e.getAction() == MotionEvent.ACTION_UP)
            text = textH;
        else
            text = textH + text;
        textView_touchInfo.setText(text);
        int res = predict();
        if (res >= 0)
            textView_resultInfo.setText(String.format(Locale.US, "Touch %d", res));
        else
            textView_resultInfo.setText("");
        return super.dispatchTouchEvent(e);
    }

    public int predict() {
        if (Math.abs(yHistory.getDelta()) > 40)
            return -1;
        int res = (int)(yHistory.getRecent() - tUpPixel) / ((tDownPixel - tUpPixel) / 6);
        if (pHistory.getRecent() > 2.50 - (res == 5?0.5:0.0) || pHistory.getDelta() > 0.80
                || pHistory.getRecent() * pHistory.getDelta() > 1.50)
            return res;
        return -1;
    }
}
