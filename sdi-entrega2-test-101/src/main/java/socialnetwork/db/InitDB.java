package socialnetwork.db;

import com.mongodb.client.*;
import org.bson.Document;

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
        initDB();
        showDataOfDB();
        // deleteTestUsers();
        //idb.createUsers();
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
