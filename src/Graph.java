import java.lang.reflect.Array;
import java.util.*;

public class Graph {

    private HashMap<String, Integer> th;
    private String[] keys;
    private ArrayList<Integer>[] adjList;
    private ArrayList<Integer>[] enlacesEntrantes;
    private TreeSet<String>[] listaPalabras;

    public Graph(int l)
    {
        th = new HashMap<String, Integer>();
        keys = new String[l];
        adjList = new ArrayList[l];
        enlacesEntrantes = new ArrayList[l];
        listaPalabras = new TreeSet[l];
    }

    public void crearGrafo(ListaWebs lista)
    {
        // Post: crea el grafo desde la lista de webs
        //       Los nodos son nombres de webs


        // Paso 1: llenar “th” ( y paso 3: llenar “adjList” )
        //th.put("nombreWeb", 0);
        //th.get("nombreWeb");  ->  0

        //Obtenemos el iterador que recorrera la lista de webs
        Iterator<Web> itr = lista.getLista().iterator();

        while (itr.hasNext()) {
            //Este arrayList se añadira a adjList
            ArrayList<Integer> idEnlaces = new ArrayList<Integer>();
            Web webActual = itr.next(); //Obtenemos la web
            try
            {
                th.put(webActual.getNombre(), webActual.getId()); //Y la añadimos al HashMap
            }
            catch (OutOfMemoryError oome)
            {
                System.out.println(webActual.getId());
                System.err.println("Max JVM memory: " + Runtime.getRuntime().maxMemory());
            }
            ArrayList<Web> enlaces = webActual.enlacesSalientes(); //Obtenemos los enlaces salientes de la web
            if (enlaces != null)
            {
                Iterator<Web> itrEnlaces = enlaces.iterator();     //Y el iterador de esa lista
                while (itrEnlaces.hasNext())                       //Recorremos los enlaces
                {
                    Web aux = itrEnlaces.next();
                    idEnlaces.add(aux.getId());  //Y añadimos los id asociados a las webs a una lista de id (integer)
                }
                adjList[webActual.getId()] = idEnlaces;            //Esa lista se añade a adjList
            }
        }

        // Paso 2: llenar “keys”
        keys = new String[th.size()];
        for (String k : th.keySet()) keys[th.get(k)] = k;

    }

    public void print()
    {
        for (int i = 0; i < adjList.length; i++)
        {
            System.out.print("Element: " + i + " " + keys[i] + " --> ");

            for (int k : adjList[i]) System.out.print(keys[k] + " ### ");

            System.out.println();
        }
    }

    public ArrayList<String> estanConectados2(String a1, String a2)
    {
        //Post : Se devuelve un arrayList con el camino que conecta a los dos elementos.
        //       Si alguno de los elementos es nulo se devuelve null.

        if (th.isEmpty() || th.get(a1) == null || th.get(a2) == null)
        {
            System.out.println("Null");
            return new ArrayList<String>();
        }
        else
        {
            Integer pos1 = th.get(a1);
            Integer pos2 = th.get(a2);
            ArrayList<String> listaRet;
            if (pos1.equals(pos2))
            {
                listaRet = new ArrayList<String>(1);
                listaRet.add(a1);
            }
            else
            {
                //Preparamos las diferentes variables
                boolean enc = false;
                boolean[] examinados = new boolean[th.size()];
                Integer[] camino = new Integer[th.size()];
                listaRet = new ArrayList<String>();
                Queue<Integer> porExaminar = new LinkedList<Integer>();
                //Anadimos a la cola por examinar el primer elemento
                porExaminar.add(pos1);
                Integer act = pos1;
                camino[pos1] = -1; // punto de inicio

                //Mientras que no encontremos el elemento final y haya elementos en la cola
                while (!enc && !porExaminar.isEmpty())
                {
                    act = porExaminar.poll();
                    examinados[act] = true;
                    for (int i = 0; i < adjList[act].size(); i++)     // bucle de relaciones
                    {
                        if (!porExaminar.contains(adjList[act].get(i)) && !examinados[adjList[act].get(i)])
                        {
                            //Se anade a la cola
                            porExaminar.add(adjList[act].get(i));

                            if (adjList[act].get(i) != act && adjList[act].get(i) != pos1)
                            {
                                //Y se anade al camino de donde viene el nuevo elemento
                                camino[adjList[act].get(i)] = act;
                            }
                        }
                    }
                    //Si hemos llegado al elemento que buscamos
                    if (pos2.equals(act)) {
                        enc = true;
                    }
                }
                if (enc)
                {

                    Integer elemAct = act;
                    listaRet.add(a2);
                    //Retrocedemos en el camino para crear la lista a devolver
                    while (elemAct != -1 || act == pos1)
                    {
                        if (!listaRet.contains(keys[elemAct])) {
                            listaRet.add(keys[elemAct]);
                        }
                        elemAct = camino[elemAct];

                    }
                }
            }
            return listaRet;

        }
    }

    public HashMap<String, Double> pageRank()
    {
        //Inicialización del método
        HashMap<String, Double> rdo = new HashMap<>();
        HashMap<String, Double> it_ant = new HashMap<>();
        Double prInicio = Double.valueOf(1 / th.size());

        for (int i = 0; i < keys.length; i++)
        {
            it_ant.put(keys[i], prInicio);
        }
        rdo = it_ant;

        int numNodos = rdo.size();
        boolean terminado = false;
        Double sumaAnt = prInicio * numNodos;
        Double sumaAct = 0.0;
        Double newPR;

        while (!terminado)
        {
            //Empiezan las iteraciones del pageRank
            for (String dir : rdo.keySet()) {
                newPR = PrOneWeb(it_ant, th.get(dir), numNodos);
                sumaAct = sumaAct + newPR;
                rdo.replace(dir, newPR);
            }
            if (Math.abs(sumaAct - sumaAnt) < 0.0001)
            {
                terminado = true;
            }
            else
            {
                sumaAnt = sumaAct;
                it_ant = rdo;
                sumaAct = 0.0;
            }
        }
        return rdo;
    }

    private Double PrOneWeb(HashMap<String, Double> hm, int i, int numNodos)
    {
        // dumping factor
        Double dFact = 0.85;
        Double rdo = ((1 - dFact) / numNodos) + dFact * sumatorio(hm, i);
        return rdo;
    }

    private Double sumatorio(HashMap<String, Double> hm, int i)
    {

        Double rdo = 0.0;
        Double enlSalientes;
        if (enlacesEntrantes[i]!=null)
        {
            for (int webI = 0; webI < enlacesEntrantes[i].size(); webI++)
            {
                int web = enlacesEntrantes[i].get(webI);
                enlSalientes = Double.valueOf(adjList[web].size());
                rdo = rdo + (hm.get(keys[web]) / enlSalientes);
            }
        }
        return rdo;
    }

    public void llenarListaPalabras()
    {
        listaPalabras = (TreeSet<String>[]) new TreeSet[adjList.length];
        for (int i = 0; i < keys.length; i++)
        {
            String dir = keys[i];
            String s = dir.substring(dir.indexOf(":") + 1, dir.indexOf("."));
            for (int pos = 0; pos < s.length(); pos++)
            {
                for (int longitud = 3; longitud <= 10 && pos + longitud <= s.length(); longitud++)
                {
                    String palabra = s.substring(pos, pos + longitud);
                    if (listaPalabras[i] == null)
                    {
                        listaPalabras[i] = new TreeSet<String>();
                    }
                    listaPalabras[i].add(palabra);
                }
            }
        }
    }

    public void imprimirListaPalabras()
    {
        for (int i =0; i<listaPalabras.length; i++)
        {
            System.out.println(keys[i]);
            for (int j=0; j<listaPalabras[i].size(); j++)
            {
                System.out.println(listaPalabras[i].iterator().next());
            }
            System.out.println("");
        }
    }

    public ArrayList<Par> buscarPaginas(String pal1, String pal2)
    {
        ArrayList<Par> rdo = new ArrayList<Par>();
        HashMap<String, Double> pageRank = pageRank();
        // bucle
        for (int i =0; i< listaPalabras.length; i++)
        {
            if (listaPalabras[i]!=null && listaPalabras[i].contains(pal1) && listaPalabras[i].contains(pal2))
            {
                String nom = keys[i];
                Par nuevoPar = new Par(nom, pageRank.get(nom));
                rdo.add(nuevoPar);
            }
        }
        // Al no tener los atributos publicos no se puede hacer un o2.pageRank, ya que tenemos que usar
        // el getter de la clase. Ademas, aqui se usa la clase comparator, que se emplea para
        // hacer el sort de manera muy eficiente y practica, puesto que ordena los elementos
        // en base a su pagerank, que es lo que nos interesa
        // por tanto, con solo emplear esta linea es suficiente
        rdo.sort(((o1, o2) -> o2.getPageRank().compareTo(o1.getPageRank())));
        return rdo;
    }
    public ArrayList<String> estanConectados(String a1, String a2){
        // El resultado será una lista de relaciones desde a1 hasta a2, donde
        // cada relación indica que esos 2 elementos están conectados.
        ArrayList<String> listaRet = new ArrayList<String>();
        if (!th.isEmpty() || th.get(a1)!=null || th.get(a2)!=null)
        {
            Integer pos1 = th.get(a1);
            Integer pos2 = th.get(a2);
            Boolean enc = false;
            Boolean[] examinados = new Boolean[th.size()];
            Integer[] camino = new Integer[th.size()];
            if (pos1.equals(pos2))
            {
                listaRet.add(keys[pos1]);
            }
            else
            {
                Queue<Integer> porExaminar = new LinkedList<Integer>();
                porExaminar.add(pos1);          // anadimos elemento a la lista
                examinados[pos1] = true;
                Integer act = porExaminar.poll();
                Integer prev = -1;              // para que en el camino el elemento inicial sea -1
                Boolean peek = false;
                while (!enc || !peek)
                {
                    for(int i = 0; i<adjList[act].size(); i++)
                    {
                        Integer numact = adjList[act].get(i);
                        if (!examinados[act] && !porExaminar.contains(act))
                        {
                            porExaminar.add(numact);
                            camino[numact] = act;
                        }
                    }
                    if (act.equals(pos2))
                    {
                        enc = true;
                    }
                    else
                    {
                        System.out.println("no es igual");
                        examinados[act] = true;
                        if (porExaminar.isEmpty()){peek=true;}
                        while ( !peek && examinados[act])
                        {
                            System.out.println(act);
                            act = porExaminar.poll();
                            peek = (porExaminar.isEmpty());

                        }
                    }
                    if (peek==true && !porExaminar.isEmpty())
                    {
                        act = porExaminar.poll();
                    }
                    peek = (porExaminar.isEmpty());
                }
                if(enc)
                {
                    Integer i = act;
                    while (!camino[i].equals(-1))
                    {
                        listaRet.add(keys[i]);
                        i = camino[i];
                    }
                }
            }
        }
        return listaRet;
    }

}