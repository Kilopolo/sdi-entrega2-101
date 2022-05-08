const {ObjectId} = require("mongodb");
module.exports = function (app, usersRepository, logger) {

    app.post("/api/socialnetworkv1.0/users/list", function (req, res) {
        //TODO

    });



    /**
     * Función que autentica a un usuario en la aplicacion, comprueba si esta en la base de datos, si es asi, marca al usuario como autenticado.
     */
    app.post("/api/socialnetworkv1.0/users/login", function (req, res) {

        try {
            let securePassword = app.get("crypto").createHmac('sha256', app.get('clave'))
                .update(req.body.password).digest('hex');
            let filter = {
                email: req.body.email,
                password: securePassword
            }
            let options = {}

            usersRepository.findUser(filter, options).then(user => {
                if (user == null) {
                    res.status(401);//Unauthorized
                    res.json({
                        message: "usuario no autorizado",
                        authenticated: false
                    })
                    logger.error("usuario no autorizado");
                } else {
                    let token = app.get('jwt').sign(
                        {user: user.email, time: Date.now() / 1000},
                        "secreto");
                    res.status(200);
                    res.json({
                        message: "usuario autorizado",
                        authenticated: true,
                        token: token
                    });
                    logger.info("Usuario autenticado");

                }
            }).catch(error => {
                res.status(401);
                res.json({
                    message: "Se ha producido un error al verificar credenciales",
                    authenticated: false,
                })
                logger.error("Se ha producido un error al verificar credenciales");
            })
        } catch (e) {
            res.status(500);
            res.json({
                message: "Se ha producido un error en la petición.",
                authenticated: false
            })
            logger.error("Se ha producido un error en la petición.");
        }


    });


};