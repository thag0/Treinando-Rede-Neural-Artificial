package rna.otimizadores;

import java.util.ArrayList;

import rna.Camada;
import rna.Neuronio;

/**
 * Implementação do algoritmo de otimização Adam.
 * <p>
 *    O algoritmo ajusta os pesos da rede neural usando o gradiente descendente 
 *    com momento e a estimativa adaptativa de momentos de primeira e segunda ordem.
 * </p>
 */
public class Adam extends Otimizador{

   /**
    * Usado para evitar divisão por zero.
    */
   private double epsilon;

   /**
    * decaimento do momentum.
    */
   private double beta1;

   /**
    * decaimento do momentum de segunda ordem.
    */
   private double beta2;
   
   /**
    * Contador de iterações.
    */
   long interacoes = 1;

   /**
    * Inicializa uma nova instância de otimizador Adam usando os 
    * valores de hiperparâmetros fornecidos.
    * @param epsilon usado para evitar a divisão por zero.
    * @param beta1 decaimento do momento de primeira ordem.
    * @param beta2 decaimento da segunda ordem.
    */
   public Adam(double epsilon, double beta1, double beta2){
      this.epsilon = epsilon;
      this.beta1 = beta1;
      this.beta2 = beta2;
   }


   /**
    * Inicializa uma nova instância de otimizador Adam.
    * <p>
    *    Os hiperparâmetros do Adam serão inicializados com os valores 
    *    padrão, que são:
    * </p>
    * {@code epsilon = 1e-7}
    * <p>
    *    {@code beta1 = 0.9}
    * </p>
    * <p>
    *    {@code beta2 = 0.999}
    * </p>
    */
   public Adam(){
      this(1e-7, 0.9, 0.999);
   }


   @Override
   public void atualizar(ArrayList<Camada> redec, double taxaAprendizagem, double momentum){
      double momentumCorrigido, segundaOrdemCorrigida;

      Neuronio neuronio;
      //percorrer rede, com exceção da camada de entrada
      for(Camada camada : redec.subList(1, redec.size())){

         int nNeuronios = camada.quantidadeNeuronios() - (camada.temBias() ? 1 : 0);
         for(int j = 0; j < nNeuronios; j++){//percorrer neurônios da camada atual
            
            double interBeta1 = (1 - Math.pow(beta1, interacoes));
            double interBeta2 = (1 - Math.pow(beta2, interacoes));
            
            neuronio = camada.neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
               neuronio.momentum[k] = (beta1 * neuronio.momentum[k]) + ((1 - beta1) * neuronio.gradiente[k]);
               neuronio.acumuladorSegundaOrdem[k] = (beta2 * neuronio.acumuladorSegundaOrdem[k]) + ((1 - beta2) * neuronio.gradiente[k] * neuronio.gradiente[k]);
               
               momentumCorrigido = neuronio.momentum[k] / interBeta1;
               segundaOrdemCorrigida = neuronio.acumuladorSegundaOrdem[k] / interBeta2;
               neuronio.pesos[k] += (taxaAprendizagem * momentumCorrigido) / (Math.sqrt(segundaOrdemCorrigida) + epsilon);
               
               interacoes++;
            }
         }
      }
   }

}
