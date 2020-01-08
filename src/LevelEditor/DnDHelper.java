package LevelEditor;

import java.awt.Component;

public class DnDHelper {

    private static Component componentMouseIn;

    public static void setComponentMouseIn(Component componentMouseIn) {
        DnDHelper.componentMouseIn = componentMouseIn;
//        System.out.println("Set component to: " + componentMouseIn);
    }

    public static Component getComponentMouseIn() {
        return DnDHelper.componentMouseIn;
    }
}
