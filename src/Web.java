import java.util.ArrayList;
import java.util.Iterator;

public class Web
{
    // ------ Atributos ------
    // los tres atributos que usamos son el nombre de la web, el identificador y la lista de las relaciones que esta tiene con las demás
    private String nombre;
    private Integer id;
    private ArrayList<Web> listaRelaciones;
    private String[] palabras;

    // la constructora que usaremos en la mayoría de los casos
    public Web(String pNombre, Integer pId)
    {
        this.id = pId;
        this.nombre = pNombre;
    }
    // esta constructora se usaría en caso de que tendríamos todos los datos de entrada, pero como por lo general tendremos
    // solo el id y el nombre cuando creemos el objeto web, no la utilizaremos.
    public Web(String pNombre, Integer pId, String[] pLista)
    {
        this.id = pId;
        this.nombre = pNombre;
        this.palabras = pLista;
    }
    public Integer getId()
    {
        return (this.id);
    }
    public String getNombre()
    {
        return (this.nombre);
    }
    public ArrayList<Web> enlacesSalientes()
    {
        return (this.listaRelaciones);
    }
    public void setPalabras(String[] pal) { palabras = pal; }
    public void setId(Integer pId)
    {
        this.id= pId;
    }
    public void editarLista(ArrayList<Web> pLista)
    {
        this.listaRelaciones = pLista;
    }
    public Iterator<Web> getIterator()
    {
        return this.getIterator();
    }
    public void obtenerPalabrasClave()
    {
        String s = nombre.substring(nombre.indexOf(":") + 1, nombre.indexOf("."));
        int i = 0;
        for(int pos=0;pos<s.length();pos++)
        {
            for(int longitud=3;longitud<=10 && pos+longitud<s.length();longitud++) {
                String palabra = s.substring(pos, pos+longitud);
                palabras[i] = palabra;
                i++;
            }
        }

    }

}

