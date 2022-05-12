package socialnetwork.db;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
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
//    private static String connectionString = "mongodb+srv://sdi2022101:Pa$$1234@sdi-node-101.axwk6.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
    //private static String connectionString = "mongodb+srv://sdi212210:Pa$$word123@cluster0.zytfv.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
    private static String connectionString = "mongodb+srv://sdi:Pa$$word123@cluster0.ipd4l.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
    private static String AppDBname = "socialNetwork";
    private static List<Document> users = new ArrayList<Document>();
    private static InitDB idb = new InitDB();

    public static void main(String[] args) {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);


        initDB();
//        deleteTestUsers();
        insertUser("admin@email.com", "admin", "admin", "ADMIN", "admin");
//        deleteTestAmistades();
        insertAmistades();

        deleteTestMessages();
        insertMessages();
        //idb.createUsers();
//        deleteTestConversaciones();
        createConversaciones();
        showDataOfDB();
    }


    private static void insertMessages() {
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


    private static void insertAmistades() {

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


    private static void initDB() {

        idb.createUsers();
        idb.insertUsers();
    }

    private void createUsers() {

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
            users.add(new Document("email", userEmail)
                    .append("password", securePassword)
                    .append("name", name)
                    .append("surname", surname)
                    .append("rol", rol)
                    .append("test", true));
        }

    }

    private static void insertUser(String userEmail, String name, String surname, String rol, String password) {

        String securePassword = AES.encrypt(password);
//            System.out.println(securePassword);
        Document user = new Document("email", userEmail)
                .append("password", securePassword)
                .append("name", name)
                .append("surname", surname)
                .append("rol", rol)
                .append("test", true);

        try (MongoClient mongoclient = MongoClients.create(connectionString)) {

            mongoclient.getDatabase(AppDBname).getCollection("users").insertOne(user);

        }

    }


    private static void insertAmistad(String user1, String user2) {

        //[7:15 p. m., 22/4/2022] Pablo: Se anyade Mensaje {emisor:User, destinatario:User, textoMensaje:String, leido:boolean}
        //[7:18 p. m., 22/4/2022] Pablo: A Amistad se le pone que tiene una conversacion:List<Mensaje>

//        List<Document> conversaciones = getDocumentsFrom("conversaciones");
//        List<Document> mensajes = getDocumentsFrom("messages");
//
//        String conversacionId = "";
//        for (Document conversacion :conversaciones) {
//
//        }

        Document user = new Document("user1", user1)
                .append("user2", user2)
//                .append("conversacionId",conversacionId)
                .append("test", true);

        try (MongoClient mongoclient = MongoClients.create(connectionString)) {
            mongoclient.getDatabase(AppDBname).getCollection("amistades").insertOne(user);

        }

//        Document conversacion = new Document();
//        try (MongoClient mongoclient = MongoClients.create(connectionString)) {
//            mongoclient.getDatabase(AppDBname).getCollection("conversaciones").insertOne(conversacion);
//
//            mongoclient.getDatabase(AppDBname).getCollection("conversaciones").find(new Document("":));
//        }


    }

    private static void insertMessage(String emailEmisor, String emailDestinatario, String textoMensaje, String fecha, boolean leido) {

        //[7:15 p. m., 22/4/2022] Pablo: Se anyade Mensaje {emisor:User, destinatario:User, textoMensaje:String, leido:boolean}
        //[7:18 p. m., 22/4/2022] Pablo: A Amistad se le pone que tiene una conversacion:List<Mensaje>
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

        try (MongoClient mongoclient = MongoClients.create(connectionString)) {
            mongoclient.getDatabase(AppDBname).getCollection("messages").insertOne(user);

        }

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


    private void insertUsers() {
        try (MongoClient mongoclient = MongoClients.create(connectionString)) {

            mongoclient.getDatabase(AppDBname).getCollection("users").insertMany(users);

        } catch (Exception e) {

        }
    }

    public static void showDataOfDB() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);

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

    private static void deleteTestConversaciones() {
        try (MongoClient mongoclient = MongoClients.create(connectionString)) {
            mongoclient.getDatabase(AppDBname).getCollection("conversaciones").deleteMany(new Document("test", true));
        }
    }

    public static void deleteTestAmistades(String user1, String user2) {
        try (MongoClient mongoclient = MongoClients.create(connectionString)) {
            mongoclient.getDatabase(AppDBname).getCollection("amistades").deleteOne(Filters.eq("user1","user"));
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
