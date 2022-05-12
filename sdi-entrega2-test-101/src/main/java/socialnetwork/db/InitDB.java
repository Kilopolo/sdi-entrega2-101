package socialnetwork.db;

import com.mongodb.client.*;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InitDB {

    //OLD
    private static String connectionString = "mongodb+srv://sdi2022101:Pa$$1234@sdi-node-101.axwk6.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
    //v2
//    private static String connectionString = "mongodb+srv://sdi212210:Pa$$word123@cluster0.zytfv.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
    //v3
    //


    private static String AppDBname = "socialNetwork";

    public static void main(String[] args) {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);

        try (MongoClient mongoclient = MongoClients.create(connectionString)) {
            initDB(mongoclient);

        }

    }

    private static void initDB(MongoClient mongoclient) {

        InitDB idb = new InitDB();

        DeleteAllTestDB(mongoclient);
        insertUser("admin@email.com", "admin", "admin", "ADMIN", "admin");
        idb.insertUsers(mongoclient, getUsers());
        idb.insertMessages(mongoclient, getMessages());
        idb.insertAmistades(mongoclient, getAmistades());
        //CONVERSACIONES NO ESTA BIEN
        //Faltaría publicaciones
        showDataOfDB();

    }

    private void insertUsers(MongoClient mongoclient, List<Document> users) {
        mongoclient.getDatabase(AppDBname).getCollection("users").insertMany(users);
    }

    private void insertMessages(MongoClient mongoclient, List<Document> mensajes) {
        mongoclient.getDatabase(AppDBname).getCollection("messages").insertMany(mensajes);
    }

    private static void insertAmistades(MongoClient mongoclient, List<Document> amistades) {
        mongoclient.getDatabase(AppDBname).getCollection("amistades").insertMany(amistades);
    }

    private static List<Document> getMessages() {
        List<Document> mensajes = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        now = now.minusHours(1);
        //conversacion de 00 con 01
        mensajes.add(getMessage("user00@email.com", "user01@email.com", "Lorem ipsum dolor sit amet...", dtf.format(now), false));
        now = now.minusHours(1);
        mensajes.add(getMessage("user01@email.com", "user00@email.com", "Lorem ipsum dolor sit amet...", dtf.format(now), false));
        now = now.minusHours(1);
        mensajes.add(getMessage("user00@email.com", "user01@email.com", "Lorem ipsum dolor sit amet...", dtf.format(now), false));
        now = now.minusHours(1);
        //conversacion de 02 con 01
        mensajes.add(getMessage("user02@email.com", "user01@email.com", "Lorem ipsum dolor sit amet...", dtf.format(now), false));
        now = now.minusHours(1);
        mensajes.add(getMessage("user01@email.com", "user02@email.com", "Lorem ipsum dolor sit amet...", dtf.format(now), false));
        now = now.minusHours(1);
        mensajes.add(getMessage("user02@email.com", "user01@email.com", "Lorem ipsum dolor sit amet...", dtf.format(now), false));
        now = now.minusHours(1);
        mensajes.add(getMessage("user01@email.com", "user02@email.com", "Lorem ipsum dolor sit amet...", dtf.format(now), false));

        return mensajes;
    }

    private static void insertMessagesOneByOne() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        now = now.minusHours(1);
        //conversacion de 00 con 01
        insertMessage("user00@email.com", "user01@email.com", "Lorem ipsum dolor sit amet...", dtf.format(now), false);
        now = now.minusHours(1);
        insertMessage("user01@email.com", "user00@email.com", "Lorem ipsum dolor sit amet...", dtf.format(now), false);
        now = now.minusHours(1);
        insertMessage("user00@email.com", "user01@email.com", "Lorem ipsum dolor sit amet...", dtf.format(now), false);
        now = now.minusHours(1);
        //conversacion de 02 con 01
        insertMessage("user02@email.com", "user01@email.com", "Lorem ipsum dolor sit amet...", dtf.format(now), false);
        now = now.minusHours(1);
        insertMessage("user01@email.com", "user02@email.com", "Lorem ipsum dolor sit amet...", dtf.format(now), false);
        now = now.minusHours(1);
        insertMessage("user02@email.com", "user01@email.com", "Lorem ipsum dolor sit amet...", dtf.format(now), false);
        now = now.minusHours(1);
        insertMessage("user01@email.com", "user02@email.com", "Lorem ipsum dolor sit amet...", dtf.format(now), false);


    }

    private static void createConversaciones() {
//Document{{_id=6278692a0601be384e20313f,
// emisor=user00@email.com, destinatario=user01@email.com,
// textoMensaje=Lorem ipsum dolor sit amet...,
// fecha=2022/05/09 02:06:49, leido=false, test=true}}

        List<Document> mensajes = getDocumentsFrom("messages");

        Map<String, Document> conversaciones = new HashMap<>();
        List<Document> amistades = getDocumentsFrom("amistades");
        for (Document amistad : amistades) {
            String messageId = "";
            Document conversacion = new Document();
            for (Document mensaje : mensajes) {

//                System.out.println(mensaje.getString("emisor") +
//                        " + " + amistad.getString("destinatario") +
//                        " + " + mensaje.getString("user1") +
//                        " + " + amistad.getString("user2"));
//


                if (mensaje.getString("emisor").equals(amistad.getString("user1")) &&
                        mensaje.getString("destinatario").equals(amistad.getString("user2")) ||
                        mensaje.getString("emisor").equals(amistad.getString("user2")) &&
                                mensaje.getString("destinatario").equals(amistad.getString("user1"))) {
                    messageId = mensaje.get("_id").toString();
                    conversacion.append("messageId" + messageId, messageId);

                }
            }
            conversaciones.put(amistad.get("_id").toString(), conversacion);
        }


        try (MongoClient mongoclient = MongoClients.create(connectionString)) {
//            for (Map.Entry<String,Document> entry : conversaciones.entrySet()) {
//                String a = entry.getKey();
//                Document c = entry.getValue();
//
//                mongoclient.getDatabase(AppDBname).getCollection("amistades")
//                        .updateOne(new Document("_id", a), Updates.push("conversacionId", c.get("_id").toString()));
//                mongoclient.getDatabase(AppDBname).getCollection("conversaciones").insertOne(c);
//
//            }
//            System.out.println(conversaciones);
        }
    }


    private static List<Document> getAmistades() {


        List<Document> amistades = new ArrayList<Document>();


        //00
        amistades.add(getAmistad("user00@email.com", "user01@email.com"));
        amistades.add(getAmistad("user00@email.com", "user02@email.com"));
        amistades.add(getAmistad("user00@email.com", "user03@email.com"));
        //01
        amistades.add(getAmistad("user01@email.com", "user02@email.com"));
        amistades.add(getAmistad("user01@email.com", "user04@email.com"));
        amistades.add(getAmistad("user01@email.com", "user05@email.com"));
        amistades.add(getAmistad("user01@email.com", "user06@email.com"));
        amistades.add(getAmistad("user01@email.com", "user07@email.com"));
        //02
        amistades.add(getAmistad("user02@email.com", "user08@email.com"));
        amistades.add(getAmistad("user02@email.com", "user09@email.com"));
        amistades.add(getAmistad("user02@email.com", "user10@email.com"));
        amistades.add(getAmistad("user02@email.com", "user11@email.com"));
        amistades.add(getAmistad("user02@email.com", "user12@email.com"));
        //03
        amistades.add(getAmistad("user03@email.com", "user13@email.com"));
        amistades.add(getAmistad("user03@email.com", "user14@email.com"));

        return amistades;

    }

    private static void insertAmistadesOneByOne() {


        //00
        insertAmistad("user00@email.com", "user01@email.com");
        insertAmistad("user00@email.com", "user02@email.com");
        insertAmistad("user00@email.com", "user03@email.com");
        //01
        insertAmistad("user01@email.com", "user02@email.com");
        insertAmistad("user01@email.com", "user04@email.com");
        insertAmistad("user01@email.com", "user05@email.com");
        insertAmistad("user01@email.com", "user06@email.com");
        insertAmistad("user01@email.com", "user07@email.com");
        //02
        insertAmistad("user02@email.com", "user08@email.com");
        insertAmistad("user02@email.com", "user09@email.com");
        insertAmistad("user02@email.com", "user10@email.com");
        insertAmistad("user02@email.com", "user11@email.com");
        insertAmistad("user02@email.com", "user12@email.com");
        //03
        insertAmistad("user03@email.com", "user13@email.com");
        insertAmistad("user03@email.com", "user14@email.com");


    }


    private static List<Document> getUsers() {
        List<Document> users = new ArrayList<Document>();
        for (int i = 0; i < 15; i++) {
            //user01@email.com, user01
            String numero = i < 10 ? "0" + String.valueOf(i) : String.valueOf(i);
            String userEmail = "user" + numero + "@email.com";
            String name = "nameUser" + numero;
            String surname = "lastNameUser" + numero;
            String rol = "USER";
            String password = "user" + numero;
            String securePassword = AES.encrypt(password);
//            System.out.println(securePassword);
            users.add(getUser(userEmail, name, surname, rol, password));
        }
        return users;
    }


    private static void insertUser(MongoClient mongoclient, String userEmail, String name, String surname, String rol, String password) {
        Document user = getUser(userEmail, name, surname, rol, password);
        mongoclient.getDatabase(AppDBname).getCollection("users").insertOne(user);
    }

    private static void insertUser(String userEmail, String name, String surname, String rol, String password) {
        Document user = getUser(userEmail, name, surname, rol, password);
        try (MongoClient mongoclient = MongoClients.create(connectionString)) {
            mongoclient.getDatabase(AppDBname).getCollection("users").insertOne(user);
        }
    }

    private static Document getUser(String userEmail, String name, String surname, String rol, String password) {
        String securePassword = AES.encrypt(password);
        Document user = new Document("email", userEmail)
                .append("password", securePassword)
                .append("name", name)
                .append("surname", surname)
                .append("rol", rol)
                .append("test", true);
        return user;
    }

    private static Document getAmistad(String user1, String user2) {

        Document amistad = new Document("user1", user1)
                .append("user2", user2)
                .append("test", true);
        return amistad;

    }

    private static void insertAmistad(MongoClient mongoclient, String user1, String user2) {

        Document user = getAmistad(user1, user2);
        mongoclient.getDatabase(AppDBname).getCollection("amistades").insertOne(user);

    }

    private static void insertAmistad(String user1, String user2) {


        Document user = getAmistad(user1, user2);
        try (MongoClient mongoclient = MongoClients.create(connectionString)) {
            mongoclient.getDatabase(AppDBname).getCollection("amistades").insertOne(user);

        }


    }

    private static Document getMessage(String emailEmisor, String emailDestinatario, String textoMensaje, String fecha, boolean leido) {
        Document mssg = createMessage(emailEmisor, emailDestinatario, textoMensaje, fecha, leido);
        return mssg;
    }

    private static void insertMessage(MongoClient mongoclient, String emailEmisor, String emailDestinatario, String textoMensaje, String fecha, boolean leido) {
        Document user = createMessage(emailEmisor, emailDestinatario, textoMensaje, fecha, leido);
        mongoclient.getDatabase(AppDBname).getCollection("messages").insertOne(user);
    }


    private static void insertMessage(String emailEmisor, String emailDestinatario, String textoMensaje, String fecha, boolean leido) {
        Document user = createMessage(emailEmisor, emailDestinatario, textoMensaje, fecha, leido);
        try (MongoClient mongoclient = MongoClients.create(connectionString)) {
            mongoclient.getDatabase(AppDBname).getCollection("messages").insertOne(user);
        }
    }

    private static Document createMessage(String emailEmisor, String emailDestinatario, String textoMensaje, String fecha, boolean leido) {

        String amistadId = "";
        List<Document> amistades = getDocumentsFrom("amistades");
        for (Document amistad : amistades) {
            if (emailEmisor.equals(amistad.getString("user1")) &&
                    emailDestinatario.equals(amistad.getString("user2")) ||
                    emailEmisor.equals(amistad.getString("user2")) &&
                            emailDestinatario.equals(amistad.getString("user1"))) {
                amistadId = amistad.get("_id").toString();
            }
        }

        Document user = new Document("emisor", emailEmisor)
                .append("destinatario", emailDestinatario)
                .append("textoMensaje", textoMensaje)
                .append("fecha", fecha)
                .append("leido", leido)
                .append("amistadId", amistadId)
                .append("test", true);
        return user;

    }


    private static List<Document> getDocumentsFrom(String collection) {
        List<Document> tmp = new ArrayList<Document>();
        try (MongoClient mongoclient = MongoClients.create(connectionString)) {

            FindIterable<Document> it = mongoclient.getDatabase(AppDBname).getCollection(collection).find();
            MongoCursor<Document> cursor = it.cursor();
            while (cursor.hasNext()) {
                Document item = cursor.next();
                tmp.add(item);
            }
        }
        return tmp;
    }


//    private void insertUsers() {
//        try (MongoClient mongoclient = MongoClients.create(connectionString)) {
//            mongoclient.getDatabase(AppDBname).getCollection("users").insertMany(users);
//        }
//    }
//
//    private void insertUsers(MongoClient mongoclient) {
//        mongoclient.getDatabase(AppDBname).getCollection("users").insertMany(users);
//    }

    public static void showDataOfDB() {

        try (MongoClient mongoclient = MongoClients.create(connectionString)) {
            List<String> databases = mongoclient.listDatabaseNames().into(new ArrayList<>());
            System.out.println("databases" + databases);

            List<String> collectionNames = mongoclient.getDatabase(AppDBname).listCollectionNames()
                    .into(new ArrayList<>());
            System.out.println("collectionNames" + collectionNames);
            MongoIterable<String> it = mongoclient.getDatabase(AppDBname).listCollectionNames();
            MongoCursor<String> cursor = it.cursor();
            while (cursor.hasNext()) {
                String item = cursor.next();
                System.out.println(item);
                FindIterable<Document> it2 = mongoclient.getDatabase(AppDBname).getCollection(item).find();
                MongoCursor<Document> cursor2 = it2.cursor();
                while (cursor2.hasNext()) {
                    Document item2 = cursor2.next();
                    System.out.println(item2);
                }
            }

        } catch (Exception e) {
        }
    }

    private static void DeleteAllTestDB(MongoClient mongoclient) {
        MongoIterable<String> it = mongoclient.getDatabase(AppDBname).listCollectionNames();
        MongoCursor<String> cursor = it.cursor();
        while (cursor.hasNext()) {
            String item = cursor.next();
            mongoclient.getDatabase(AppDBname).getCollection(item).deleteMany(new Document("test", true));

        }
    }

    private static void deleteTestFromCollection(MongoClient mongoclient, String collection) {
        mongoclient.getDatabase(AppDBname).getCollection(collection).deleteMany(new Document("test", true));
    }

    private static void deleteTestConversaciones() {
        try (MongoClient mongoclient = MongoClients.create(connectionString)) {
            mongoclient.getDatabase(AppDBname).getCollection("conversaciones").deleteMany(new Document("test", true));
        }
    }

    private static void deleteTestAmistades() {
        try (MongoClient mongoclient = MongoClients.create(connectionString)) {
            mongoclient.getDatabase(AppDBname).getCollection("amistades").deleteMany(new Document("test", true));
        }
    }

    public static void deleteTestMessages() {
        try (MongoClient mongoclient = MongoClients.create(connectionString)) {
            mongoclient.getDatabase(AppDBname).getCollection("messages").deleteMany(new Document("test", true));
        }
    }

    public static void deleteTestUsers() {
        try (MongoClient mongoclient = MongoClients.create(connectionString)) {
            mongoclient.getDatabase(AppDBname).getCollection("users").deleteMany(new Document("test", true));
        }
    }

    public void deleteUserByEmail(String email) {
        try (MongoClient mongoclient = MongoClients.create(connectionString)) {
            mongoclient.getDatabase(AppDBname).getCollection("users").deleteMany(new Document("email", email));
        }
    }
}
