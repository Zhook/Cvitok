package net.vc9ufi.cvitok;

public class Timer {

    final long start;
    long time;
    final String name;

    public Timer(String name) {
        this.name = name;
        start = System.nanoTime();
        time = System.nanoTime();
    }

    public void println(String descr) {
        System.out.println("myout: " + name + " " + descr + " " + String.valueOf((System.nanoTime() - time)/1000000));
        time = System.nanoTime();
    }

    public void printlnFullTime() {
        System.out.println("myout: " + name + " " + " " + String.valueOf((System.nanoTime() - start)/1000000));
    }
}
