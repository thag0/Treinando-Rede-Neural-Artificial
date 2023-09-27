package rna.ativacoes;

import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação Softmax para uso 
 * dentro da {@code Rede Neural}.
 */
public class Softmax extends FuncaoAtivacao{

   /**
    * Instancia uma nova função de ativação Softmax.
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
