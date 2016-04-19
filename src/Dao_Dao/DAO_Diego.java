package Dao_Dao;

import net.xqj.exist.ExistXQDataSource;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import javax.xml.xquery.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by damuser on 29/03/16.
 * @author
 */
public class DAO_Diego {

    private String PORT = "8080";
    private String host = "localhost";
    private String USER = "admin";
    private String PASSWORD = "admin";

    private String URI = "xmldb:exist://"+ host +":"+ PORT +"/exist/xmlrpc/db/";
    private static final String DRIVER = "org.exist.xmldb.DatabaseImpl";
    private XQConnection connection;
    private XQDataSource xqds;

    //variables para el gestor de eventos (LOG)
    private StringBuilder logOutput;
    private DateFormat dateFormat;

    /**
     * Constructor para hacer consultas y "administrar las colecciones de ExistDB"
     * @param host {@link String}
     * @param port {@link String}
     * @param user {@link String}
     * @param password {@link String}
     */
    public DAO_Diego(String host, String port, String user, String password){

        this.host = host;
        this.URI = "xmldb:exist://" + host + ":"+ PORT +"/exist/xmlrpc/db/";
        this.PORT = port;
        this.USER = user;
        this.PASSWORD = password;
        xqds = new ExistXQDataSource();
        logOutput = new StringBuilder();
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    }
    /**
     * Constructor solo para hacer consultas a ExistDB
     * @param host {@link String}
     * @param port {@link String}
     */
    public DAO_Diego(String host, String port){
        host = host;
        PORT = port;
        xqds = new ExistXQDataSource();
        logOutput = new StringBuilder();
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    }
    /**
     * Constructor vacío, para hacer consultas y administrar las bases de datos
     * se necesitan setear las propiedades (host,PORT,USER,PASSWORD)
     */
    public DAO_Diego(){
        logOutput = new StringBuilder();
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    }
    /**
     * Crea una colección dentro del directorio raíz donde se encuentran las bases de datos.
     * Necesitamos pasarle como parámetro el nombre de la colección
     * @param collectionName {@link String}
     * */
    public void createCollection(String collectionName){
        logOutput.delete(0,logOutput.length());
        try {
            Database db = (Database) Class.forName(DRIVER).newInstance();
            db.setProperty("create-database", "true");

            DatabaseManager.registerDatabase(db);
            Collection parentCollection = DatabaseManager.getCollection(URI, USER, PASSWORD);
            logOutput.append(dateFormat.format(new Date()) + "\tObteniendo colección del servidor: host=" + host
                    + " user=" + USER + " pass=" + PASSWORD + "\n");

            Collection child = parentCollection.getChildCollection(collectionName);//comprobando si la colección no está creada
            if (child == null){
                CollectionManagementService colManagement = (CollectionManagementService) parentCollection.getService("CollectionManagementService", "1.0");
                colManagement.createCollection(collectionName);//Creando colección
                logOutput.append(dateFormat.format(new Date()) + "\tColección creada: " + collectionName + "\n");//guardando en el LOG
                System.out.println("\nColección creada.\n");
            }else{
                logOutput.append(dateFormat.format(new Date()) + "\tNo se ha podido crear la colección: " + collectionName + " porque ya existe.\n");
                System.out.println("La colección ya existe, no se ha podido crear");
            }

        } catch (XMLDBException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        writeLog();//guardando tracking
    }

    /**
     * Elimina una colección verificando primero si esta existe
     * @param collectionName {@link String}
     */
    public void deleteCollection(String collectionName){
        logOutput.delete(0,logOutput.length());
        try {
            Database db = (Database) Class.forName(DRIVER).newInstance();
            db.setProperty("remove-database", "true");

            DatabaseManager.registerDatabase(db);
            System.out.println("URI: " + URI);
            Collection parentCollection = DatabaseManager.getCollection(URI, USER, PASSWORD);

            logOutput.append(dateFormat.format(new Date()) + "\tObteniendo colección del servidor: host=" + host + " user=" + USER
                    + " pass=" + PASSWORD + "\n");
            logOutput.append(dateFormat.format(new Date()) + "\tBorrando Colección: " + collectionName + "\n");

            if (parentCollection.getChildCollection(collectionName) != null){
                CollectionManagementService colManagement = (CollectionManagementService) parentCollection.getService("CollectionManagementService", "1.0");
                colManagement.removeCollection(collectionName);
                logOutput.append(dateFormat.format(new Date()) + "\tColección eliminada: " + collectionName + "\n");//guardando en el LOG
            }else{
                logOutput.append(dateFormat.format(new Date()) + "\tNo se ha podido eliminar la colección: " + collectionName + " porque no existe.\n");
                System.out.println("La colección ya existe, no se ha podido eliminar");
            }


        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (XMLDBException e) {
            e.printStackTrace();
        }

        writeLog();
        System.out.println("\nColección eliminada.\n");

    }

    /**
     * Agrega una base de datos XML dentro de una colección,
     * ambos parámetros son obligatorios.
     * @param resourceName {@link String}
     * @param collectionName {@link String}
     */
    public void addResource(String resourceName, String collectionName){
        logOutput.delete(0,logOutput.length());
        try {

            File file = new File(resourceName);//creando una instancia de un archivo a partir del nombre introducido por el usuario
            Collection collection = DatabaseManager.getCollection(URI + collectionName, USER, PASSWORD);//obteniendo la colección introducida por el usuario

            if (collection.getResource(resourceName) == null){//comprobando si el recurso no existe
                Resource resource = collection.createResource(resourceName, "XMLResource");//creando recurso a partir de la colección
                resource.setContent(file);//seteando el archivo al recurso
                collection.storeResource(resource);//guardando el archivo XML en la colección
            }

        } catch (XMLDBException e) {
            e.printStackTrace();
        }
        logOutput.append(dateFormat.format(new Date()) + "\tRecurso agregado: " + resourceName
                + " en Colección: " + collectionName + "\n");
        writeLog();
        System.out.println("\nRecurso añadido.\n");
    }

    /**
     * Elimina un recurso pasándole una colección
     * @param resourceName {@link String}
     * @param collectionName {@link String}
     */
    public void deleteResource(String resourceName, String collectionName){
        logOutput.delete(0,logOutput.length());
        try {
            Collection collection = DatabaseManager.getCollection(URI + collectionName, USER, PASSWORD);//obteniendo la colección introducida por el usuario
            Resource resource = collection.getResource(resourceName);//obteniendo el recurso a partir del nombre insertado por el usuario

            if (resource != null){
                collection.removeResource(resource);//Elimina el recurso solo si existe.
                System.out.println("\nRecurso eliminado.\n");
                logOutput.append(dateFormat.format(new Date()) + "\tRecurso eliminado: " + resourceName +
                        " en Coleccion: " + collectionName + "\n");
            }else{
                System.out.println("\nEl recurso no existe\n");
                logOutput.append(dateFormat.format(new Date()) + "\tRecurso no encontrado: " + resourceName +
                        " en Coleccion: " + collectionName + "\n");
            }

        } catch (XMLDBException e) {
            e.printStackTrace();
        }
        writeLog();
    }

    /**
     * Devuelve un listado de colecciones que tiene el servidor
     * array de Strings
     * @return {@link String[]}
     */
    public String[] getCollections(){
        logOutput.delete(0,logOutput.length());
        String[] collections = new String[0];
        try {

            Database db = (Database) Class.forName(DRIVER).newInstance();
            DatabaseManager.registerDatabase(db);
            Collection parentCollection = DatabaseManager.getCollection(URI, USER, PASSWORD);
            collections = parentCollection.listChildCollections();

        } catch (XMLDBException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        logOutput.append(dateFormat.format(new Date()) + "\tListando colecciones: " + collections + "\n");
        writeLog();

        return collections;
    }


    /**
     * Abre una conexión al servidor ExistDB
     * en el puerto y el host especificado en las variables(host,PORT)
     */
    public void openConnection(){
        logOutput.delete(0,logOutput.length());
        try {
            if (connection == null || connection.isClosed()){
                xqds.setProperty("serverName", host);
                xqds.setProperty("port", PORT);
                connection = xqds.getConnection();
            }else
                System.out.println("La conexión esta abierta");
        } catch (XQException e) {
            e.printStackTrace();
        }
        logOutput.append(dateFormat.format(new Date()) + "\tAbriendo conexión.. \n");
        writeLog();
    }

    /**
     * Cierra la conexión al servidor ExistDB
     */
    public void closeConnection(){
        logOutput.delete(0,logOutput.length());
        try {
            if (!connection.isClosed() || connection != null)
                connection.close();
        } catch (XQException e) {
            e.printStackTrace();
        }
        logOutput.append(dateFormat.format(new Date()) + "\tCerrando conexión.. \n");
        writeLog();
    }

    /**
     * Antes de ejecutar una consulta se necesita (host,PORT) seteados.
     * Ejecuta una consulta a la base de datos recibiendo como parámetro una String.
     * Devuelte una String con el resultado de la consulta.
     * @param query
     * @return
     */
    public String executeQuery(String query) {
        logOutput.delete(0,logOutput.length());
        String line;
        String result = "";
        try {
            XQPreparedExpression preparedExpression = connection.prepareExpression(query);
            XQResultSequence rs = preparedExpression.executeQuery();

            while (rs.next()){
                line = rs.getItemAsString(null);
                result += line;
            }

        } catch (XQException e) {
            System.err.println("Error de consulta");
        }

        logOutput.append(dateFormat.format(new Date()) + "\tEjecutando consulta: " + query + "\n");
        writeLog();

        return result;
    }

    /**
     * Guarda en un fichero (log) las operaciones realizadas
     */
    private void writeLog(){
        try {
            FileWriter fw = new FileWriter("log_dao.log", true);
            fw.write(logOutput.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String getPORT() {
        return PORT;
    }
    public void setPORT(String PORT) {
        this.PORT = PORT;
    }
    public String gethost() {
        return host;
    }
    public void sethost(String host) {
        this.host = host;
    }
    public String getUSER() {
        return USER;
    }
    public void setUSER(String USER) {
        this.USER = USER;
    }
    public String getPASSWORD() {
        return PASSWORD;
    }
    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }
}
