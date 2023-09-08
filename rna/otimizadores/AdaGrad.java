package rna.otimizadores;

import java.util.ArrayList;

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
    * {@code epsilon = 1e-7}
    */
   public AdaGrad(){
      this(1e-7);
   }

   
   @Override
    public void atualizar(ArrayList<Camada> redec, double taxaAprendizagem, double momentum){  
      Neuronio neuronio;

      //percorrer rede, com exceção da camada de entrada
      for(Camada camada : redec.subList(1, redec.size())){
  
         int nNeuronios = camada.quantidadeNeuronios() - ((camada.temBias()) ? 1 : 0);
         for(int j = 0; j < nNeuronios; j++){//percorrer neurônios da camada atual
  
            neuronio = camada.neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
               neuronio.acumuladorGradiente[k] += neuronio.gradiente[k] * neuronio.gradiente[k];
               neuronio.pesos[k] += (taxaAprendizagem / Math.sqrt(neuronio.acumuladorGradiente[k] + epsilon)) * neuronio.gradiente[k];
            }
         }
      }
   }
   
}
