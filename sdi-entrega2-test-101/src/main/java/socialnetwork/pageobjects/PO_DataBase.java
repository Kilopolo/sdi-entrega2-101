package socialnetwork.pageobjects;

import socialnetwork.db.*;

public class PO_DataBase {
    private static InitDB idb = new InitDB();
    public static void deleteUserByEmail(String email){
        idb.deleteUserByEmail(email);
    }
}
