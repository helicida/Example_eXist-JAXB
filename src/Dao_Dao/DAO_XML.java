package Dao_Dao;

import Dao_Dao.generated.*;

import org.xmldb.api.base.XMLDBException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.io.File;
import java.util.Scanner;

/**
 * Created by mireia on 18/04/2016.
 */
public class DAO_XML {

    private Scanner teclat = new Scanner(System.in);
    private DAO_Diego DAO_eXists;

    private String usuario = "";
    private String contrasenya = "";
    private String ip = "";
    private String puerto = "";
    private String coleccion = "";
    private String ruta_bbdd_XML = "";
    private File archivoBBDD;
    private TiendaType raizBBDD;
    private JAXBContext JaxbContext;

    // Constante que contiene el driver XML
    private final String DRIVER = "org.exist.xmldb.DatabaseImpl";

    /**
     *Constructor vacío
     */
    public DAO_XML(){

    }

    /**
     *
     * @param ip
     * @param puerto
     * @param coleccion
     * @param ruta_bbdd_XML
     */
    public DAO_XML(String ip, String puerto , String coleccion, String ruta_bbdd_XML, String usuario, String contrasenya){
        this.ip = ip;
        this.puerto = puerto;
        this.coleccion = coleccion;
        this.ruta_bbdd_XML = ruta_bbdd_XML;
        this.archivoBBDD = new File(ruta_bbdd_XML);
        this.usuario = usuario;
        this.contrasenya = contrasenya;

        DAO_eXists = new DAO_Diego(ip, puerto, usuario, contrasenya);
        DAO_eXists.openConnection();

        if(!archivoBBDD.exists()){
            System.out.println("No se ha encontrado la BBDD indicada");
        }

        try {
            JaxbContext = JAXBContext.newInstance(TiendaType.class);
            Unmarshaller ums = JaxbContext.createUnmarshaller();
            raizBBDD  = (TiendaType) ums.unmarshal(archivoBBDD);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sube el XML indicado a la BBDD eXists
     * @throws XMLDBException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException

     */
    void subirRecurso() throws XMLDBException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        DAO_eXists.createCollection(coleccion);
        DAO_eXists.deleteResource(ruta_bbdd_XML, coleccion);
        DAO_eXists.addResource(ruta_bbdd_XML, coleccion);
    }

    /**
     * Anyade un cliente a nuestra BBDD XML
     * @throws JAXBException
     */
    void anyadirCliente() throws JAXBException, XMLDBException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        System.out.println("Introduce el DNI del cliente");
            String dni = teclat.nextLine();
        System.out.println("Introduce el nombre del cliente");
            String nombre = teclat.nextLine();
        System.out.println("Introduce el apellido del cliente");
            String apellido = teclat.nextLine();

        // Objeto que anyadiremos
        ClienteType cliente = new ClienteType();
        cliente.setDni(dni);
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);

        System.out.println(" - Añadiendo cliente");

        raizBBDD.getClientes().getCliente().add(cliente);
        Marshaller ms = JaxbContext.createMarshaller();
        ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ms.marshal(raizBBDD, archivoBBDD);

        subirRecurso();
        System.out.println("- Se ha añadido el cliente correctamente");

    }

    /**
     * Anyade un empleado a nuestra BBDD XML
     * @throws JAXBException
     */
    void anyadirEmpleado() throws JAXBException, XMLDBException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        System.out.println("Introduce la ID del empleado");
            String id = teclat.nextLine();
        System.out.println("Introduce el nombre del empleado");
            String nombre = teclat.nextLine();
        System.out.println("Introduce el apellido del empleado");
            String apellido = teclat.nextLine();
        System.out.println("Introduce el sueldo del empleado");
            String sueldo = teclat.nextLine();
        System.out.println("Introduce la antiguedad del trabajador");
            String antiguedad = teclat.nextLine();

        // Objeto que anyadiremos
        EmpleadoType empleado = new EmpleadoType();
        empleado.setId(id);
        empleado.setNombre(nombre);
        empleado.setApellido(apellido);
        empleado.setSueldo(sueldo);
        empleado.setAntiguedad(antiguedad);

        System.out.println(" - Añadiendo empleado");

        raizBBDD.getEmpleados().getEmpleado().add(empleado);
        Marshaller ms = JaxbContext.createMarshaller();
        ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ms.marshal(raizBBDD, archivoBBDD);

        subirRecurso();
        System.out.println("- Se ha añadido el empleado correctamente");

    }

    /**
     * Anyade una factura a nuestra BBDD XML
     * @throws JAXBException
     */
    void anyadirFactura() throws JAXBException, XMLDBException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        System.out.println("Introduce DNI del Cliente");
            String dni = teclat.nextLine();
        System.out.println("Introduce la id del Producto");
            String id = teclat.nextLine();
        System.out.println("Introduce el valor del producto");
            String precio_articulo = teclat.nextLine();
        System.out.println("Introduce el total de la factura");
            String precio_total = teclat.nextLine();
        System.out.println("Introduce el procentaje de impuestos");
            String iva = teclat.nextLine();

        // Objeto que anyadiremos
        FacturaType factura = new FacturaType();
        factura.setDniCliente(dni);
        factura.setIdProducto(id);
        factura.setPrecioArticulo(precio_articulo);
        factura.setIva(iva);
        factura.setPrecioTotal(precio_total);

        System.out.println(" - Añadiendo factura");

        raizBBDD.getFacturas().getFactura().add(factura);
        Marshaller ms = JaxbContext.createMarshaller();
        ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ms.marshal(raizBBDD, archivoBBDD);

        subirRecurso();
        System.out.println("- Se ha añadido la factura correctamente");
    }

    /**
     * Anyade un producto a nuestra BBDD XML
     * @throws JAXBException
     */
    void anyadirProducto() throws JAXBException, XMLDBException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        System.out.println("Introduce la ID del Producto");
            String id = teclat.nextLine();
        System.out.println("Introduce el nombre del producto");
            String nombre = teclat.nextLine();
        System.out.println("Introduce el precio del procuto");
            String precio = teclat.nextLine();
        System.out.println("¿Cuantos productos hay en stock?");
            String stock = teclat.nextLine();

        // Objeto que anyadiremos
        ProductoType producto = new ProductoType();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setStock(stock);

        System.out.println(" - Añadiendo producto");

        raizBBDD.getInventario().getProducto().add(producto);
        Marshaller ms = JaxbContext.createMarshaller();
        ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ms.marshal(raizBBDD, archivoBBDD);

        subirRecurso();
        System.out.println("- Se ha añadido el producto correctamente");

    }

    // Consultas

    /**
     * Muestra a todos los empleados con el nombre introducido
     */
    void empleadosNombre() {

        System.out.println("Introduce el nombre del empleado que quieres buscar");
            String nombre = teclat.nextLine();

        System.out.println("ID       | Nombre    | Apellidos     | Sueldo\n---------------------------------------");

        for (int iterador = 0; iterador < raizBBDD.getEmpleados().getEmpleado().size(); iterador++)  {

            EmpleadoType empleado = raizBBDD.getEmpleados().getEmpleado().get(iterador);

            if (raizBBDD.getEmpleados().getEmpleado().get(iterador).getNombre().equalsIgnoreCase(nombre)) {
                System.out.println(empleado.getId() + " - " +
                        empleado.getNombre() + "  -  " +
                        empleado.getApellido() + "  ->  " +
                        empleado.getSueldo());
            }
        }
    }

    /**
     * Muestra a los empleados con el apellido indicado
     */
    void empleadosApellidos(){

        System.out.println("Introduce el apellido del empleado que quieres bucar");
            String apellidos = teclat.nextLine();

        System.out.println("ID       | Nombre    | Apellidos     | Sueldo\n---------------------------------------");

        for (int iterador = 0; iterador < raizBBDD.getEmpleados().getEmpleado().size(); iterador++)  {

            EmpleadoType empleado = raizBBDD.getEmpleados().getEmpleado().get(iterador);

            if (raizBBDD.getEmpleados().getEmpleado().get(iterador).getApellido().equalsIgnoreCase(apellidos)) {
                System.out.println(empleado.getId() + " - " +
                        empleado.getNombre() + "  -  " +
                        empleado.getApellido() + "  ->  " +
                        empleado.getSueldo());
            }
        }
    }

    /**
     * Muestra a todoss los empleados con un sueldo en específico
     */
    void empleadosSalario(){

        System.out.println("Introduce el sueldo del empleado que quieres buscar");
            String sueldo = teclat.nextLine();

        System.out.println("ID       | Nombre    | Apellidos     | Sueldo\n---------------------------------------");

        for (int iterador = 0; iterador < raizBBDD.getEmpleados().getEmpleado().size(); iterador++)  {

            EmpleadoType empleado = raizBBDD.getEmpleados().getEmpleado().get(iterador);

            if (raizBBDD.getEmpleados().getEmpleado().get(iterador).getSueldo().equalsIgnoreCase(sueldo)) {
                System.out.println(empleado.getId() + " - " +
                        empleado.getNombre() + "  -  " +
                        empleado.getApellido() + "  ->  " +
                        empleado.getSueldo());
            }
        }
    }

    /**
     * Muestra todos los empleados con una antiguedad concreta
     */
    void empleadosAntiguedad(){

        System.out.println("Introduce la antiguedad del empleado que quieres buscar");
            String antiguedad = teclat.nextLine();

        System.out.println("ID       | Nombre    | Apellidos     | Sueldo\n---------------------------------------");

        for (int iterador = 0; iterador < raizBBDD.getEmpleados().getEmpleado().size(); iterador++)  {

            EmpleadoType empleado = raizBBDD.getEmpleados().getEmpleado().get(iterador);

            if (raizBBDD.getEmpleados().getEmpleado().get(iterador).getAntiguedad().equalsIgnoreCase(antiguedad)) {
                System.out.println(empleado.getId() + " - " +
                        empleado.getNombre() + "  -  " +
                        empleado.getApellido() + "  ->  " +
                        empleado.getSueldo());
            }
        }
    }

    /**
     * Muestra todas las facturas de un cliente en especifico
     */
    void facturasClientes(){

        System.out.println("Introduce el DNI del cliente");
            String dni = teclat.nextLine();

        System.out.println("ID Producto | Impuestos | Precio Total\n---------------------------------------");

        for (int iterador = 0; iterador < raizBBDD.getFacturas().getFactura().size(); iterador++)  {

            if (raizBBDD.getFacturas().getFactura().get(iterador).getDniCliente().equalsIgnoreCase(dni)) {

                FacturaType factura = raizBBDD.getFacturas().getFactura().get(iterador);

                System.out.println(factura.getIdProducto() + "         -   " +
                factura.getIva() + "      -     " +
                factura.getPrecioTotal());
            }
        }
    }

    // Borrar

    /**
     * Borra un cliente de la BBDD XML
     */
    void borrarCliente() {

        System.out.println("Introduce el DNI del cliente a Borrar");
            String dni = teclat.nextLine();

        for (int iterador = 0; iterador < raizBBDD.getEmpleados().getEmpleado().size(); iterador++)  {

            if (raizBBDD.getClientes().getCliente().get(iterador).getDni().equalsIgnoreCase(dni)) {

                raizBBDD.getClientes().getCliente().remove(iterador);

                System.out.println("- Se ha borrado al cliente correctamente");
            }
        }
    }

    /**
     * Borra un empleado de la BBDD XML
     */
    void borrarEmpleado() {

        System.out.println("Introduce el DNI del empleado a Borrar");
            String dni = teclat.nextLine();

        for (int iterador = 0; iterador < raizBBDD.getEmpleados().getEmpleado().size(); iterador++)  {

            if (raizBBDD.getEmpleados().getEmpleado().get(iterador).getId().equalsIgnoreCase(dni)) {

                raizBBDD.getEmpleados().getEmpleado().remove(iterador);

                System.out.println("- Se ha borrado al empleado correctamente");
            }
        }
    }
}

    /*
    // JAXB Controler
    JaxbContext = JAXBContext.newInstance(TiendaType.class);
    Unmarshaller ums = JaxbContext.createUnmarshaller();
    TiendaType bbddTienda = (TiendaType) ums.unmarshal(new File("/home/46465442z/IdeaProjects/Example_XML_API/src/Dao_Dao/bbdd.xml"));
    System.out.println(bbddTienda.getClientes().getCliente().get(0).getDni());*/