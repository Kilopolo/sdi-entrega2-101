var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  console.log(req.session.user)
  // res.redirect('/');
  res.render('index.twig');
});



module.exports = router;
