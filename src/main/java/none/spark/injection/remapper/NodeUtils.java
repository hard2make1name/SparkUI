package none.spark.injection.remapper;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

public class NodeUtils {

    public static InsnList toNodes(AbstractInsnNode... args) {
        InsnList insnList = new InsnList();

        for (AbstractInsnNode node : args) {
            insnList.add(node);
        }

        return insnList;
    }
}
