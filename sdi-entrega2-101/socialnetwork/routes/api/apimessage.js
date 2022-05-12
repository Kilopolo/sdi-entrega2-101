const {ObjectId} = require("mongodb");
module.exports = function (app, messageRepository) {

    let logger = app.get("log4js")

    /**
     * Función que agrega un nuevo mensaje con la información proporcionada por un usuario en su conversación
     * con otro. Se incluirá, además del texto del mensaje, la fecha de envío y la información de ambos emisor
     * y destinatario.
     */
    app.post("/api/v1.0/messages/agregar/:id", function (req, res) {
        try {
            let emisor = res.user;
            console.log(emisor);
            let date = new Date();
            let fechaString = date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear() + " "
                + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();

            let mssg = req.body.mensaje
            let destinatario = mssg.emisor == req.body.emisor ? mssg.destinatario : mssg.emisor;
            console.log(destinatario)


            let mensajeNuevo = {
                // emisor: emisor,
                // destinatario: req.params.destinatario,
                emisor: req.body.emisor,
                destinatario: destinatario,
                textoMensaje: req.body.contenido,
                fecha: fechaString,
                leido: false,
                amistadId: req.params.id
            };
            console.log(mensajeNuevo)

            if (mensajeNuevo.emisor == mensajeNuevo.destinatario) throw err;

            messageRepository.insertMessage(mensajeNuevo, {}).then(response => {
                logger.info("Mensaje insertado satisfactoriamente en la conversación.")
                res.status(200);
                res.json({
                    message: "Mensaje insertado satisfactoriamente en la conversación.",
                });
            }).catch(err => {
                logger.error("Se ha producido un error al insertar el mensaje: " + err)
                res.status(500);
                res.json({error: "Se ha producido un error al insertar el mensaje."})
            });

        } catch (err) {
            logger.error("El emisor y el destinatario no pueden ser el mismo usuario. ")
            res.status(500);
            res.json({error: "El emisor y el destinatario no pueden ser el mismo usuario."})
        }
    });

    /**
     * Función que recupera el listado de mensajes entre dos usuarios, ordenados por fecha.
     */
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
}
