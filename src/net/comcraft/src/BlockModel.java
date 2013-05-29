/*
 * Copyright (C) 2012 Piotr Wójcik
 *
 */

package net.comcraft.src;

import javax.microedition.m3g.Node;

/**
 *
 * @author Piotr Wójcik
 */
public class BlockModel extends Block {
    
    private String modelPath;
    
    public BlockModel(int id, String modelPath) {
        super(id);
        
        this.modelPath = modelPath;
    }

    public boolean canBePieced() {
        return false;
    }

    public boolean canBePiecedVertically() {
        return false;
    }

    public boolean shouldSideBeRendered(World world, int x, int y, int z, int side) {
        return true;
    }

    public boolean isNormal() {
        return false;
    }
    
    public Node getBlockModel(World world, int x, int y, int z) {
        return ModelsList.getModel(modelPath);
    }
    
    public int getRenderType() {
        return 6;
    }
    
}