const {ObjectId} = require("mongodb");
module.exports = function (app, usersRepository, amistadesRepository, messageRepository) {


    /**
     * Función que te redirige a la ventana de login por defecto
     */
    app.get('/', function (req, res) {
        //TODO index
        res.redirect("/apiclient/client.html");
    });

    app.get("/api/v1.0/messages/:id", function (req, res) {
        let user = res.user;
        // let list = []
        // list.push({email:"aaaa",text:"aaaaaaaaaaaaaa"})
        // list.push({email:"bbbb",text:"bbbbbbbbbbbbbb"})
        let mssgId = ObjectId(req.params.id)
        let filter = {amistadId: mssgId};
        let options = {};
        messageRepository.findMessages(filter, options).then(messages => {


            res.status(200);
            res.json({
                message: "Lista de amistades con ultimo mensaje de la conversación.",
                chatList: messages,
                userLoggedIn: user
            });
        }).catch(e => {
            res.status(500);
            res.json({error: "Se ha producido un error al recuperar los mensajes."})
        });


    });


    app.get("/api/v1.0/friends/list", function (req, res) {

        let user = res.user;
        console.log(user);
        let userJoinMssg = [];

        amistadesRepository.findAmistades({$or: [{user1: user}, {user2: user}]}, {}).then(friendList => {
            usersRepository.findUsers({}, {}).then(userList => {
                messageRepository.findMessages({}, {}).then(messageList => {
                    let userWithLastMssg = {
                        friendshipId: "",
                        userEmail: "",
                        userName: "",
                        userSurname: "",
                        mssgDate: "",
                        mssgText: ""
                    };
                    // console.log(friendList)
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
                        let flID = flID = (friendList[i]._id.toString());
                        if (flID != conversacionID) {
                            for (let j = 0; j < messageList.length; j++) {
                                conversacionID = flID;
                                if (flID == messageList[j].amistadId) {
                                    console.log(flID + "   " + j + "   " + messageList[j].amistadId)

                                    conversacion.push(messageList[j]);
                                }
                            }
                        }
                        conversaciones.push(conversacion);
                    }

                    for (let i = 0; i < friendListObjects.length; i++) {
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
                                let lastMessage;
                                let fecha = new Date(null);
                                //por cada conversacion
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
                        //pusheamos con o sin conversacion
                        userJoinMssg.push(userWithLastMssg);
                    }


                    res.status(200);
                    res.json({
                        message: "Lista de amistades con ultimo mensaje de la conversación.",
                        users: userJoinMssg
                    });

                }).catch(e => {
                    res.status(500);
                    res.json({error: "Se ha producido un error al recuperar los mensajes."})
                });
            }).catch(e => {
                res.status(500);
                res.json({error: "Se ha producido un error al encontrar algún usuario"});
            });
        }).catch(e => {
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