import java.util.Scanner;
import java.util.ArrayList;

public class gorilla{
private static int [][] matrix; // Kostnadsmatris
private static String letters; // bokstäverna som finns med
private static ArrayList<ArrayList<String>> qs; // Queries
private static int nbrQueries; // Antal queries

    public void handleData(){
        Scanner scan=null;
        scan=new Scanner(System.in);
        letters=scan.nextLine(); 
        letters=letters.replaceAll(" ", ""); // ta bort mellanrum i letters
        matrix=new int[letters.length()][letters.length()]; // Fyller kostnadsmatrisen
        for(int i=0;i<letters.length();i++){
            for(int j=0;j<letters.length();j++){
                matrix[i][j]=scan.nextInt();
            }
        }
        nbrQueries=scan.nextInt();
        qs=new ArrayList<ArrayList<String>>(); // Lägger in queries i ArrayList
        for(int i=0; i<nbrQueries;i++){
            ArrayList<String> temp=new ArrayList<>();
            for(int j=0;j<2;j++){
            temp.add(scan.next());
            }
            qs.add(temp);
        }

        scan.close();
    }

    public void alignStrings(String x, String y){
        int delta=-4; // Kostnad för att lägga in "*"
         int[][] opt=new int[x.length()+1][y.length()+1];  // Optmatris
        for(int i =0; i<=x.length();i++){
            opt[i][0]=delta*i;
        }
        for(int j=0; j<y.length();j++){
            opt[0][j]=delta*j;
        }

        // Räkna ut alla optimala vägar/byten/ersättningar
        for (int i=1;i<=x.length();i++){  
            for (int j=1;j<=y.length();j++){
                int a=matrix[letters.indexOf(x.charAt(i-1))][letters.indexOf(y.charAt(j-1))]+opt[i-1][j-1];
                int b=delta+opt[i-1][j];
                int c=delta+opt[i][j-1];
                opt[i][j]=Math.max(Math.max(a,b),c); 
            }
        }

        //Bygg stringsen enligt optmatrisen för högsta värde
        StringBuilder sb1=new StringBuilder();
        StringBuilder sb2=new StringBuilder();
        int m=x.length();
        int n=y.length();

        while(m>0 && n>0){
            int optScore = opt[m][n];
			int moveLeft = opt[m-1][n];
			int moveDiag = opt[m-1][n-1];
			if(optScore == moveDiag + matrix[letters.indexOf(x.charAt(m-1))][letters.indexOf(y.charAt(n-1))]) {
				// match eller missmatch
				sb1 = sb1.insert(0, x.charAt(m-1));
				sb2 = sb2.insert(0, y.charAt(n-1));
				n--;
				m--;
			} else if (optScore == moveLeft + delta ) {
				// sätt in "*" i sb2
				sb1 = sb1.insert(0, x.charAt(m-1));
				sb2 = sb2.insert(0, '*');
				m--;
			} else{
				// sätt in "*" i sb2
				sb1 = sb1.insert(0, '*');
				sb2 = sb2.insert(0, y.charAt(n-1));
				n--;
			}
		}

        // Om vi inte är längst "till vänster" i någon av strängarna, fyll med "*"
        while(m > 0) {
			sb1 = sb1.insert(0, x.charAt(m-1));
			sb2 = sb2.insert(0, '*');
			m--;
		}

		while(n > 0) {
			sb1 = sb1.insert(0, '*');
			sb2 = sb2.insert(0, y.charAt(n-1));
			n--;
		}
        String ans1=sb1.toString();
        String ans2=sb2.toString();
        System.out.println(ans1+" "+ans2);
	}
        
        

    public static void main(String[] args) {
        gorilla g=new gorilla();
        g.handleData();
        for(int i=0;i<nbrQueries;i++){
          g.alignStrings(qs.get(i).get(0), qs.get(i).get(1));  
        }
        
    }
}


// m tecken i x, n tecken i y. Behöver fylla i hela tabellen/matrisen. Konstant arbete att fylla i varje "ruta". Tidskomplexitet O(n*m).
// Löst iterativt?. 
// Utan cache så måste man räkna om optimala vägen varje gång ,blir exponentiellt?
// Man kan använda de vid typ felskrivningskoll, och isf är kostnaden avståndet mellan bokstäverna på tangentbordet. 