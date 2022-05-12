module.exports = function (app, amistadesRepository, usersRepository) {

    let logger = app.get("log4js")

    /**
     * Método get para ver las amistades
     */
    app.get("/amistades", function (req, res) {
        logger.info("GET /amistades");
        let filter = {$or: [{user1: req.session.user.email}, {user2: req.session.user.email}]};
        let options = {};
        amistadesRepository.findAmistadesByEmail(filter, options).then(amistades => {
            getUserFromAmistades(req, amistades).then(p => {
                res.render("amistades/list.twig", {amistades: p});
            }).catch(error => {
                logger.error("Se ha producido un error al encontrar algún usuario de las amistades" + error);
                res.render("error.twig", {
                    mensaje: "Se ha producido un error al encontrar algún usuario de las amistades",
                    elError: error
                });
            });
        }).catch(error => {
            logger.error("Se ha producido un error al listar los amigos: " + error);
            res.render("error.twig", {
                mensaje: "Se ha producido un error al listar los amigos",
                elError: error
            });
        });
    });
    /**
     * Método que devuelve el email del usuario amigo al que está en sesión
     * @param req
     * @param amistad
     * @returns {*}
     */
    function getEmailFromAmistad(req, amistad) {
        if (amistad.user1 === req.session.user.email) {
            return amistad.user2;
        }
        return amistad.user1;
    }

    /**
     * Método que devuelve el usuario que es amigo del usuario del que está en sesión
     * @param req
     * @param amistades
     * @returns {Promise<*[]>}
     */
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
        };
    };