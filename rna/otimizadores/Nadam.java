package rna.otimizadores;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;


/**
 * Implementação do algoritmo de otimização Nadam.
 * <p>
 *    O algoritmo ajusta os pesos da rede neural usando o gradiente descendente 
 *    com momento e a estimativa adaptativa de momentos de primeira e segunda ordem.
 * </p>
 * O adicional do Nadam é usar o acelerador de nesterov durante a correção dos
 * pesos da rede.
 */
public class Nadam extends Otimizador{

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
   private double[] m;

   /**
    * Coeficientes de momentum de segunda orgem.
    */
   private double[] v;

   /**
    * Contador de iterações.
    */
   long interacoes = 0;

   /**
    * Inicializa uma nova instância de otimizador <strong> Nadam </strong> 
    * usando os valores de hiperparâmetros fornecidos.
    * @param tA valor de taxa de aprendizagem.
    * @param beta1 decaimento do momento de primeira ordem.
    * @param beta2 decaimento da segunda ordem.
    * @param epsilon usado para evitar a divisão por zero.
    */
   public Nadam(double tA, double beta1, double beta2, double epsilon){
      this.taxaAprendizagem = tA;
      this.beta1 = beta1;
      this.beta2 = beta2;
      this.epsilon = epsilon;
   }

   /**
    * Inicializa uma nova instância de otimizador <strong> Nadam </strong>.
    * <p>
    *    Os hiperparâmetros do Nadam serão inicializados com os valores padrão, que são:
    * </p>
    * <p>
    *    {@code taxaAprendizagem = 0.001}
    * </p>
    * <p>
    *    {@code beta1 = 0.9}
    * </p>
    * <p>
    *    {@code beta2 = 0.999}
    * </p>
    * <p>
    *    {@code epsilon = 1e-7}
    * </p>
    */
   public Nadam(){
      this(0.001, 0.9, 0.999, 1e-7);
   }

   @Override
   public void inicializar(int parametros){
      this.m = new double[parametros];
      this.v = new double[parametros];
   }

   /**
    * Aplica o algoritmo do Nadam para cada peso da rede neural.
    * <p>
    *    O Nadam funciona usando a seguinte expressão:
    * </p>
    * <pre>
    *    p[i] -= (tA * mc) / ((√ vc) + eps)
    * </pre>
    * Onde:
    * <p>
    *    {@code p} - peso que será atualizado.
    * </p>
    * <p>
    *    {@code tA} - valor de taxa de aprendizagem do otimizador.
    * </p>
    * <p>
    *    {@code mc} - valor de momentum corrigido
    * </p>
    * <p>
    *    {@code m2c} - valor de velocidade (momentum de segunda ordem) corrigido
    * </p>
    * Os valores de momentum e velocidade corrigidos se dão por:
    * <pre>
    *    mc = ((beta1 * m) + ((1 - beta1) * g[i])) / (1 - beta1ⁱ)
    * </pre>
    * <pre>
    *    vc = (beta2 * v) / (1 - beta2ⁱ)
    * </pre>
    * Onde:
    * <p>
    *    {@code m} - valor de momentum correspondete a conexão do peso que está
    *     sendo atualizado.
    * </p>
    * <p>
    *    {@code v} - valor de velocidade correspondete a conexão do peso que está 
    *    sendo atualizado.
    * </p>
    * <p>
    *    {@code g} - gradiente correspondente a conexão do peso que será
    *    atualizado.
    * </p>
    * <p>
    *    {@code i} - contador de interações (épocas passadas em que o otimizador foi usado) 
    * </p>
    */
   @Override
   public void atualizar(Camada[] redec){
      double mChapeu, vChapeu, g;
      Neuronio neuronio;
      
      interacoes++;
      double forcaB1 = (1 - Math.pow(beta1, interacoes));
      double forcaB2 = (1 - Math.pow(beta2, interacoes));
      
      //percorrer rede, com exceção da camada de entrada
      int indice = 0;
      for(int i = 1; i < redec.length; i++){
         
         Camada camada = redec[i];
         int nNeuronios = camada.quantidadeNeuroniosSemBias();
         for(int j = 0; j < nNeuronios; j++){

            neuronio = camada.neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){
               g = neuronio.gradiente[k];

               m[indice] = (beta1 * m[indice]) + ((1 - beta1) * g);
               v[indice] = (beta2 * v[indice]) + ((1 - beta2) * (g*g));
               
               // correções
               mChapeu = (beta1 * m[indice] + ((1 - beta1) * g)) / forcaB1;
               vChapeu = (beta2 * v[indice]) / forcaB2;
               
               neuronio.pesos[k] -= (taxaAprendizagem * mChapeu) / (Math.sqrt(vChapeu) + epsilon);

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
