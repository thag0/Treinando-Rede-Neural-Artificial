package rna.ativacoes;

import java.io.Serializable;

/**
 * Classe genérica para a implementação das funções de ativação.
 * <p>
 *    Novas funções de ativações devem sobrescrever os métodos existentes {@code ativar()} e {@code derivada()}.
 * </p>
 */
public abstract class FuncaoAtivacao implements Serializable{
   

   /**
    * Calcula o resultado da ativação de acordo com a função configurada
    *
    * @param x O valor de entrada.
    * @return O resultado da ativação.
    */
   public double ativar(double x){
      throw new java.lang.UnsupportedOperationException("Método de atualização da função de ativação não foi implementado.");
   }


   /**
    * Calcula o resultado da derivada da função de ativação de acordo com a função configurada
    *
    * @param x O valor de entrada.
    * @return O resultado da derivada.
    */
   public double derivada(double x){
      throw new java.lang.UnsupportedOperationException("Método de atualização da derivada da função de ativação não foi implementado.");
   }
}