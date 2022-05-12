var express = require('express');
var router = express.Router();
const log4jslet = require("log4js");

/* GET home page. */
router.get('/', function(req, res, next) {
  let logger = log4jslet.getLogger();
  logger.info("GET /amistades");
  // console.log(req.session.user)
  // res.redirect('/');
  res.render('index.twig',{userInSession: req.session.user});
});



module.exports = router;
