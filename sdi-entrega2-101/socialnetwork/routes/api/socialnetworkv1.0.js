const {ObjectId} = require("mongodb");
module.exports = function (app, usersRepository, amistadesRepository) {


    /**
     * Función que te redirige a la ventana de login por defecto
     */
    app.get('/', function (req, res) {
        //TODO index
        res.redirect("/apiclient/client.html");
    });


    app.get("/api/v1.0/friends/list", function (req, res) {

        let user = req.session.user
        console.log(user)

        let filter = {$or: [{user1: req.session.user}, {user2: req.session.user}]};
        let options = {};
        amistadesRepository.findAmistadesByEmail(filter, options).then(amistades => {
            getUserFromAmistades(req, amistades).then(p => {
                res.status(200);
                res.send({friendList: p})
            }).catch(error => {
                res.status(500);
                res.json({error: "Se ha producido un error al encontrar algún usuario de las amistades" + error});
            });
        }).catch(error => {
            res.status(500);
            res.json({error: "Se ha producido un error al recuperar las amistades."})
        });


    });

    function getEmailFromAmistad(req, amistad) {
        if (amistad.user1 === req.session.user) {
            return amistad.user2;
        }
        return amistad.user1;
    }


    async function getUserFromAmistades(req, amistades) {
        if (amistades == null || amistades.length == 0) {
            return [];
        } else {
            let usuarios = [];
            let userEmail = "";
            for (let i = 0; i < amistades.length; i++) {
                userEmail = getEmailFromAmistad(req, amistades[i]);
                await usersRepository.findUser({email: userEmail}, {}).then(async user => {
                    let a = await user;
                    usuarios.push(user);
                });
            }
            return usuarios;
        }
    };

    /**
     * Función que autentica a un usuario en la aplicacion, comprueba si esta en la base de datos, si es asi, marca al usuario como autenticado.
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

                    //logger.error("usuario no autorizado");
                    res.status(401);//Unauthorized
                    res.json({
                        message: "usuario no autorizado",
                        authenticated: false
                    })

                } else {
                    let token = app.get('jwt').sign(
                        {user: user.email, time: Date.now() / 1000},
                        "secreto");
                    //logger.info("Usuario autenticado");
                    res.status(200);
                    res.json({
                        message: "usuario autorizado",
                        authenticated: true,
                        token: token
                    });


                }
            }).catch(error => {
                //logger.error("Se ha producido un error al verificar credenciales");
                res.status(401);
                res.json({
                    message: "Se ha producido un error al verificar credenciales",
                    authenticated: false,
                })

            })
        } catch (e) {
            //logger.error("Se ha producido un error en la petición.");
            res.status(500);
            res.json({
                message: "Se ha producido un error en la petición.",
                authenticated: false
            })

        }


    });


};