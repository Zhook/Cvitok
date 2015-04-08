package net.vc9ufi.cvitok.data;


import android.view.View;
import net.vc9ufi.cvitok.App;

import net.vc9ufi.cvitok.control.LookAt;
import net.vc9ufi.cvitok.petal.Petal;
import net.vc9ufi.cvitok.petal.Pointers;
import net.vc9ufi.cvitok.views.settings.Setting;
import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;
import java.util.List;

public class Flower {
    public static final float[] BACKGROUND = new float[]{0.9f, 1.0f, 0.9f, 1.0f};

    private String name = "";
    private ArrayList<Petal> petals = new ArrayList<>();

    private volatile Light light = new Light();
    private volatile float[] background = new float[4];

    private Petal selectedPetal;

    private SelectedVertices left = new SelectedVertices();
    private SelectedVertices right = new SelectedVertices();

    private LookAt mFlowerOnTouchListener;

    public Flower() {
        mFlowerOnTouchListener = new LookAt();
    }

    public boolean setNewFlower(String flowerName) {
        if (setName(flowerName)) {
            petals = new ArrayList<>();
            selectedPetal = new Petal(new Parameters("Petal1"), Setting.getInstance().getQuality(), this);
            petals.add(selectedPetal);
            light = new Light();
            background = BACKGROUND.clone();
            left.clean();
            right.clean();
            return true;
        }
        return false;
    }

    //-----------------------------------------------------------------------------------Name
    public String getName() {
        return name;
    }

    public boolean setName(String flowerName) {
        if (SaveNLoad.isFileNameValid(flowerName)) {
            name = flowerName;
            return true;
        }
        return false;
    }


    //-----------------------------------------------------------------------------------Petal
    public boolean addPetal(String name) {
        if (petals != null) {
            for (Petal petal : petals) {
                if (name.equals(petal.getName())) return false;
            }
        }
        Petal petal = new Petal(new Parameters(name), Setting.getInstance().getQuality(), this);
        petals.add(petal);
        selectedPetal = petal;
        return true;
    }

    public boolean delPetal() {
        boolean result = petals.remove(selectedPetal);
        setSelectedPetal(0);
        return result;
    }

    public List<String> getPetalsNames() {
        if (petals != null) {
            List<String> names = new ArrayList<>();
            for (Petal petal : petals) {
                names.add(petal.getName());
            }
            return names;
        } else return null;
    }


    //-----------------------------------------------------------------------------------Light


    @NotNull
    public Light getLight() {
        return light;
    }

    public float[] getBackground() {
        return background;
    }

    public void setBackground(float[] background) {
        if (background == null) return;
        if (background.length == 4)
            this.background = background;
    }

    //----------------------------------------------------------------------------------SelectedPetal
    public boolean setSelectedPetal(int index) {
        if (wrongIndex(index))
            return false;

        selectedPetal = petals.get(index);
        return true;
    }

    public boolean setSelectedPetals(String name) {
        for (Petal petal : petals) {
            if (petal.getName().equals(name)) {
                selectedPetal = petal;
                return true;
            }
        }
        return false;
    }

    public String getSelectedName() {
        if (selectedPetal == null) return null;
        return selectedPetal.getName();
    }

    public int getSelectedIndex() {
        if (selectedPetal != null) {
            for (int i = 0; i < petals.size(); i++) {
                if (petals.get(i) == selectedPetal) {
                    return i;
                }
            }
        }
        return -1;
    }

    public Petal getSelectedPetal() {
        return selectedPetal;
    }

    public View.OnTouchListener getOnTouchListener(App.MODE mode) {
        switch (mode) {
            case NULL:
            case FLOWER:
                return mFlowerOnTouchListener;
            case PETAL:
                return selectedPetal.getPetalOnTouchListener();
            case VERTEX:
                return selectedPetal.getVerticesOnTouchListener();
            case LIGHT:
                return light.getOnTouchListener();
        }
        return mFlowerOnTouchListener;
    }

    public LookAt getFlowerOnTouchListener() {
        return mFlowerOnTouchListener;
    }

    //-------------------------------------------------------------------------------------Vertices
    public void setSelectedVertices(SelectedVertices left, SelectedVertices right) {
        this.left = left;
        this.right = right;
    }

    public void setVerticesColor(float[] color) {
        if (color == null) return;
        if (selectedPetal == null) return;

        selectedPetal.setColor(color, left, right);
    }

    public SelectedVertices getRight() {
        return right;
    }

    public SelectedVertices getLeft() {
        return left;
    }

    public void setQuality(int quality) {
        if (petals != null)
            for (Petal petal : petals) {
                petal.setQuality(quality);
            }
    }


    public void paint(GL10 gl) {
        if (petals != null)
            for (Petal petal : petals) {
                Pointers p = petal.getPointers();
                if (p != null) p.paint(gl);
            }
    }


    public FlowerFile getFlowerFile() {
        FlowerFile ff = new FlowerFile();
        ff.name = name;
        for (Petal p : petals)
            ff.petals.add(p.getParameters());

        ff.light = light;
        ff.background = background;

        return ff;
    }

    public void setFlower(FlowerFile flower) {
        name = flower.name;

        petals = new ArrayList<>();
        for (Parameters p : flower.petals)
            petals.add(new Petal(p, Setting.getInstance().getQuality(), this));
        if (petals.size() > 0) setSelectedPetal(0);

        light = flower.light;
        background = flower.background;
    }


    boolean wrongIndex(int index) {
        return (index < 0) || (index >= petals.size());
    }

}
