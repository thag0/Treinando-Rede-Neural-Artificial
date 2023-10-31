package rna.ativacoes;

import rna.estrutura.Camada;
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
   public void calcular(Camada camada){
      for(Neuronio neuronio : camada.neuronios()){
         neuronio.saida = tanh(neuronio.somatorio);
      }
   }

   @Override
   public void derivada(Camada camada){
      //aproveitando o valor pre calculado
      for(Neuronio neuronio : camada.neuronios()){
         neuronio.derivada = 1 - (neuronio.saida * neuronio.saida);
      }
   }
}
