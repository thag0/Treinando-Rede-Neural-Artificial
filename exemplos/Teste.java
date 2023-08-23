package exemplos;

import utilitarios.ged.Ged;

public class Teste {
   public static void main(String[] args) {
      Ged ged = new Ged();

      double[][] mat = new double[3][3];
      ged.matrizIdentidade(mat);
      ged.imprimirMatriz(mat);
   }
}
