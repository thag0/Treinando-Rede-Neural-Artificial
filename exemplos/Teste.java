package exemplos;

import utilitarios.ged.Dados;
import utilitarios.ged.Ged;

class Teste{
   public static void main(String[] args) {
      Ged ged = new Ged();

      double[][] a = {
         {0},
         {1},
         {2},
         {3},
      };

      Dados dados = new Dados(a);

      ged.categorizar(dados, 0);
      ged.imprimirDados(dados);
   }
}