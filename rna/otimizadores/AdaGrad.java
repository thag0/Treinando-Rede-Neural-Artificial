package rna.otimizadores;

import java.util.ArrayList;

import rna.Camada;
import rna.Neuronio;

/**
 * Implementa uma versão do algoritmo AdaGrad (Adaptive Gradient Algorithm).
 * O algoritmo otimiza o processo de aprendizado adaptando a taxa de aprendizagem 
 * de cada parâmetro com base no histórico de atualizações 
 * anteriores
 */
public class AdaGrad extends Otimizador{
   double epsilon = 1e-8;//evitar divisão por zero

   
   @Override
    public void atualizar(ArrayList<Camada> redec, double taxaAprendizagem, double momentum){  
      for(int i = 1; i < redec.size(); i++){//percorrer rede
  
         Camada camadaAtual = redec.get(i);
         int nNeuronios = camadaAtual.obterQuantidadeNeuronios();
         nNeuronios -= (camadaAtual.temBias()) ? 1 : 0;
         for(int j = 0; j < nNeuronios; j++){//percorrer neurônios da camada atual
  
            Neuronio neuronio = camadaAtual.neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
               neuronio.acumuladorGradiente[k] += neuronio.gradiente[k] * neuronio.gradiente[k];
               neuronio.pesos[k] += (taxaAprendizagem / Math.sqrt(neuronio.acumuladorGradiente[k] + epsilon)) * neuronio.gradiente[k];
            }
         }
      }
   }
   
}
