// Minigame 1 - Guess the Price
// Client
var Game1 = function(player, socket, canvas) {
	
	var ctx,
		playerGuess,
		hasGuessed,
		itemName;
	
	
	function init() {
		setEventHandlers();
		socket.emit("game1 get item name");
		alert("Running Minigame 1");
		ctx = canvas.getContext("2d");
		ctx.font = "italic 50px Calibri";
		ctx.fillText = ("Guess the Price!", 50, 50);
		
		hasGuessed = false;
		
		
		ctx.fillText = (itemName, 50, 150);
		
		askForInput();
		waitForInput();
	} 
	
	function setEventHandlers() {
		socket.on("game1 set item name", onGetItemName);
		socket.on("game1 results", onResults);
	}
	
	function onGetItemName(data) {
		console.log("LOL");
		itemName = data.itemName;
	}
	
	function onResults(data) {
		socket.sockets.emit("display loser", {loserId: data.loserId});
		
		// End Game
	}
	
	function askForInput() {
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
	
	function waitForInput() {
		if (!hasGuessed)
			setTimeout(waitForInput,100);
		else {
			socket.emit("game1 submit", {guess: playerGuess});
		}
	}
	
	
	
	
	
	
}
