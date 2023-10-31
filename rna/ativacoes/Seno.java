package rna.ativacoes;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação Seno para uso dentro 
 * da {@code Rede Neural}.
 */
public class Seno extends Ativacao{

   /**
    * Instancia a função de ativação Seno.
    */
   public Seno(){

   }

   @Override
   public void calcular(Camada camada){
      for(Neuronio neuronio : camada.neuronios()){
         neuronio.saida = Math.sin(neuronio.somatorio);
      }
   }

   @Override
   public void derivada(Camada camada){
      for(Neuronio neuronio : camada.neuronios()){
         neuronio.derivada = Math.cos(neuronio.somatorio);
      }
   }
   
}
