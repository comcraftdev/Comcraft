package net.comcraft.src;

import java.io.DataInputStream;

import com.google.minijoe.sys.JsObject;
import com.google.minijoe.sys.JsFunction;

public class Mod {

    private String info = "No Mod Info";
    private String ModName;
    private String ModDescription;
    private boolean running = false;
    public boolean enabled = false;
    private JsObject global = new BaseMod();
    private ModLoader ml;
    public boolean wasenabled = false;

    public Mod(ModLoader modLoader, String name, String desc, DataInputStream dis) {
        ModName = name;
        ModDescription = desc;
        ml = modLoader;
        if (!ml.isDisabled(name)) {
            enabled = true;
            wasenabled = true;
        } else {
            return;
        }
        try {
            JsFunction.exec(dis, global);
            running = true;
        } catch (Exception e) {
            e.printStackTrace();
            info = e.getMessage();
        }
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
