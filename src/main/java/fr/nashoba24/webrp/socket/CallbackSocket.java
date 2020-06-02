package fr.nashoba24.webrp.socket;

import fr.nashoba24.webrp.modules.Module;
import org.json.simple.JSONObject;

public interface CallbackSocket {

    void socketMessage(Module module, JSONObject data);

    void closeConnection(Module module);
}
