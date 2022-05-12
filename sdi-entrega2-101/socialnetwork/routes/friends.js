module.exports = function (app, amistadesRepository, usersRepository) {

    let logger = app.get("log4js")

    /**
     * Método get para ver las amistades
     */
    app.get("/amistades", function (req, res) {
        logger.info("GET /amistades");
        let filter = {$or: [{user1: req.session.user.email}, {user2: req.session.user.email}]};
        let options = {};
        ///
        let page = parseInt(req.query.page); // Es String !!!
        if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0") { //
            page = 1;
        }
        amistadesRepository.getAmistadesPg(filter, options, page).then(result => {
            let lastPage = result.total / 4;
            if (result.total % 4 > 0) { // Sobran decimales
                lastPage = lastPage + 1;
            }
            let pages = []; // paginas mostrar
            for (let i = page - 2; i <= page + 2; i++) {
                if (i > 0 && i <= lastPage) {
                    pages.push(i);
                }
            }
            getUserFromAmistades(req, result.amistades).then(p => {
                let response = {
                    amistades: p,
                    pages: pages,
                    currentPage: page
                }
                res.render("amistades/list.twig", {response:response, userInSession: req.session.user});
            }).catch(error => "Se ha producido un error al encontrar algun usuario de las amistades" + error);
        }).catch(error => {
            res.send("Se ha producido un error al listar los amigos:" + error)
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
            return usuarios;
        }
    }
}