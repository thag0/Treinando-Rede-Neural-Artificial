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
    * Valor de taxa de aprendizagem do otimizador.
    */
   private double taxaAprendizagem;

   /**
    * Usado para evitar divisão por zero.
    */
   private double epsilon;

   /**
    * Acumuladores dos gradientes ao quadrado.
    */
   private double[] acumulador;

   /**
    * Inicializa uma nova instância de otimizador <strong> AdaGrad </strong> 
    * usando os valores de hiperparâmetros fornecidos.
    * @param tA valor de taxa de aprendizagem.
    * @param epsilon usado para evitar a divisão por zero.
    */
   public AdaGrad(double tA, double epsilon){
      this.taxaAprendizagem = tA;
      this.epsilon = epsilon;
   }

   /**
    * Inicializa uma nova instância de otimizador <strong> AdaGrad </strong>.
    * <p>
    *    Os hiperparâmetros do AdaGrad serão inicializados com os valores padrão, que são:
    * </p>
    * <p>
    *    {@code taxaAprendizagem = 0.01}
    * </p>
    * <p>
    *    {@code epsilon = 1e-7}
    * </p>
    */
   public AdaGrad(){
      this(0.01, 1e-7);
   }

   @Override
   public void inicializar(int parametros){
      this.acumulador = new double[parametros];

      for(int i = 0; i < this.acumulador.length; i++){
         this.acumulador[i] = 0.1;
      }
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
   public void atualizar(Camada[] redec){  
      double g;
      int id = 0;//indice de busca na lista de coeficientes
      for(Camada camada : redec){
         for(Neuronio neuronio : camada.neuronios()){
            for(int i = 0; i < neuronio.pesos.length; i++){
               g = neuronio.gradientes[i];

               acumulador[id] += (g * g);
               neuronio.pesos[i] -= (taxaAprendizagem * g) / (Math.sqrt(acumulador[id] + epsilon));

               id++;
            }
         }
      }
   }

   @Override
   public String info(){
      String buffer = "";

      String espacamento = "    ";
      buffer += espacamento + "TaxaAprendizagem: " + this.taxaAprendizagem + "\n";
      buffer += espacamento + "Epsilon: " + this.epsilon + "\n";

      return buffer;
   }
   
}
