import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;
import java.lang.StringBuilder;

public class wl {
    private static int nbrWords;
    private int nbrQueries;
    private static Integer[] queries;
    private static ArrayList<ArrayList<Integer>> neighbours;
    private static ArrayList<String> words;
    

public void handleData(){
    words=new ArrayList<String>();
   
    neighbours=new ArrayList<ArrayList<Integer>>();
    Scanner scan=null;
    scan=new Scanner(System.in);
    nbrWords=scan.nextInt();
    nbrQueries=scan.nextInt(); 
    queries=new Integer[nbrQueries*2];
    for(int i=0;i<nbrWords;i++){
        String w=scan.next();
        words.add(w);
        ArrayList<Integer> list=new ArrayList<>();
        neighbours.add(list);
    }
    for(int i=0;i<nbrQueries*2;i++){
        String w1=scan.next();
        String w2=scan.next();
        queries[i]=words.indexOf(w1);
        queries[i+1]=words.indexOf(w2);
        i++;
        
        
    }

    scan.close();
}

public void findRoads(){
int counter;
for(int i=0;i<nbrWords;i++){
    String w=words.get(i);
    char[] chars=w.toCharArray();
    char[] lastfour=Arrays.copyOfRange(chars, 1, chars.length);
    int nbr=-1;
    for(int q=0;q<nbrWords;q++){
        counter=0;
        String w2=words.get(q);
        nbr++;
        for(int z=0;z<lastfour.length;z++){
        for(int p=0;p<w2.length();p++){
            if(w2.charAt(p)==(lastfour[z])){
                    counter++;
                    StringBuilder sb=new StringBuilder();
                    sb.append(w2);
                    sb.deleteCharAt(p);
                    w2=sb.toString();
                    break;
                }
            }

        } 
        if(counter>3){
        neighbours.get(i).add(nbr);
    }
   

    }

}
}

 public boolean BFS(int src,int dest,int v,Integer pred[],int dist[]){
    if(src==dest){
        dist[dest]=0;
        return true;
    }
     LinkedList<Integer> queue=new LinkedList<>();
     boolean visited[]=new boolean[nbrWords];
     for(int i=0; i<nbrWords;i++){
         visited[i]=false;
         dist[i]=Integer.MAX_VALUE;
         pred[i]=-1;
     }
     

     visited[src]=true;
     dist[src]=0;
     queue.add(src);

     while(!queue.isEmpty()){
         int u=queue.remove();
         for (int i = 0; i < neighbours.get(u).size(); i++) {
            if (visited[neighbours.get(u).get(i)] == false) {
                visited[neighbours.get(u).get(i)] = true;
                dist[neighbours.get(u).get(i)] = dist[u] + 1;
                pred[neighbours.get(u).get(i)] = u;
                queue.add(neighbours.get(u).get(i));
                if (neighbours.get(u).get(i) == dest){
                    return true;
                }
            }
        }
    }
        return false;
     
     
  
} 
            

 

public static void main(String[] args) {
    wl wordLadder = new wl();
    wordLadder.handleData();
    wordLadder.findRoads();
    Integer[] pred=new Integer[nbrWords];
    int[] dist=new int[nbrWords];
    for(int i=0;i<queries.length;i++){
        if(wordLadder.BFS(queries[i], queries[i+1], nbrWords, pred, dist)==false){
            System.out.println("Impossible");
        }else{
            System.out.println(dist[queries[i+1]]);
        }
        i++;
    }
    
}





}


// The graph is represented with an ArrayList<ArrayList<Integer>> where each index represents a word in the graph and the
// list in this index is a list of the specific words neighbours.  
// In BFS we use a LinkedList to store the queue
// To backtrack we would have to use the pred vector and trace our steps. 
// DFS does not neccessarily find the shortest path in an unweighted graph unless tweaked.
// DFS find a path, and since it marks everything it visited as visited you would have to unmark these to find another path and so on. 
// Application GPS (BFS)