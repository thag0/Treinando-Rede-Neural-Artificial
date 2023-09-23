package rna.ativacoes;

import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação GELU para uso dentro 
 * da {@code Rede Neural}.
 */
public class GELU extends FuncaoAtivacao{

   /**
    * Intancia uma nova função de ativação GELU.
    */
   public GELU(){

   }

   @Override
   public void ativar(Neuronio[] neuronios, int quantidade){
      double x;
      for(int i = 0; i < quantidade; i++){
         x = neuronios[i].somatorio;
         x = 0.5 * x * (1.0 + Math.tanh(Math.sqrt(2.0 / Math.PI) * (x + 0.044715 * Math.pow(x, 3))));
         neuronios[i].saida = x;
      }
   }

   @Override
   public void derivada(Neuronio[] neuronios, int quantidade){
      double x, cdf;
      for(int i = 0; i < quantidade; i++){
         x = neuronios[i].somatorio;
         cdf = 0.5 * (1.0 + Math.tanh(Math.sqrt(2.0 / Math.PI) * (x + 0.044715 * Math.pow(x, 3))));
         neuronios[i].derivada = 0.5 * (1.0 + cdf + x * Math.exp(-Math.pow(x, 2) / 2.0) / Math.sqrt(2.0 * Math.PI));
      }
   }
}
