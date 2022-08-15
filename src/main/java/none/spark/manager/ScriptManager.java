package none.spark.manager;

import none.spark.script.Script;
import none.spark.util.DebugUtils;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ScriptManager {

    public final File scriptDir;
    public ArrayList<Script> scripts;

    public ScriptManager(File scriptDir) {
        this.scriptDir = scriptDir;
        this.scripts = new ArrayList<>();
    }

    public void loadScripts() {
        this.scripts = new ArrayList<>();

        File[] files = this.scriptDir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.getName().toLowerCase().endsWith(".js")) {
                scripts.add(new Script(file));
                DebugUtils.info("Script " + file.getName() + " loaded.");
            }
        }
    }

    public void runScripts() {
        for (Script script : scripts) {
            try {
                script.readSourceCode();
                script.run();
            } catch (ScriptException e) {
                System.err.print("\nScriptException: \n" + e.getMessage() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
