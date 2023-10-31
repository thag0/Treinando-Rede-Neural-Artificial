package rna.ativacoes;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação Swish para uso 
 * dentro da {@code Rede Neural}.
 */
public class Swish extends Ativacao{

   /**
    * Instancia a função de ativação Swish.
    */
   public Swish(){

   }

   private double sigmoid(double x){
      return (1 / (1 + Math.exp(-x)) );
   }

   @Override
   public void calcular(Camada camada){
      for(Neuronio neuronio : camada.neuronios()){
         neuronio.saida = neuronio.somatorio * sigmoid(neuronio.somatorio);
      }
   }

   @Override
   public void derivada(Camada camada){
      for(Neuronio neuronio : camada.neuronios()){
         double sig = sigmoid(neuronio.somatorio);
         neuronio.derivada = sig + (neuronio.somatorio * sig * (1 - sig));
      }
   }
}
