package none.spark.manager;

import none.spark.util.DebugUtils;

import java.io.File;
import java.util.HashMap;

public class FileManager {

    public final File root;

    public final HashMap<String, File> dirs;

    public FileManager(File root) {
        this.root = root;
        this.dirs = new HashMap<>();
    }

    public void addDir(String name) {
        this.dirs.put(name, new File(root, name));
    }

    public void setupDirs() {
        if (!root.exists()) {
            if (!root.mkdir()) {
                DebugUtils.info("Cannot make root dir: " + root.getAbsolutePath());
            }
        }
        for (File dir : this.dirs.values()) {
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    DebugUtils.info("Cannot make dir: " + root.getAbsolutePath());
                }
            }
        }

    }

    public File getDir(String name) {
        return this.dirs.get(name);
    }
}