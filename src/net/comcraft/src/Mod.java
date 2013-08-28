package net.comcraft.src;

import java.io.DataInputStream;
import java.io.IOException;

import com.google.minijoe.sys.JsObject;
import com.google.minijoe.sys.JsFunction;

public class Mod {

    private String info = "No Mod Info";
    private String ModName;
    private String ModDescription;
    private boolean running = false;
    public boolean enabled = false;

    public Mod(String name, String desc, DataInputStream dis) {
        ModName = name;
        ModDescription = desc;
        if (true) { // not disabled
            enabled = true;
        }
        JsObject global = new BaseMod();
        try {
            JsFunction.exec(dis, global);
            running = true;
        } catch (IOException e) {
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
}
