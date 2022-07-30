package none.spark.injection.remapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Remapper {

    private static final HashMap<String, HashMap<String, String>> fields = new HashMap<>();
    private static final HashMap<String, HashMap<String, String>> methods = new HashMap<>();

    public static void parseSrg(InputStream srgInputStream) {

        BufferedReader reader;

        try {
            InputStreamReader isr = new InputStreamReader(srgInputStream, StandardCharsets.UTF_8);
            reader = new BufferedReader(isr);

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("FD:")) {
                    String[] args = line.split(" ");
                    String name = args[1];
                    String srg = args[2];

                    String className = name.substring(0, name.lastIndexOf('/')).replace('/', '.');
                    String fieldName = name.substring(name.lastIndexOf('/') + 1);
                    String fieldSrg = srg.substring(srg.lastIndexOf('/') + 1);

                    if (!fields.containsKey(className)) {
                        fields.put(className, new HashMap<>());
                    }

                    if (fields.containsKey(className)) {
                        fields.get(className).put(fieldSrg, fieldName);
                    }
                }

                if (line.startsWith("MD:")) {
                    String[] args = line.split(" ");
                    String name = args[1];
                    String desc = args[2];
                    String srg = args[3];

                    String className = name.substring(0, name.lastIndexOf('/')).replace('/', '.');
                    String methodName = name.substring(name.lastIndexOf('/') + 1);
                    String methodSrg = srg.substring(srg.lastIndexOf('/') + 1);

                    if (!methods.containsKey(className)) {
                        methods.put(className, new HashMap<>());
                    }

                    if (methods.containsKey(className)) {
                        methods.get(className).put(methodSrg + desc, methodName);
                    }
                }
            }

            reader.close();
            srgInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String remapField(Class<?> clazz, String name) {
        if (fields.containsKey(clazz.getName())) {
            return fields.get(clazz.getName()).getOrDefault(name, name);
        }

        return name;
    }

    public static String remapMethod(Class<?> clazz, String name, String desc) {
        if (methods.containsKey(clazz.getName())) {
            return methods.get(clazz.getName()).getOrDefault(name + desc, name);
        }

        return name;
    }

}