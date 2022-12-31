public class Par
{
    private String web;
    private Double pageRank;
    public Par(String s, Double pR)
    {
        web = s;
        pageRank= pR;
    }
    public Double getPageRank()
    {
        return pageRank;
    }
    public String getWeb()
    {
        return web;
    }
}

