package rna.ativacoes;

import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação Argmax para uso 
 * dentro da {@code Rede Neural}.
 */
public class Argmax extends FuncaoAtivacao{

   /**
    * Intancia uma nova função de ativação Softmax.
    * <p>
    *    A função argmax encontra o maior valor de saída dentre os neurônios
    *    da camada e converte ele para 1, as demais saídas dos neurônios serão
    *    convertidas para zero, fazendo a camada classificar uma única saída com
    *    base no maior valor.
    * </p>
    */
   public Argmax(){

   }

   @Override
   public void ativar(Neuronio[] neuronios){
      int indiceMaximo = 0;
      double valorMaximo = neuronios[0].somatorio;

      for(int i = 1; i < neuronios.length; i++){
         if(neuronios[i].somatorio > valorMaximo){
            indiceMaximo = i;
            valorMaximo = neuronios[i].somatorio;
         }
      }

      for(int i = 0; i < neuronios.length; i++){
         neuronios[i].saida = (i == indiceMaximo) ? 1 : 0;
      }
   }
}
