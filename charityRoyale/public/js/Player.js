/**************************************************
** GAME PLAYER CLASS
**************************************************/
var Player = function(startName, startThreshold) {
	var name = startName,
		threshold = startThreshold,
		score = 0,
		id;
	
	var getName = function() {
	    return name;
	};

	var getThreshold = function() {
	    return threshold;
	};

	var getScore = function() {
	    return score;
	};

	var setScore = function(newScore) {
	    score = newScore;
	};

	return {
		getName: getName,
		getThreshold: getThreshold,
		getScore: getScore,
		setScore: setScore
	}
};