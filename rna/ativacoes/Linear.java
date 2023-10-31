package rna.ativacoes;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação Linear para uso dentro 
 * da {@code Rede Neural}.
 */
public class Linear extends Ativacao{

   /**
    * Instancia a função de ativação Linear.
    */
   public Linear(){

   }

   @Override
   public void calcular(Camada camada){
      for(Neuronio neuronio : camada.neuronios()){
         neuronio.saida = neuronio.somatorio;
      }
   }

   @Override
   public void derivada(Camada camada){
      for(Neuronio neuronio : camada.neuronios()){
         neuronio.derivada = 1;
      }
   }
}
