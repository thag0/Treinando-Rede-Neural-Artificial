package exemplos;

import rna.inicializadores.Inicializador;
import utilitarios.ged.Ged;

public class Teste{
   static Ged ged = new Ged();

   public static void main(String[] args){
      limparConsole();

      double[] array = new double[5];
      double[][] mat = new double[1][];
      mat[0] = array;
      ged.imprimirMatriz(mat, "Array antes");
      
      Inicializador.aleatorio(array, 10);

      mat[0] = array;
      ged.imprimirMatriz(mat, "Array depois");
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
