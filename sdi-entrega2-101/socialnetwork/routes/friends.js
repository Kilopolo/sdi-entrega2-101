module.exports = function(app, amistadesRepository,usersRepository) {

    app.get("/friends", function (req, res) {
        let filter = {user1 : {email : req.session.user} };
        let options = {};
        usersRepository.findUser({email: "prueba1@email.com"},{}).then(user1 => {
            usersRepository.findUser({email: "Mike@email.com"},{}).then(user2 => {
                let amistad = {
                    user1: user1,
                    user2: user2
                }
                amistadesRepository.insertAmistad(amistad, {});
                amistadesRepository.findAmistadesByEmail(filter, options).then(amistades => {
                    //TODO ambos emails
                    res.render("friends/list.twig", {amistades: amistades});
                }).catch(error => {
                    res.send("Se ha producido un error al listar los amigos:" + error)
                });
            });
        });


    });
};