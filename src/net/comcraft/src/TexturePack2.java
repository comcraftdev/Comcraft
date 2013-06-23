/*
 * Copyright (C) 2013 Piotr Wójcik
 * 
 * This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.comcraft.src;

import java.io.InputStream;
import net.comcraft.client.Comcraft;

public abstract class TexturePack2 implements TexturePack {

    private String texturePackName = "";
    private String texturePackDescription = "";
    private int supportedWidth;
    private int supportedHeight;
    protected String path;
    protected final Comcraft cc;

    public TexturePack2(Comcraft cc) {
        this(cc, "");
    }

    public TexturePack2(Comcraft cc, String path) {
        this.cc = cc;
        this.path = path;
        texturePackName = "Unknown";
        texturePackDescription = "Unknown";
        supportedWidth = 0;
        supportedWidth = 0;
        initTexturePack();
    }

    public InputStream getResourceAsStream(String path) {
        InputStream inputStream = (net.comcraft.src.TexturePack.class).getResourceAsStream("/" + path);
        
        if (inputStream == null) {
            inputStream = (net.comcraft.src.TexturePack.class).getResourceAsStream("/" + Comcraft.getScreenWidth() + "-" + Comcraft.getScreenHeight() + "/" + path);
        }

        return inputStream;
    }

    protected abstract void initTexturePack();

    protected final void setTexturePackName(String name) {
        texturePackName = name;
    }

    public String getTexturePackName() {
        return texturePackName;
    }

    protected final void setTexturePackDescription(String description) {
        texturePackDescription = description;
    }

    public String getTexturePackDescription() {
        return texturePackDescription;
    }

    protected final void setResolution(int width, int height) {
        supportedWidth = width;
        supportedHeight = height;
    }

    public boolean isTexturePackSupported() {
        return Comcraft.screenWidth == supportedWidth && Comcraft.screenHeight == supportedHeight;
    }
}
