package none.spark.script;

import net.minecraft.client.Minecraft;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Script {
    public File scriptFile;
    public String sourceCode;
    public ScriptEngine scriptEngine;
    public Invocable scriptInvocable;

    public Script(File scriptFile) {
        this.scriptFile = scriptFile;
    }

    public void readSourceCode() throws IOException {
        byte[] fileContent = new byte[(int) this.scriptFile.length()];

        FileInputStream in = new FileInputStream(this.scriptFile);
        in.read(fileContent);
        in.close();

        this.sourceCode = new String(fileContent, StandardCharsets.UTF_8);
    }

    public void run() throws ScriptException {

        this.scriptEngine = (new ScriptEngineManager()).getEngineByName("nashorn");

        this.scriptEngine.put("mc", Minecraft.getMinecraft());
        this.scriptEngine.eval(this.sourceCode);
        this.scriptInvocable = (Invocable) this.scriptEngine;

    }
}
