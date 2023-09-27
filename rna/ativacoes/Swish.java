package rna.ativacoes;

import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação Swish para uso 
 * dentro da {@code Rede Neural}.
 */
public class Swish extends FuncaoAtivacao{

   /**
    * Instancia a função de ativação Swish.
    */
   public Swish(){

   }

   private double sigmoid(double x){
      return (1 / (1 + Math.exp(-x)) );
   }

   @Override
   public void ativar(Neuronio[] neuronios){
      for(int i = 0; i < neuronios.length; i++){
         neuronios[i].saida = neuronios[i].somatorio * sigmoid(neuronios[i].somatorio);
      }
   }

   @Override
   public void derivada(Neuronio[] neuronios){
      for(int i = 0; i < neuronios.length; i++){
         double sig = sigmoid(neuronios[i].somatorio);
         neuronios[i].derivada = sig + (neuronios[i].somatorio * sig * (1 - sig));
      }
   }
}
