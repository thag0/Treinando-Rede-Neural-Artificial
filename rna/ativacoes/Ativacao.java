package rna.ativacoes;

import rna.estrutura.Camada;

/**
 * Classe base para a implementação das funções de ativação.
 * <p>
 *    Novas funções de ativações devem sobrescrever os métodos existentes {@code ativar()} e {@code derivada()}.
 * </p>
 */
public abstract class Ativacao{

   /**
    * Calcula o resultado da ativação de acordo com a função configurada
    * <p>
    *    O resultado da derivada de cada neurônio é salvo na propriedade {@code neuronio.saida}.
    * </p>
    * @param neuronios conjunto de neurônios para ativação.
    * @param quantidade quantidade de neurônios que serão ativados, em ordem crescente.
    */
   public void calcular(Camada camada){
      throw new UnsupportedOperationException(
         "Método de atualização da função de ativação não foi implementado."
      );
   }

   /**
    * Calcula o resultado da derivada da função de ativação de acordo com a função configurada
    * <p>
    *    O resultado da derivada de cada neurônio é salvo na propriedade {@code neuronio.derivada}.
    * </p>
    * @param neuronios conjunto de neurônios para ativação.
    * @param quantidade quantidade de neurônios que serão ativados, em ordem crescente.
    */
   public void derivada(Camada camada){
      throw new UnsupportedOperationException(
         "Método de atualização da derivada da função de ativação não foi implementado."
      );
   }
}