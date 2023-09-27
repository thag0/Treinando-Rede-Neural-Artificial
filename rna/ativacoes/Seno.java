package rna.ativacoes;

import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação Seno para uso dentro 
 * da {@code Rede Neural}.
 */
public class Seno extends FuncaoAtivacao{

   /**
    * Instancia a função de ativação Seno.
    */
   public Seno(){

   }

   @Override
   public void ativar(Neuronio[] neuronios){
      for(int i = 0; i < neuronios.length; i++){
         neuronios[i].saida = Math.sin(neuronios[i].somatorio);
      }
   }

   @Override
   public void derivada(Neuronio[] neuronios){
      for(int i = 0; i < neuronios.length; i++){
         neuronios[i].derivada = Math.cos(neuronios[i].somatorio);
      }
   }
   
}
