package rna.avaliacao.perda;

import rna.estrutura.RedeNeural;

/**
 * Classe base para criação de funções de perda.
 * <p>
 *    Novas funções de perda devem implementar o método {@code calcular()}.
 * </p>
 */
public abstract class Perda{

   /**
    * Calcula a função de perda configurada.
    * @param rede rede neural.
    * @param entrada dados de entrada.
    * @param saida dados de saída relativos a entrada.
    * @return valor de perda de acordo com a função configurada.
    */
   public double calcular(RedeNeural rede, double[][] entrada, double[][] saida){
      throw new UnsupportedOperationException(
         "É necessário implementar a função de perda."
      );
   }

   public double derivada(double previsto, double real){
      throw new UnsupportedOperationException(
         "É necessário implementar a derivada da função de perda."
      );
   }
}
