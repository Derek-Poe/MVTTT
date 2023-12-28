let debugMode = true;
let matches;
let games;
let currentMatch;
let currentGame;
let checkingForMatchUpdates;
let updateMatchCheckInterval = 1000;
let maxCreationGames = 9;
let maxDestructionGames = 9;
let creationGame;
let validatingCredsInput;
let checkingForNewMatches;
let checkForNewMatchInterval = 1000;

let winPattens = ["***------", "*---*---*", "*--*--*--", "-*--*--*-", "--*-*-*--", "--*--*--*", "---***---", "------***"];

document.addEventListener("DOMContentLoaded", e => {
    init();
});

document.body.addEventListener("click", e => {
    switch (true) {
        case /boardSpace/.test(e.target.className):
            selectBoardSpace(e);
            break;
        case /matchOption/.test(e.target.className):
            selectMatch(e);
            break;
        case /gameDrawerBoardClickOverlay/.test(e.target.className):
            selectGame(e);
            break;
        case /playerOption/.test(e.target.className):
            selectMatchCreationPlayerOption(e);
            break;
    }
    switch (e.target.id) {
        case "btn_login":
            loginPlayer();
            break;
        case "btn_logout":
            logoutPlayer();
            break;
        case "btn_signUp":
            openPlayerCreation();
            break;
        case "btn_cancelCreatePlayer":
            closePlayerCreation();
            break;
        case "btn_createPlayer":
            createPlayer();
            break;
        case "btn_newMatch":
            openMatchCreation();
            break;
        case "btn_cancelCreateMatch":
            closeMatchCreation();
            break;
        case "btn_createMatch":
            createMatch();
            break;
    }
});

document.body.addEventListener("keyup", e => {
    if (debugMode) {
        switch (e.key) {
            case "0":
                startDebug(1);
                break;
            case "-":
                startDebug(2);
                break;
            case "=":
                startDebug(3);
                break;
        }
    }
    switch (e.target.id) {
        case "in_playerCreationUsername":
        case "in_playerCreationPassword":
        case "in_playerCreationPasswordConf":
            validateCredsInputTrigger(e.target.id);
            break;
    }
});

function init() {
    checkForSession();
}

async function checkForSession() {
    if (typeof localStorage["session"] === "undefined" || localStorage["session"] === "" || typeof localStorage["loggedIn"] === "undefined" || localStorage["loggedIn"] === "false") {
        localStorage["session"] = crypto.randomUUID();
    }
    else if (typeof localStorage["player_id"] !== "undefined" && typeof localStorage["player_name"] !== "undefined") {
        document.querySelector("#in_username").value = "";
        document.querySelector("#in_password").value = "";
        document.querySelector("#div_loginMenu").style.display = "none";
        fillMatchesMenu(await getMatches());
        checkForNewMatch();
    }
}

function startDebug(op) {
    // document.querySelector("#div_debugMenu").style.display = "block";
    switch (op) {
        case 1:
            document.querySelector("#in_username").value = "PlayerX";
            document.querySelector("#in_password").value = "1234";
            loginPlayer();
            break;
        case 2:
            document.querySelector("#in_username").value = "PlayerO";
            document.querySelector("#in_password").value = "1234";
            loginPlayer();
            break;
        case 3:
            document.querySelector("#in_username").value = "NewPlayer";
            document.querySelector("#in_password").value = "1234";
            loginPlayer();
            break;
    }
}

function openPlayerCreation() {
    document.querySelector("#div_playerCreationMenu").style.display = "flex";
    document.querySelector("#div_loginMenu").style.display = "none";
}

function closePlayerCreation() {
    document.querySelector("#div_loginMenu").style.display = "flex";
    document.querySelector("#div_playerCreationMenu").style.display = "none";
}

async function openMatchCreation() {
    document.querySelector("#div_matchCreationMenu").style.display = "flex";
    fillMatchesPlayerMenu(await getPlayers());
    document.querySelector("#div_matchMenu").style.display = "none";
}

function closeMatchCreation() {
    document.querySelector("#div_matchMenu").style.display = "flex";
    checkForNewMatch();
    document.querySelector("#div_matchCreationMenu").style.display = "none";
}

function validateCredsInputTrigger(target) {
    clearTimeout(validatingCredsInput);
    validatingCredsInput = setTimeout(validateCredsInput, 500, target);
}

function validateCredsInput(target) {
    let status = document.querySelector("#cell_playerCreationOptionsStatus");
    if (target === "in_playerCreationUsername") {
        let username = document.querySelector("#in_playerCreationUsername").value;
        if (username.length < 3) { status.innerText = "Username must be at least 3 characters"; return false; }
        if (/\s+/g.test(username)) { status.innerText = "Username cannot have spaces"; return false; }
    }
    else if (target === "in_playerCreationPassword") {
        let password = document.querySelector("#in_playerCreationPassword").value;
        if (password.length < 8) { status.innerText = "Password must be at least 8 characters"; return false; }
        if (/\s+/g.test(password)) { status.innerText = "Password cannot have spaces"; return false; }
        if (password.match(/[a-z]/g) === null || password.match(/[a-z]/g).length === 0) { status.innerText = "Password must have at least one lower case character"; return false; }
        if (password.match(/[A-Z]/g) === null || password.match(/[A-Z]/g).length === 0) { status.innerText = "Password must have at least one upper case character"; return false; }
        if (password.match(/[0-9]/g) === null || password.match(/[0-9]/g).length === 0) { status.innerText = "Password must have at least one number"; return false; }
        if (password.match(/[^a-zA-Z\d]/g) === null || password.match(/[^a-zA-Z\d]/g).length === 0) { status.innerText = "Password must have at least one special character"; return false; }
    }
    else if (target === "in_playerCreationPasswordConf") {
        let password = document.querySelector("#in_playerCreationPassword").value;
        let passwordConf = document.querySelector("#in_playerCreationPasswordConf").value;
        if (password !== passwordConf && password !== "") { status.innerText = "Passwords must match"; return false; }
    }
    status.innerText = "";
    return true;
}

function fillMatchesPlayerMenu(players) {
    let tStr = `<table class="menuTable" id="table_playerOptions"><tbody><tr><th>Name</th><th>Wins</th><th>Losses</th></tr>`;
    for (let i = 0; i < players.length; i++) {
        if (players[i].player_id === +localStorage["player_id"]) continue;
        tStr += `<tr class="unselectedPlayer" data-player_id="${players[i].player_id}"><td class="playerOption rowLeft">${players[i].player_name}</td><td class="playerOption rowMiddle">${players[i].player_wins}</td><td class="playerOption rowRight">${players[i].player_losses}</td></tr>`;
    }
    tStr += "</tbody></table>";
    document.querySelector("#div_matchCreationPlayersTableCon").innerHTML = tStr;
}

function selectMatchCreationPlayerOption(e) {
    document.querySelectorAll(".selectedPlayer").forEach(row => row.className = "unselectedPlayer");
    e.target.parentNode.className = "selectedPlayer";
}

async function selectMatch(e) {
    document.querySelector("#div_matchMenuTableCon").innerHTML = "<span>Loading...</span>";
    localStorage["match_id"] = e.target.dataset.match_id;
    await startMatch();
    document.querySelector("#div_matchMenu").style.display = "none";
}

function fillMatchesMenu(matches) {
    if (matches[0].match_id === -1) {
        document.querySelector("#div_matchMenuTableCon").innerHTML = `<span>You are not currently in any matches...</span>`;
        document.querySelector("#div_matchMenuButtonsCon").style.display = "flex";
        return;
    }
    let tStr = `<table class="menuTable" id="table_matches"><tbody>`;
    for (let i = 0; i < matches.length; i++) {
        let opponentName;
        if (matches[i].player_x_id === +localStorage["player_id"]) opponentName = matches[i].player_o_name;
        else opponentName = matches[i].player_x_name;
        tStr += `<tr><td class="matchOption" data-match_id="${matches[i].match_id}">${opponentName}</td></tr>`;
    }
    tStr += "</tbody></table>";
    document.querySelector("#div_matchMenuTableCon").innerHTML = tStr;
    document.querySelector("#div_matchMenuButtonsCon").style.display = "flex";
}

function changeMatchUpdateToken(token) {
    token = `${token}`;
    localStorage["match_updateToken"] = token;
}

function changePlayerMatchesUpdateToken(token) {
    token = `${token}`;
    localStorage["player_matchesUpdateToken"] = token;
}

async function updateMatchInfo() {
    changeMatchUpdateToken(currentMatch.match_updateToken);
    let p1;
    let p2;
    if (localStorage["player_symbol"] === "X") {
        p1 = "X";
        p2 = "O";
    }
    else {
        p1 = "O";
        p2 = "X";
    }
    document.querySelector("#span_playerSymbol_left").innerText = p1;
    document.querySelector("#span_playerSymbol_left").style.color = "inherit";
    document.querySelector("#span_playerSymbol_right").innerText = p2;
    document.querySelector("#span_playerSymbol_right").style.color = "inherit";
    document.querySelector("#span_playerName_left").innerText = currentMatch[`player_${p1.toLowerCase()}_name`];
    document.querySelector("#span_playerName_left").style.fontSize = `${lerp(300, 25, (currentMatch[`player_${p1.toLowerCase()}_name`].length / 20))}%`;
    document.querySelector("#span_playerScore_left").innerText = currentMatch[`player_${p1.toLowerCase()}_score`];
    document.querySelector("#span_playerName_right").innerText = currentMatch[`player_${p2.toLowerCase()}_name`];
    document.querySelector("#span_playerName_right").style.fontSize = `${lerp(300, 25, (currentMatch[`player_${p2.toLowerCase()}_name`].length / 20))}%`;
    document.querySelector("#span_playerScore_right").innerText = currentMatch[`player_${p2.toLowerCase()}_score`];
    if (
        (currentMatch.match_turn === 1 && p1 === "X") ||
        (currentMatch.match_turn === 2 && p1 === "O")
    ) document.querySelector("#span_playerSymbol_left").style.color = "#ff0000";
    else document.querySelector("#span_playerSymbol_right").style.color = "#ff0000";
    clearTimeout(checkingForMatchUpdates);
    if (
        (currentMatch.match_turn === 1 && p1 === "O") ||
        (currentMatch.match_turn === 2 && p1 === "X")
    ) updateMatchCheck();
}

async function startMatch() {
    currentMatch = await getMatch(+localStorage["match_id"]);
    if (currentMatch.player_x_id === +localStorage["player_id"]) localStorage["player_symbol"] = "X";
    else localStorage["player_symbol"] = "O";
    if (localStorage["player_symbol"] === "X") {
        localStorage["opponent_id"] = currentMatch.player_o_id;
        localStorage["opponent_name"] = currentMatch.player_o_name;
    }
    else {
        localStorage["opponent_id"] = currentMatch.player_x_id;
        localStorage["opponent_name"] = currentMatch.player_x_name;
    }
    localStorage["match_type"] = currentMatch.match_type;
    if (+localStorage["match_type"] === 1) {
        localStorage["match_typeName"] = "Creation";
    }
    else if (+localStorage["match_type"] === 2) {
        localStorage["match_typeName"] = "Destruction";
    }
    localStorage["match_boardLimit"] = currentMatch.match_boardLimit;
    localStorage["match_scoreGoal"] = currentMatch.match_scoreGoal;
    await updateMatchInfo();
    games = await getGames();
    if (games.filter(game => game.game_id === currentMatch.match_lastMoveGame).length > 0) currentGame = games.filter(game => game.game_id === currentMatch.match_lastMoveGame)[0];
    else currentGame = games[0];
    fillGameDrawer();
    fillMainBoard();
}

async function updateMatchCheck() {
    let updateToken = await getMatchUpdateToken(currentMatch.match_id);
    if (updateToken !== +localStorage["match_updateToken"]) {
        currentMatch = await getMatch(currentMatch.match_id);
        await updateMatchInfo();
        games = await getGames();
        if (games.filter(game => game.game_id === currentMatch.match_lastMoveGame).length > 0) {
            currentGame = games.filter(game => game.game_id === currentMatch.match_lastMoveGame)[0];
            fillMainBoard();
        }
        else {
            currentGame = games[0];
            document.querySelector("#table_mainBoard").style.display = "none";
        }
        fillGameDrawer();
    }
    else setTimeout(updateMatchCheck, updateMatchCheckInterval);
}

async function checkForNewMatch() {
    let updateToken = await getPlayerMatceshUpdateToken(localStorage["player_id"]);
    if (updateToken !== +localStorage["player_matchesUpdateToken"]) {
        fillMatchesMenu(await getMatches());
        changePlayerMatchesUpdateToken(updateToken);
    }
    setTimeout(checkForNewMatch, updateMatchCheckInterval);
}

function selectGame(e) {
    document.querySelectorAll(".gameDrawerBoardCon").forEach(con => con.style.borderColor = "#ffffff");
    e.target.parentNode.style.borderColor = "#ff0000";
    currentGame = e.target.parentNode.querySelector("table").game;
    fillMainBoard();
}

function fillGameDrawer() {
    document.querySelectorAll(".gameDrawerRow").forEach(row => row.innerHTML = "");
    for (let i = 0; i < games.length; i++) {
        let board = document.createElement("table");
        board.game = games[i];
        board.className = "gameDrawerBoard";
        board.innerHTML = `<tbody><tr><td></td><td></td><td></td></tr><tr><td></td><td></td><td></td></tr><tr><td></td><td></td><td></td></tr></tbody>`;
        board = fillGameDrawerBoard(board);
        let con = document.createElement("div");
        con.className = "gameDrawerBoardCon";
        if (board.game.game_id === currentGame.game_id) con.style.borderColor = "#ff0000";
        con.appendChild(board);
        let clickOL = document.createElement("div");
        clickOL.className = "gameDrawerBoardClickOverlay";
        con.appendChild(clickOL);
        document.querySelector(`.gameDrawerRow:nth-child(${(i % 3) + 1})`).appendChild(con);
    }
}

function selectBoardSpace(e) {
    if (
        (currentMatch.match_turn === 1 && localStorage["player_symbol"] === "O") ||
        (currentMatch.match_turn === 2 && localStorage["player_symbol"] === "X")
    ) {
        alert(`It is currently ${localStorage["opponent_name"]}'s turn.`);
        return;
    }
    if (e.target.innerText !== "") {
        alert("Board space is occupied.");
        return;
    }
    e.target.innerText = localStorage["player_symbol"];
    updateCurrentGameData();
    if (+localStorage["match_type"] === 1) performCreationMove();
    else turnUpdate(false);
}

function performCreationMove() {
    creationGame = JSON.parse(JSON.stringify(currentGame));
    if (games.length < maxCreationGames) {
        let spaces = [];
        let currentGameMovePos;
        for (let i = 0; i < 9; i++) {
            if (currentGame.board_current[i] !== currentGame.board_prev[i]) currentGameMovePos = i;
            if (creationGame.board_current[i] === "-") spaces.push(i);
        }
        if (spaces.length > 0) {
            let pos = spaces[randomInt(0, (spaces.length - 1))];
            creationGame.board_current = `${creationGame.board_current.substring(0, pos)}${localStorage["player_symbol"]}${creationGame.board_current.substring((pos + 1))}`;
            creationGame.board_current = `${creationGame.board_current.substring(0, currentGameMovePos)}-${creationGame.board_current.substring((currentGameMovePos + 1))}`;
            turnUpdate(true);
        }
        else turnUpdate(false);
    }
    else turnUpdate(false);
}

function fillMainBoard() {
    boardStr = currentGame.board_current;
    for (let i = 0; i < 9; i++) {
        let piece = "";
        (boardStr[i] === "-") ? piece = "" : piece = boardStr[i];
        document.querySelector(`#table_mainBoard tr:nth-child(${Math.floor(i / 3) + 1}) > td:nth-child(${(i % 3) + 1})`).innerText = piece;
    }
    document.querySelector("#table_mainBoard").style.display = "table";
}

function fillGameDrawerBoard(board) {
    boardStr = board.game.board_current;
    for (let i = 0; i < 9; i++) {
        let piece = "";
        (boardStr[i] === "-") ? piece = "" : piece = boardStr[i];
        board.querySelector(`tr:nth-child(${Math.floor(i / 3) + 1}) > td:nth-child(${(i % 3) + 1})`).innerText = piece;
    }
    return board;
}

function updateCurrentGameData() {
    let boardStr = "";
    document.querySelectorAll("#table_mainBoard td").forEach(cell => {
        if (cell.innerText === "") {
            boardStr += "-";
        }
        else boardStr += cell.innerText;
    });
    (currentMatch.match_turn === 1) ? currentMatch.match_turn = 2 : currentMatch.match_turn = 1;
    currentMatch.match_lastMoveGame = currentGame.game_id;
    currentGame.board_prev = currentGame.board_current;
    currentGame.board_current = boardStr;
    currentGame.game_lastPlayer = +localStorage["player_id"];
    currentGame.game_status = 1;
    if (checkForGameWin(currentGame, localStorage["player_symbol"])) {
        currentGame.game_status = 2;
        currentGame.game_winner = +localStorage["player_id"];
        currentMatch[`player_${localStorage["player_symbol"].toLowerCase()}_score`]++;
        document.querySelector("#table_mainBoard").style.display = "none";
    }
}

function updateCreationGameData(gameData) {
    currentGame.board_prev = currentGame.board_current;
    currentGame.board_current = boardStr;
    currentGame.game_lastPlayer = +localStorage["player_id"];
    currentGame.game_status = 1;
    if (checkForGameWin(currentGame, localStorage["player_symbol"])) {
        currentGame.game_status = 2;
        currentGame.game_winner = +localStorage["player_id"];
        currentMatch[`player_${localStorage["player_symbol"].toLowerCase()}_score`]++;
        document.querySelector("#table_mainBoard").style.display = "none";
    }
}

function checkForGameWin(game, player) {
    checkStr = game.board_current.replaceAll(player, "*");
    for (let i = 0; i < winPattens.length; i++) {
        patHit = 0;
        for (let ii = 0; ii < winPattens[i].length; ii++) {
            if (winPattens[i][ii] === checkStr[ii] && checkStr[ii] === "*") {
                patHit++;
                if (patHit > 2) {
                    return true;
                }
            }
        }
    }
    return false;
}

async function loginPlayer() {
    if (document.querySelector("#in_username").value.toLowerCase().trim() === "debug") { startDebug(+document.querySelector("#in_password").value); return; }
    let credSet = { username: document.querySelector("#in_username").value.trim(), password: document.querySelector("#in_password").value, email: "" };
    let res = await fetch("login", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "session": localStorage["session"]
        },
        body: JSON.stringify(credSet)
    });
    let result = await res.json();
    if (result.success) {
        localStorage["player_id"] = result.id;
        localStorage["player_name"] = result.username;
        document.querySelector("#in_username").value = "";
        document.querySelector("#in_password").value = "";
        document.querySelector("#div_loginMenu").style.display = "none";
        localStorage["loggedIn"] = "true";
        fillMatchesMenu(await getMatches());
        checkForNewMatch();
    }
    else {
        alert("incorrect username/password");
    }
}

function logoutPlayer() {
    fetch("logout", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "session": localStorage["session"]
        },
        body: JSON.stringify({ str: localStorage["session"] })
    });
    clearInterval(checkingForNewMatches);
    localStorage["loggedIn"] = "false";
    localStorage["session"] = "";
    localStorage["player_id"] = "";
    localStorage["player_name"] = "";
    document.querySelector("#div_loginMenu").style.display = "flex";
}

async function createPlayer() {
    if(!validateCredsInput("in_playerCreationUsername")) return;
    if(!validateCredsInput("in_playerCreationPassword")) return;
    if(!validateCredsInput("in_playerCreationPasswordConf")) return;
    let credSet = { username: document.querySelector("#in_playerCreationUsername").value, password: document.querySelector("#in_playerCreationPassword").value, email: "" };
    let res = await fetch("createPlayer", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(credSet)
    });
    let result = await res.json();
    if (result.success) {
        localStorage["player_id"] = result.id;
        localStorage["player_name"] = document.querySelector("#in_playerCreationUsername").value;
        document.querySelector("#div_playerCreationMenu").style.display = "none";
        localStorage["loggedIn"] = "true";
        fillMatchesMenu(await getMatches());
        checkForNewMatch();
    }
    else {
        document.querySelector("#cell_playerCreationOptionsStatus").innerText = "Username has already been taken";
    }
}

async function createMatch() {
    if (document.querySelectorAll(".selectedPlayer").length === 0) {
        alert("Opponent must be selected.");
        return;
    }
    let playerPositions;
    if (randomInt(0, 1) === 0) playerPositions = `${localStorage["player_id"]},${document.querySelectorAll(".selectedPlayer")[0].dataset.player_id}`;
    else playerPositions = `${document.querySelectorAll(".selectedPlayer")[0].dataset.player_id},${localStorage["player_id"]}`;
    let gameMode;
    if (document.querySelector("#sel_gameModes").value === "Creation") gameMode = 1;
    else if (document.querySelector("#sel_gameModes").value === "Destruction") gameMode = 2;
    else gameMode = 0;
    let scoreGoal = document.querySelector("#sel_pointsToWin").value;
    document.querySelector("#div_matchCreationMenu").innerHTML = "<span>Loading...</span>";
    let res = await fetch("createMatch", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ str: `${playerPositions},${randomInt(1, 2)},${gameMode},${scoreGoal}` })
    });
    let match = await res.json();
    localStorage["match_id"] = match.match_id;
    await startMatch();
    clearInterval(checkingForNewMatches);
    document.querySelector("#div_matchCreationMenu").style.display = "none";
}

function completeMatch() {

}

async function getPlayers() {
    let res = await fetch("getPlayers", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "session": localStorage["session"]
        },
        body: JSON.stringify({})
    });
    let players = await res.json();
    if (!Array.isArray(players)) players = [players];
    return players;
}

async function getMatchUpdateToken(match_id) {
    let res = await fetch("getMatchUpdateToken", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "session": localStorage["session"]
        },
        body: JSON.stringify({ str: match_id })
    });
    let updateToken = await res.json();
    return updateToken.token;
}

async function getPlayerMatceshUpdateToken(player_id) {
    let res = await fetch("getPlayerMatceshUpdateToken", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "session": localStorage["session"]
        },
        body: JSON.stringify({ str: player_id })
    });
    let updateToken = await res.json();
    return updateToken.token;
}

async function getMatch(match_id) {
    let res = await fetch("getMatch", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "session": localStorage["session"]
        },
        body: JSON.stringify({ str: match_id })
    });
    let match = await res.json();
    return match;
}

async function getMatches() {
    let res = await fetch("getMatches", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "session": localStorage["session"]
        },
        body: JSON.stringify({ str: +localStorage["player_id"] })
    });
    matches = await res.json();
    if (!Array.isArray(matches)) matches = [matches];
    return matches;
}

async function getGames() {
    let res = await fetch("getGames", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "session": localStorage["session"]
        },
        body: JSON.stringify({ str: +localStorage["match_id"] })
    });
    let gamesData = await res.json();
    if (!Array.isArray(games)) games = [games];
    return gamesData;
}

async function gameUpdate(game) {
    let res = await fetch("updateGame", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "session": localStorage["session"]
        },
        body: JSON.stringify(game)
    });
    // let gameData = await res.json();
}

async function matchUpdate(match) {
    let res = await fetch("updateMatch", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "session": localStorage["session"]
        },
        body: JSON.stringify(match)
    });
    let matchData = await res.json();
    return matchData;
}

async function turnUpdate(creation) {
    let bodyStr;
    if (creation) bodyStr = `creation<~>${JSON.stringify(currentMatch)}<~>${JSON.stringify(currentGame)}<~>${JSON.stringify(creationGame)}`;
    else bodyStr = `normal<~>${JSON.stringify(currentMatch)}<~>${JSON.stringify(currentGame)}`;
    let res = await fetch("turnUpdate", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "session": localStorage["session"]
        },
        body: JSON.stringify({ str: bodyStr })
    });
    if (+localStorage["match_type"] === 1) {
        let oldGames = JSON.parse(JSON.stringify(games));
        games = await getGames();
        let newGameIds = games.map(game => game.game_id).filter(gId => oldGames.map(oldGame => oldGame.game_id).indexOf(gId) === -1);
        if (newGameIds.length > 0) {
            let newGame = games.filter(game => game.game_id === newGameIds[0])[0];
            if (checkForGameWin(newGame, localStorage["player_symbol"])) {
                newGame.game_status = 2;
                newGame.game_winner = +localStorage["player_id"];
                currentMatch[`player_${localStorage["player_symbol"].toLowerCase()}_score`]++;
                await gameUpdate(newGame);
            }
        }
        currentMatch = await matchUpdate(currentMatch);
        await updateMatchInfo();
        games = await getGames();
        fillGameDrawer();
    }
    else {
        currentMatch = await res.json();
        await updateMatchInfo();
        games = await getGames();
        fillGameDrawer();
    }
}

const lerp = (a, b, alpha) => a + alpha * (b - a);
const randomInt = (min, max) => Math.floor(Math.random() * (max - min + 1) + min);