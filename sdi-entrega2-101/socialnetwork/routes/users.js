module.exports = function (app, usersRepository, logger) {

    app.get('/home', function (req, res) {
        let user = req.session.user
        if (user == undefined) {
            user.name = 'ANONIMO'
            user.rol = 'ANONIMO'
        }
        res.render("home.twig", {user: user});//

    });


    app.get('/users', function (req, res) {
        let user = req.session.user;
        renderUserList(req,res,user);

    });

    function renderUserList(req,res,user) {
        let filter = {
            rol: 'USER',
            email:{$ne:req.session.user.email}
        };
        let options = {};
        usersRepository.findUsers(filter, options).then(users => {
            if (users == null) {
                //algun error
            } else {
                res.render("users/list.twig", {users: users, user: user});//
            }
        });
    }

    app.get('/users/signup', function (req, res) {
        res.render("users/signup.twig");
    });

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
            let options = {}
            usersRepository.findUser(filter, options).then(userFound => {
                if (userFound == null) {
                    //si no hay ninguno se puede registrar
                    usersRepository.insertUser(user).then(userId => {
                        //res.send('Usuario registrado ' + userId);
                        res.render("home.twig", {user: user});

                    }).catch(error => {
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

        usersRepository.findUser(filter, options).then(user => {


            if (user == null) {
                req.session.user = null;
                //res.send('Usuario no identificado');
                res.redirect("/users/login" +
                    "?message=Email o password incorrecto" +
                    "&messageType=alert-danger ");


            } else {
                req.session.user = user;
                renderUserList(req,res,user);

            }


        }).catch(error => {
            req.session.user = null;
            res.redirect("/users/login" +
                "?message=Se ha producido un error al buscar el usuario" +
                "&messageType=alert-danger ");
        });
    });

    app.get('/users/logout', function (req, res) {
        req.session.user = null;
        res.render("index.twig");
    })

}