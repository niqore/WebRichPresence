package fr.nashoba24.webrp;

import fr.nashoba24.webrp.modules.Module;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModulesConfiguration {

    private File configFile;
    private JSONObject configObject;

    public ModulesConfiguration(HashMap<String, Module> modules) throws IOException {
        configFile = new File(new File(System.getenv("APPDATA")), "WebRP");
        if (!configFile.exists()) {
            configFile.mkdir();
        }
        configFile = new File(configFile, "webrp.json");
        if (!configFile.exists()) {
            configFile.createNewFile();
            createDefaultJsonObject(modules);
            save();
            return;
        }
        FileReader reader = new FileReader(configFile);
        try {
            configObject = (JSONObject) new JSONParser().parse(reader);
        } catch (ParseException e) {
            e.printStackTrace();
            createDefaultJsonObject(modules);
            save();
            return;
        }
        syncModulesWithJsonObject(modules);
    }

    private void createDefaultJsonObject(HashMap<String, Module> modules) {
        configObject = new JSONObject();
        JSONObject modulesObject = new JSONObject();
        configObject.put("modules", modulesObject);
        for (Map.Entry<String, Module> set : modules.entrySet()) {
            modulesObject.put(set.getKey(), set.getValue().isEnabled());
        }
    }

    private void syncModulesWithJsonObject(HashMap<String, Module> modules) {
        if (configObject == null || !configObject.containsKey("modules")) {
            return;
        }
        JSONObject modulesObject = (JSONObject) configObject.get("modules");
        for (Map.Entry<String, Module> set : modules.entrySet()) {
            if (modulesObject.containsKey(set.getKey())) {
                set.getValue().setEnabled((Boolean) modulesObject.get(set.getKey()));
            }
        }
    }

    public void setModuleEnabled(String module, boolean enabled) {
        ((JSONObject) configObject.get("modules")).put(module, enabled);
    }

    public void save() {
        try {
            FileWriter fw = new FileWriter(configFile);
            fw.write(configObject.toJSONString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
