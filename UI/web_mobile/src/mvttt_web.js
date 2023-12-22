let currentMatch;
let currentGame;
let gotMatches = false;
let gotGames = false;

document.body.addEventListener("click", e => {
    switch (e.target.className) {
        case "boardSpace":
            selectBoardSpace(e);
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

function startDebug() {
    document.querySelector("#div_debugMenu").style.display = "block";
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
