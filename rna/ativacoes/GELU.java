package rna.ativacoes;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação GELU para uso dentro 
 * da {@code Rede Neural}.
 */
public class GELU extends Ativacao{

   /**
    * Intancia uma nova função de ativação GELU.
    */
   public GELU(){

   }

   @Override
   public void calcular(Camada camada){
      double x;
      for(Neuronio neuronio : camada.neuronios()){
         x = neuronio.somatorio;
         x = 0.5 * x * (1.0 + Math.tanh(Math.sqrt(2.0 / Math.PI) * (x + 0.044715 * Math.pow(x, 3))));
         neuronio.saida = x;
      }
   }

   @Override
   public void derivada(Camada camada){
      double x, cdf;
      for(Neuronio neuronio : camada.neuronios()){
         x = neuronio.somatorio;
         cdf = 0.5 * (1.0 + Math.tanh(Math.sqrt(2.0 / Math.PI) * (x + 0.044715 * Math.pow(x, 3))));
         neuronio.derivada = 0.5 * (1.0 + cdf + x * Math.exp(-Math.pow(x, 2) / 2.0) / Math.sqrt(2.0 * Math.PI));
      }
   }
}
