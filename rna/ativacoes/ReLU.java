package rna.ativacoes;

import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação ReLU para uso dentro 
 * da {@code Rede Neural}.
 */
public class ReLU extends FuncaoAtivacao{

   /**
    * Instancia a função de ativação ReLU.
    */
   public ReLU(){

   }

   @Override
   public void ativar(Neuronio[] neuronios){
      for(int i = 0; i < neuronios.length; i++){
         neuronios[i].saida = (neuronios[i].somatorio > 0) ? neuronios[i].somatorio : 0;
      }
   }

   @Override
   public void derivada(Neuronio[] neuronios){
      for(int i = 0; i < neuronios.length; i++){
         neuronios[i].derivada = (neuronios[i].somatorio > 0) ? 1 : 0;
      }
   }
}
