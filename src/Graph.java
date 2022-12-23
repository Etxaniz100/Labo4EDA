import java.lang.reflect.Array;
import java.util.*;

public class Graph {

    private HashMap<String, Integer> th;
    private String[] keys;
    private ArrayList<Integer>[] adjList;


    public Graph(int l)
    {
        th = new HashMap<String, Integer>();
        keys = new String[l];
        adjList = new ArrayList[l];
    }

    public void crearGrafo(ListaWebs lista){
        // Post: crea el grafo desde la lista de webs
        //       Los nodos son nombres de webs


        // Paso 1: llenar “th” ( y paso 3: llenar “adjList” )
        //th.put("nombreWeb", 0);
        //th.get("nombreWeb");  ->  0

        //Obtenemos el iterador que recorrera la lista de webs
        Iterator<Web> itr = lista.getLista().iterator();

        while (itr.hasNext())
        {
            //Este arrayList se añadira a adjList
            ArrayList<Integer> idEnlaces = new ArrayList<Integer>();
            Web webActual = itr.next(); //Obtenemos la web
            try
            {
                th.put(webActual.getNombre(), webActual.getId()); //Y la añadimos al HashMap
            }
            catch(OutOfMemoryError oome)
            {
                System.out.println(webActual.getId());
                System.err.println("Max JVM memory: " + Runtime.getRuntime().maxMemory());
            }
            ArrayList<Web> enlaces = webActual.enlacesSalientes(); //Obtenemos los enlaces salientes de la web
            if (enlaces!=null)
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
        for (String k: th.keySet()) keys[th.get(k)] = k;

    }

    public void print(){
        for (int i = 0; i < adjList.length; i++)
        {
            System.out.print("Element: " + i + " " + keys[i] + " --> ");

            for (int k: adjList[i])  System.out.print(keys[k] + " ### ");

            System.out.println();
        }
    }
    public ArrayList<String> estanConectados2(String a1, String a2)
    {
        //Post : Se devuelve un arrayList con el camino que conecta a los dos elementos.
        //       Si alguno de los elementos es nulo se devuelve null.

        if (th.isEmpty() || th.get(a1)==null || th.get(a2)==null){
            System.out.println("Null");
            return new ArrayList<String>();}
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
                boolean        enc         =     false;
                boolean[]      examinados  = new boolean[th.size()];
                Integer[]      camino      = new Integer[th.size()];
                               listaRet    = new ArrayList<String>();
                Queue<Integer> porExaminar = new LinkedList<Integer>();

                //Anadimos a la cola por examinar el primer elemento
                porExaminar.add(pos1);
                Integer act = pos1;
                camino[pos1]= -1; // punto de inicio

                //Mientras que no encontremos el elemento final y haya elementos en la cola
                while (!enc && !porExaminar.isEmpty())
                {
                    act = porExaminar.poll();
                    examinados[act] = true;
                    for(int i = 0; i < adjList[act].size(); i++)     // bucle de relaciones
                    {
                        if(!porExaminar.contains(adjList[act].get(i)) && !examinados[adjList[act].get(i)])
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
                    if(pos2.equals(act))
                    {
                        enc = true;
                    }

                    //Sacamos al siguiente elemento por examinar



                }
                if (enc)
                {

                    ArrayList<Integer> prueba = new ArrayList<>(Arrays.asList(camino));
                    //System.out.println(prueba);
                    Integer elemAct = act;
                    listaRet.add(a2);
                    //Retrocedemos en el camino para crear la lista a devolver
                    while (elemAct != -1 || act==pos1)
                    {
                        if (listaRet.contains(keys[elemAct]))
                        {
                            elemAct = camino[elemAct];
                        }
                        else
                        {
                            listaRet.add(keys[elemAct]);
                            elemAct = camino[elemAct];
                        }

                    }
                }
            }
            return listaRet;

        }
    }
    /*
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
     */
}