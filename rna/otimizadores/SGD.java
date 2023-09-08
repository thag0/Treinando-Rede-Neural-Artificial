package rna.otimizadores;

import java.util.ArrayList;

import rna.Camada;
import rna.Neuronio;

/**
 * Classe que implementa o otimizador Gradiente Descentente Estocástico.
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
   public void atualizar(ArrayList<Camada> redec, double taxaAprendizagem, double momentum){
      Camada camada;
      Neuronio neuronio;

      for(int i = 1; i < redec.size(); i++){//percorrer rede 
         
         camada = redec.get(i);
         int nNeuronios = camada.obterQuantidadeNeuronios() - ((camada.temBias()) ? 1 : 0);
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
