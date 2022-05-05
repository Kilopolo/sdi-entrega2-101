module.exports = function(app, publicationsRepository) {

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
      usersRepository.insertUser(user).then(() => {
        res.send("Publicación añadida " + publication);
        //res.redirect("/publications");
      }).catch(error => {
        res.send("Error al añadir la publicación");
      });
   });

   function formattedDate() {
      let fullDate = new Date();
      let day = ("0" + fullDate.getDate()).slice(-2);
      let month = ("0" + (fullDate.getMonth() + 1)).slice(-2);
      let year = fullDate.getFullYear();
      return year + "-" + month + "-" + date;
   }
}