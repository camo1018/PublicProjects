// Minigame 1 - Guess the Price
// Client
 var ctx,
	playerGuess,
	hasGuessed,
	itemName;
	
	
	function Game1Init() {
		Game1SetEventHandlers();
		socket.emit("game1 get item name");
		alert("Running Minigame 1");
		ctx = canvas.getContext("2d");
		ctx.font = "italic 50px Calibri";
		ctx.fillText = ("Guess the Price!", 50, 50);
		
		hasGuessed = false;
		
		
		ctx.fillText = (itemName, 50, 150);
		
		Game1AskForInput();
		Game1WaitForInput();
	} 
	
	function Game1SetEventHandlers() {
		socket.on("game1 set item name", Game1OnGetItemName);
		socket.on("game1 results", Game1OnResults);
	}
	
	function Game1OnGetItemName(data) {
		itemName = data.itemName;
	}
	
	function Game1OnResults(data) {
		socket.sockets.emit("display loser", {loserId: data.loserId});
		
		// End Game
	}
	
	function Game1AskForInput() {
		smoke.prompt("Guess the price of " + itemName + "!", function(guess) {
			var number = Number(guess);
			if (guess && !isNaN(number)) {
				playerGuess = guess;
				hasGuessed = true;
			}
			else {
				askForInput();
			}
		});
				
	}
	
	function Game1WaitForInput() {
		if (!hasGuessed)
			setTimeout(Game1WaitForInput,100);
		else {
			socket.emit("game1 submit", {guess: playerGuess});
		}
	}
	
	
	
	
	
	
}
