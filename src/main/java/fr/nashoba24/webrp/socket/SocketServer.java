package fr.nashoba24.webrp.socket;

import fr.nashoba24.webrp.Main;
import fr.nashoba24.webrp.modules.Module;
import fr.nashoba24.webrp.utils.Pair;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.BindException;
import java.net.InetSocketAddress;

public class SocketServer extends WebSocketServer {

    private static final int TCP_PORT = 54917;

    private Pair<WebSocket, Module> currModule;
    private CallbackSocket callback;

    public SocketServer(CallbackSocket callback) {
        super(new InetSocketAddress(TCP_PORT));
        this.callback = callback;
    }

    public void removeConnectionModule() {
        if (currModule != null && currModule.snd != null) {
            if (currModule.fst.isOpen()) {
                currModule.fst.close();
            }
            callback.closeConnection(currModule.snd);
        }
        currModule = null;
    }

    public Module getCurrentModule() {
        if (currModule == null) {
            return null;
        }
        return currModule.snd;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        if (currModule != null && currModule.fst.isOpen()) {
            System.out.println("A module tried to connect but an other module is already active");
            conn.close();
            return;
        }
        System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
        currModule = new Pair<>(conn, null);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        if (currModule != null && conn == currModule.fst) {
            System.out.println("Module disconnected: " + currModule.snd.getModuleName());
            removeConnectionModule();
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        if (currModule != null && currModule.fst.getRemoteSocketAddress().getAddress() == currModule.fst.getRemoteSocketAddress().getAddress()) {
            if (message.charAt(0) == 'a') {
                currModule.snd = Main.getModuleFromName(message.substring(1));
                if (!currModule.snd.isEnabled()) {
                    System.out.println("Module " + currModule.snd.getModuleName() + " is disabled");
                    removeConnectionModule();
                    return;
                }
                System.out.println("Module connected: " + currModule.snd.getModuleName());
            } else if (message.charAt(0) == 'b' && currModule.snd != null) {
                try {
                    callback.socketMessage(currModule.snd, (JSONObject) new JSONParser().parse(message.substring(1)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("Connection error");
        if (ex instanceof BindException) {
            System.out.println("The port " + TCP_PORT + " is already used");
            System.exit(0);
        }
        ex.printStackTrace();
        if (currModule != null && conn == currModule.fst) {
            removeConnectionModule();
        }
    }

    @Override
    public void onStart() {
        System.out.println("Web server started on port " + TCP_PORT);
    }
}