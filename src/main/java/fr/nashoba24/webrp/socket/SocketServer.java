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

    public void removeConnectionModule(WebSocket conn) {
        if (currModule != null && currModule.fst == conn) {
            callback.closeConnection(currModule.snd);
            currModule = null;
        }
        if (conn.isOpen()) {
            conn.close();
        }
    }

    public void removeConnectedModule() {
        removeConnectionModule(currModule.fst);
    }

    public Module getCurrentModule() {
        if (currModule == null) {
            return null;
        }
        return currModule.snd;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        if (currModule != null && conn == currModule.fst) {
            if (currModule.snd != null) {
                System.out.println("Module disconnected: " + currModule.snd.getModuleName());
            } else {
                System.out.println("Connection closed");
            }
            removeConnectionModule(conn);
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        if (message.charAt(0) == 'a') {
            if (currModule != null) {
                System.out.println("Module " + message.substring(1) + " tried to connect but another one is already active");
            }
            Module m = Main.getModuleFromName(message.substring(1));
            if (m == null) {
                System.out.println("Module " + message.substring(1) + " not found. Disconnection...");
                removeConnectionModule(conn);
                return;
            }
            if (!m.isEnabled()) {
                System.out.println("Module " + currModule.snd.getModuleName() + " is disabled");
                removeConnectionModule(conn);
                return;
            }
            currModule = new Pair<>(conn, m);
            System.out.println("Module connected: " + currModule.snd.getModuleName());
        } else if (message.charAt(0) == 'b' && currModule != null && conn.getRemoteSocketAddress().getAddress() == currModule.fst.getRemoteSocketAddress().getAddress() && currModule.snd != null) {
            try {
                System.out.println(message.substring(1));
                callback.socketMessage(currModule.snd, (JSONObject) new JSONParser().parse(message.substring(1)));
            } catch (ParseException e) {
                e.printStackTrace();
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
        removeConnectionModule(conn);
    }

    @Override
    public void onStart() {
        System.out.println("Web server started on port " + TCP_PORT);
    }
}