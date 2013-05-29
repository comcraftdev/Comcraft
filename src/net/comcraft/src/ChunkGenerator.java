package net.comcraft.src;

public abstract class ChunkGenerator {

    protected long seed;
    
    public ChunkGenerator(long seed) {
        this.seed = seed;
    }
    
    public abstract ChunkStorage[] generateChunk(int x, int z);
}
