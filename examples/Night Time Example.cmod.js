// Night Time Example.cmod - Demo of Render.Init event to set environment background to a dark grey
EventHandler.bindEvent('Render.Init', function(background){
	background.setColor(0x303030)
})
