package rna.ativacoes;

import rna.estrutura.Camada;

/**
 * Implementação da função de ativação Argmax para uso 
 * dentro da {@code Rede Neural}.
 */
public class Argmax extends Ativacao{

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
   public void calcular(Camada camada){
      int indiceMaximo = 0;
      double valorMaximo = camada.neuronio(0).somatorio;

      for(int i = 1; i < camada.neuronios().length; i++){
         if(camada.neuronio(i).somatorio > valorMaximo){
            indiceMaximo = i;
            valorMaximo = camada.neuronio(i).somatorio;
         }
      }

      for(int i = 0; i < camada.neuronios().length; i++){
         camada.neuronio(i).saida = (i == indiceMaximo) ? 1 : 0;
      }
   }
}
