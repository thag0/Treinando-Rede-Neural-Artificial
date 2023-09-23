package rna.ativacoes;

import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação Tangente Hiperbólica 
 * para uso dentro da {@code Rede Neural}.
 */
public class TanH extends FuncaoAtivacao{

   /**
    * Instancia a função de ativação Tangente Hiperbólica.
    */
   public TanH(){

   }

   @Override
   public void ativar(Neuronio[] neuronios, int quantidade){
      for(int i = 0; i < quantidade; i++){
         neuronios[i].saida = Math.tanh(neuronios[i].somatorio);
      }
   }

   @Override
   public void derivada(Neuronio[] neuronios, int quantidade){
      double t;
      for(int i = 0; i < quantidade; i++){
         t = Math.tanh(neuronios[i].somatorio);
         neuronios[i].derivada = 1 - (t * t);
      }
   }
}
