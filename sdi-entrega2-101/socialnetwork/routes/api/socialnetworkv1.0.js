const {ObjectId} = require("mongodb");
module.exports = function (app, usersRepository, amistadesRepository, messageRepository) {


    /**
     * Función que te redirige a la ventana de login por defecto
     */
    app.get('/', function (req, res) {
        //TODO index
        res.redirect("/apiclient/client.html");
    });

    app.get("/api/v1.0/friends/list", function (req, res) {

        let user = res.user;
        console.log(user);
        let userJoinMssg = [];

        amistadesRepository.findAmistades({$or: [{user1: user}, {user2: user}]}, {}).then(friendList => {
            usersRepository.findUsers({}, {}).then(userList => {
                messageRepository.findMessages({}, {}).then(messageList => {
                    let userWithLastMssg = {
                        userEmail: "",
                        userName: "",
                        userSurname: "",
                        mssgDate: "",
                        mssgText: ""
                    };

                    let friendListObjects = [];
                    for (let i = 0; i < friendList.length; i++) {
                        let friendUserEmail = friendList[i].user1 === user ? friendList[i].user2 : friendList[i].user1;
                        for (let j = 0; j < userList.length; j++) {
                            let userMongo = userList[j];
                            let friend = (friendUserEmail == userMongo.email) ? userMongo : null;
                            if (friend!=null)friendListObjects.push(friend);
                        }
                    }

                    for (let i = 0; i < friendListObjects.length; i++) {
                        let lastMessage;
                        let fecha = new Date(null);
                        //recorremos los mensajes
                        for (let j = 0; j < messageList.length; j++) {
                            let friend = friendListObjects[i];
                            let message = messageList[j];
                            //si el emisor o el destinatario son el amigo
                            if (message.emisor == friend.email ||
                                message.destinatario == friend.email) {
                                //ponemos el usuario
                                userWithLastMssg.userEmail = friend.email;
                                userWithLastMssg.userSurname = friend.surname;
                                userWithLastMssg.userName = friend.name;
                                //falta fecha, hora y texto del último mensaje

                                //cojemos el ultimo mensaje cronologicament
                                let fechaParsed = parseDateFromMssg(message.fecha);
                                if (fecha.getTime() < fechaParsed.getTime()) {
                                    lastMessage = message;
                                    fecha = fechaParsed;
                                }
                                if (lastMessage != undefined) {
                                    // actualizamos el usuario que vamos a enviar
                                    userWithLastMssg.mssgDate = lastMessage.fecha;
                                    userWithLastMssg.mssgText = lastMessage.textoMensaje;
                                }
                                userJoinMssg.push(userWithLastMssg);
                            }

                        }
                    }

                    console.log(userJoinMssg);
                    res.status(200);
                    res.json({
                        message: "Lista de amistades con ultimo mensaje de la conversación.",
                        users: userJoinMssg
                    });

                }).catch(e=>{
                    res.status(500);
                    res.json({error: "Se ha producido un error al recuperar los mensajes."})
                });
            }).catch(e=>{
                res.status(500);
                res.json({error: "Se ha producido un error al encontrar algún usuario"});
            });
        }).catch(e=>{
            res.status(500);
            res.json({error: "Se ha producido un error al recuperar las amistades."})
        });


    });



    function parseDateFromMssg(fechaStr) {
        let a = fechaStr.split(" ");
        let dia = a[0];
        let hora = a[1];
        let b = dia.split("/");
        let c = hora.split(":");

        let diaMMDDYYYY = parseInt(b[1]) + "/" + parseInt(b[2]) + "/" + parseInt(b[0]);

        let date = new Date(diaMMDDYYYY);

        date.setHours(parseInt(c[0]));
        date.setMinutes(parseInt(c[1]));
        date.setSeconds(parseInt(c[2]));


        // date = new Date();
        //  console.log(date);

        return date;
    }


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