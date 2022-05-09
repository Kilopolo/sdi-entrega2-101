module.exports = function(app, publicationsRepository, amistadesRepository) {

    app.get("/publications", function (req, res) {

        let filter = {email: req.session.user.email };
        let options = {};

        publicationsRepository.findPublications(filter, options).then(publications => {
            res.render("publications/list.twig", {publications: publications});
        }).catch(() => {
            res.send("Error al listar las publicaciones");
        });

    });

    app.get('/publications/add', function (req, res) {
      res.render("publications/add.twig");
    });

    app.post('/publications/add', function (req, res) {
        let publication = {
            email: req.session.user.email,
            titulo: req.body.titulo,
            texto: req.body.texto,
            fechaCreacion: formattedDate()
        }
        publicationsRepository.insertPublication(publication).then((publicationId) => {
            res.redirect("/publications");
        }).catch(() => {
            res.send("Error al añadir la publicación");
        });
    });

    app.get("/publications/list/:email", function (req, res) {
        let filter = {$or: [{user1 : req.session.user.email, user2: req.params.email},
                {user1: req.params.email, user2: req.session.user.email}]};
        let options = {};
        amistadesRepository.findAmistad(filter, options).then( amistad => {
            filter = { email: req.params.email };
            console.log(req.params.email)
            publicationsRepository.findPublications(filter, options).then(publications => {
                res.render("publications/friendList.twig", {friend: req.params.email, publications: publications});
            }).catch(() => {
                res.send("Error al listar las publicaciones");
            });
        }).catch(() => {
            res.send("Error al obtener relación de amistad");
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