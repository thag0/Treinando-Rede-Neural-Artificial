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
    * Coeficientes de momentum.
    */
   private double[] momentum;

   /**
    * Coeficientes de momentum de segunda ordem
    */
   private double[] velocidade;

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
      this.momentum = new double[parametros];
      this.velocidade = new double[parametros];
      this.interacoes = 0;
   }

   /**
    * Aplica o algoritmo do Adamax para cada peso da rede neural.
    * <p>
    *    O Adamax funciona usando a seguinte expressão:
    * </p>
    * <pre>
    *    p[i] -= (tA * m[i]) / ((1 - beta1ⁱ) * (v[i] + eps))
    * </pre>
    * Onde:
    * <p>
    *    {@code tA} - valor de taxa de aprendizagem do otimizador.
    * </p>
    * <p>
    *    {@code m} - coeficiente de momentum correspondente ao peso
    *    que será atualizado.
    * </p>
    * <p>
    *    {@code beta1} - fator de decaimento do momento de primeira ordem.
    * </p>
    * <p>
    *    {@code i} - contador de interações do otimizador.
    * </p>
    * <p>
    *    {@code v} - coeficiente de momentum de segunda ordem correspondente 
    *    ao peso que será atualizado.
    * </p>
    * <p>
    *    {@code eps} - pequeno valor usado para evitar divisão por zero.
    * </p>
    * Os valores de <strong>m</strong> e <strong>v</strong> são dados por:
    *<pre>
    *m[i] = m[i] + ((g[i] - m[i]) * (1 - beta1))
    *v[i] = max((beta2 * v[i]), abs(g[i]))
    *</pre>
    * Onde:
    * <p>
    *    {@code g} - gradiente correspondente ao peso que sera atualizado.
    * </p>
    * <p>
    *    {@code beta2} - fator de decaimento do momentum de segunda ordem.
    * </p>
    */
   @Override
   public void atualizar(Camada[] redec){
      Neuronio neuronio;
      
      interacoes++;
      double forcaB1 = Math.pow(beta1, interacoes);

      //percorrer a rede, com exceção da camada de entrada
      int indice = 0;
      for(int i = 0; i < redec.length; i++){

         int nNeuronios = redec[i].quantidadeNeuronios();
         for(int j = 0; j < nNeuronios; j++){

            neuronio = redec[i].neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){
               double g = neuronio.gradiente[k];

               momentum[indice] += (g - momentum[indice]) * (1 - beta1);
               velocidade[indice] = Math.max(beta2 * velocidade[indice], Math.abs(g));

               neuronio.pesos[k] -= taxaAprendizagem * momentum[indice] / ((1 - forcaB1) * (velocidade[indice] + epsilon));

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
