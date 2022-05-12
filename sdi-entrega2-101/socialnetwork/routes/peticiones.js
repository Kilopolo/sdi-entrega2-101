const usersRepository = require("../repositories/usersRepository");

module.exports = function (app, peticionesRepository, usersRepository, amistadesRepository) {
    /**
     * Método get que devuelve la lista de peticiones
     */
    app.get("/peticiones", function (req, res) {
        let filter = {user2: req.session.user.email};
        let options = {};
        let page = parseInt(req.query.page); // Es String !!!
        if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0") { //
            page = 1;
        }
        peticionesRepository.getPeticionesPg(filter, options, page).then(result => {
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
            getUserFromPeticiones(result.peticiones).then(usersPeticiones => {
                let response = {
                    peticiones: usersPeticiones,
                    pages: pages,
                    currentPage: page
                }
                res.render("peticiones/list.twig", {response:response ,userInSession: req.session.user});
            }).catch(error => "Se ha producido un error al encontrar algun usuario de las amistades" + error);
        }).catch(error => {
            res.send("Se ha producido un error al listar los amigos:" + error)


        });
    });
    /**
     * Método get que acepta una petición
     */
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
    /**
     * Método get que envía una petición
     */
    app.get("/peticiones/enviar/:email", function (req, res) {
        if( req.params.email != req.session.user.email && req.params.email != "admin@email.com"){
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