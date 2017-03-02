var circles = new Array();
var colors = new Array('rgb(255,255,255)', "rgb(120,120,120)", "rgb(10,10,10)", "rgb(255,75,56)");
var fillColors = new Array('rgba(255,255,255, 0.2)', "rgba(120,120,120,0.2)", "rgba(10,10,10,0.2)", "rgba(255,75,56,0.5)");
var mouseXPressed = 0;
var mouseYPressed = 0;
var nextColorIndex = 0;
var currentCircle;

function setup() {
  createCanvas(1500, 600)
  noFill()
  setupUI()
}
var wordInput;


function setupUI() {
  wordInput = createInput();
  wordInput.position(width / 2 - 500, height / 2)
  wordInput.size(1000, 30)
  wordInput.elt.focus();
  wordInput.input(onKeyPressed);
}

function onKeyPressed() {
  console.log(wordInput.value());
  var currentValue = wordInput.value();
  if(currentValue.endsWith(".")) 
  {
    wordInput.value("");
  }
}

/*function keyPressed(){
  if (keyCode==RETURN){
    wordInput.value("");
  }
}*/

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

}

function mousePressed() {
  mouseXPressed = mouseX;
  mouseYPressed = mouseY;

  if (circles.length >= 4) {
    circles.splice(0, 1);
  }
    //wordInput.elt.focus();
}

//function mouseClicked(){
  //  wordInput.elt.focus();
//}

function mouseReleased() {

  circles.push(currentCircle);
  ++nextColorIndex
  if (nextColorIndex == colors.length) {
    nextColorIndex = 0;
  }

  for (var i = 0; i < circles.length; ++i) {
    var circle = circles[i];
    sendOsc("/circles", i + 1 + ";" + circle.x + ";" + circle.y + ";" + circle.radius);

  }

  //wordInput.elt.focus();
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
  return circle;
}

function mouseDistanceToCenter() {
  return int(dist(width / 2, height / 2, mouseX, mouseY));
}

function drawCircle(circle) {
  fill(color(circle.fillColor));
  stroke(color(circle.color));
  strokeWeight(1);
  ellipse(circle.x, circle.y, circle.radius, circle.radius);
}