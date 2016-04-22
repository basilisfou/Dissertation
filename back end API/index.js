var express = require('express'); // using express framework
var mongoose = require('mongoose');
var app = express(); //getting an instance of the express framework
var connect = require('connect');
var express = require('express'); // using express framework
var app = express(); //getting an instance of the express framework
var bodyParser = require("body-parser"); // using body - parser for parsing
//using modules
app.use(bodyParser.json()); // parsing json
app.use(bodyParser.urlencoded({"extended" : false})); //This will send all of our data to the Node server as query strings

var router = require('./router')(app); // instance of the file router
//log modules
app.use(connect.logger('dev'));
app.use(connect.json());
app.use(connect.urlencoded());
//connect to the Database
mongoose.connect('mongodb://localhost:27017',function(err){
  if(err){
    console.log('connection error',err);
  }else{
    console.log('connection to the database successful');
  }
});

//assign the port in the Server - Starting the Server
var server = app.listen(3000, function() {
  console.log("listening to port 3000");
});
