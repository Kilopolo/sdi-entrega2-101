module.exports = function(app, amistadesRepository,usersRepository) {

    app.get("/friends", function (req, res) {
        let filter = {$or: [{user1 : req.session.user}, {user2: req.session.user}]};
        let options = {};
        amistadesRepository.findAmistadesByEmail(filter, options).then(amistades => {
            getUserFromAmistades(req,amistades).then(p => {
                res.render("friends/list.twig", {amistades:p});
            }).catch(error=> "Se ha producido un error al encontrar algun usuario de las amistades" + error);
           // }).catch(error => "Se ha producido un error al encontrar algun usuario de las amistades" + error);
        }).catch(error => {
            res.send("Se ha producido un error al listar los amigos:" + error)
        });
    });

    function getEmailFromAmistad(req, amistad) {
            if(amistad.user1 === req.session.user){
                return amistad.user2;
            }
            return  amistad.user1;
        }


    async function getUserFromAmistades(req,amistades) {
        if (amistades == null || amistades.length == 0) {
            return [];
        } else {
            let usuarios = [];
            let userEmail = "";
            for (let i = 0; i < amistades.length; i++) {
                userEmail = getEmailFromAmistad(req,amistades[i]);
                await usersRepository.findUser({email: userEmail},{}).then(async user => {
                    let a = await user;
                    usuarios.push(user);
                });
            }
            return usuarios;
        }
    };
};