package exemplos;

import utilitarios.ged.Ged;

public class Teste {
   public static void main(String[] args){
      Ged ged = new Ged();

      int[][] a = new int[2][2];
      int[][] b = new int[2][2];

      for(int i = 0; i < a.length; i++){
         for(int j = 0; j < a[i].length; j++){
            a[i][j] = i*(i+1) + i+j+1;
            b[i][j] = i*(i+1) + i+j+1;
         }
      }

      int[][] r = ged.produtoHadamard(a, b);
      ged.imprimirMatriz(r, "r");

   }
  
}
