package net.comcraft.src;

import java.io.DataInputStream;

import com.google.minijoe.sys.JsObject;
import com.google.minijoe.sys.JsFunction;

public class Mod {

    private String info;
    private String ModName;
    private String ModDescription;
    private boolean running = false;
    public boolean enabled = false;
    private ModLoader ml;
    public boolean wasenabled = false;

    public Mod(ModLoader modLoader, String name, String desc, String info, boolean running) {
        ModName = name;
        ModDescription = desc;
        ml = modLoader;
        this.info = info;
        if (!ml.isDisabled(name)) {
            enabled = true;
            wasenabled = true;
        } else {
            return;
        }
        this.running = running;
    }

    public String getModName() {
        return ModName;
    }

    public String getModDescription() {
        return ModDescription;
    }

    public boolean isRunning() {
        return running;
    }

    public String getModInfo() {
        return info;
    }

    public void enable() {
        ml.enable(ModName);
        enabled = true;
    }

    public void disable() {
        ml.disable(ModName);
        enabled = false;
    }
}
