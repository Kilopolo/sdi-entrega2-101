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


        let filter = {$or: [{user1: user}, {user2: user}]};
        let options = {};
        amistadesRepository.findAmistades(filter, options).then(friendList => {
            //dame la lista de amigos modificada
            getFriendListWithLastMssg(user, friendList).then(x => {
                res.status(200);
                res.json({message: "Lista de amistades con ultimo mensaje de la conversación.",
                            users:x});
            }).catch(err => {
                res.status(500);
                res.json({error: "Se ha producido un error al encontrar algún usuario de las amistades" + error});
            });


        }).catch(err => {
            res.status(500);
            res.json({error: "Se ha producido un error al recuperar las amistades."})
        })




    });

    /**
     * Devuelve la lista de amigos en join con el último mensaje de la conversación.
     * @param user usuario que realiza la petición
     * @param friendList lista de amigos del usuario
     * @returns {Promise<void>} lista de amistades joinUserLastmessage
     */
     async function getFriendListWithLastMssg(user, friendList) {

        if (friendList == null || friendList.length == 0) {
            //No se han encontrado amigos
        } else {
            //Si tiene amigos se buscan los usuarios correspondientes

            let userEmail = "";
            for (let i = 0; i < friendList.length; i++) {
                //seleccionamos el amigo
                userEmail = friendList[i].user1 === user ? friendList[i].user2 : friendList[i].user1;
                let filter = {email: userEmail};

                usersRepository.findUser(filter, {}).then(async u => {
                    //espero por el usuario porque la programacion funcional no mola
                    let friendOfUser = await u;
                    let users = [];
                    getMessageFromUser(user, friendOfUser).then(async x => {

                        let joinUserMensaje = await x;
                        users.push(joinUserMensaje);

                    });

                    return users;

                });
            }
        }
    }

    /**
     * Función asíncrona que devuelve un join entre usuario y último mensaje de una conversación
     * @param user usuario que hace la petición
     * @param friendOfUser amigo del usuario
     * @returns {Promise<void>} async joinUserMensaje
     */
    async function getMessageFromUser(user, friendOfUser) {
        let userWithLastMssg = {
            userEmail: "",
            userName: "",
            userSurname: "",
            mssgDate: "",
            mssgText: ""
        };
        //ponemos el usuario
        userWithLastMssg.userEmail = friendOfUser.email;
        userWithLastMssg.userSurname = friendOfUser.surname;
        userWithLastMssg.userName = friendOfUser.name;
        //falta fecha, hora y texto del último mensaje



        //todos los mensajes en los que el emisor o el destinatario sea el usuario
        let filter = {$or: [{emisor: user}, {destinatario: user}]};
        messageRepository.findMessages(filter, {}).then(messages => {
            if (messages == null || messages.length == 0) {
                //No se han encontrado messages
                return userWithLastMssg;
            } else {
                let lastMessage;
                let fecha = 0;
                let fechaStr;
                //recorremos los mensajes
                for (let i = 0; i < messages.length; i++) {
                    //si el emisor o el destinatario son el amigo
                    if (messages[i].emisor == friendOfUser.email ||
                        messages[i].destinatario == friendOfUser.email) {
                        //cojemos el ultimo mensaje cronologicamente
                        fechaStr = messages[i].fecha;
                        if(fecha > Date.parse( fechaStr)){
                            lastMessage = messages[i];
                            fecha = Date.parse( fechaStr);
                        }
                    }
                }
                // actualizamos el usuario que vamos a enviar
                userWithLastMssg.mssgDate = lastMessage.fecha;
                userWithLastMssg.mssgText = lastMessage.textoMensaje;
            }
            //haya o no mensajes hay que devolver la lista de amistades
            users.push(userWithLastMssg);


        });

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