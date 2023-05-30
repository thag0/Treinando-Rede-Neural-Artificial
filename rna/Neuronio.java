package rna;

import java.util.Random;

public class Neuronio{
   public double[] pesos;
   public double saida;
   public double erro;//implementar backpropagation

   public int qtdLigacoes;
   private Random random = new Random();

   public Neuronio(int qtdLigacoes){
      this.qtdLigacoes = qtdLigacoes;

      pesos = new double[qtdLigacoes];
      for(int i = 0; i < qtdLigacoes; i++){
         pesos[i] = random.nextDouble(-100, 100);//-100 a 100
      }

      this.saida = 0;
   }
}
