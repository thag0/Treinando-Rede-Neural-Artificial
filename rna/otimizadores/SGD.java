package rna.otimizadores;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

/**
 * Classe que implementa o otimizador Gradiente Descentente Estocástico com momentum.
 * <p>
 *    Também possui o adicional do acelerador de nesterov, mas deve ser configurado.
 * </p>
 * Esse é o otimizador que me deu os melhores resultados de convergência até agora.
 */
public class SGD extends Otimizador{

   /**
    * Valor de taxa de aprendizagem do otimizador.
    */
   private double taxaAprendizagem;

   /**
    * Valor de taxa de momentum do otimizador.
    */
   private double momentum;

   /**
    * Usar acelerador de Nesterov.
    */
   private boolean nesterov;

   /**
    * Coeficientes de momentum.
    */
   private double[] m;

   /**
    * Inicializa uma nova instância de otimizador <strong> Stochastic Gradient Descent (SGD) </strong> 
    * usando os valores de hiperparâmetros fornecidos.
    * @param tA valor de taxa de aprendizagem.
    * @param momentum valor de taxa de momentum.
    * @param nesterov usar acelerador de nesterov.
    */
   public SGD(double tA, double momentum, boolean nesterov){
      this.taxaAprendizagem = tA;
      this.momentum = momentum;
      this.nesterov = nesterov;
   }

   /**
    * Inicializa uma nova instância de otimizador <strong> Stochastic Gradient Descent (SGD) </strong> 
    * usando os valores de hiperparâmetros fornecidos.
    * @param tA valor de taxa de aprendizagem.
    * @param momentum valor de taxa de momentum.
    */
   public SGD(double tA, double momentum){
      this.taxaAprendizagem = tA;
      this.momentum = momentum;
      this.nesterov = false;
   }

   /**
    * Inicializa uma nova instância de otimizador <strong> Stochastic Gradient Descent (SGD) </strong>.
    * <p>
    *    Os hiperparâmetros do SGD serão inicializados com seus os valores padrão, que são:
    * </p>
    * <p>
    *    {@code taxaAprendizagem = 0.001}
    * </p>
    * <p>
    *    {@code momentum = 0.99}
    * </p>
    * <p>
    *    {@code nesterov = false}
    * </p>
    */
   public SGD(){
      this(0.001, 0.99, false);
   }

   @Override
   public void inicializar(int parametros){
      this.m = new double[parametros];
   }

   /**
    * Aplica o algoritmo do SGD com momentum (e nesterov, se configurado) para cada peso 
    * da rede neural.
    * <p>
    *    O SGD funciona usando a seguinte expressão:
    * </p>
    * <pre>
    *    p[i] -= (M * m[i]) + (g[i] * tA)
    * </pre>
    * Onde:
    * <p>
    *    {@code p} - peso que será atualizado.
    * </p>
    * <p>
    *    {@code M} - valor de taxa de momentum (ou constante de momentum) 
    *    do otimizador.
    * </p>
    * <p>
    *    {@code m} - valor de momentum da conexão correspondente ao peso
    *    que será atualizado.
    * </p>
    * <p>
    *    {@code g} - gradiente correspondente a conexão do peso que será
    *    atualizado.
    * </p>
    * <p>
    *    {@code tA} - taxa de aprendizagem do otimizador.
    * </p>
    */
   @Override
   public void atualizar(Camada[] redec){
      Neuronio neuronio;
      
      //percorrer rede, com exceção da camada de entrada
      int indice = 0;
      for(int i = 1; i < redec.length; i++){

         int nNeuronios = redec[i].quantidadeNeuroniosSemBias();
         for(int j = 0; j < nNeuronios; j++){
            
            neuronio = redec[i].neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){
               m[indice] = (momentum * m[indice]) + (neuronio.gradiente[k] * taxaAprendizagem);

               if(nesterov){
                  neuronio.pesos[k] -= (neuronio.gradiente[k] * taxaAprendizagem) + (momentum * m[indice]);
               }else{
                  neuronio.pesos[k] -= m[indice];
               }

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
      buffer += espacamento + "Momentum: " + this.momentum + "\n";
      buffer += espacamento + "Nesterov: " + this.nesterov + "\n";

      return buffer;
   }

}
