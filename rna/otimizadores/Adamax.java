package rna.otimizadores;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

public class Adamax extends Otimizador{

   /**
    * Valor de taxa de aprendizagem do otimizador.
    */
   private double taxaAprendizagem;

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
    * Acumulador de primeira ordem.
    */
   private double[] m;

   /**
    * Acumulador de segunda ordem
    */
   private double[] v;

   /**
    * Contador de interações
    */
   private long interacoes;

   /**
    * Inicializa uma nova instância de otimizador <strong> Adamax </strong> 
    * usando os valores de hiperparâmetros fornecidos.
    * @param tA valor de taxa de aprendizagem.
    * @param beta1 decaimento do momento de primeira ordem.
    * @param beta2 decaimento do momento de segunda ordem.
    * @param epsilon usado para evitar a divisão por zero.
    */
   public Adamax(double tA, double beta1, double beta2, double epsilon){
      this.taxaAprendizagem = tA;
      this.epsilon = epsilon;
      this.beta1 = beta1; 
      this.beta2 = beta2;
   }

   /**
    * Inicializa uma nova instância de otimizador <strong> Adamax </strong>.
    * <p>
    *    Os hiperparâmetros do Adamax serão inicializados com seus os valores padrão, que são:
    * </p>
    * <p>
    *    {@code taxaAprendizagem = 0.001}
    * </p>
    * <p>
    *    {@code beat1 = 0.9}
    * </p>
    * <p>
    *    {@code beta2 = 0.999}
    * </p>
    * <p>
    *    {@code epsilon = 1e-7}
    * </p>
    */
   public Adamax(){
      this(0.001, 0.9, 0.999, 1e-7);
   }

   @Override
   public void inicializar(int parametros){
      this.m = new double[parametros];
      this.v = new double[parametros];
      this.interacoes = 0;
   }

   /**
    * oi
    */
   @Override
   public void atualizar(Camada[] redec){
      Neuronio neuronio;
      
      interacoes++;
      double forcaB1 = Math.pow(beta1, interacoes);

      //percorrer a rede, com exceção da camada de entrada
      int indice = 0;
      for(int i = 1; i < redec.length; i++){

         int nNeuronios = redec[i].quantidadeNeuroniosSemBias();
         for(int j = 0; j < nNeuronios; j++){

            neuronio = redec[i].neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){
               double g = neuronio.gradiente[k];

               m[indice] += (g - m[indice]) * (1 - beta1);
               v[indice] = Math.max(beta2 * v[indice], Math.abs(g));

               double correcao = (taxaAprendizagem * m[indice] / ((1 - forcaB1) * (v[indice] + epsilon)));
               neuronio.pesos[k] -= correcao;

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
      buffer += espacamento + "Beta1: " + this.beta1 + "\n";
      buffer += espacamento + "Beta2: " + this.beta2 + "\n";
      buffer += espacamento + "Epsilon: " + this.epsilon + "\n";

      return buffer;
   }
}
