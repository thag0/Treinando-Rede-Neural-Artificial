package rna.ativacoes;

import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação Linear para uso dentro 
 * da {@code Rede Neural}.
 */
public class Linear extends FuncaoAtivacao{

   /**
    * Instancia a função de ativação Linear.
    */
   public Linear(){

   }

   @Override
   public void ativar(Neuronio[] neuronios){
      for(int i = 0; i < neuronios.length; i++){
         neuronios[i].saida = neuronios[i].somatorio;
      }
   }

   @Override
   public void derivada(Neuronio[] neuronios){
      for(int i = 0; i < neuronios.length; i++){
         neuronios[i].derivada = 1;
      }
   }
}
