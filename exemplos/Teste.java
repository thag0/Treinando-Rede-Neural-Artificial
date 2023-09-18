package exemplos;

import java.util.concurrent.TimeUnit;

import rna.ativacoes.Sigmoid;
import rna.estrutura.RedeNeural;
import rna.otimizadores.SGD;

public class Teste{
   public static void main(String[] args) {
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

      int[] arq = {2, 2, 1};
      RedeNeural rede = new RedeNeural(arq);
      rede.compilar();
      rede.configurarFuncaoAtivacao(new Sigmoid());
      rede.configurarOtimizador(new SGD());
      System.out.println(rede.info());


      long t1 = System.nanoTime();
      rede.treinar(e, s, 4000);
      long t2 = System.nanoTime();
      long t3 = TimeUnit.NANOSECONDS.toSeconds(t2 - t1);
      long segundos = t3 % 60;
      System.out.println("Terminado em " + segundos + "s");
      System.out.println("c = " + rede.avaliador.erroMedioQuadrado(e, s));

      System.out.println(rede);
   }
}
