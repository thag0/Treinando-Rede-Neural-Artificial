package rna.ativacoes;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação ReLU para uso dentro 
 * da {@code Rede Neural}.
 */
public class ReLU extends Ativacao{

   /**
    * Instancia a função de ativação ReLU.
    * <p>
    *    A ativação ReLU (Rectified Linear Unit) funciona retornando o próprio valor recebido
    *    caso ele seja maior que um, caso contrário ela retorna zero.
    * </p>
    */
   public ReLU(){

   }

   @Override
   public void calcular(Camada camada){
      for(Neuronio neuronio : camada.neuronios()){
         neuronio.saida = (neuronio.somatorio > 0) ? neuronio.somatorio : 0;
      }
   }

   @Override
   public void derivada(Camada camada){
      for(Neuronio neuronio : camada.neuronios()){
         neuronio.derivada = (neuronio.somatorio > 0) ? 1 : 0;
      }
   }
}
