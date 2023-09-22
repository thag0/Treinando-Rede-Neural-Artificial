package exemplos;

import java.util.ArrayList;
import java.util.List;

import rna.inicializadores.Inicializador;
import utilitarios.ged.Ged;

@SuppressWarnings("unused")
public class Teste{
   static Ged ged = new Ged();

   public static void main(String[] args){
      limparConsole();

      int[][] mat = {
         {1},
         {2},
         {3},
         {4},
         {5},
         {6},
      };

      ged.embaralharDados(mat);
      ged.imprimirMatriz(mat);
   }

   static void limparConsole(){
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
