package none.spark.manager;

import org.lwjgl.opengl.GL11;

import java.util.LinkedList;

public final class GLCapStack {
    public static LinkedList<Integer> capNameStack = new LinkedList<>();
    public static LinkedList<Boolean> capStateStack = new LinkedList<>();
    public static LinkedList<Integer> capNumberStack = new LinkedList<>();

    // Why GL11.glPushAttrib() and GL11.glPopAttrib() doesn't work ???
    // Then I made it.
    // I can't know when it will underflow or overflow because of your wrongs, so you'd better be careful

    public static void push(int... caps) {
        for (int cap : caps) {
            capNameStack.push(cap);
            capStateStack.push(GL11.glIsEnabled(cap));
        }
        capNumberStack.push(caps.length);
    }

    public static void pop() {
        for (int i = 0, len = capNumberStack.pop(); i < len; i++) {
            int name = capNameStack.pop();
            boolean state = capStateStack.pop();
            if (state) {
                GL11.glEnable(name);
            } else {
                GL11.glDisable(name);
            }
        }
    }
}
