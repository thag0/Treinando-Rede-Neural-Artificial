package rna.ativacoes;

import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação Sigmóide para uso 
 * dentro da {@code Rede Neural}.
 */
public class Sigmoid extends FuncaoAtivacao{

   /**
    * Instancia a função de ativação Sigmoid.
    */
   public Sigmoid(){

   }

   private double sigmoid(double x){
      return 1 / (1 + Math.exp(-x));
   }

   @Override
   public void ativar(Neuronio[] neuronios){
      for(int i = 0; i < neuronios.length; i++){
         neuronios[i].saida = sigmoid(neuronios[i].somatorio);
      }
   }

   @Override
   public void derivada(Neuronio[] neuronios){
      for(int i = 0; i < neuronios.length; i++){
         double sig = neuronios[i].saida;
         neuronios[i].derivada = sig * (1 - sig);
      }
   }
}
