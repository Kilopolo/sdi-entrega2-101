package socialnetwork.db;

import com.mongodb.client.*;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InitDB {


    private static String connectionString = "mongodb+srv://sdi2022101:Pa$$1234@sdi-node-101.axwk6.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
    private static String AppDBname = "socialNetwork";
    private static List<Document> users = new ArrayList<Document>();
    private static InitDB idb = new InitDB();

    public static void main(String[] args) {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);


//        initDB();
//        deleteTestUsers();
//        insertUser("admin@email.com", "admin", "admin", "ADMIN", "admin");
//        insertAmistades();
//        deleteTestMessages();
//        insertMessages();
        //idb.createUsers();

        showDataOfDB();
    }

    private static void insertMessages() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        now = now.minusHours(1);
        //conversacion de 00 con 01
        insertMessage("user00@email.com","user01@email.com","Lorem ipsum dolor sit amet...", dtf.format(now),false);
        now = now.minusHours(1);
        insertMessage("user01@email.com","user00@email.com","Lorem ipsum dolor sit amet...", dtf.format(now), false);
        now =  now.minusHours(1);
        insertMessage("user00@email.com","user01@email.com","Lorem ipsum dolor sit amet...", dtf.format(now), false);
        now =  now.minusHours(1);
        //conversacion de 02 con 01
        insertMessage("user02@email.com","user01@email.com","Lorem ipsum dolor sit amet...", dtf.format(now), false);
        now =  now.minusHours(1);
        insertMessage("user01@email.com","user02@email.com","Lorem ipsum dolor sit amet...", dtf.format(now), false);
        now =   now.minusHours(1);
        insertMessage("user02@email.com","user01@email.com","Lorem ipsum dolor sit amet...", dtf.format(now), false);
        now =  now.minusHours(1);
        insertMessage("user01@email.com","user02@email.com","Lorem ipsum dolor sit amet...", dtf.format(now), false);


    }



    private static void insertAmistades() {

        //00
        insertAmistad("user00@email.com","user01@email.com");
        insertAmistad("user00@email.com","user02@email.com");
        insertAmistad("user00@email.com","user03@email.com");
        //01
        insertAmistad("user01@email.com","user04@email.com");
        insertAmistad("user01@email.com","user05@email.com");
        insertAmistad("user01@email.com","user06@email.com");
        insertAmistad("user01@email.com","user07@email.com");
        //02
        insertAmistad("user02@email.com","user08@email.com");
        insertAmistad("user02@email.com","user09@email.com");
        insertAmistad("user02@email.com","user10@email.com");
        insertAmistad("user02@email.com","user11@email.com");
        insertAmistad("user02@email.com","user12@email.com");
        //03
        insertAmistad("user03@email.com","user13@email.com");
        insertAmistad("user03@email.com","user14@email.com");


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

        // List<Document> messages = getDocumentsFrom("messages");


        Document user = new Document("user1", user1)
                .append("user2", user2)
                .append("test", true);

        try (MongoClient mongoclient = MongoClients.create(connectionString)) {
            mongoclient.getDatabase(AppDBname).getCollection("amistades").insertOne(user);

        }

    }

    private static void insertMessage(String emailEmisor, String emailDestinatario, String textoMensaje, String fecha, boolean leido) {

        //[7:15 p. m., 22/4/2022] Pablo: Se anyade Mensaje {emisor:User, destinatario:User, textoMensaje:String, leido:boolean}
        //[7:18 p. m., 22/4/2022] Pablo: A Amistad se le pone que tiene una conversacion:List<Mensaje>

        // List<Document> messages = getDocumentsFrom("messages");


        Document user = new Document("emisor", emailEmisor)
                .append("destinatario", emailDestinatario)
                .append("textoMensaje", textoMensaje)
                .append("fecha",fecha)
                .append("leido", leido)
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
