
import javax.print.DocFlavor;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class main {

    // ------ Atributos ------
    //private ArrayList<Escenario> lista;
    private static main miMain;

    // ----- Constructora -----
    private main() {
    }

    // ------ Metodos ------
    public static void main(String args[]) {

        double aux = 0;
        Stopwatch reloj = new Stopwatch();       // Inicialización
        aux = reloj.elapsedTime();
        System.out.println("Tiempo inicial: " + aux);


        /*
         ------------------ Casos de prueba : ------------------

        Lista con la que se trabaja estas pruebas :

            0:freerobloxmoneynoscam4k.com
            1:ilikemoney.es
            2:bitcoinmoney.yes
            3:freemoney.com
            4:thisisfree.net
            5:freeandbitcoin.es
            6:moneyforever.as
            7:freevbucksandmoney.er
            8:moneymoneymoney.es
            9:nomoney.no

            0 ---> 5
            1 ---> 2 ### 5
            2 ---> 1 ### 3 ### 7
            3 ---> 2 ### 4 ### 7 ### 8 ### 9
            4 ---> 3
            5 ---> 0 ### 1 ### 6
            6 ---> 5
            7 ---> 2 ### 3
            8 ---> 3 ### 9
            9 ---> 3 ### 8

        (En el programa utilizamos un damping factor de 0.85)

       Iteraciones : 0      /   1     /     2   ... /  40

            PR(0)   0.1     0.04333     0.07945      0.05798
            PR(1)   0.1     0.07167     0.11260      0.09308
            PR(2)   0.1     0.11700     0.11459      0.12651
            PR(3)   0.1     0.25583     0.16431      0.20036
            PR(4)   0.1     0.03200     0.05849      0.04916
            PR(5)   0.1     0.22750     0.11912      0.15984
            PR(6)   0.1     0.04333     0.07945      0.05798
            PR(7)   0.1     0.06033     0.09164      0.08426
            PR(8)   0.1     0.07450     0.09015      0.08539
            PR(9)   0.1     0.07450     0.09015      0.08539

        Como la diferencia entre la suma de los page rank actual y anterior esperada es menor a 0.0001
        para estabilizarse llega hasta la iteracion numero 40 (en este caso)
        */

        Path pathPrueba = Paths.get("");

        main.getMiMain().cargarListaPalabras(pathPrueba.toAbsolutePath().toString() + "\\words.txt");
        System.out.println("Tiempo para cargar las palabras: " + (reloj.elapsedTime() - aux));
        aux = reloj.elapsedTime();

        main.getMiMain().cargarListaIndex(pathPrueba.toAbsolutePath().toString() + "\\listaPrueba");
        System.out.println("Tiempo para cargar las webs: " + (reloj.elapsedTime() - aux));
        aux = reloj.elapsedTime();
        main.getMiMain().cargarListaRelaciones(pathPrueba.toAbsolutePath().toString() + "\\enlacesPrueba");
        System.out.println("Tiempo para cargar las relaciones: " + (reloj.elapsedTime() - aux));
        aux = reloj.elapsedTime();

        Graph grafoPrueba = new Graph(ListaWebs.getMiListaWebs().getLista().size());
        grafoPrueba.crearGrafo(ListaWebs.getMiListaWebs());
        System.out.println("Tiempo para crear el grafo: " + (reloj.elapsedTime() - aux));
        aux = reloj.elapsedTime();

        grafoPrueba.llenarListaPalabras();
        System.out.println("Tiempo para llenar las palabras: " + (reloj.elapsedTime() - aux));
        aux = reloj.elapsedTime();

        HashMap<String, Double> pageRankPrueba = grafoPrueba.pageRank();
        System.out.println("Tiempo para calcular el pagerank: " + (reloj.elapsedTime() - aux));
        aux = reloj.elapsedTime();

        ArrayList<Par> listParPrueba = grafoPrueba.buscarPaginas("free","money", pageRankPrueba);
        System.out.println("Valores esperados para las webs : ");
        System.out.println("");
        System.out.println("        0:freerobloxmoneynoscam4k.com\n" +
                "        1:ilikemoney.es\n" +
                "        2:bitcoinmoney.yes\n" +
                "        3:freemoney.com\n" +
                "        4:thisisfree.net\n" +
                "        5:freeandbitcoin.es\n" +
                "        6:moneyforever.as\n" +
                "        7:freevbucksandmoney.er\n" +
                "        8:moneymoneymoney.es\n" +
                "        9:nomoney.no");
        System.out.println("");
        System.out.println("       Iteraciones : 0      /   1     /     2   ... /  40\n" +
                "\n" +
                "            PR(0)   0.1     0.04333     0.07945      0.05798\n" +
                "            PR(1)   0.1     0.07167     0.11260      0.09308\n" +
                "            PR(2)   0.1     0.11700     0.11459      0.12651\n" +
                "            PR(3)   0.1     0.25583     0.16431      0.20036\n" +
                "            PR(4)   0.1     0.03200     0.05849      0.04916\n" +
                "            PR(5)   0.1     0.22750     0.11912      0.15984\n" +
                "            PR(6)   0.1     0.04333     0.07945      0.05798\n" +
                "            PR(7)   0.1     0.06033     0.09164      0.08426\n" +
                "            PR(8)   0.1     0.07450     0.09015      0.08539\n" +
                "            PR(9)   0.1     0.07450     0.09015      0.08539 ");
        System.out.println("");
        System.out.println("Valores reales para las webs (buscadas con las palabras clave free y money): ");
        for (int i =0; i<listParPrueba.size(); i++)
        {
            System.out.println("< " + listParPrueba.get(i).getWeb() + ", " + listParPrueba.get(i).getPageRank() + " >");
        }
        System.out.println("Tiempo para buscar las paginas: " + (reloj.elapsedTime() - aux));
        aux = reloj.elapsedTime();


    }

    public static main getMiMain() {
        // el metodo getMiMAE, ya que estamos delante de una mae.
        if (miMain == null) {
            miMain = new main();
        }
        return (miMain);
    }

    public void cargarListaIndex(String nomF) {
        // Coste operativo : O(n) --> Lineal
        System.out.println("Cargando lista index");
        try {
            Scanner entrada = new Scanner(new FileReader(nomF));
            String linea;
            while (entrada.hasNext()) {
                linea = entrada.nextLine();
                // el split para dividir los datos
                String datos[] = linea.split(":+");
                // Creamos la web con los dos datos leidos
                // el limite de 1000 es algo arbitrario
                Web laWeb = new Web(datos[1], Integer.valueOf(datos[0]), new String[datos[1].length()]);
                laWeb.obtenerPalabrasClave();
                ListaWebs.getMiListaWebs().insertarWeb(laWeb);
            }
            entrada.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Lista Index cargada");
        ListaPalabras.getMiListaPalabras().crearHashMap();
        System.out.println("Palabras insertadas");
    }
    public void cargarListaPalabras(String nomF)
    {
        //Coste operativo : O(n) --> Lineal
        System.out.println("Cargando lista palabras");
        try
        {
            //Abrimos y preparamos el scanner
            Scanner entrada = new Scanner(new FileReader(nomF));
            String linea;

            while (entrada.hasNext())
            {
                //Leemos la palabra
                linea = entrada.nextLine();

                //Y la metemos en la lista de la MAE
                ListaPalabras.getMiListaPalabras().insertarPalabra(linea);
            }
            entrada.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        ListaPalabras.getMiListaPalabras().crearHashMap();
        System.out.println("Lista palabras cargada");
    }
    public void cargarListaRelaciones(String nomF) {
        //Coste operativo de esto : n * media de relaciones entre webs, pero no es mucho entonces O(n) --> coste lineal

        System.out.println("Cargando lista relaciones");
        try {
            //Abrimos y preparamos el scanner
            Scanner entrada = new Scanner(new FileReader(nomF));
            String linea;
            while (entrada.hasNext()) {
                //Leemos la linea correspondiente del texto
                linea = entrada.nextLine();

                //Separamos en dos la linea, en una parte el index, y en la otra todas las relaciones
                String datos[] = linea.split("\\s+?-+>+\\s+?");

                //Creamos la lista que usaremos para el metodo anadirRelacion que tiene que ser de integers
                ArrayList<Web> listaRelaciones = new ArrayList<Web>();

                //Separamos todas las relaciones en forma de Strings
                if (datos.length > 1) {
                    String[] relacionesTexto = datos[1].split("\\s+?#+\\s+?");

                    //En este bucle convertimos uno a uno las relaciones a Integer, buscamos en la lista webs y las añadimos a la lista
                    for (int i = 0; i < relacionesTexto.length; i++) {
                        listaRelaciones.add(ListaWebs.getMiListaWebs().getLista().get(Integer.valueOf(relacionesTexto[i])));  // aqui convertimos la string a integer y luego a web
                    }
                }
                //Añadimos las relaciones a la web correspondiente
                ListaWebs.getMiListaWebs().anadirRelacion(Integer.valueOf(datos[0]), listaRelaciones);
            }
            entrada.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Lista relaciones cargada");
    }

    public void guardarListaIndex(String nomF) {
        BufferedWriter bw;
        try {
            ListaWebs.getMiListaWebs().ajustarIds();

            bw = new BufferedWriter(new FileWriter(nomF));
            ArrayList<Web> listaWeb = ListaWebs.getMiListaWebs().getLista();
            Integer cont = 0;
            Web webActual;
            while (cont < listaWeb.toArray().length) {
                webActual = listaWeb.get(cont);
                bw.write(webActual.getId().toString() + ":" + webActual.getNombre());
                cont++;
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void guardarRelaciones(String nomF) {

        // Abrimos el bufferedwriter que es el objeto que nos deja escribir en un fichero de texto.
        BufferedWriter bw;
        try {
            ListaWebs.getMiListaWebs().ajustarIds();
            bw = new BufferedWriter(new FileWriter(nomF));
            //ListaWebs.getMiListaWebs().ordenarWebs();
            ArrayList<Web> listaWeb = ListaWebs.getMiListaWebs().getLista();
            ArrayList<Web> listaRelaciones;
            Integer cont = 0;
            Web webActual;
            while (cont < listaWeb.size()) {
                webActual = listaWeb.get(cont);
                listaRelaciones = webActual.enlacesSalientes();
                bw.write(webActual.getId().toString() + " ---> ");
                if (listaRelaciones.iterator().hasNext()) {
                    bw.write(listaRelaciones.iterator().next().getId().toString());
                }
                Iterator<Web> itr = listaRelaciones.iterator();
                while (itr.hasNext()) {
                    bw.write(" ### " + itr.next().getId().toString());
                }

                cont++;
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException npe) {
            System.out.println("La lista es vacía");
        }
    }
}
