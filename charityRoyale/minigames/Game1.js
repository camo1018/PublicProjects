// Minigame 1 - Guess the Price
// Server

	var loserId;
	var loserGuessDiff = 0;
	var price;
	var submissionCount = 0;
	var itemName;
	var intervalId;		
	
	exports.init = function()  {
		// Debug
		price = 100;
		itemName = "Keagan";
		console.log("Game called");
	};
	
	exports.onSubmit = function(data) {
		console.log("Guess Submitted");
		if (Math.abs(data.guess - price) > loserGuessDiff) {
			loserGuessDiff = Math.abs(data.guess - price);
			loserId = data.pid; 
		}
		submissionCount++;
		console.log(submissionCount);
		console.log(players.length);
		

		if (submissionCount >= players.length) {
			// We're done.  Display results and end game.
			console.log("Broadcasting Results");
			this.broadcast.emit("game1 results", {loserId: loserId});
			this.emit("game1 results", {loserId: loserId});
			// End game
			
		}
	};
	
	exports.onGetItemName = function() {
		console.log("Getting item name");
		this.emit("game1 set item name", {itemName: itemName});
	}
