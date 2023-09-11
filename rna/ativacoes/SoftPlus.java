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
   public void ativar(Neuronio[] neuronios, int quantidade){
      for(int i = 0; i < quantidade; i++){
         neuronios[i].saida = Math.log(1 + Math.exp(neuronios[i].somatorio));
      }
   }

   @Override
   public void derivada(Neuronio[] neuronios, int quantidade){
      double exp;
      for(int i = 0; i < quantidade; i++){
         exp = Math.exp(neuronios[i].somatorio);
         neuronios[i].derivada = exp / (1 + exp);
      }
   }
}
