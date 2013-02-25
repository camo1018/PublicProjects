// Minigame 1 - Guess the Price
// Server
var Game1 = function(players, socket, server) {
	var loserId;
	var loserGuessDiff = 0;
	var price;
	var submissionCount = 0;
	var itemName;
	var intervalId;
	
	function init() {
		// Debug
		price = 100;
		itemName = "Keagan";

		setEventHandlers();
	};
	
	function setEventHandlers() {
		socket.on("game1 submit", onSubmit);
		socket.on("game1 get item name", onGetItemName);
	};
	
	function onSubmit(data) {
		if (Math.abs(data.guess - price) > loserGuessDiff) {
			loserId = data.pid; 
		}
		submissionCount++;
		
		if (submissionCount >= players.length) {
			// We're done.  Display results and end game.
			this.broadcast.emit("game1 results", {loserId: loserId});
			// End game
			
		}
	};
	
	function onGetItemName() {
		this.broadcast.emit("game1 set item name", {itemName: itemName});
	}
	
	init();
	
	
};

exports.Game = Game1;