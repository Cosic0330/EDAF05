public class node {
    private String word;
    private boolean visited=false;


    public node(String w){
        this.word=w;
    }

    public String getWord(){
        return word;
    }

    public boolean isVisited(){
        return visited;
    }

    public void setVisited(){
        visited=true;
    }
}