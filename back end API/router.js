
//user's actions
var chgpass = require('./node_modules/users/chgpass');
var register = require('./node_modules/users/register');
var login = require('./node_modules/users/login');
var saveLocation = require('./node_modules/Locations/saveLocation');

module.exports = function(app) {
  //**************************************************************************************
  //Add new user in MongoDB - register
  //**************************************************************************************
  app.post('/users/register',function(req, res){

    var name = req.body.name;
    var surname = req.body.surname;
    var email = req.body.email;
    var userName = req.body.userName;
    var passWord = req.body.passWord;

    register.register(name, surname , email, userName, passWord, function (callback){
        console.log(callback);
        res.json(callback);
    });
  });
  //**************************************************************************************
  //login - sign in
  //**************************************************************************************
  app.post('/users/login',function(req,res){
    var passWord = req.body.passWord;
    var email = req.body.email;

    login.login(email,passWord, function(callback){
      console.log(callback);
      res.json(callback);
    });
  });
  //******************************************************************************************
  //reset password and send e-mail (the user has forgotten hes/his password)
  //******************************************************************************************
  app.post('/users/resetpass',function(req,res){
    var userName = req.body.userName;
    var email = req.body.email;

    chgpass.resetpass(userName, email,function(callback){
      console.log(callback);
      res.json(callback);
    });
  });

  //************************************************************************************************
  //change the password and send e-mail to the user
  //************************************************************************************************
  app.post('/users/changepass', function(req,res){
    var userName = req.body.userName;
    var email = req.body.email;
    var oldPassword = req.body.oldPassword;
    var newPassword = req.body.newPassword;

    chgpass.changePassword(userName,email,oldPassword,newPassword,function(callback){
      console.log(callback);
      res.json(callback);
    });
  });
  //***************************************************************************************************
  //Save longidude and latitude
  //***************************************************************************************************
  app.post('/gpc/saveCoord',function(req,res){
    var token = req.body.token;
    var longitude = req.body.longitude;
    var latitude = req.body.latitude;

    saveLocation.saveCoordinations(token,latitude,longitude,function(callback){
      console.log(callback);
      res.json(callback);
    });
  });

  //******************************************************************************************************
  // Get longidude and latitude
  //****************************************************************************************************
  app.post('/gpc/getCoord',function(req,res){
    var token = req.body.token;
    saveLocation.getCoordinations(token,function(callback){
      console.log(callback);
      res.json(callback);
    });
  });

  //testing
  app.get('/',function(req,res){
	 res.send('Connected with OCEANOS, in the port 3000');
  });

  //******************************************************************************************************
  // Get longitude and latitude with date
  //****************************************************************************************************
  app.post('/gpc/getCoordDate',function(req,res){
    var token = req.body.token;
    var date = req.body.date;
    saveLocation.getCoordinationsDates(token,date,function(callback){
      console.log(callback);
      res.json(callback);
    });
  });
}
