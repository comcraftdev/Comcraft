// Random Stuff Mod.cmod is an example based off from michal5575's Mod it is only a snippet as not all features are implemented yet
cgn=ChunkGeneratorNormal

cgn.addGenerateRule(function(biome,level, x,y,z, cx,cz) {
    if (y == level + 1) {
        id=null
        if (biome == 0 && (cx * 4 + x) % (cgn.randNext(2) + 1) == 0 && (cz * 4 + z) % (cgn.randNext(2) + 1) == 0) {
            n=cgn.flowersNoise((cx * 4 + x) / 24, (cz * 4 + z) / 24)
            //Console.log("Flower noise" + n.toString())
            if (n > 0.10) {
                id= 45//Block.treePlant.blockID;
            }
            if (n > 0.79) {
                id= 19//Block.pumpkin.blockID;
            }
            if (n > 0.29) {
                id= 47//Block.mushroom.blockID;
            }
            if (n > 0.80) {
                id= 123//Block.animalSheep.blockID;
            }
            if (n > 0.20) {
                id= 127//Block.animalPig.blockID;
            }
            if (n > 0.40) {
                id= 126//Block.animalCow.blockID;
            }
            if (n > 0.60) {
                id= 125//Block.animalChicken.blockID;
            }
            //Console.log("Nothing yet")
        }
        if (biome == 1 && (cx * 4 + x) %  (cgn.randNext(2) + 1) == 0 && (cz * 4 + z) % (cgn.randNext(2) + 1) == 0) {
            //Console.log("Cactus noise" + n.toString())
            n=cgn.flowersNoise((cx * 4 + x) / 24, (cz * 4 + z) / 24)
            if (n > 0.10 && n < 0.69) {
                id= 31//Block.cactus.blockID;
            }
            if (n > 0.80) {
                id= id= 123//Block.animalSheep.blockID;
            }
            if (n > 0.20) {
                id= 127//Block.animalPig.blockID;
            }
            if (n > 0.40) {
                id= 126//Block.animalCow.blockID;
            }
            if (n > 0.60) {
                id= 125//Block.animalChicken.blockID;
            }
            //Console.log("Nothing")
        }
        if (id!=null) {
            return id
        }
    }
})