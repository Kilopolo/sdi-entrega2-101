module.exports = function (app, publicationsRepository, amistadesRepository) {

    let logger = app.get("log4js")

    app.get("/publications", function (req, res) {
        logger.info("GET /publications");
        let filter = {email: req.session.user.email};
        let options = {};

        publicationsRepository.findPublications(filter, options).then(publications => {
            res.render("publications/list.twig", {publications: publications});
        }).catch((error) => {
            logger.error("Error al listar las publicaciones" + error);
            res.render("error.twig", {
                mensaje: "Error al listar las publicaciones",
                elError: error
            });
        });

    });

    app.get('/publications/add', function (req, res) {
        logger.info("GET /publications/add");
        res.render("publications/add.twig");
    });

    app.post('/publications/add', function (req, res) {
        logger.info("POST /publications/add");
        console.log(req.body.titulo + req.body.texto);
        if (req.body.titulo === "" || req.body.titulo === null
            || req.body.texto === "" || req.body.texto === null) {
            res.redirect("/publications/add" +
                "?message=Ni el título ni el contenido de la publicación pueden estar vacíos" +
                "&messageType=alert-danger ");
        }
        let publication = {
            email: req.session.user.email,
            titulo: req.body.titulo,
            texto: req.body.texto,
            fechaCreacion: formattedDate()
        }
        publicationsRepository.insertPublication(publication).then((publicationId) => {
            res.redirect("/publications");
        }).catch((error) => {
            logger.error("Error al añadir las publicaciones" + error);
            res.render("error.twig", {
                mensaje: "Error al añadir las publicaciones",
                elError: error
            });
        });
    });

    app.get("/publications/list/:email", function (req, res) {
        logger.info("GET /publications/list/:email");
        let filter = {
            $or: [{user1: req.session.user.email, user2: req.params.email},
                {user1: req.params.email, user2: req.session.user.email}]
        };
        let options = {};
        amistadesRepository.findAmistad(filter, options).then(amistad => {
            if (amistad !== null && amistad.length !== 0) {
                filter = {email: req.params.email};
                console.log(req.params.email)
                publicationsRepository.findPublications(filter, options).then(publications => {
                    res.render("publications/friendList.twig", {friend: req.params.email, publications: publications});
                }).catch((error) => {
                    logger.error("Error al listar las publicaciones" + error);
                    res.render("error.twig", {
                        mensaje: "Error al listar las publicaciones",
                        elError: error
                    });
                });
            } else {
                res.redirect("/amistades" +
                    "?message=No tienes permiso para acceder a estas publicaciones" +
                    "&messageType=alert-danger");
            }
        }).catch((error) => {
            logger.error("Error al listar las publicaciones" + error);
            res.render("error.twig", {
                mensaje: "Error al listar las publicaciones",
                elError: error
            });
        });
    });

    function formattedDate() {
        let fullDate = new Date();
        let day = ("0" + fullDate.getDate()).slice(-2);
        let month = ("0" + (fullDate.getMonth() + 1)).slice(-2);
        let year = fullDate.getFullYear();
        return year + "-" + month + "-" + day;
    }
}