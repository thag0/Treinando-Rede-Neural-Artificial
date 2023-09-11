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
   public void ativar(Neuronio[] neuronios, int quantidade){
      for(int i = 0; i < quantidade; i++){
         neuronios[i].saida = Math.sin(neuronios[i].somatorio);
      }
   }

   @Override
   public void derivada(Neuronio[] neuronios, int quantidade){
      for(int i = 0; i < quantidade; i++){
         neuronios[i].derivada = Math.cos(neuronios[i].somatorio);
      }
   }
   
}
