const {ObjectId} = require("mongodb");
module.exports = function (app, usersRepository, amistadesRepository, messageRepository) {

    let logger = app.get("log4js")
    /**
     * Función que te redirige a la ventana de login por defecto
     */
    app.get('/apiclient', function (req, res) {
        //TODO index
        res.redirect("/apiclient/client.html");
    });

    app.post("/api/v1.0/messages/agregar/:id/:destinatario", function (req, res) {
        let emisor = res.user;
        //console.log(user);
        let date = new Date();
        let fechaString = date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear() + " Hora: " + date.getHours() + ":" + date.getMinutes();

        let mensajeNuevo = {
            emisor: emisor,
            destinatario: req.params.destinatario,
            textoMensaje: req.body.contenido,
            fecha: fechaString,
            leido: false,
            amistadId: req.params.id
        };

        //console.log(mensajeNuevo)

        messageRepository.insertMessage(mensajeNuevo, {}).then(response => {
            logger.info("Mensaje insertado satisfactoriamente en la conversación.");
            res.status(200);
            res.json({
                message: "Mensaje insertado satisfactoriamente en la conversación.",
            });

        }).catch(err => {
            logger.error("Se ha producido un error al insertar el mensaje.")
            res.status(500);
            res.json({error: "Se ha producido un error al insertar el mensaje."})
        });


    });

    app.get("/api/v1.0/messages/:id", function (req, res) {
        let user = res.user;
        // console.log(user);

        let filter = {amistadId: req.params.id};
        let options = {};
        messageRepository.findMessages(filter, options).then(messages => {


            let mensajes = []
            for (let i = 0; i < messages.length; i++) {

            }
            logger.info("Lista de mensajes de la conversación.");
            res.status(200);
            res.json({
                message: "Lista de mensajes de la conversación.",
                chatList: messages,
                userLoggedIn: user
            });
        }).catch(e => {
            logger.error("Se ha producido un error al recuperar los mensajes.");
            res.status(500);
            res.json({error: "Se ha producido un error al recuperar los mensajes."})
        });


    });


    app.get("/api/v1.0/friends/list", function (req, res) {


        let user = res.user;
        // console.log(user);


        amistadesRepository.findAmistades({$or: [{user1: user}, {user2: user}]}, {}).then(friendList => {
            usersRepository.findUsers({}, {}).then(userList => {
                messageRepository.findMessages({}, {}).then(messageList => {

                        let userJoinMssg = [];
                        try {

                            let conversaciones = [];
                            let friendListObjects = [];
                            for (let i = 0; i < friendList.length; i++) {
                                let friendUserEmail = friendList[i].user1 === user ? friendList[i].user2 : friendList[i].user1;
                                for (let j = 0; j < userList.length; j++) {
                                    let userMongo = userList[j];
                                    let friend = (friendUserEmail == userMongo.email) ? userMongo : null;
                                    if (friend != null) friendListObjects.push(friend);
                                }
                                let conversacion = []
                                let conversacionID = "";
                                let flID = (friendList[i]._id.toString());
                                if (flID != conversacionID) {
                                    for (let j = 0; j < messageList.length; j++) {
                                        conversacionID = flID;
                                        if (flID == messageList[j].amistadId) {
                                            // console.log(flID + "   " + j + "   " + messageList[j].amistadId)
                                            conversacion.push(messageList[j]);
                                        }
                                    }
                                }
                                conversaciones.push(conversacion);
                            }


                            //ya tenga o no algo la conversacion hay que mostrar las amistades
                            for (let i = 0; i < friendListObjects.length; i++) {
                                let userWithLastMssg = {
                                    friendshipId: "",
                                    userEmail: "",
                                    userName: "",
                                    userSurname: "",
                                    mssgDate: "",
                                    mssgText: ""
                                };
                                let friend = friendListObjects[i];
                                //ponemos el usuario
                                userWithLastMssg.userEmail = friend.email;
                                userWithLastMssg.userSurname = friend.surname;
                                userWithLastMssg.userName = friend.name;


                                //comprobamos las conversaciones
                                for (let j = 0; j < conversaciones.length; j++) {
                                    let conversacion = conversaciones[j];
                                    if (conversacion.length == 0) {
                                        //conversacion vacia
                                    } else {

                                        //si la conversacion se corresponde a la amistad
                                        if (conversacion[0].emisor == friend.email && conversacion[0].destinatario == user ||
                                            conversacion[0].emisor == user && conversacion[0].destinatario == friend.email) {

                                            let lastMessage;
                                            let fecha = new Date(null);
                                            //por cada conversacion recorro los mensajes
                                            for (let k = 0; k < conversacion.length; k++) {
                                                let mensaje = conversacion[k];
                                                //cogemos el último mensaje cronológicamente
                                                let fechaParsed = parseDateFromMssg(mensaje.fecha);
                                                if (fecha.getTime() < fechaParsed.getTime()) {
                                                    lastMessage = mensaje;
                                                    fecha = fechaParsed;
                                                }
                                                if (lastMessage != undefined) {
                                                    // actualizamos el usuario que vamos a enviar
                                                    userWithLastMssg.friendshipId = lastMessage.amistadId;
                                                    userWithLastMssg.mssgDate = lastMessage.fecha;
                                                    userWithLastMssg.mssgText = lastMessage.textoMensaje;
                                                }


                                            }
                                        }
                                    }
                                }

                                // console.log(userWithLastMssg.friendshipId)
                                //pusheamos con o sin conversacion
                                userJoinMssg.push(userWithLastMssg);
                            }
                            // console.log(userJoinMssg)


                        } catch
                            (e) {
                            console.log(e);
                        }
                    logger.info("Lista de amistades con ultimo mensaje de la conversación.");
                        res.status(200);
                        res.json({
                            message: "Lista de amistades con ultimo mensaje de la conversación.",
                            users: userJoinMssg
                        });

                    }
                ).catch(e => {
                    logger.error("Se ha producido un error al recuperar los mensajes.");
                    res.status(500);
                    res.json({error: "Se ha producido un error al recuperar los mensajes."})
                });
            }).catch(e => {
                logger.error("Se ha producido un error al encontrar algún usuario");
                res.status(500);
                res.json({error: "Se ha producido un error al encontrar algún usuario"});
            });
        }).catch(e => {
            logger.error("Se ha producido un error al recuperar las amistades.");
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
        // console.log(date);

        return date;
    }


    /**
     * Función que autentica a un usuario en la aplicación, comprueba si esta en la base de datos, si es asi, marca al usuario como autenticado.
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
                    // console.log(Date.now() / 1000)
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

    // /**
    //  * Función que permite crear un mensaje aun usuario autenticado
    //  */
    // app.post('/api/v1.0/message/add', function (req, res) {
    //     try {
    //         let song = {
    //             emisor: req.body.emisor,
    //             destinatario: req.body.destinatario,
    //             textoMensaje: req.body.texto,
    //             leido: false
    //         }
    //         messageRepository.insertOne(message, function (insertedId) {
    //             if (insertedId === null) {
    //                 res.status(409);
    //                 res.json({error: "No se ha podido crear el mensaje"});
    //             } else {
    //                 res.status(201);
    //                 res.json({
    //                     message: "Mensaje añadido correctamente",
    //                     _id: insertedId
    //                 })
    //             }
    //         });
    //     } catch (err) {
    //         res.status(500);
    //         res.json({error: "Se ha producido un error al intentar añadir el mensaje: " + err})
    //     }
    // });

};