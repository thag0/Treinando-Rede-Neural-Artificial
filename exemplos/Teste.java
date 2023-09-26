package exemplos;

import java.util.ArrayList;
import java.util.List;

import render.JanelaRede;
import rna.estrutura.Camada;
import rna.estrutura.Neuronio;
import rna.estrutura.RedeNeural;
import rna.inicializadores.Aleatorio;
import rna.inicializadores.Inicializador;
import rna.otimizadores.SGD;
import rna.serializacao.Serializador;
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
      SGD sgd = new SGD();
      rede.compilar(sgd);
      rede.configurarFuncaoAtivacao(2);

      JanelaRede jr = new JanelaRede();
      jr.desenhar(rede);
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
