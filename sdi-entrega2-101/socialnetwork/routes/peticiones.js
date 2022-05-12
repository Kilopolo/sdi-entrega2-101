const usersRepository = require("../repositories/usersRepository");
//TODO arreglar
module.exports = function (app, peticionesRepository, usersRepository, amistadesRepository) {

    app.get("/peticiones", function (req, res) {
        let filter = {user2: req.session.user.email};
        let options = {};
        peticionesRepository.findPeticionesByEmail(filter, options).then(peticiones => {
            getUserFromPeticiones(peticiones).then(usersPeticiones => {
                res.render("peticiones/list.twig", {peticiones: usersPeticiones});
            }).catch(error => "Se ha producido un error al encontrar algun usuario de las peticiones" + error);
        }).catch(error => {
            res.send("Se ha producido un error al listar las peticiones:" + error)
        });
    });

    app.get("/peticiones/aceptar/:email", function (req, res) {
        let filter = {$and: [{user1: req.params.email}, {user2: req.session.user.email}]};
        let options = {};
        peticionesRepository.findPeticionesByEmail(filter, options).then(peticion => {
            let id = {"_id": peticion[0]._id};
            peticionesRepository.deletePeticion(id).then(borrado => {
                let amistad = {
                    user1: req.params.email,
                    user2: req.session.user.email
                }
                amistadesRepository.insertAmistad(amistad);
            });
            //creear amistad
            res.redirect("/peticiones");
        });
    });

    app.get("/peticiones/enviar/:email", function (req, res) {
        if( req.params.email != req.session.user.email){
        usersRepository.findUser({email: req.params.email}, {}).then(user => {
            if(user != null) {
            let peticion = {
                user1: req.session.user.email,
                user2: req.params.email
            };
            if (req.params.email != req.session.user.email) {
                peticionesRepository.findPeticionesByEmail(peticion, {}).then(peticionYaCreada => {
                    if (peticionYaCreada.length <= 0) {
                        let amistad = {
                            $or: [
                                {
                                    $and: [
                                        {user1: req.session.user.email},
                                        {user2: req.params.email}
                                    ]
                                },
                                {
                                    $and: [
                                        {user2: req.session.user.email},
                                        {user1: req.params.email}]
                                }]
                        }
                        amistadesRepository.findAmistadesByEmail(amistad, {}).then(amistadYaCreada => {
                            if (amistadYaCreada.length <= 0) {
                                let peticionInversa = {
                                    user2: req.session.user.email,
                                    user1: req.params.email
                                };
                                peticionesRepository.findPeticionesByEmail(peticionInversa, {}).then(peticionInversaCreada => {
                                    if (peticionInversaCreada.length <= 0) {
                                        peticionesRepository.insertPeticion(peticion, {}).then(peticion => {
                                            res.redirect("/users");
                                        });
                                    } else {
                                        amistadesRepository.insertAmistad({
                                            user1: req.session.user.email,
                                            user2: req.params.email
                                        }, {}).then(peticion => {
                                            peticionesRepository.deletePeticion({"_id": peticionInversaCreada[0]._id}).then(borrado=>{
                                                res.redirect("/users");
                                            });

                                        });
                                    }
                                })

                            } else {res.redirect("/users");}
                        })

                    }
                    else {res.redirect("/users");}
                }).catch()
            }}
            else {

            }

        });} else { res.redirect("/users");}
        })
};


async function getUserFromPeticiones(peticiones) {
    if (peticiones == null || peticiones.length == 0) {
        return [];
    } else {
        let usuarios = [];
        for (let i = 0; i < peticiones.length; i++) {
            await usersRepository.findUser({email: peticiones[i].user1}, {}).then(async user => {
                let a = await user;
                usuarios.push(user);
            });
        }
        return usuarios;
    }
};