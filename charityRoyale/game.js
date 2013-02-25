// Server

var util = require("util"),
	io = require("socket.io"),
	Player = require("./Player").Player,
	Game1 = require("./minigames/Game1").Game;

var socket, players, waitingForPlayers, gameId;

function init() {
	gameId = -1; // No game yet
	players = [];
	waitingForPlayers = true;
	socket = io.listen(8000);
	socket.configure(function() {
		socket.set("transports", ["websocket"]);
		socket.set("log level", 2);
	});
	setEventHandlers();
	
	waitForPlayers();
};

function waitForPlayers() {
	if (players.length < 1) {
		setTimeout(waitForPlayers, 100);
	}
	else {
		waitingForPlayers = false;
		findGame();
	}
}

// Find a  minigame to play
function findGame() {
	//var randomNum = Math.floor(Math.random()*10) + 1;
	var randomNum = 1;
	var miniGame;
	gameId = randomNum;
	
	switch (randomNum) {
		case 1: miniGame = new Game1(players, socket); break;
		case 2: miniGame = new Game2(players, socket); break;
	// Continue on...
	};
	
	
};	

var setEventHandlers = function() {
	socket.sockets.on("connection", onSocketConnection);
};

function onGetWaitingStatus() {
	this.emit("waiting status", {isWaiting: waitingForPlayers});
};

function onSocketConnection(client) {
	util.log("New player has connected: " + client.id);
	client.on("disconnect", onClientDisconnect);
	client.on("new player", onNewPlayer);
	client.on("remove player", onRemovePlayer);
	client.on("get waiting status", onGetWaitingStatus);
	client.on("get game", onFindGame);
};

function onClientDisconnect() {
	util.log("Player has disconnected: " + this.id);
};

function onNewPlayer(data) {
	var newPlayer = new Player(data.name, data.threshold);
	newPlayer.id = this.id;
	
	this.broadcast.emit("new player", {id: newPlayer.id, name: newPlayer.getName(), threshold: newPlayer.getThreshold()});
	var i, existingPlayer;
	for (i = 0; i < players.length; i++) {
		existingPlayer = players[i];
		this.emit("new player", {id: existingPlayer.id, name: existingPlayer.getName(), y: existingPlayer.getThreshold()});
	};
	
	players.push(newPlayer);
};

function onRemovePlayer(data) {
	var removePlayer = playerById(this.id);
	
	if (!removePlayer) {
		util.log("Player not found: " + this.id);
		return;
	};
	
	players.splice(players.indexOf(removePlayer), 1);
	this.broadcast.emit("remove player", {id: this.id});
};

function onFindGame() {
	if (this.gameId >= 0) {
		console.log("?");
		this.emit("found game", {gameId: this.gameId});
	}
};

function playerById(id) {
	var i;
	for (i = 0; i < players.length; i++) {
		if (players[i].id == id)
			return players[i];
	};
	
	return false;
};

init();