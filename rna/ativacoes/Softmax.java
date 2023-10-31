package rna.ativacoes;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação Softmax para uso 
 * dentro da {@code Rede Neural}.
 */
public class Softmax extends Ativacao{

   /**
    * Instancia a função de ativação Softmax.
    * <p>
    * A função Softmax  transforma os valores de entrada em probabilidades normalizadas, 
    * permitindo que o neurônio com a maior saída tenha uma probabilidade mais alta.
    * </p>
    */
   public Softmax(){

   }

   @Override
   public void calcular(Camada camada){
      double somaExp = 0;

      for(Neuronio neuronio : camada.neuronios()){
         somaExp += Math.exp(neuronio.somatorio);
      }

      for(Neuronio neuronio : camada.neuronios()){
         neuronio.saida = Math.exp(neuronio.somatorio) / somaExp;
      }
   }
}
