package rna.ativacoes;

import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação Argmax para uso 
 * dentro da {@code Rede Neural}.
 */
public class Argmax extends FuncaoAtivacao{

   /**
    * Intancia uma nova função de ativação Softmax.
    */
   public Argmax(){

   }

   @Override
   public void ativar(Neuronio[] neuronios, int quantidade){
      int indiceMaximo = 0;
      double valorMaximo = neuronios[0].somatorio;

      for(int i = 1; i < quantidade; i++){
         if(neuronios[i].somatorio > valorMaximo){
            indiceMaximo = i;
            valorMaximo = neuronios[i].somatorio;
         }
      }

      for(int i = 0; i < quantidade; i++){
         neuronios[i].saida = (i == indiceMaximo) ? 1 : 0;
      }
   }
}
