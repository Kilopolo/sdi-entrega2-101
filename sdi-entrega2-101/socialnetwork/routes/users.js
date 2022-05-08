module.exports = function (app, usersRepository) {

  app.get('/home',function (req,res) {
    // let user = req.session.user
    // if (user == undefined){
    //   user.name = 'ANONIMO'
    //   user.rol = 'ANONIMO'
    // }
    res.render("home.twig");//,{user:user}
    // res.send('home');
  });

  app.get('/users', function (req, res) {
    res.send('lista de usuarios');
  });

  app.get('/users/signup', function (req, res) {
    res.render("signup.twig");
  });

  app.post('/users/signup', function (req, res) {
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
    usersRepository.insertUser(user).then(userId => {
      //res.send('Usuario registrado ' + userId);
      res.redirect("/home");

    }).catch(error => {
      res.send("Error al insertar el usuario");
    });
  });

  app.get('/users/login', function (req, res) {
    res.render("login.twig");
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
            "?message=Email o password incorrecto"+
            "&messageType=alert-danger ");



      } else {
        req.session.user = user.email;
        res.redirect("/home");

      }

    }).catch(error => {
      req.session.user = null;
      res.redirect("/users/login" +
          "?message=Se ha producido un error al buscar el usuario"+
          "&messageType=alert-danger ");
    });
  });

  app.get('/users/logout', function (req, res) {
    req.session.user = null;
    res.send("El usuario se ha desconectado correctamente");
  })

}