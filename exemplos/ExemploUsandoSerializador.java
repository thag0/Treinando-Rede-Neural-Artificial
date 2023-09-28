package exemplos;

import rna.estrutura.RedeNeural;
import rna.serializacao.Serializador;
import utilitarios.ged.Dados;

public class ExemploUsandoSerializador {

   public static void main(String[] args){
      limparConsole();

      double[][] e = {
         {0, 0},
         {0, 1},
         {1, 0},
         {1, 1}
      };

      double[][] s = {
         {0},
         {1},
         {1},
         {0}
      };

      RedeNeural rede = new RedeNeural(2, 2, 1, 1);
      rede.compilar();
      rede.configurarAtivacao(2);
      rede.treinar(e, s, 10_000);

      System.out.println(rede);
      System.out.println("p = " + rede.avaliador.erroMedioQuadrado(e, s));
      Serializador.salvar(rede, "./rede-xor.txt", new Dados());
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
