package net.vc9ufi.cvitok.data;

public class SelectedVertices {
    public boolean start;
    public boolean p1;

    public boolean p2;
    public boolean finish;

    public void clean() {
        start = false;
        p1 = false;
        p2 = false;
        finish = false;
    }
}
