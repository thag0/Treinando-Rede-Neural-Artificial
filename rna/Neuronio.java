package rna;

import java.io.Serializable;
import java.util.Random;

/**
 * Representa um neurônio individual dentro da estrutura da rede neural.
 * Cada neurônio possui um conjunto de pesos para suas conexões com os neurônios da próxima camada,
 * além de valores de entrada e saída.
 */
public class Neuronio implements Serializable{
   public double entrada;
   public double[] pesos;
   public double saida;
   public double erro;//implementar backpropagation

   private Random random = new Random();

   /**
    * Instancia um neurônio individual da rede, com pesos aleatórios para cada ligação
    * @param ligacoes quantidade de ligações, deve estar relacionada com a quatidade de neurônios da próxima camada
    * @param alcancePeso valor de alcance em que o peso aleatório será gerado, deve ser um valor positivo e diferente de zero
    * @throws IllegalArgumentException se o valor de alcance dos pesos for menor ou igual a zero.
    */
   public Neuronio(int ligacoes, double alcancePeso){
      if(alcancePeso <= 0) throw new IllegalArgumentException("O valor de alcance do peso deve ser positivo e diferente de zero.");

      this.entrada = 0;
      
      pesos = new double[ligacoes];
      for(int i = 0; i < pesos.length; i++){
         pesos[i] = random.nextDouble(-alcancePeso, alcancePeso);
      }

      this.saida = 1;// considerar que pode ter bias aplicado ao modelo
      this.erro = 0;
   }
}
