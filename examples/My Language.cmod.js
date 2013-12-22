// My Language.cmod - shows how to add a custom language.
// Requires you to make the file /lang/custom.lng in resources folder in the IDE
EventHandler.bindEvent("Language.List", function(){
	return ["My Custom Language", "/lang/custom.lng"]
})