package rna.otimizadores;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

/**
 * Classe que implementa o algoritmo de Descida do Gradiente para otimização de redes neurais.
 * Atualiza diretamente os pesos da rede com base no gradiente.
 */
public class GradientDescent extends Otimizador{

   /**
    * Inicializa uma nova instância de otimizador da Descida do Gradiente.
    */
   public GradientDescent(){

   }

   @Override
   /**
    * @param redec rede neural em formato de array de camadas
    * @param taxaAprendizagem valor de taxa de aprendizagem da rede neural
    * @param momentum valor de taxa de momentum da rede neural
    */
   public void atualizar(Camada[] redec, double taxaAprendizagem, double momentum){
      Neuronio neuronio;

      //percorrer rede, com exceção da camada de entrada
      for(int i = 1; i < redec.length; i++){
         
         Camada camada = redec[i];
         int nNeuronios = camada.quantidadeNeuroniosSemBias();
         for(int j = 0; j < nNeuronios; j++){//percorrer neurônios da camada atual
            
            neuronio = camada.neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
               neuronio.pesos[k] -= neuronio.gradiente[k];
            }
         }
      } 
   }
   
}
