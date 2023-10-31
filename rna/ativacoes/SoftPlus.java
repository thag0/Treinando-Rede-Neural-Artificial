package rna.ativacoes;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação SoftPlus para uso 
 * dentro da {@code Rede Neural}.
 */
public class SoftPlus extends Ativacao{

   /**
    * Instancia a função de ativação SoftPlus.
    */
   public SoftPlus(){

   }

   @Override
   public void calcular(Camada camada){
      for(Neuronio neuronio : camada.neuronios()){
         neuronio.saida = Math.log(1 + Math.exp(neuronio.somatorio));
      }
   }

   @Override
   public void derivada(Camada camada){
      for(Neuronio neuronio : camada.neuronios()){
         double exp = Math.exp(neuronio.somatorio);
         neuronio.derivada = exp / (1 + exp);
      }
   }
}
