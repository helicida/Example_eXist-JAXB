package Dao_Dao;

/**
 * Created by 46465442z on 19/04/16.
 */
public class DAO_dionis {

    public static void main(String[] args) {
        DAO_Diego dd = new DAO_Diego("172.31.101.225","8080","admin","dionis");

        dd.openConnection();

        dd.createCollection("dionisGuay");

        dd.closeConnection();

    }
}
