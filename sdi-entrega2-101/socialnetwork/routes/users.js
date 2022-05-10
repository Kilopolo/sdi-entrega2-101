module.exports = function (app, usersRepository, amistadesRepository, peticionesRepository, logger) {

    app.get('/home', function (req, res) {
        let user = req.session.user
        if (user == undefined) {
            user.name = 'ANONIMO'
            user.rol = 'ANONIMO'
        }
        res.render("home.twig", {user: user});//

    });

    app.get('/users/signup', function (req, res) {
        res.render("users/signup.twig");
    });
    app.get('/users', function (req, res) {
        let filter = {};

        if (req.query.search != null && typeof (req.query.search) != "undefined" &&
            req.query.search != "") {
            filter = {
                "$or": [
                    {"name": {$regex: ".*" + req.query.search + ".*"}},
                    {"surname": {$regex: ".*" + req.query.search + ".*"}},
                    {"email": {$regex: ".*" + req.query.search + ".*"}}
                ]
            };
        }

        let page = parseInt(req.query.page);
        if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0") {
            page = 1;
        }
        usersRepository.getUsersPage(filter, {}, page, 4).then(result => {
            let lastPage = result.total / 4;
            if (result.total % 4 > 0) {
                lastPage++;
            }
            let pages = [];
            for (let i = page - 2; i <= page + 2; i++) {
                if (i > 0 && i <= lastPage) {
                    pages.push(i);
                }
            }
        })
    });

        function renderUserList(req, res, user) {
            let filter = {
                rol: 'USER',
                email: {$ne: req.session.user.email}
            };
            let options = {};
            usersRepository.findUsers(filter, options).then(users => {
                if (users == null) {
                    //algun error
                } else {
                    //TODO {user1 : req.session.user.email},{user2:1, _id:0} forma de hacerlo mejor?
                    amistadesRepository.findAmistadesByEmail({$or: [{user1: req.session.user.email}, {user2: req.session.user.email}]}, {}).then(amistades => {
                        peticionesRepository.findPeticionesByEmail({user1: req.session.user.email}, {}).then(peticiones => {
                            let emailsAmistades = getEmailFromList(amistades, req);
                            let emailsPeticiones = getEmailFromList(peticiones, req);
                            res.render("users/list.twig", {
                                users: users,
                                emailsAmistades: emailsAmistades,
                                emailsPeticiones,
                                emailsPeticiones,
                                user: user
                            });
                        }).catch(error => "Sucedió un error buscando las peticiones" + error);
                    }).catch(error => "Sucedió un error buscando las amistades" + error);


                } /*else {
                amistadesRepository.deleteAmistades(filter2,{}).then(amistades=>{
                    if(amistades==null){
                        res.redirect("/users" + "?message=Se ha producido un error al eliminar los amigos" + "&messageType=alert-danger");
                    }
                    else{
                        usersRepository.deleteUsers(filter,{}).then(users => {
                            res.redirect("/users");
                        }).catch(error => {
                            res.render("error.twig", {
                                mensaje : "Se ha producido un error al listar los usuarios del sistema",
                                elError : error
                            });
                        });
                    }
                }).catch(error => {
                    res.render("error.twig", {
                        mensaje : "Se ha producido un error al eliminar los amigos",
                        elError : error
                    });
                });
            }*/
            }).catch(error => {
                res.render("error.twig", {
                    mensaje: "Se ha producido un error al eliminar las invitaciones del sistema",
                    elError: error
                });
            });
        }

        app.post('/users/signup', function (req, res) {
            if (req.body.password != req.body.passwordConfirm) {
                res.redirect("/users/signup" +
                    "?message=La contraseña no se ha repetido correctamente" +
                    "&messageType=alert-danger ");
                // logger.error("El usuario que va a registrarse ha introducido mal su contraseña");
            } else {
                let securePassword = app.get("crypto")
                    .createHmac('sha256', app.get('clave'))
                    .update(req.body.password).digest('hex');
                let user = {
                    email: req.body.email,
                    password: securePassword,
                    name: req.body.name,
                    surname: req.body.surname,
                    rol: "USER"
                }

                let filter = {email: user.email}
                usersRepository.findUser(filter, {}).then(userFound => {
                    if (userFound == null) {
                        //si no hay ninguno se puede registrar
                        usersRepository.insertUser(user).then(userId => {
                            //res.send('Usuario registrado ' + userId);
                            res.render("home.twig", {user: user});
                        }).catch(err => {
                            res.send("Error al insertar el usuario");
                        });
                    } else {
                        //Si ya hay un email en la BDD lanzamos el error
                        res.redirect("/users/signup" +
                            "?message=Ya existe un usuario registrado con ese email" +
                            "&messageType=alert-danger ");
                        // logger.error("El usuario que va a registrarse ha introducido mal su contraseña");
                    }
                })


            }
        });

        app.get('/users/login', function (req, res) {
            res.render("users/login.twig");
        });

        app.post('/users/login', function (req, res) {
            let securePassword = app.get("crypto").createHmac('sha256', app.get('clave'))
                .update(req.body.password).digest('hex');
            let filter = {
                email: req.body.email,
                password: securePassword
            }
            let options = {};

            usersRepository.findUser(filter, {}).then(user => {


                if (user == null) {
                    req.session.user = null;
                    //res.send('Usuario no identificado');
                    res.redirect("/users/login" +
                        "?message=Email o password incorrecto" +
                        "&messageType=alert-danger ");


                } else {
                    req.session.user = user;
                    /*renderUserList(req,res,user);*/
                    res.redirect("/users");

                }


            }).catch(err => {
                req.session.user = null;
                res.redirect("/users/login" +
                    "?message=Se ha producido un error al buscar el usuario" +
                    "&messageType=alert-danger ");
            });
        });

        app.get('/users/logout', function (req, res) {
            req.session.user = null;
            res.render("index.twig");
        });

        /*function getEmailFromList(list,req) {
            let resultList = [];
            for (let i = 0; i< list.length; i++){
                if(list[i].user1 === req.session.user.email){
                    resultList.push(list[i].user2);
                } else {
                    resultList.push(list[i].user1);
                }
            }
            return resultList;
        }*/


}


