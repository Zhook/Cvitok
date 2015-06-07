package net.vc9ufi.cvitok.data;

import net.vc9ufi.geometry.Vector3f;
import net.vc9ufi.geometry.temp.GlObject;

import java.security.InvalidParameterException;
import java.util.Comparator;

public class RangeComparator implements Comparator<GlObject> {

    private final Vector3f point;

    public RangeComparator(float[] point) throws InvalidParameterException {
        if (point == null || point.length != 3) throw new InvalidParameterException();
        this.point = new Vector3f(point);
    }

    @Override
    public int compare(GlObject tr1, GlObject tr2) {
        float r1 = tr1.calcRange(point);
        float r2 = tr2.calcRange(point);
        if (r1 > r2) return -1;
        if (r1 < r2) return 1;
        return 0;
    }

}
