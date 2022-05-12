const express = require('express');
const userSessionRouter = express.Router();
const log4jslet = require("log4js");
userSessionRouter.use(function(req, res, next) {
    // console.log("routerUsuarioSession");
    let logger = log4jslet.getLogger();
    if ( req.session.user ) {
// dejamos correr la petición
        next();
    } else {
        console.log("va a: " + req.originalUrl);
        res.redirect("/users/login");
        logger.warn("redirect routerUsuarioSession");
    }

});
module.exports = userSessionRouter;