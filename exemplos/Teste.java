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
      
      // Dados dados = ged.lerCsv("./dados/portas-logicas/xor-cascata.csv");
      Dados dados = ged.lerCsv("./teste.csv");
      dados.editarNome("Teste");

      dados.imprimir();
      System.out.println(dados.info());
   }
}
