package exemplos;

import utilitarios.ged.Dados;
import utilitarios.ged.Ged;

public class Teste{
   public static void main(String[] args){
      Ged ged = new Ged();

      Dados xor = ged.lerCsv("./dados/portas-logicas/xor.csv");
      ged.imprimirDados(xor);
   }
  
}
