package rna.otimizadores;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

/**
 * Classe que implementa o otimizador Gradiente Descentente Estocástico com momentum.
 */
public class SGD extends Otimizador{

   /**
    * Usar acelerador de Nesterov.
    */
   boolean nesterov;


   /**
    * Inicializa uma nova instância de otimizador Stochastic Gradient Descent (SGD) 
    * usando os valores de hiperparâmetros fornecidos.
    * @param nesterov usar acelerador de nesterov.
    */
   public SGD(boolean nesterov){
      this.nesterov = nesterov;
   }
      
      
   /**
    * Inicializa uma nova instância de otimizador Stochastic Gradient Descent (SGD) .
    * <p>
    *    Os hiperparâmetros do SGD serão inicializados com os valores padrão, que são:
    * </p>
    * {@code nesterov = false}
    */
   public SGD(){
      this(false);
   }


   @Override
   public void atualizar(Camada[] redec, double taxaAprendizagem, double momentum){
      Neuronio neuronio;

      //percorrer rede, com exceção da camada de entrada
      for(int i = 1; i < redec.length; i++){
         Camada camada = redec[i];

         int nNeuronios = camada.quantidadeNeuronios() - (camada.temBias() ? 1 : 0);
         for(int j = 0; j < nNeuronios; j++){//percorrer neurônios da camada atual
            
            neuronio = camada.neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
               if(nesterov){
                  double momentumAnterior = neuronio.momentum[k];
                  neuronio.pesos[k] += momentum * momentumAnterior;
                  neuronio.momentum[k] = momentum * momentumAnterior + neuronio.gradiente[k];
                  neuronio.pesos[k] -= taxaAprendizagem * neuronio.momentum[k];
                  
               }else{
                  neuronio.momentum[k] = (momentum * neuronio.momentum[k]) + neuronio.gradiente[k];
                  neuronio.pesos[k] += neuronio.momentum[k];
               }
            }
         }
      }
   }
   
}
