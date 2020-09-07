package fr.nashoba24.webrp;

import fr.nashoba24.webrp.modules.Module;
import fr.nashoba24.webrp.modules.SeriesModule;
import fr.nashoba24.webrp.modules.SoundcloudModule;
import fr.nashoba24.webrp.socket.CallbackSocket;
import fr.nashoba24.webrp.socket.SocketServer;
import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Main {

    private static HashMap<String, Module> modules = new HashMap<>(10);
    private static RPCController rpcController;
    private static SocketServer socketServer;
    private static ModulesConfiguration configuration;

    public static void main(String[] args) {
        rpcController = new RPCController();
        registerModules();
        try {
            configuration = new ModulesConfiguration(modules);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        createSystemTray();
    }

    private static void registerModules() {
        modules.put("soundcloud", new SoundcloudModule("SoundCloud", "690280081851285524"));
        modules.put("crunchyroll", new SeriesModule("Crunchyroll", "716788502322217063"));
        modules.put("adn", new SeriesModule("ADN", "717080315297923106"));
        modules.put("wakanim", new SeriesModule("Wakanim", "717148086585131009"));
        modules.put("netflix", new SeriesModule("Netflix", "717421367607427162"));
        modules.put("primevideo", new SeriesModule("Prime Video", "717443400684666941"));

        modules.put("nekosama", new SeriesModule("Neko-Sama", "718190543087075359"));
        modules.put("fullanimefr", new SeriesModule("FullAnimeVF (fullanimefr.com)", "718190543087075359"));
        modules.put("mavanimes", new SeriesModule("Mavanimes", "718190543087075359"));
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

    private static void createSystemTray() {
        if (!SystemTray.isSupported()) {
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final SystemTray tray = SystemTray.getSystemTray();
        final TrayIcon trayIcon;
        try {
            trayIcon = new TrayIcon(ImageIO.read(Objects.requireNonNull(Main.class.getClassLoader().getResource("icons/tray-icon-" + tray.getTrayIconSize().width + ".gif"))));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Menu modulesMenu = new Menu("Modules");
        for (Map.Entry<String, Module> set : modules.entrySet()) {
            CheckboxMenuItem cmi = new CheckboxMenuItem(set.getValue().getModuleName(), set.getValue().isEnabled());
            cmi.addItemListener(itemEvent -> {
                boolean enabled = itemEvent.getStateChange() == ItemEvent.SELECTED;
                set.getValue().setEnabled(enabled);
                configuration.setModuleEnabled(set.getKey(), enabled);
                configuration.save();
                Module currModule = getSocketServer().getCurrentModule();
                if (currModule == set.getValue() && !enabled) {
                    getSocketServer().removeConnectedModule();
                }
            });
            modulesMenu.add(cmi);
        }
        popup.add(modulesMenu);

        MenuItem quitMenuItem = new MenuItem("Quit");
        quitMenuItem.addActionListener(actionEvent -> {
            getRpcController().moduleConnectionClosed();
            System.exit(0);
        });
        popup.add(quitMenuItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
