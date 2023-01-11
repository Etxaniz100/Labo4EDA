
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


        Path path = Paths.get("");
        main.getMiMain().cargarListaPalabras(path.toAbsolutePath().toString() + "\\words.txt");

        System.out.println("Tiempo para cargar las palabras: " + (reloj.elapsedTime() - aux));
        aux = reloj.elapsedTime();
        /*
        HashMap<Integer, Character> hs = ListaPalabras.getMiListaPalabras().getHashMap();
        System.out.println(hs.get(1));
        System.out.println(hs.keySet());
        */
        main.getMiMain().cargarListaIndex(path.toAbsolutePath().toString() + "\\listaPeque");
        System.out.println("Tiempo para cargar las webs: " + (reloj.elapsedTime() - aux));
        aux = reloj.elapsedTime();
        main.getMiMain().cargarListaRelaciones(path.toAbsolutePath().toString() + "\\relacionesPeque");
        System.out.println("Tiempo para cargar las relaciones: " + (reloj.elapsedTime() - aux));
        aux = reloj.elapsedTime();
        Graph grafo = new Graph(ListaWebs.getMiListaWebs().getLista().size());
        grafo.crearGrafo(ListaWebs.getMiListaWebs());
        System.out.println("Tiempo para crear el grafo: " + (reloj.elapsedTime() - aux));
        aux = reloj.elapsedTime();
        /*
        System.out.println("Caso de prueba web2Words");
        System.out.println(ListaWebs.getMiListaWebs().web2Words("devalt.org"));
        System.out.println(ListaWebs.getMiListaWebs().web2Words("campofrio.es"));
        System.out.println(ListaWebs.getMiListaWebs().web2Words("alphadatainternational.com"));
        System.out.println(ListaWebs.getMiListaWebs().web2Words("business-english-dresden.de"));
        */
        grafo.llenarListaPalabras();
        System.out.println("Tiempo para llenar las palabras: " + (reloj.elapsedTime() - aux));
        aux = reloj.elapsedTime();
        /*
        grafo.print();
        System.out.println("Tiempo para los casos de prueba: " + reloj.elapsedTime());
        System.out.println("Caso 1-> El mismo elemento");
        System.out.println(grafo.estanConectados2(ListaWebs.getMiListaWebs().getLista().get(0).getNombre(),ListaWebs.getMiListaWebs().getLista().get(0).getNombre()).toString());
        System.out.println("Caso 2-> Dos elementos directamente conectados");
        System.out.println(grafo.estanConectados2(ListaWebs.getMiListaWebs().getLista().get(3).getNombre(),ListaWebs.getMiListaWebs().getLista().get(398861).getNombre()).toString());
        System.out.println("Caso 2.1-> Dos elementos conectados mediante otro elemento de por medio");
        System.out.println(grafo.estanConectados2(ListaWebs.getMiListaWebs().getLista().get(3).getNombre(),ListaWebs.getMiListaWebs().getLista().get(115696).getNombre()).toString());
        System.out.println("Caso 2.2-> Dos elementos conectados mediante dos elementos de por medio");
        System.out.println(grafo.estanConectados2(ListaWebs.getMiListaWebs().getLista().get(3).getNombre(),ListaWebs.getMiListaWebs().getLista().get(115515).getNombre()).toString());
        System.out.println("Caso 3-> Dos elementos no estan conectados");
        System.out.println(grafo.estanConectados2(ListaWebs.getMiListaWebs().getLista().get(845).getNombre(),ListaWebs.getMiListaWebs().getLista().get(92355).getNombre()).toString());
        System.out.println("Caso 3.1-> Un elemento no esta en la lista");
        System.out.println(grafo.estanConectados2(ListaWebs.getMiListaWebs().getLista().get(1224).getNombre(),"webquenoestaenlalista"));
        System.out.println("Caso 3.2-> Ningun elemento esta en la lista");
        System.out.println(grafo.estanConectados2("webquenoestaenlalista1","webquenoestaenlalista2"));;
        System.out.println("Tiempo para los casos de prueba: " + reloj.elapsedTime());
         */
        HashMap<String, Double> pageRank = grafo.pageRank();
        System.out.println("Tiempo para calcular el pagerank: " + (reloj.elapsedTime() - aux));
        aux = reloj.elapsedTime();

        // palabras que si estan
        ArrayList<Par> listPar = grafo.buscarPaginas("beach","the", pageRank);
        for (int i =0; i<listPar.size(); i++)
        {
            System.out.println("< " + listPar.get(i).getWeb() + ", " + listPar.get(i).getPageRank() + " >");
        }
        System.out.println("Tiempo para buscar las paginas: " + (reloj.elapsedTime() - aux));

        aux = reloj.elapsedTime();
        listPar = grafo.buscarPaginas("blue","money", pageRank);
        for (int i =0; i<listPar.size(); i++)
        {
            System.out.println("< " + listPar.get(i).getWeb() + ", " + listPar.get(i).getPageRank() + " >");
        }
        System.out.println("Tiempo para buscar las paginas: " + (reloj.elapsedTime() - aux));

        // palabras que no estan
        aux = reloj.elapsedTime();
        listPar = grafo.buscarPaginas("unapalabraquenoesta", "otrapalabratotalmentedistinta", pageRank);
        System.out.println("Tiempo para buscar las paginas: " + (reloj.elapsedTime() - aux));
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
