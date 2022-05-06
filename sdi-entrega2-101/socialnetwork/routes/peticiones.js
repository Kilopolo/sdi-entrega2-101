const usersRepository = require("../repositories/usersRepository");
//TODO arreglar
module.exports = function(app, peticionesRepository, usersRepository) {

    app.get("/peticiones", function (req, res) {
        let filter = {user2 : req.session.user};
        let options = {};
        peticionesRepository.findPeticionesByEmail(filter, options).then(peticiones => {
            getUserFromPeticiones(peticiones).then(usersPeticiones => {
                res.render("peticiones/list.twig", {peticiones:usersPeticiones});
            }).catch(error=> "Se ha producido un error al encontrar algun usuario de las peticiones" + error);
        }).catch(error => {
            res.send("Se ha producido un error al listar las peticiones:" + error)
        });
    });

    app.get("/peticiones/aceptar/:email", function (req, res) {
        let filter = {$and: [{user1 : req.params.email}, {user2: req.session.user}]};
        let options = {};
        peticionesRepository.findPeticionesByEmail(filter, options).then(peticion => {
            let id = {"_id": peticion[0]._id};
            peticionesRepository.deletePeticion(id);
            //creear amistad
            res.redirect("/peticiones");
        });
    });
};


async function getUserFromPeticiones(peticiones) {
    if (peticiones == null || peticiones.length == 0) {
        return [];
    } else {
        let usuarios = [];
        for (let i = 0; i < peticiones.length; i++) {
            await usersRepository.findUser({email: peticiones[i].user1},{}).then(async user => {
                let a = await user;
                usuarios.push(user);
            });
        }
        return usuarios;
    }
};