const {ObjectId} = require("mongodb");
module.exports = function (app) {

    let logger = app.get("log4js")

    /**
     * Función que te redirige a la ventana de login por defecto al acceder a la API
     */
    app.get('/apiclient', function (req, res) {
        res.redirect("/apiclient/client.html");
    });

};