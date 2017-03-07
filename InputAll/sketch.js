var socket;

var circles = new Array();
var colors = new Array('rgb(255,75,56)', "rgb(120,120,120)", "rgb(10,10,10)", "rgb(255,255,255)");
var fillColors = new Array('rgba(255,75,56, 0.5)', "rgba(120,120,120,0.2)", "rgba(10,10,10,0.2)", "rgba(255,255,255,0.4)");
var mouseXPressed = 0;
var mouseYPressed = 0;
var nextColorIndex = 0;
var currentCircle;


function setup() {
  createCanvas(1440, 750);
  noFill();
  setupUI();
  
  if(typeof io === "function"){ 
    var host = "localhost";   
    socket = io("http://" + host + ":3000");  
    socket.on('connect', function() { print(" Connected. "); });    
  } else {
    console.log("io library not loaded.");
  }
}

function sendOsc(address, value) {
  if(socket){    
    socket.emit('message', [address].concat(value));
  }
}

function setupUI() {
  var inputWidth = 800;
  wordInput = createInput();
  wordInput.position(width / 2 - (inputWidth/2), height / 2);
  wordInput.size(inputWidth, 30);
  wordInput.elt.focus();
  wordInput.input(onKeyPressed);
}

function onKeyPressed(event) {
  
}

function keyPressed(){
  if(keyCode == RETURN){
    var currentValue = wordInput.value();
    var sentences = currentValue.split(".");
    console.log(sentences);

    for (var i = 0; i < sentences.length; ++i) {    
      var sentence = sentences[i];
      if(sentence == ""){ continue; }
      sendOsc("/words", sentence);
    }
    wordInput.value("");
  }
}

function draw() {
  background(50);

  if (mouseIsPressed) {
    var fillColor = fillColors[nextColorIndex];
    var color = colors[nextColorIndex];
    currentCircle = createCircle(color, fillColor);
    drawCircle(currentCircle);
    wordInput.elt.focus();
  }

  for (var i = 0; i < circles.length; ++i) {
    drawCircle(circles[i]);
  }
  
  fill(255, 75, 56);
  noStroke();
  textSize(30);
  textAlign(LEFT, TOP);
  textFont("Courier", "monospace");
  var labelWidth = 350;
  var labelHeight = 120;
  var labelX = width / 2 - labelWidth / 2;
  var labelY = height / 2 - labelHeight / 2;
  text("What's on your mind?", labelX, labelY, labelX, labelY);
  
}

function mousePressed() {
  mouseXPressed = mouseX;
  mouseYPressed = mouseY;

  if (circles.length >= 4) {
    circles.splice(0, 1);
  }
}

function mouseReleased() {

  circles.push(currentCircle);
  ++nextColorIndex
  if (nextColorIndex == colors.length) {
    nextColorIndex = 0;
  }
  
 for (var i = 0; i < circles.length; ++i){
  var circle = circles[i];
  sendOsc("/circles", i+1 + ";" + circle.x + ";" + circle.y +";" + circle.radius);
 }

  
}

function calculateRadius() {
  return 2 * int(dist(mouseXPressed, mouseYPressed, mouseX, mouseY));
}

function createCircle(color, fillColor) {
  var circle = new Object({});
  circle.radius = calculateRadius();
  circle.x = mouseXPressed;
  circle.y = mouseYPressed;
  circle.color = color;
  circle.fillColor = fillColor;
  circle.currentExpand = 0.01;
  circle.currentExpandDirection = 1.0;
  return circle;
}

function mouseDistanceToCenter() {
  return int(dist(width / 2, height / 2, mouseX, mouseY));
}

function rgbColor(color){
  return "rgba(" + color.r + "," + color.g + "," + color.b + "," + color.a + ")";
}

function drawCircle(circle) {

  var maximumExpand = circle.radius / 10.0;
  var expandStep = (maximumExpand / circle.radius*20/*8.0*/) * circle.currentExpandDirection;

  circle.currentExpand = circle.currentExpand + expandStep;
  if(circle.currentExpand > maximumExpand || circle.currentExpand < -maximumExpand){
     circle.currentExpandDirection *= -1;
     //circle.currentExpand = 0;
  }
  
  fill(color(circle.fillColor));
  stroke(color(circle.color));
  strokeWeight(0);
  ellipse(circle.x, circle.y, circle.radius + circle.currentExpand, circle.radius + circle.currentExpand);

  }
