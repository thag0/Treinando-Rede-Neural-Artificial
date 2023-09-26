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
   public void ativar(Neuronio[] neuronios, int quantidade){
      for(int i = 0; i < quantidade; i++){
         neuronios[i].saida = sigmoid(neuronios[i].somatorio);
      }
   }

   @Override
   public void derivada(Neuronio[] neuronios, int quantidade){
      for(int i = 0; i < quantidade; i++){
         double sig = neuronios[i].saida;//sigmoid já foi calculada
         neuronios[i].derivada = sig * (1 - sig);
      }
   }
}
