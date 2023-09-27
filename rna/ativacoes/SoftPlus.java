package rna.ativacoes;

import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação SoftPlus para uso 
 * dentro da {@code Rede Neural}.
 */
public class SoftPlus extends FuncaoAtivacao{

   /**
    * Instancia a função de ativação SoftPlus.
    */
   public SoftPlus(){

   }

   @Override
   public void ativar(Neuronio[] neuronios){
      for(int i = 0; i < neuronios.length; i++){
         neuronios[i].saida = Math.log(1 + Math.exp(neuronios[i].somatorio));
      }
   }

   @Override
   public void derivada(Neuronio[] neuronios){
      for(int i = 0; i < neuronios.length; i++){
         double exp = Math.exp(neuronios[i].somatorio);
         neuronios[i].derivada = exp / (1 + exp);
      }
   }
}
