package none.spark.manager;

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
        if (!root.exists()) root.mkdir();
        for (File dir : this.dirs.values()) {
            if (!dir.exists()) {
                dir.mkdir();
            }
        }
    }

    public File getDir(String name) {
        return this.dirs.get(name);
    }
}