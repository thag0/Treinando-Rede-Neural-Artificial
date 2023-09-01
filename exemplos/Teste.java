package exemplos;

import utilitarios.ged.Dados;
import utilitarios.ged.Ged;

public class Teste{

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

   public static void main(String[] args){
      limparConsole();
      Ged ged = new Ged();
      
      double[][] a = {
         {1, 2, 3},
         {4, 5, 6},
         {7, 8, 9}
      };

      double[][] b = ged.obterSubLinhas(a, 0, 2);
      ged.imprimirMatriz(b);
   }
}
