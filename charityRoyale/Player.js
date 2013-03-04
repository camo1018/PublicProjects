// Server Player

var Player = function(startName, startThreshold) {
	var name = startName,
		threshold = startThreshold,
		score = 0,
		id;

	return {
		name: name,
		threshold: threshold,
		score: score,
		id: id
	}
};

exports.Player = Player;
