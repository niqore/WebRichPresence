package fr.nashoba24.webrp;

import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import fr.nashoba24.webrp.modules.Module;
import org.json.simple.JSONObject;

public class RPCController {

    private DiscordRPC lib;
    private Module lastModule = null;

    public RPCController() {
        lib = DiscordRPC.INSTANCE;
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
            }
        }, "RPC-Callback-Handler").start();
    }

    public void updateRPC(Module module, JSONObject data) {
        if (lastModule == module && !module.needUpdate(data)) {
            return;
        }
        if (lastModule != module) {
            lib.Discord_ClearPresence();
            lib.Discord_Shutdown();
            lib.Discord_Initialize(module.getApplicationId(), null, true, null);
            lastModule = module;
        }
        DiscordRichPresence presence = new DiscordRichPresence();
        presence = module.formatRichPresence(presence, data);
        lib.Discord_UpdatePresence(presence);
        System.out.println("Updated rich presence");
    }

    public void moduleConnectionClosed() {
        lib.Discord_ClearPresence();
        lib.Discord_Shutdown();
        lastModule = null;
    }
}
