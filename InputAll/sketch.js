var socket;

var circles = new Array();
var colors = new Array('rgb(255,75,56)', "rgb(120,120,120)", "rgb(10,10,10)", "rgb(255,255,255)");
var fillColors = new Array('rgba(255,75,56, 0.5)', "rgba(120,120,120,0.2)", "rgba(10,10,10,0.2)", "rgba(255,255,255,0.4)");
var maxCircles = 4;
var mouseXPressed = 0;
var mouseYPressed = 0;
var currentCircle = null;
var isExistingCircle = false;

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
  return;
  if(socket){    
    socket.emit('message', [address].concat(value));
    console.log([address].concat(value));
  }
}

function setupUI() {
  var inputWidth = 350;
  wordInput = createInput();
  wordInput.position(width / 2 - (inputWidth/2), height / 2);
  wordInput.size(inputWidth, 30);
  wordInput.elt.focus();
  wordInput.input(function(evt){});
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

function mouseDragged() {  
  if(!currentCircle) return;

  if(isExistingCircle && keyIsDown(SHIFT)) {
    currentCircle.x = mouseX;
    currentCircle.y = mouseY;
  } else {
    currentCircle.radius = calculateRadius();
  }    
}

function intersectingCircle(x,y){
  var circleIndex = -1;
  var minDistance = Number.MAX_VALUE;

  for (var i = 0; i < circles.length; ++i) {
    var circle = circles[i];
    var distance = Math.sqrt((x-circle.x)*(x-circle.x) + (y-circle.y)*(y-circle.y))
        
    if(distance < round(circle.radius + circle.currentExpand) / 2 && distance < minDistance){
      circleIndex = i;
    }
  }  
  return circleIndex;
}

function mousePressed() {  
  mouseXPressed = mouseX;
  mouseYPressed = mouseY;
  var clickedCircleIndex = intersectingCircle(mouseXPressed, mouseYPressed);

  if (clickedCircleIndex != -1) {        
    currentCircle  = circles[clickedCircleIndex];
    isExistingCircle = true;
    return;
  } else {
    isExistingCircle = false;
  }

  if (circles.length >= maxCircles) {    
    //circles.splice(0, 1);
  }

  if(circles.length < maxCircles) {    
    var id = circles.length + 1;
    var fillColor = fillColors[circles.length];
    var color = colors[circles.length];    
    currentCircle = createCircle(id, color, fillColor);    
    circles.push(currentCircle);
  }
}

function mouseReleased() {
  if(currentCircle){
    sendOsc("/circles", currentCircle.id + ";" + currentCircle.x + ";" + currentCircle.y +";" + currentCircle.radius);
  }
  currentCircle = null;
  wordInput.elt.focus();
}

function calculateRadius() {
  var radius = 2 * int(dist(mouseXPressed, mouseYPressed, mouseX, mouseY));
  return radius == 0 ? 1 : radius;  
}

function createCircle(id, color, fillColor) {
  var circle = new Object({});
  circle.id = id;
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
  if(circle == currentCircle) expandStep = 0;
  circle.currentExpand = circle.currentExpand + expandStep;

  if(circle.currentExpand > maximumExpand || circle.currentExpand < -maximumExpand){
     circle.currentExpandDirection *= -1;
  }  
  
  fill(color(circle.fillColor));
  stroke(color(circle.color));
  strokeWeight(0);
  ellipse(circle.x, circle.y, circle.radius + circle.currentExpand, circle.radius + circle.currentExpand);
}
