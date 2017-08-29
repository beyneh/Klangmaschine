
// before running this you must do:
// npm install

var express = require('express');
var app = express();
var serveIndex = require('serve-index');
var serveStatic = require('serve-static');
var http = require('http').Server(app);
var io = require('socket.io')(http);

var socketNames = [];

app.use(serveStatic(__dirname));
app.use('/', serveIndex(__dirname));

http.listen(3000, function(){
  console.log('listening on *:3000');
});


var osc = require('node-osc');

// this will need to be changed to run on the correct 
var oscClient = new osc.Client("localhost", 32000);

var isConnected = false;

io.sockets.on('connection', function (socket) {

	console.log('connection');

	socket.on("config", function (obj)
	{
		isConnected = true;
		socket.emit("connected", 1);
	});

	socket.on("message", function (obj) {
		console.log(obj);
		oscClient.send.apply(oscClient, obj);
	});

	socket.on('disconnect', function(){
		console.log("disconnect")
	});
	
});
