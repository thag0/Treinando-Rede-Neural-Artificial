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

   /**
    * Aplica o algoritmo do SGD com momentum para cada peso da rede neural.
    * <p>
    *    O SGD funciona usando a seguinte expressão:
    * </p>
    * <pre>
    *    p[i] -= (M * m[i]) + g[i]
    * </pre>
    * Onde:
    * <p>
    *    {@code p} - peso que será atualizado.
    * </p>
    * <p>
    *    {@code M} - valor de taxa de momentum (ou constante de momentum) 
    *    da rede neural.
    * </p>
    * <p>
    *    {@code m} - valor de momentum da conexão correspondente ao peso
    *    que será atualizado.
    * </p>
    * <p>
    *    {@code g} - gradiente correspondente a conexão do peso que será
    *    atualizado.
    * </p>
    */
   @Override
   public void atualizar(Camada[] redec, double taxaAprendizagem, double momentum){
      Neuronio neuronio;

      //percorrer rede, com exceção da camada de entrada
      for(int i = 1; i < redec.length; i++){
         
         Camada camada = redec[i];
         int nNeuronios = camada.quantidadeNeuroniosSemBias();
         for(int j = 0; j < nNeuronios; j++){
            
            neuronio = camada.neuronio(j);
            if(nesterov){
               for(int k = 0; k < neuronio.pesos.length; k++){
                  double momentumAnterior = neuronio.momentum[k];
                  neuronio.pesos[k] -= momentum * momentumAnterior;
                  neuronio.momentum[k] = (momentum * momentumAnterior) + neuronio.gradiente[k];
                  neuronio.pesos[k] += taxaAprendizagem * neuronio.momentum[k];
               }
                  
            }else{
               for(int k = 0; k < neuronio.pesos.length; k++){
                  neuronio.momentum[k] = (momentum * neuronio.momentum[k]) + neuronio.gradiente[k];
                  neuronio.pesos[k] -= neuronio.momentum[k];
               }
            }
         }
      }
   }

}
