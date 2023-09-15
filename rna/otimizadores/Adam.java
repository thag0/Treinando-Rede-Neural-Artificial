package rna.otimizadores;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

/**
 * Implementação do algoritmo de otimização Adam.
 * <p>
 *    O algoritmo ajusta os pesos da rede neural usando o gradiente descendente 
 *    com momento e a estimativa adaptativa de momentos de primeira e segunda ordem.
 * </p>
 * <p>
 * 	Os hiperparâmetros do Adam podem ser ajustados para controlar o 
 * 	comportamento do otimizador durante o treinamento.
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
   long interacoes = 0;
 
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

   /**
    * Aplica o algoritmo do Adam para cada peso da rede neural.
    * <p>
    *    O Adam funciona usando a seguinte expressão:
    * </p>
    * <pre>
    *    p[i] -= (tA * mc) / ((√ m2c) + eps)
    * </pre>
    * Onde:
    * <p>
    *    {@code p} - peso que será atualizado.
    * </p>
    * <p>
    *    {@code tA} - valor de taxa de aprendizagem (learning rate).
    * </p>
    * <p>
    *    {@code mc} - valor de momentum corrigido.
    * </p>
    * <p>
    *    {@code m2c} - valor de momentum de segunda ordem corrigido.
    * </p>
    * Os valores de momentum corrigido (mc) e momentum de segunda ordem
    * corrigido (m2c) se dão por:
    * <pre>
    *    mc = m[i] / (1 - beta1ⁱ)
    * </pre>
    * <pre>
    *    m2c = m2[i] / (1 - beta2ⁱ)
    * </pre>
    * Onde:
    * <p>
    *    {@code m} - valor de momentum correspondete a conexão do peso que está
    *     sendo atualizado.
    * </p>
    * <p>
    *    {@code m2} - valor de momentum de segunda ordem correspondete a conexão 
    *    do peso que está sendo atualizado.
    * </p>
    * <p>
    *    {@code i} - contador de interações (épocas passadas em que o otimizador 
    *    foi usado) .
    * </p>
    */
   @Override
   public void atualizar(Camada[] redec, double taxaAprendizagem, double momentum){
      double mc, m2c, g, divB1, divB2;
      Neuronio neuronio;

      //percorrer rede, com exceção da camada de entrada
      interacoes++;
      for(int i = 1; i < redec.length; i++){
         
         Camada camada = redec[i];
         int nNeuronios = camada.quantidadeNeuroniosSemBias();
         for(int j = 0; j < nNeuronios; j++){
            
            divB1 = (1 - Math.pow(beta1, interacoes));
            divB2 = (1 - Math.pow(beta2, interacoes));
            
            neuronio = camada.neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){
               g = neuronio.gradiente[k];
               
               neuronio.momentum[k] =  (beta1 * neuronio.momentum[k])  + ((1 - beta1) * g);
               neuronio.momentum2[k] = (beta2 * neuronio.momentum2[k]) + ((1 - beta2) * g * g);
               
               mc = neuronio.momentum[k] / divB1;
               m2c = neuronio.momentum2[k] / divB2;

               neuronio.pesos[k] -= (taxaAprendizagem * mc) / (Math.sqrt(m2c) + epsilon);
            }
            
         }
      }
   }

}
