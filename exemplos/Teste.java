package exemplos;

import utilitarios.ged.Dados;

public class Teste{

   public static void main(String[] args){
      limparConsole();

      int[][] a = {
         {1, 2, 3},
         {4, 5, 6},
         {7, 8, 9}
      };
      
      Dados dados = new Dados(a);
      dados.editarNome("Teste");
      Dados d = dados.coluna(1);
      dados.imprimir();
      d.imprimir();
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
