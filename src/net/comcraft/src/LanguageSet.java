/*
 * Copyright (C) 2012 Piotr Wójcik
 *
 */
package net.comcraft.src;

/**
 *
 * @author Piotr Wójcik
 */
public class LanguageSet {

    private String languageName;
    private String patch;

    public LanguageSet(String name, String patch) {
        this.languageName = name;
        this.patch = patch;
    }

    public String getLanguageName() {
        return languageName;
    }

    public String getPatch() {
        return patch;
    }
}