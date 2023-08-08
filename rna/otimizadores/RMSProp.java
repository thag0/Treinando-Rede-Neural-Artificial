package rna.otimizadores;

import java.util.ArrayList;

import rna.Camada;
import rna.Neuronio;

/**
 * Implementa o treino da rede neural usando o algoritmo RMSProp (Root Mean Square Propagation).
 *
 * Ele é uma adaptação do Gradiente Descendente Estocástico (SGD) que ajuda a lidar com a
 * oscilação do gradiente, permitindo que a taxa de aprendizado seja adaptada para cada parâmetro 
 * individualmente.
 */
public class RMSProp extends Otimizador{
   double epsilon = 1e-9;//evitar divisão por zero
   double taxaDecaimento = 0.9;//fator de decaimento do RMSprop


   @Override
    public void atualizar(ArrayList<Camada> redec, double taxaAprendizagem, double momentum){
      for(int i = 1; i < redec.size(); i++){//percorrer rede

         Camada camadaAtual = redec.get(i);
         Camada camadaAnterior = redec.get(i - 1);
         for(int j = 0; j < camadaAtual.neuronios.length; j++){//percorrer neurônios da camada atual

            Neuronio neuronio = camadaAtual.neuronios[j];
            for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
               neuronio.gradiente = neuronio.erro * camadaAnterior.neuronios[k].saida;
               neuronio.acumuladorGradiente[k] = (taxaDecaimento * neuronio.acumuladorGradiente[k]) + ((1 - taxaDecaimento) * neuronio.gradiente * neuronio.gradiente);
               neuronio.pesos[k] += (taxaAprendizagem / Math.sqrt(neuronio.acumuladorGradiente[k] + epsilon)) * neuronio.gradiente;
            }
         }
      }
   }
}
