
let matches;
let games;
let currentMatch;
let currentGame;
let checkingForMatchUpdates;

let winPattens = ["***------", "*---*---*", "*--*--*--", "-*--*--*-", "--*-*-*--", "--*--*--*", "---***---", "------***"];

document.addEventListener("DOMContentLoaded", e => {
    init();
});

document.body.addEventListener("click", e => {
    switch (e.target.className) {
        case "boardSpace":
            selectBoardSpace(e);
            break;
        case "matchOption":
            selectMatch(e);
            break;
        case "gameDrawerBoardClickOverlay":
            selectGame(e);
            break;
    }
    switch (e.target.id) {
        case "btn_login":
            loginPlayer();
            break;
    }
});

document.body.addEventListener("keyup", e => {
    switch (e.key) {
        case "d":
            startDebug(1);
            break;
        case "f":
            startDebug(2);
            break;
        case "t":
            turnUpdate();
            break;
    }
});

function init() {
    checkForSession();
}

function checkForSession() {
    if (typeof localStorage["session"] === "undefined") {
        localStorage["session"] = crypto.randomUUID();
    }
}

function startDebug(op) {
    // document.querySelector("#div_debugMenu").style.display = "block";
    switch (op) {
        case 1:
            document.querySelector("#in_username").value = "1234";
            document.querySelector("#in_password").value = "1234!@#$";
            loginPlayer();
            break;
        case 2:
            document.querySelector("#in_username").value = "2345";
            document.querySelector("#in_password").value = "2345@#$%";
            loginPlayer();
            break;
    }
}

async function selectMatch(e) {
    document.querySelector("#div_matchMenuTableCon").innerHTML = "<span>Loading...</span>";
    localStorage["match_id"] = e.target.dataset.match_id;
    await startMatch();
    document.querySelector("#div_matchMenu").style.display = "none";
}

function fillMatchesMenu(matches) {
    if (matches[0].match_id === -1) {
        alert("No Matches Found");
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
}

function changeMatchUpdateToken(token){
    token = `${currentMatch.match_updateToken}`;
    localStorage["match_updateToken"] = token;
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
    document.querySelector("#span_playerSymbol_left").style.color = "#000000";
    document.querySelector("#span_playerSymbol_right").innerText = p2;
    document.querySelector("#span_playerSymbol_right").style.color = "#000000";
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
        localStorage["opponent_id"] = currentMatch.player_o_name;
        localStorage["opponent_name"] = currentMatch.player_o_name;
    }
    else {
        localStorage["opponent_id"] = currentMatch.player_x_name;
        localStorage["opponent_name"] = currentMatch.player_x_name;
    }
    await updateMatchInfo();
    await getGames();
    if (games.filter(game => game.game_id === currentMatch.match_lastMoveGame).length > 0) currentGame = games.filter(game => game.game_id === currentMatch.match_lastMoveGame)[0];
    else currentGame = games[0];
    fillGameDrawer();
    fillMainBoard();
}

async function updateMatchCheck() {
    let updateToken = await getMatchUpdateToken(currentMatch.match_id);
    if (updateToken !== +localStorage["match_updateToken"]) {
        console.log("updating match");
        currentMatch = await getMatch(currentMatch.match_id);
        await updateMatchInfo();
        await getGames();
        if (games.filter(game => game.game_id === currentMatch.match_lastMoveGame).length > 0) {
            currentGame = games.filter(game => game.game_id === currentMatch.match_lastMoveGame)[0];
            fillMainBoard();
        }
        else {
            document.querySelector("#table_mainBoard").style.display = "none";
        }
        fillGameDrawer();
    }
    else setTimeout(updateMatchCheck, 2000);
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
    turnUpdate();
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
    let credSet = { username: document.querySelector("#in_username").value.toLowerCase().trim(), password: document.querySelector("#in_password").value };
    let res = await fetch("login", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "session": localStorage["session"]
        },
        body: JSON.stringify(credSet),
    });
    let result = await res.json();
    if (result.success) {
        localStorage["player_id"] = result.id;
        localStorage["player_name"] = result.username;
        document.querySelector("#div_loginMenu").style.display = "none";
        fillMatchesMenu(await getMatches());
    }
    else {
        alert("incorrect username/password");
    }
}

async function createPlayer() {
    let credSet = { username: "2345", password: "2345@#$%" };

    let res = await fetch("createPlayer", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(credSet),
    });
    let result = await res.json();
    console.log(result);
}

async function getMatch(match_id) {
    let res = await fetch("getMatch", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "session": localStorage["session"]
        },
        body: JSON.stringify(match_id),
    });
    let match = await res.json();
    return match;
}

async function getMatchUpdateToken(match_id) {
    let res = await fetch("getMatchUpdateToken", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "session": localStorage["session"]
        },
        body: JSON.stringify(match_id),
    });
    let updateToken = await res.json();
    return updateToken.token;
}

async function getMatches() {
    let res = await fetch("getMatches", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "session": localStorage["session"]
        },
        body: JSON.stringify(+localStorage["player_id"]),
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
        body: JSON.stringify(+localStorage["match_id"]),
    });
    games = await res.json();
    if (!Array.isArray(games)) games = [games];
    return games;
}

async function turnUpdate() {
    let res = await fetch("turnUpdate", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "session": localStorage["session"]
        },
        body: `${JSON.stringify(currentMatch)}<~>${JSON.stringify(currentGame)}`,
    });
    currentMatch = await res.json();
    await updateMatchInfo();
    await getGames();
    fillGameDrawer();
}

const lerp = (a, b, alpha) => a + alpha * (b - a);
