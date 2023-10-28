package rna.ativacoes;

import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação Tangente Hiperbólica 
 * para uso dentro da {@code Rede Neural}.
 */
public class TanH extends Ativacao{

   /**
    * Instancia a função de ativação Tangente Hiperbólica.
    */
   public TanH(){

   }

   private double tanh(double x){
      //abordagem que se mostrou mais rápida
      return 2 / (1 + Math.exp(-2*x)) -1;
   }

   @Override
   public void ativar(Neuronio[] neuronios){
      for(int i = 0; i < neuronios.length; i++){
         neuronios[i].saida = tanh(neuronios[i].somatorio);
      }
   }

   @Override
   public void derivada(Neuronio[] neuronios){
      //aproveitando o valor pre calculado
      for(int i = 0; i < neuronios.length; i++){
         neuronios[i].derivada = 1 - (neuronios[i].saida * neuronios[i].saida);
      }
   }
}
