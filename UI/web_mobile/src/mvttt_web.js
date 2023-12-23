let matches;
let games;
let currentMatch;
let currentGame;

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
            startDebug();
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

function startDebug() {
    // document.querySelector("#div_debugMenu").style.display = "block";
    document.querySelector("#in_username").value = "1234";
    document.querySelector("#in_password").value = "1234!@#$";
    loginPlayer();
}

function selectBoardSpace(e) {
    if (e.target.innerText === "") e.target.innerText = "X";
    else if (e.target.innerText === "X") e.target.innerText = "O";
    else e.target.innerText = "";
}

async function selectMatch(e){
    document.querySelector("#div_matchMenuTableCon").innerHTML = "<span>Loading...</span>";
    localStorage["match_id"] = e.target.dataset.match_id;
    await startMatch();
    document.querySelector("#div_matchMenu").style.display = "none";
}

function fillMatchesMenu(matches) {
    if(matches[0].match_id === -1){
        alert("No Matches Found");
    }
    let tStr = `<table class="menuTable" id="table_matches"><tbody>`;
    for (let i = 0; i < matches.length; i++) {
        let opponentName;
        if(+matches[i].player_x_id === +localStorage["player_id"]) opponentName = matches[i].player_x_name;
        else opponentName = matches[i].player_o_name;
        tStr += `<tr><td class="matchOption" data-match_id="${matches[i].match_id}">${opponentName}</td></tr>`;
    }
    tStr += "</tbody></table>";
    document.querySelector("#div_matchMenuTableCon").innerHTML = tStr;
}

function updateMatchInfo(){
    let p1;
    let p2;
    if(localStorage["player"] === "x"){
        p1 = "x";
        p2 = "o";
    }
    else{
        p1 = "o";
        p2 = "x";
    }
    document.querySelector("#span_playerSymbol_left").innerText = p1.toUpperCase();
    document.querySelector("#span_playerSymbol_right").innerText = p2.toUpperCase();
    document.querySelector("#table_matchInfo_left tr:nth-child(1) td").innerText = currentMatch[`player_${p1}_name`];
    document.querySelector("#table_matchInfo_left tr:nth-child(2) td").innerText = currentMatch[`player_${p1}_score`];
    document.querySelector("#table_matchInfo_right tr:nth-child(1) td").innerText = currentMatch[`player_${p2}_name`];
    document.querySelector("#table_matchInfo_right tr:nth-child(2) td").innerText = currentMatch[`player_${p2}_score`];
}

async function startMatch(){
    if(matches.filter(match => match.match_id === +localStorage["match_id"])[0].player_x_id === +localStorage["player_id"]) localStorage["player"] = "x";
    else localStorage["player"] = "o";
    currentMatch = matches.filter(match => match.match_id === +localStorage["match_id"])[0];
    updateMatchInfo();
    await getGames();
    currentGame = games.filter(game => game.game_id === currentMatch.match_lastMoveGame)[0];
    startGame();
}

function selectGame(e){
    
    startGame();
}

function startGame(){
    fillBoard(currentGame.board_current);
}

function fillBoard(board) {
    for (let i = 0; i < 9; i++) {
        let piece = "";
        (board[i] === "-") ? piece = "" : piece = board[i];
        document.querySelector(`#table_mainBoard tr:nth-child(${Math.floor(i / 3) + 1}) > td:nth-child(${(i % 3) + 1})`).innerText = piece;
    }
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
    let credSet = { username: "1234", password: "1234!@#$" };

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

async function getGames(){
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