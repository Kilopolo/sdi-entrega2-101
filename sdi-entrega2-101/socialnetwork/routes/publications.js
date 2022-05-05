module.exports = function(app, publicationsRepository) {

    app.get("/publications", function (req, res) {

        let filter = {user : {email : req.session.user} };
        let options = {};

        publicationsRepository.findPublications(filter, options).then(publications => {
            res.render("publications/list.twig", {publications: publications});
        }).catch(() => {
            res.send("Error al insertar el usuario");
        });

    });

    app.get('/publications/add', function (req, res) {
      res.render("publications/add.twig");
    });

    app.post('/publications/add', function (req, res) {
      let publication = {
        email: req.session.user,
        name: req.body.titulo,
        surname: req.body.texto,
        fechaCreacion: formattedDate()
      }
        publicationsRepository.insertPublication(publication).then((publicationId) => {
        res.send("Publicación añadida " + publicationId);
        //res.redirect("/publications");
      }).catch(() => {
        res.send("Error al añadir la publicación");
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