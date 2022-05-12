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

};