package rna.otimizadores;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

/**
 * Classe que implementa o algoritmo de Descida do Gradiente com momentum 
 * para otimização da Rede Neural.
 */
public class GDM extends Otimizador{

   /**
    * Valor de taxa de aprendizagem do otimizador.
    */
   private double taxaAprendizagem;

   /**
    * Valor de taxa de momentum do otimizador.
    */
   private double taxaMomentum;

   /**
    * Coeficientes de momentum.
    */
   private double[] momentum;

   /**
    * Inicializa uma nova instância de otimizador da <strong> Descida do Gradiente 
    * com Momentum </strong> usando os valores de hiperparâmetros fornecidos.
    * @param tA valor de taxa de aprendizagem.
    * @param tA valor de taxa de momentum.
    */
   public GDM(double tA, double tm){
      this.taxaAprendizagem = tA;
      this.taxaMomentum = tm;
   }

   /**
    * Inicializa uma nova instância de otimizador da <strong> Descida do Gradiente com Momentum</strong>.
    * <p>
    *    Os hiperparâmetros do GDM serão inicializados com os valores padrão, que são:
    * </p>
    * <p>
    *    {@code taxaAprendizagem = 0.01}
    * </p>
    * <p>
    *    {@code taxaMomentum = 0.9}
    * </p>
    */
   public GDM(){
      this(0.01, 0.9);
   }

   @Override
   public void inicializar(int parametros){
      this.momentum = new double[parametros];
   }

   /**
    * Aplica o algoritmo do Gradiente descendente para cada peso da rede neural.
    * <p>
    *    O Gradiente descendente funciona usando a seguinte expressão:
    * </p>
    * <pre>
    *    p[i] -= (g[i] * tA) + (m[i] * tm)
    * </pre>
    * Onde:
    * <p>
    *    {@code p} - peso que será atualizado.
    * </p>
    *    {@code g} - gradiente correspondente a conexão do peso que será
    *    atualizado.
    * </p>
    * <p>
    *    {@code tA} - taxa de aprendizagem.
    * </p>
    * <p>
    *    {@code m} - coeficiente de momentum correspondente ao peso que
    *    será atualizado.
    * </p>
    * <p>
    *    {@code tm} - taxa de momentum.
    * </p>
    */
    @Override
   public void atualizar(Camada[] redec){
      Neuronio neuronio;

      //percorrer rede, com exceção da camada de entrada
      for(int i = 0; i < redec.length; i++){
         
         Camada camada = redec[i];
         int nNeuronios = camada.quantidadeNeuronios();
         for(int j = 0; j < nNeuronios; j++){
            
            neuronio = camada.neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){
               momentum[k] = (neuronio.gradiente[k] * taxaAprendizagem) + (taxaMomentum * momentum[k]);
               neuronio.pesos[k] -= momentum[k];
            }
         }
      } 
   }

   @Override
   public String info(){
      String buffer = "";

      String espacamento = "    ";
      buffer += espacamento + "TaxaAprendizagem: " + this.taxaAprendizagem + "\n";
      buffer += espacamento + "Momentum: " + this.taxaMomentum + "\n";

      return buffer;
   }
   
}
