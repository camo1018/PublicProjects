// Client

/**************************************************
 ** GAME VARIABLES
 **************************************************/
var canvas,			// Canvas DOM element
ctx,			// Canvas rendering context
keys,			// Keyboard input
localPlayer,	// Local player
remotePlayers,
inputEntered,
threshold,
socket,
isWaiting,
hasGame, // Does the game client have a minigame to play?
Game1;

/**************************************************
 ** GAME INITIALISATION
 **************************************************/
function init() {
	// Test Init
	inputEntered = false;
	isWaiting = true;
	hasGame = false;

	askForInput();
	waitForInput();



};

function askForInput() {
	smoke.prompt("What is the maximum amount of donation you are willing to pay?", function(inputThreshold) {
		var number = Number(inputThreshold);
		if (inputThreshold && !isNaN(number)) {
			threshold = inputThreshold;
			inputEntered = true;
		}
		else {
			askForInput();
		}

	});
}

function waitForInput() {
	if (!inputEntered)
		setTimeout(waitForInput,100);
	else
	{
		// Declare the canvas and rendering context
		canvas = document.getElementById("gameCanvas");
		ctx = canvas.getContext("2d");

		// Maximise the canvas
		canvas.width = window.innerWidth;
		canvas.height = window.innerHeight;

		ctx.font = "italic 50px Calibri";
		ctx.fillText(threshold, 50, 50);

		// Initialise keyboard controls
		keys = new Keys();

		// Initialise the local player
		localPlayer = new Player(name, threshold);

		// Start listening for events
		socket = io.connect("http://localhost", {port: 8000, transports:["websocket"]});
		remotePlayers = [];
		setEventHandlers();
		
		waitForPlayers();
	}
};

function waitForPlayers() {
	if (isWaiting) {
		smoke.signal("Waiting for players");
		socket.emit("get waiting status");
		setTimeout(waitForPlayers, 100);
	}
	else {
		waitForGame();
	}
};

function waitForGame() {
	if (!hasGame) {
		smoke.signal("Determining minigame...");
		socket.emit("get game");
		setTimeout(waitForGame, 100);
	}
}

/**************************************************
 ** GAME EVENT HANDLERS
 **************************************************/
var setEventHandlers = function() {
	// Keyboard
	window.addEventListener("keydown", onKeydown, false);
	window.addEventListener("keyup", onKeyup, false);

	// Window resize
	window.addEventListener("resize", onResize, false);

	socket.on("connect", onSocketConnected);
	socket.on("disconnect", onSocketDisconnect);
	socket.on("new player", onNewPlayer);
	socket.on("remove player", onRemovePlayer);
	socket.on("found game", onFoundGame);
	socket.on("display loser", onDisplayLoser);
	socket.on("waiting status", onGotWaitingStatus);
};

//Keyboard key down
function onKeydown(e) {
	if (localPlayer) {
		keys.onKeyDown(e);
	};
};

//Keyboard key up
function onKeyup(e) {
	if (localPlayer) {
		keys.onKeyUp(e);
	};
};

//Browser window resize
function onResize(e) {
	// Maximise the canvas
	canvas.width = window.innerWidth;
	canvas.height = window.innerHeight;
};

function onSocketConnected() {
	console.log("Connected to socket server");
	socket.emit("new player", {name: localPlayer.getName(), threshold: localPlayer.getThreshold()});
};

function onSocketDisconnect() {
	console.log("Disconnected from socket server");
};

function onNewPlayer(data) {
	console.log("New player connected: "+data.id);
	var newPlayer = new Player(data.name, data.threshold);
	newPlayer.id = data.id;
	remotePlayers.push(newPlayer);
};

function onRemovePlayer(data) {
	var removePlayer = playerById(data.id);

	if (!removePlayer) {
		console.log("Player not found: " + data.id);
		return;
	};

	remotePlayers.splice(remotePlayers.indexOf(removePlayer), 1);
};

function onFoundGame(data) {
	if (!hasGame) {
		switch (data.gameId) {
			case 1: Game1Init(); break;
			case 2: Game2Init(); break;
		}

		hasGame = true;
		alert(hasGame);
	}
};

function onDisplayLoser(data) {
	var loser = playerById(data.loserId);
	
	if (!loser) {
		console.log("Loser not found: " + data.loserId);
	}
	
	smoke.signal(loser.getName() + " is the loser!");
};

function onGotWaitingStatus(data) {
	isWaiting = data.isWaiting;
};

/*
function animate() {
	update();
	draw();

	// Request a new animation frame using Paul Irish's shim
	window.requestAnimFrame(animate);
};
 */


/*
function update() {
	if (localPlayer.update(keys)) {
		socket.emit("move player", {x: localPlayer.getX(), y: localPlayer.getY()});
	};
};
 */

/**************************************************
 ** GAME DRAW
 **************************************************/
function draw() {
	// Wipe the canvas clean
	ctx.clearRect(0, 0, canvas.width, canvas.height);

	// Draw the local player
	localPlayer.draw(ctx);
	var i;
	for (i = 0; i < remotePlayers.length; i++) {
		remotePlayers[i].draw(ctx);
	}
};

function playerById(id) {
	var i;
	for (i = 0; i < remotePlayers.length; i++) {
		if (remotePlayers[i].id == id)
			return remotePlayers[i];
	};

	return false;
};
