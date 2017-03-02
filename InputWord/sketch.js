var socket;

function setup() {
  createCanvas(1000, 1000);
  // make a magic internet portal!
  // leave this line
  socket = io('http://Sbhklr-MacBook-Pro.local:3000');

  // this is how we know we're connected
  socket.on('connect', function() {
    print(" I connected!! ");
  });
  setupUI()
}

function sendOsc(address, value) {
  socket.emit('message', [address].concat(value));
}

var wordInput;

function setupUI() {
  wordInput = createInput();
  wordInput.position(10,10)
  wordInput.size(300,30)
  wordInput.input(onKeyPressed);
  
}

function onKeyPressed() {
  console.log(wordInput.value());
  var currentValue = wordInput.value();
  if (currentValue.endsWith(" ")) {
    wordInput.value("");
    sendOsc("words", currentValue);
  }
}