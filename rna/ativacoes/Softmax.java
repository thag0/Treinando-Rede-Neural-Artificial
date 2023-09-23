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
   public void ativar(Neuronio[] neuronios, int quantidade){
      double somaExp = 0;

      for(int i = 0; i < quantidade; i++){
         somaExp += Math.exp(neuronios[i].somatorio);
      }

      for(int i = 0; i < quantidade; i++){
         neuronios[i].saida = Math.exp(neuronios[i].somatorio) / somaExp;
      }
   }
}
