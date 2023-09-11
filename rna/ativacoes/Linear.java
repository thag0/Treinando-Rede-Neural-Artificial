package rna.ativacoes;

import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação Linear para uso dentro 
 * da {@code Rede Neural}.
 */
public class Linear extends FuncaoAtivacao{

   /**
    * Instancia a função de ativação Linear.
    */
   public Linear(){

   }

   @Override
   public void ativar(Neuronio[] neuronios, int quantidade){
      for(int i = 0; i < quantidade; i++){
         neuronios[i].saida = neuronios[i].somatorio;
      }
   }

   @Override
   public void derivada(Neuronio[] neuronios, int quantidade){
      for(int i = 0; i < quantidade; i++){
         neuronios[i].derivada = 1;
      }
   }
}
