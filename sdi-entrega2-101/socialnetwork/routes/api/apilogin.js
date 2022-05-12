const {ObjectId} = require("mongodb");
module.exports = function (app, usersRepository) {

    let logger = app.get("log4js")

    /**
     * Función que autentica a un usuario en la aplicación, comprueba si esta en la base de datos, y si es asi,
     * marca al usuario como autenticado.
     */
    app.post("/api/v1.0/users/login", function (req, res) {

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

                    logger.error("usuario no autorizado");
                    res.status(401);//Unauthorized
                    res.json({
                        message: "usuario no autorizado",
                        authenticated: false
                    })

                } else {
                    let token = app.get('jwt').sign(
                        {user: user.email, time: Date.now() / 1000},
                        "secreto");
                    logger.info("Usuario autenticado");
                    res.status(200);
                    res.json({
                        message: "usuario autorizado",
                        authenticated: true,
                        token: token
                    });

                }
            }).catch(error => {
                logger.error("Se ha producido un error al verificar credenciales");
                res.status(401);
                res.json({
                    message: "Se ha producido un error al verificar credenciales",
                    authenticated: false,
                })

            })
        } catch (e) {
            logger.error("Se ha producido un error en la petición.");
            res.status(500);
            res.json({
                message: "Se ha producido un error en la petición.",
                authenticated: false
            })
        }

    });
}