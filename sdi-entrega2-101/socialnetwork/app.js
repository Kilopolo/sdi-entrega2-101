const {MongoClient} = require("mongodb");
const url = 'mongodb+srv://sdi2022101:Pa$$1234@sdi-node-101.axwk6.mongodb.net/myFirstDatabase?retryWrites=true&w=majority';

let createError = require('http-errors');
let express = require('express');
let path = require('path');
let cookieParser = require('cookie-parser');
let logger = require('morgan');



let indexRouter = require('./routes/index');
let usersRouter = require('./routes/users');
let publicationsRouter = require('./routes/publications');

let app = express();

let rest = require('request');
app.set('rest', rest);

let jwt = require('jsonwebtoken');
app.set('jwt', jwt);

app.use(function (req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Credentials", "true");
  res.header("Access-Control-Allow-Methods", "POST, GET, DELETE, UPDATE, PUT");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token");
// Debemos especificar todas las headers que se aceptan. Content-Type , token
  next();
});
var log4jslet = require("log4js");
var theAppLog = log4jslet.getLogger();
theAppLog.level = "debug";
app.set('log4js',theAppLog);
theAppLog.log("debug","LOGGER INICIALIZADO")


let expressSession = require('express-session');
app.use(expressSession({
  secret: 'abcdefg',
  resave: true,
  saveUninitialized: true
}));

let fileUpload = require('express-fileupload');
app.use(fileUpload({
  limits: {fileSize: 50 * 1024 * 1024},
  createParentPath: true
}));
app.set('uploadPath', __dirname)

//Ctrypto
let crypto = require('crypto');
app.set('clave', 'abcdefg');
app.set('crypto', crypto);

//Mongo
app.set('connectionStrings', url);

//BodyParser
let bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

//Logger
// let log4js = require("log4js");
// let logger = log4js.getLogger();
// logger.level = "debug";




const usersRepository = require("./repositories/usersRepository.js");
//Amistades.js
const amistadesRepository = require("./repositories/amistadesRepository.js");
amistadesRepository.init(app, MongoClient);
require("./routes/friends.js")(app, amistadesRepository,usersRepository);
//Peticiones.js
const peticionesRepository = require("./repositories/peticionesRepository.js");
peticionesRepository.init(app, MongoClient);
require("./routes/peticiones.js")(app, peticionesRepository, usersRepository, amistadesRepository);
//Users.js
usersRepository.init(app, MongoClient);
require("./routes/users.js")(app, usersRepository,amistadesRepository,peticionesRepository);

//Publications.js
const publicationsRepository = require("./repositories/publicationsRepository.js");
publicationsRepository.init(app, MongoClient);
require("./routes/publications.js")(app, publicationsRepository, amistadesRepository);

//Messages.js
const messagesRepository = require("./repositories/messagesRepository.js");
messagesRepository.init(app, MongoClient);



//Seguridad
const userSessionRouter = require('./routes/userSessionRouter');
app.use("/friends", userSessionRouter);
app.use("/publications/add", userSessionRouter);
app.use("/publications", userSessionRouter);

//JQuery Client
const userTokenRouter = require('./routes/userTokenRouter');

require("./routes/api/socialnetworkv1.0.js")(app,usersRepository,amistadesRepository,messagesRepository);
app.use("/api/v1.0/friends/list", userTokenRouter);
app.use("/api/v1.0/messages", userTokenRouter);

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'twig');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/publications', publicationsRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
