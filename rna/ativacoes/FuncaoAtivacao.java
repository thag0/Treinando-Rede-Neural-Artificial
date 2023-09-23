package rna.ativacoes;

import rna.estrutura.Neuronio;

/**
 * Classe genérica para a implementação das funções de ativação.
 * <p>
 *    Novas funções de ativações devem sobrescrever os métodos existentes {@code ativar()} e {@code derivada()}.
 * </p>
 */
public abstract class FuncaoAtivacao{

   /**
    * Calcula o resultado da ativação de acordo com a função configurada
    * <p>
    *    O resultado da derivada de cada neurônio deve ser salvo na propriedade {@code neuronio.saida}.
    * </p>
    * @param neuronios conjunto de neurônios para ativação.
    * @param quantidade quantidade de neurônios que serão ativados, em ordem crescente.
    */
   public void ativar(Neuronio[] neuronios, int quantidade){
      throw new java.lang.UnsupportedOperationException("Método de atualização da função de ativação não foi implementado.");
   }

   /**
    * Calcula o resultado da derivada da função de ativação de acordo com a função configurada
    * <p>
    *    O resultado da derivada de cada neurônio deve ser salvo na propriedade {@code neuronio.derivada}.
    * </p>
    * @param neuronios conjunto de neurônios para ativação.
    * @param quantidade quantidade de neurônios que serão ativados, em ordem crescente.
    */
   public void derivada(Neuronio[] neuronios, int quantidade){
      throw new java.lang.UnsupportedOperationException("Método de atualização da derivada da função de ativação não foi implementado.");
   }
}