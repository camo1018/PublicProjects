// Server Game

var util = require("util"),
	io = require("socket.io"),
	Player = require("./Player").Player;

var socket, waitingForPlayers, gameId;
global.players;
global.main = this;
global.PLAYERCOUNT = 4;

var Game1 = require("./minigames/Game1");

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
	if (players.length < PLAYERCOUNT) {
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
		case 1: Game1.init(); break;
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
	client.on("game1 submit", Game1.onSubmit);
	client.on("game1 get item name", Game1.onGetItemName);
	client.on("get players list", onGetPlayersList);
};

function onClientDisconnect() {
	util.log("Player has disconnected: " + this.id);
};

function onGetPlayersList() {
	this.emit("players list", {players:  players});
};

function onNewPlayer(data) {
	var newPlayer = new Player(data.name, data.threshold);
	newPlayer.id = this.id;
	this.emit("assign player id", {pid: this.id});
	
	this.broadcast.emit("new player", {id: newPlayer.id, name: newPlayer.name, threshold: newPlayer.threshold});
	var i, existingPlayer;
	for (i = 0; i < players.length; i++) {
		existingPlayer = players[i];
		this.emit("new player", {id: existingPlayer.id, name: existingPlayer.name, y: existingPlayer.threshold});
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
	if (gameId >= 0) {
		this.emit("found game", {gameId: gameId});
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
