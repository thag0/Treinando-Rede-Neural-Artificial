package rna.otimizadores;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

/**
 * Implementa uma versão do algoritmo AdaGrad (Adaptive Gradient Algorithm).
 * O algoritmo otimiza o processo de aprendizado adaptando a taxa de aprendizagem 
 * de cada parâmetro com base no histórico de atualizações 
 * anteriores
 */
public class AdaGrad extends Otimizador{

   /**
    * Usado para evitar divisão por zero.
    */
   private double epsilon;

   /**
    * Inicializa uma nova instância de otimizador AdaGrad usando os valores 
    * de hiperparâmetros fornecidos.
    * @param epsilon usado para evitar a divisão por zero.
    */
   public AdaGrad(double epsilon){
      this.epsilon = epsilon;
   }

   /**
    * Inicializa uma nova instância de otimizador AdaGrad.
    * <p>
    *    Os hiperparâmetros do AdaGrad serão inicializados com os valores padrão, que são:
    * </p>
    * {@code epsilon = 1e-8}
    */
   public AdaGrad(){
      this(1e-8);
   }

   /**
    * Aplica o algoritmo do AdaGrad para cada peso da rede neural.
    * <p>
    *    O Adagrad funciona usando a seguinte expressão:
    * </p>
    * <pre>
    *    p[i] -= (tA * g[i]) / (√ ac[i] + eps)
    * </pre>
    * Onde:
    * <p>
    *    {@code p} - peso que será atualizado.
    * </p>
    * <p>
    *    {@code tA} - valor de taxa de aprendizagem (learning rate).
    * </p>
    * <p>
    *    {@code g} - gradiente correspondente a conexão do peso que será
    *    atualizado.
    * </p>
    * <p>
    *    {@code ac} - acumulador de gradiente correspondente a conexão
    *    do peso que será atualizado.
    * </p>
    * <p>
    *    {@code eps} - um valor pequeno para evitar divizões por zero.
    * </p>
    */
   @Override
   public void atualizar(Camada[] redec, double taxaAprendizagem, double momentum){  
      double g;
      Neuronio neuronio;

      //percorrer rede, com exceção da camada de entrada
      for(int i = 1; i < redec.length; i++){
         Camada camada = redec[i];
  
         int nNeuronios = camada.quantidadeNeuroniosSemBias();
         for(int j = 0; j < nNeuronios; j++){
  
            neuronio = camada.neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){
               g = neuronio.gradiente[k];
               neuronio.acumuladorGradiente[k] += (g * g);
               neuronio.pesos[k] -= (taxaAprendizagem * g) / Math.sqrt(neuronio.acumuladorGradiente[k] + epsilon);
            }
         }
      }
   }
   
}
