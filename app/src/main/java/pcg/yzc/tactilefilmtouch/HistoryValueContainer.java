package pcg.yzc.tactilefilmtouch;

import java.util.LinkedList;
import java.util.Queue;

public class HistoryValueContainer {

    public double value = 0;

    private Queue<Double> values = new LinkedList<Double>();
    private Queue<Long> timestamps = new LinkedList<Long>();

    private double ms, lowWeight, recent;

    HistoryValueContainer(double ms, double lowWeight) {
        this.ms = ms;
        this.lowWeight = lowWeight;
    }

    public double getValue() {
        return value;
    }

    public double getDelta() {
        return recent - value;
    }

    public void update(double newValue, long timestamp_ms) {
        values.offer(newValue);
        timestamps.offer(timestamp_ms);
        recent = newValue;
        while (timestamp_ms - timestamps.peek() > ms) {
            values.poll();
            timestamps.poll();
        }
        double k = (1 -  lowWeight) / (values.size() - 1);
        double now = lowWeight, tot = 0;
        value = 0;
        for (Double x: values) {
            value += x * now;
            tot += now;
            now += k;
        }
        value /= tot;
    }
}