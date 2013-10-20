// Diamond Slab.cmod - Demonstration of how to use custom block rendering, collisions and Block API rules
Block(128).create("diamondSlab", {
	textures: [24, 24],
	canBePiecedVertically: false,
	getBlockVertexBufferPieced: Model.PieceSlab[1],
	getBlockIndexBuffer: Model.PieceSlab[0],
	isNormal: false,
	getCollisionBoundingBoxFromPool: function (world, x, y, z) {
		return AxisAlignedBB(x, y, z, x + 1, y + 0.5, z + 1)
	},
	canPlaceBlockAt: function (world, x, y, z) {
		i = world.getBlockID(x, y, z)
		return i == 129 || i == 0
	},
	shouldSideBeRendered: function (world, x, y, z, side) {
		!world.isBlockNormal(x, y, z) || side == 4
	}
})
