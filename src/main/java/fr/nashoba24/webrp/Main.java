package fr.nashoba24.webrp;

import fr.nashoba24.webrp.modules.*;
import fr.nashoba24.webrp.modules.Module;
import fr.nashoba24.webrp.socket.CallbackSocket;
import fr.nashoba24.webrp.socket.SocketServer;
import org.json.simple.JSONObject;

import java.util.HashMap;

public class Main {

    private static HashMap<String, Module> modules = new HashMap<>(10);
    private static RPCController rpcController;
    private static SocketServer socketServer;

    public static void main(String[] args) {
        rpcController = new RPCController();
        registerModules();
        socketServer = new SocketServer(new CallbackSocket() {
            @Override
            public void socketMessage(Module module, JSONObject data) {
                getRpcController().updateRPC(module, data);
            }

            @Override
            public void closeConnection(Module module) {
                getRpcController().moduleConnectionClosed();
            }
        });
        socketServer.start();
    }

    private static void registerModules() {
        modules.put("soundcloud", new SoundcloudModule());
        modules.put("crunchyroll", new CrunchyrollModule());
        modules.put("adn", new ADNModule());
        modules.put("wakanim", new WakanimModule());
        modules.put("netflix", new NetflixModule());
    }

    public static Module getModuleFromName(String name) {
        return modules.get(name);
    }

    public static RPCController getRpcController() {
        return rpcController;
    }

    public static SocketServer getSocketServer() {
        return socketServer;
    }
}
