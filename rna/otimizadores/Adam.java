package rna.otimizadores;

import java.util.ArrayList;

import rna.Camada;
import rna.Neuronio;

/**
 * Implementação do algoritmo de otimização Adam.
 * O algoritmo ajusta os pesos da rede neural usando o gradiente descendente com momento
 * e a estimativa adaptativa de momentos de primeira e segunda ordem.
 */
public class Adam extends Otimizador{
   //parâmetros do adam
   private double epsilon = 1e-8;//evitar divisão por zero
   private double beta1 = 0.9;//decaimento do momento
   private double beta2 = 0.999;//decaimento da segunda ordem


   @Override
   public void atualizar(ArrayList<Camada> redec, double taxaAprendizagem, double momentum){
      double t = 1; //contador de iterações
      double momentumCorrigido, segundaOrdemCorrigida;
      for(int i = 1; i < redec.size(); i++){//percorrer rede

         Camada camadaAtual = redec.get(i);
         int nNeuronios = camadaAtual.obterQuantidadeNeuronios();
         nNeuronios -= (camadaAtual.temBias) ? 1 : 0;
         for(int j = 0; j < nNeuronios; j++){//percorrer neurônios da camada atual

            Neuronio neuronio = camadaAtual.neuronios[j];
            for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
               //atualização do momentum
               neuronio.momentum[k] = (beta1 * neuronio.momentum[k]) + ((1 - beta1) * neuronio.gradiente[k]);
               // Atualização do acumulador da segunda ordem
               neuronio.acumuladorSegundaOrdem[k] = (beta2 * neuronio.acumuladorSegundaOrdem[k]) + ((1 - beta2) * neuronio.gradiente[k] * neuronio.gradiente[k]);
               //bias corrigido pelo momento
               momentumCorrigido = neuronio.momentum[k] / (1 - Math.pow(beta1, t));
               //bias corrigido pela segunda ordem
               segundaOrdemCorrigida = neuronio.acumuladorSegundaOrdem[k] / (1 - Math.pow(beta2, t));
               //atualização dos pesos usando o Adam
               neuronio.pesos[k] += taxaAprendizagem * momentumCorrigido / (Math.sqrt(segundaOrdemCorrigida) + epsilon);
            }
         }
      }
   }

}
