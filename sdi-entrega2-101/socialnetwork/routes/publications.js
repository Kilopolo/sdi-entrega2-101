module.exports = function(app, publicationsRepository) {

    app.get("/publications", function (req, res) {

        let filter = {user : {email : req.session.user} };
        let options = {};

        publicationsRepository.findPublications(filter, options).then(publications => {
            res.render("publications/list.twig", {publications: publications});
        }).catch(error => {
            res.send("Error al insertar el usuario");
        });

    });
};