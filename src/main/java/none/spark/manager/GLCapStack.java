package none.spark.manager;

import org.lwjgl.opengl.GL11;

import java.util.LinkedList;

public class GLCapStack {

    public static LinkedList<Boolean> flagStack = new LinkedList<>();
    public static LinkedList<Integer> stack = new LinkedList<>();

    public static void push(int... caps) {
        for (int cap : caps) {
            stack.add(cap);
            flagStack.add(GL11.glIsEnabled(cap));
        }
    }

    public static void pop() {
        stack.removeLast();
    }

    public static void pop(int length) {
        for (int i = 0; i < length; i++) {
            int cap = stack.pop();
            if (flagStack.pop()) GL11.glEnable(cap);
            else GL11.glDisable(cap);
        }
    }
}
