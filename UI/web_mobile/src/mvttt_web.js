let currentMatch;
let currentGame;
let gotMatches = false;
let gotGames = false;

document.addEventListener("DOMContentLoaded", e => {
    init();
});

document.body.addEventListener("click", e => {
    switch (e.target.className) {
        case "boardSpace":
            selectBoardSpace(e);
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
    if(result.success){
        document.querySelector("#div_loginMenu").style.display = "none";
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