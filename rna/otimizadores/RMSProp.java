package rna.otimizadores;

import java.util.ArrayList;

import rna.Camada;
import rna.Neuronio;

/**
 * Implementa o treino da rede neural usando o algoritmo RMSProp (Root Mean Square Propagation).
 *
 * Ele é uma adaptação do Gradiente Descendente Estocástico (SGD) que ajuda a lidar com a
 * oscilação do gradiente, permitindo que a taxa de aprendizado seja adaptada para cada parâmetro 
 * individualmente.
 */
public class RMSProp extends Otimizador{

   /**
    * Usado para evitar divisão por zero.
    */
   private double epsilon = 1e-8;

   
   /**
    * fator de decaimento do RMSprop.
    */
   double beta = 0.9;


   /**
    * Inicializa uma nova instância de otimizador RMSProp.
    * <p>
    *    Os hiperparâmetros do RMSProp serão inicializados com os valores padrão, que são:
    * </p>
    * {@code epsilon = 1e-8}
    * <p>
    *    {@code beta = 0.9}
    * </p>
    */
   public RMSProp(){
      this(1e-8, 0.9);
   }


   /**
    * Inicializa uma nova instância de otimizador RMSProp usando os valores 
    * de hiperparâmetros fornecidos.
    * @param epsilon usado para evitar a divisão por zero.
    * @param beta fator de decaimento do RMSProp.
    */
   public RMSProp(double epsilon, double beta){
      this.epsilon = epsilon;
      this.beta = beta;
   }


   @Override
    public void atualizar(ArrayList<Camada> redec, double taxaAprendizagem, double momentum){
      Camada camada;
      Neuronio neuronio;

      for(int i = 1; i < redec.size(); i++){//percorrer rede

         camada = redec.get(i);
         int nNeuronios = camada.obterQuantidadeNeuronios() - ((camada.temBias()) ? 1 : 0);
         for(int j = 0; j < nNeuronios; j++){//percorrer neurônios da camada atual

            neuronio = camada.neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
               neuronio.acumuladorGradiente[k] = (beta * neuronio.acumuladorGradiente[k]) + ((1 - beta) * neuronio.gradiente[k] * neuronio.gradiente[k]);
               neuronio.pesos[k] += (taxaAprendizagem / Math.sqrt(neuronio.acumuladorGradiente[k] + epsilon)) * neuronio.gradiente[k];
            }
         }
      }
   }

}
