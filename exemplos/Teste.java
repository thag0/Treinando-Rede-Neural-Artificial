package exemplos;

import utilitarios.ged.Dados;
import utilitarios.ged.Ged;

public class Teste{
   public static void main(String[] args) {
      Ged ged = new Ged();
      
      String[][] a = {
         {"1", "5"},
         {"2", "6"},
         {"3", "a"},
         {"4", "8"},
         {"5", "9"},
      };

      Dados dados = new Dados();
      dados.atribuir(a);

      ged.normalizar(dados);
      ged.imprimirDados(dados, "Normalizados");
   }
}
