function buildCanvas() {
    let canvas = document.createElement("canvas");
    canvas.id = "can";
    canvas.width = 1080;
    canvas.height = 1920;
    canvas.ls = 1;
    canvas.rs = canvas.width;
    canvas.ts = 1;
    canvas.bs = canvas.height;
    canvas.w = canvas.rs - canvas.ls;
    canvas.h = canvas.bs - canvas.ts;
    canvas.ctx = canvas.getContext("2d");
    return document.body.appendChild(canvas);
}

const can = buildCanvas();

function drawBorder(){
    let ctx = can.ctx;
    ctx.beginPath();
    ctx.lineWidth = 1;
    ctx.strokeStyle = "#ffa700";
    ctx.moveTo(can.ls, can.ts);
    ctx.lineTo(can.rs, can.ts);
    ctx.lineTo(can.rs, can.bs);
    ctx.lineTo(can.ls, can.bs);
    ctx.lineTo(can.ls, can.ts);
    //ctx.closePath()
    ctx.stroke();
}
function drawMeasureLines(){
    let ctx = can.ctx;
    ctx.lineWidth = 1;
    ctx.strokeStyle = "#ffa700";
    ctx.beginPath();
    ctx.moveTo(can.ls, can.h * 0.25);
    ctx.lineTo(can.rs, can.h * 0.25);
    ctx.closePath();
    ctx.stroke();
    ctx.beginPath();
    ctx.moveTo(can.ls, can.h * 0.5);
    ctx.lineTo(can.rs, can.h * 0.5);
    ctx.closePath();
    ctx.stroke();
    ctx.beginPath();
    ctx.moveTo(can.ls, can.h * 0.75);
    ctx.lineTo(can.rs, can.h * 0.75);
    ctx.closePath();
    ctx.stroke();
    // ctx.beginPath();
    // console.log((can.w * 0.5), can.ts);
    // console.log((can.w * 0.5), can.bs);
    // ctx.moveTo((can.w * 0.5), can.ts);
    // ctx.lineTo((can.w * 0.5), can.bs);
    // ctx.closePath();
    // ctx.stroke();
    // ctx.beginPath();
    // ctx.moveTo(can.w * 0.5, can.ts);
    // ctx.lineTo(can.w * 0.5, can.bs);
    // ctx.stroke();
    // ctx.beginPath();
    // ctx.moveTo(can.w * 0.5, can.ts);
    // ctx.lineTo(can.w * 0.5, can.bs);
    // ctx.stroke();
}


// drawBorder();
drawMeasureLines()