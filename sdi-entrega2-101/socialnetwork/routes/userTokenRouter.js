const jwt = require("jsonwebtoken");
const express = require('express');
const log4jslet = require("log4js");

const userTokenRouter = express.Router();
userTokenRouter.use(function (req, res, next) {

    let logger = log4jslet.getLogger();
    logger.level = "debug";
    let token = req.headers['token'] || req.body.token || req.query.token;
    if (token != null) {
        // verificar el token
        jwt.verify(token, 'secreto', {}, function (err, infoToken) {
            let timeElapsed = Date.now() / 1000 - infoToken.time;
            let timeOfSession = 1240;
            let timeLeftOfSession = (timeOfSession - timeElapsed)
            if (err || (timeLeftOfSession) > 0) {
                logger.error("Token inválido o caducado")
                res.status(403); // Forbidden
                res.json({
                    authorized: false,
                    error: 'Token inválido o caducado'
                });

            } else {
                // dejamos correr la petición
                logger.info("Usuario " + infoToken.user + " autenticado, tiempo restante de sesion: " +  timeLeftOfSession )
                res.user = infoToken.user;
                next();
            }
        });
    } else {
        logger.error("No hay Token")
        res.status(403); // Forbidden
        res.json({
            authorized: false,
            error: 'No hay Token'
        });
    }
});
module.exports = userTokenRouter;