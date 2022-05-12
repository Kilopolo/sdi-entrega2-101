const {ObjectId} = require("mongodb");
module.exports = function (app, usersRepository, amistadesRepository, messageRepository) {

    let logger = app.get("log4js")

    /**
     * Función que recupera la lista de amistades del usuario autenticado, con el último mensaje entre ellos.
     * En caso de no estar autenticado, se reenviará al login.
     */
    app.get("/api/v1.0/friends/list", function (req, res) {


        let user = res.user;
        console.log(user);


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

    /**
     * Método auxiliar que parsea la fecha del mensaje a partir del string almacenado
     * @param fechaStr La fecha en formato String
     * @returns {Date} La fecha real a recuperar
     */
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
}