package rna;

/**
 * Implementação antiga.
 * Classe dedicada para guardar as funções de ativação da rede neural
 */
public class AtivacoesAntigo{
   private static double alfaLeakyRelu = 0.01;
   private static double alfaElu = 0.01;

   public static double relu(double x){
      if(x > 0) return x;
      return 0;
   }


   public static double reluDx(double x){
      if(x > 0) return 1;
      return 0;
   }


   public static double sigmoid(double x){
      return (1 / (1 + Math.exp(-x)) );
   }


   public static double sigmoidDx(double x){
      double sig = sigmoid(x);
      return (sig * (1-sig));
   }


   public static double tanH(double x){
      return Math.tanh(x);
   }


   public static double tanHDx(double x){
      double resultado = Math.tanh(x);
      return (1 - Math.pow(resultado, 2));
   }


   public static double leakyRelu(double x){
      if(x > 0) return x;
      else return ((alfaLeakyRelu) * x);
   }


   public static double leakyReluDx(double x){
      if(x > 0) return 1;
      else return alfaLeakyRelu;
   }


   public static double elu(double x){
      if(x > 0) return x;
      else return (alfaElu * (Math.exp(x)-1));
   }


   public static double eluDx(double x){
      if(x > 0) return 1;
      else return (alfaElu * Math.exp(x));
   }


   public static double swish(double x){
      return (x * sigmoid(x));
   }


   public static double swishDx(double x){
      double sig = sigmoid(x);
      return (x * sig + sigmoidDx(x));
  }


   public static double gelu(double x){
      return 0.5 * x * (1.0 + Math.tanh(Math.sqrt(2.0 / Math.PI) * (x + 0.044715 * Math.pow(x, 3))));      
   }


   public static double geluDx(double x){
      double cdf = 0.5 * (1.0 + Math.tanh(Math.sqrt(2.0 / Math.PI) * (x + 0.044715 * Math.pow(x, 3))));
      return 0.5 + 0.5 * Math.tanh(Math.sqrt(2.0 / Math.PI) * (x + 0.044715 * Math.pow(x, 3))) + x * cdf;
   }


   public static double linear(double x){
      return x;
   }


   public static double linearDx(double x){
      return 1;
   }


   public static double seno(double x){
      return Math.sin(x);
   }


   public static double senoDx(double x){
      return Math.cos(x);
   }


   public static void argmax(Camada camada){
      int indiceMaior = 0;
      double maiorValor = camada.neuronios[0].somatorio;

      //procurar maior valor da saída
      for(int i = 0; i < camada.neuronios.length; i++){
         if(camada.neuronios[i].saida > maiorValor){
            indiceMaior = i;
            maiorValor = camada.neuronios[i].somatorio;
         }
      }

      //aplicar argmax
      for(int i = 0; i < camada.neuronios.length; i++){
         if(i == indiceMaior) camada.neuronios[i].saida = 1;
         else camada.neuronios[i].saida = 0;
      }
   }


   //corrigir para nova arquitetura----------------
   public static void softmax(Camada camada){
      double somaExponencial = 0.0;

      //soma exponencial da saída
      for(int i = 0; i < camada.neuronios.length; i++){
         somaExponencial += Math.exp(camada.neuronios[i].somatorio);
      }

      //aplicar softmax
      for (int i = 0; i < camada.neuronios.length; i++){
         double valorExponencial = Math.exp(camada.neuronios[i].somatorio);
         camada.neuronios[i].saida = (valorExponencial / somaExponencial);
      }
   }
}
