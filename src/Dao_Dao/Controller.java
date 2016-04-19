package Dao_Dao;

import org.xmldb.api.base.XMLDBException;
import javax.xml.bind.JAXBException;
import java.util.Scanner;

/**
 * Created by sergi on 17/04/16.
 */
public class Controller {

    public static void main(String[] args) throws JAXBException, XMLDBException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        // DAO's
        DAO_XML DAO_XML = new DAO_XML("172.31.101.225", "8080", "coleccionAModificar", "/home/46465442z/IdeaProjects/Example_XML_API/src/Dao_Dao/bbdd.xml", "admin", "dionis");

        // Menú
        Scanner teclat = new Scanner(System.in);

        int opcio = 1;

        while (opcio != 0){

            System.out.println("MENU:");

            System.out.println("--------------Menu--------------");
            System.out.println("1 - Añadir Empleado");
            System.out.println("2 - Añadir Cliente");
            System.out.println("3 - Añadir Factura");
            System.out.println("4 - Añadir Producto");
            System.out.println("5 - Borrar Empleado ");
            System.out.println("6 - Borrar Cliente");
            System.out.println("7 - Facturas de un cliente");
            System.out.println("8 - Empleado por nombre");
            System.out.println("9 - Empleado por apellido");
            System.out.println("10 - Empleado por sueldo");
            System.out.println("11 -  Empleado por años trabajados");

            System.out.println("0 - Sortir ");

            opcio = teclat.nextInt();

            switch (opcio){

                case 0:

                break;

                case 1:
                    // Anyadir un empleado al XML
                    DAO_XML.anyadirEmpleado();
                break;

                case 2:
                    // Anyadir un cliente al XML
                    DAO_XML.anyadirCliente();
                break;

                case 3:
                   // Anyadir una factura al XML
                    DAO_XML.anyadirFactura();
                break;

                case 4:
                    // Anyadir un producto al XML
                    DAO_XML.anyadirProducto();
                break;

                case 5:
                    // Permite borrar un empleado
                    DAO_XML.borrarEmpleado();
                break;

                case 6:
                    // Permite borrar un cliente
                    DAO_XML.borrarCliente();
                break;

                case 7:
                    // Buscar las facturas de un cliente
                    DAO_XML.facturasClientes();
                break;

                case 8:
                    // Buscar empleados por nombre
                    DAO_XML.empleadosNombre();
                break;

                case 9:
                    // Buscar empleados por apellidos
                    DAO_XML.empleadosApellidos();
                break;

                case 10:
                    // Buscar empleados por su sueldo
                    DAO_XML.empleadosSalario();
                break;

                case 11:
                    // Buscar empleados por antiguedad
                    DAO_XML.empleadosAntiguedad();
                break;
            }
        }
    }
}
