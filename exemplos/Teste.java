package exemplos;

import java.util.ArrayList;
import java.util.List;

import rna.estrutura.RedeNeural;
import rna.inicializadores.Inicializador;
import utilitarios.ged.Ged;

@SuppressWarnings("unused")
public class Teste{
   static Ged ged = new Ged();

   public static void main(String[] args){
      limparConsole();

      double[][] in = {
         {0, 0},
         {0, 1},
         {1, 0},
         {1, 1}
      };
      double[][] out = {
         {0},
         {1},
         {1},
         {0}
      };

      int[] arq = {2, 2, 1};
      RedeNeural rede = new RedeNeural(arq);
      rede.compilar();
      rede.configurarFuncaoAtivacao(2);

      rede.treinar(in, out, 10_000);

      System.out.println(rede);
      System.out.println("c = " + rede.avaliador.erroMedioQuadrado(in, out));
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
