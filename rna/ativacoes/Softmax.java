package rna.ativacoes;

import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação Softmax para uso 
 * dentro da {@code Rede Neural}.
 */
public class Softmax extends FuncaoAtivacao{

   /**
    * Instancia a função de ativação Softmax.
    * <p>
    * A função Softmax  transforma os valores de entrada em probabilidades normalizadas, 
    * permitindo que o neurônio com a maior saída tenha uma probabilidade mais alta.
    * </p>
    */
   public Softmax(){

   }

   @Override
   public void ativar(Neuronio[] neuronios){
      double somaExp = 0;

      for(int i = 0; i < neuronios.length; i++){
         somaExp += Math.exp(neuronios[i].somatorio);
      }

      for(int i = 0; i < neuronios.length; i++){
         neuronios[i].saida = Math.exp(neuronios[i].somatorio) / somaExp;
      }
   }
}
