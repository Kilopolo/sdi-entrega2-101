const jwt = require("jsonwebtoken");
const express = require('express');
const log4jslet = require("log4js");
const userTokenRouter = express.Router();
userTokenRouter.use(function (req, res, next) {

    let logger = log4jslet.getLogger();
    logger.level = "debug";
    console.log("userTokenRouter");
    let token = req.headers['token'] || req.body.token || req.query.token;
    if (token != null) {
        // verificar el token
        jwt.verify(token, 'secreto', {}, function (err, infoToken) {

            let timeElapsed = Date.now() / 1000 - infoToken.time;
            let timeOfSession = 1240;
            let timeLeftOfSession = (timeOfSession - timeElapsed)
            console.log(timeLeftOfSession);
            // if (err || (timeLeftOfSession >= 0)) {
                if (err || (Date.now() / 1000 - infoToken.time) > 1240) {
                logger.error("Token inválido o caducado: Token Value=" + token+", Time Left Of Session="+timeLeftOfSession)

                res.status(403); // Forbidden
                res.json({
                    authorized: false,
                    error: 'Token inválido o caducado'
                });

            } else {
                logger.info("Usuario " + infoToken.user + " autenticado, tiempo restante de sesion: " +  timeLeftOfSession + " seconds." )
                // dejamos correr la petición
                res.user = infoToken.user;
                next();
            }
        });
    } else {
        // logger.error("No hay Token")
        res.status(403); // Forbidden
        res.json({
            authorized: false,
            error: 'No hay Token'
        });
    }
});
module.exports = userTokenRouter;