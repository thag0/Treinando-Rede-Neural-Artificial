package exemplos;

import utilitarios.ged.Dados;
import utilitarios.ged.Ged;

public class Teste{

   public static void main(String[] args){
      limparConsole();
      Ged ged = new Ged();

      // final String caminho = "./dados/datasets-maiores/breast-cancer-wisconsin.csv";
      final String caminho = "./dados/datasets-maiores/iris.csv";
      Dados da = ged.lerCsv(caminho);

      double[][] xord = {
         {0, 0, 0},
         {0, 1, 1},
         {1, 0, 1},
         {1, 1, 0},
      };
      Dados xor = new Dados(xord);
      
      ged.imprimirInicio(xor);
      ged.imprimirInicio(da);
   }


   public static void limparConsole(){
      try{
         String nomeSistema = System.getProperty("os.name");

         if(nomeSistema.contains("Windows")){
         new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            return;
         }else{
            for (int i = 0; i < 100; i++){
               System.out.println();
            }
         }
      }catch(Exception e){
         return;
      }
   }
}
