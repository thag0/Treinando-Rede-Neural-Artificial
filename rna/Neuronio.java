package rna;

import java.io.Serializable;
import java.util.Random;

public class Neuronio implements Serializable{
   public double saida;
   public double entrada;
   public double[] pesos;
   public double erro;//implementar backpropagation

   public int qtdLigacoes;
   private Random random = new Random();

   /**
    * Instancia um neurônio individual da rede, com pesos aleatórios para cada ligação
    * @param qtdLigacoes quantidade de ligações, deve estar relacionada com a quatidade de neurônios da próxima camada
    * @param alcancePeso valor de alcance em que o peso aleatório será gerado, deve ser um valor positivo e diferente de zero
    */
   public Neuronio(int qtdLigacoes, double alcancePeso){
      if(alcancePeso <= 0) throw new IllegalArgumentException("O valor de alcance do peso deve ser positivo e diferente de zero.");

      this.qtdLigacoes = qtdLigacoes;

      pesos = new double[qtdLigacoes];
      for(int i = 0; i < pesos.length; i++){
         pesos[i] = random.nextDouble((-1 * alcancePeso), alcancePeso);
      }

      this.entrada = 0;
      this.saida = 1;// considerar que pode ter bias aplicado ao modelo
   }
}
