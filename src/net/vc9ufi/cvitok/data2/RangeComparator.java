package net.vc9ufi.cvitok.data2;

import net.vc9ufi.geometry.Triangle;

import java.security.InvalidParameterException;
import java.util.Comparator;

public class RangeComparator implements Comparator<Triangle> {

    private final float[] point;

    public RangeComparator(float[] point) throws InvalidParameterException {
        if (point == null || point.length != 3) throw new InvalidParameterException();
        this.point = point;
    }

    @Override
    public int compare(Triangle tr1, Triangle tr2) {
        float r1 = calcRange(tr1);
        float r2 = calcRange(tr2);
        if (r1 > r2) return -1;
        if (r1 < r2) return 1;
        return 0;
    }

    public float calcRange(Triangle tr) {
        float temp;
        float result;

        temp = tr.v1.p[0] - point[0];
        result = temp*temp;
        temp = tr.v1.p[1] - point[1];
        result += temp*temp;
        temp = tr.v1.p[2] - point[2];
        result += temp*temp;

        temp = tr.v2.p[0] - point[0];
        result += temp*temp;
        temp = tr.v2.p[1] - point[1];
        result += temp*temp;
        temp = tr.v2.p[2] - point[2];
        result += temp*temp;

        temp = tr.v3.p[0] - point[0];
        result += temp*temp;
        temp = tr.v3.p[1] - point[1];
        result += temp*temp;
        temp = tr.v3.p[2] - point[2];
        result += temp*temp;

        return result;
    }
}
