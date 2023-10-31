package exemplos;

import java.util.Random;

import rna.estrutura.*;
import rna.inicializadores.*;
import rna.otimizadores.*;
import rna.serializacao.Serializador;
import utilitarios.ged.Ged;

@SuppressWarnings("unused")
class Teste{
   static Ged ged = new Ged();

   public static void main(String[] args){
      ged.limparConsole();

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

      RedeNeural rede = new RedeNeural(new int[]{2, 2, 1});
      rede.compilar(new SGD(), new Xavier());
      rede.configurarAtivacao("sigmoid");

      rede.treinar(e, s, 3_000);

      double perda = rede.avaliador.erroMedioQuadrado(e, s);
      System.out.println(perda);

      for(int i = 0; i < 2; i++){
         for(int j = 0; j < 2; j++){
            double[] entrada = new double[]{i, j};
            rede.calcularSaida(entrada);
            System.out.println(i + " - " + j + " = " + rede.obterSaidas()[0]);
         }
      }
   }
}