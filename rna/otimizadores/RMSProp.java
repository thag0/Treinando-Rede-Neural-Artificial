package rna.otimizadores;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

/**
 * Implementa o treino da rede neural usando o algoritmo RMSProp (Root Mean Square Propagation).
 *
 * Ele é uma adaptação do Gradiente Descendente Estocástico (SGD) que ajuda a lidar com a
 * oscilação do gradiente, permitindo que a taxa de aprendizado seja adaptada para cada parâmetro 
 * individualmente.
 * <p>
 * 	Os hiperparâmetros do RMSProp podem ser ajustados para controlar 
 *    o comportamento do otimizador durante o treinamento.
 * </p
 */
public class RMSProp extends Otimizador{

   /**
    * Valor de taxa de aprendizagem do otimizador.
    */
   private double taxaAprendizagem;

   /**
    * Usado para evitar divisão por zero.
    */
   private double epsilon;
  
   /**
    * fator de decaimento do RMSprop.
    */
   private double rho;

   /**
    * Acumuladores
    */
   private double[] ac;

   /**
    * Inicializa uma nova instância de otimizador <strong> RMSProp </strong> 
    * usando os valores de hiperparâmetros fornecidos.
    * @param tA valor de taxa de aprendizagem.
    * @param rho fator de decaimento do RMSProp.
    * @param epsilon usado para evitar a divisão por zero.
    */
   public RMSProp(double tA, double rho, double epsilon){
      this.taxaAprendizagem = tA;
      this.rho = rho;
      this.epsilon = epsilon;
   }

   /**
    * Inicializa uma nova instância de otimizador <strong> RMSProp </strong>.
    * <p>
    *    Os hiperparâmetros do RMSProp serão inicializados com os valores padrão, que são:
    * </p>
    * <p>
    *    {@code taxaAprendizagem = 0.001}
    * </p>
    * <p>
    *    {@code rho = 0.99}
    * </p>
    * <p>
    *    {@code epsilon = 1e-7}
    * </p>
    */
   public RMSProp(){
      this(0.001, 0.99, 1e-7);
   }

   @Override
   public void inicializar(int parametros){
      this.ac = new double[parametros];

      for(int i = 0; i < this.ac.length; i++){
         this.ac[i] = 0.1;
      }
   }

   /**
    * Aplica o algoritmo do RMSProp para cada peso da rede neural.
    * <p>
    *    O Nadam funciona usando a seguinte expressão:
    * </p>
    * <pre>
    *    p[i] -= tA / ((√ ac[i]) + eps) * g[i]
    * </pre>
    * Onde:
    * <p>
    *    {@code p} - peso que será atualizado.
    * </p>
    * <p>
    *    {@code tA} - valor de taxa de aprendizagem (learning rate).
    * </p>
    * <p>
    *    {@code ac} - acumulador de gradiente correspondente a conexão do 
    *    peso que será atualizado.
    * </p>
    * <p>
    *    {@code g} - gradiente correspondente a conexão do peso que será
    *    atualizado.
    * </p>
    */
   @Override
   public void atualizar(Camada[] redec){
      double g;
      Neuronio neuronio;
      
      //percorrer rede, com exceção da camada de entrada
      int indice = 0;
      for(int i = 1; i < redec.length; i++){

         int nNeuronios = redec[i].quantidadeNeuroniosSemBias();
         for(int j = 0; j < nNeuronios; j++){

            neuronio = redec[i].neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){
               g = neuronio.gradiente[k];
               
               ac[indice] = (rho * ac[indice]) + (1 - rho) * (g*g);
               neuronio.pesos[k] -= (taxaAprendizagem * g) / (Math.sqrt(ac[indice] + epsilon));

               indice++;
            }
         }
      }
   }

   @Override
   public String info(){
      String buffer = "";

      String espacamento = "    ";
      buffer += espacamento + "TaxaAprendizagem: " + this.taxaAprendizagem + "\n";
      buffer += espacamento + "Rho: " + this.rho + "\n";
      buffer += espacamento + "Epsilon: " + this.epsilon + "\n";

      return buffer;
   }

}
