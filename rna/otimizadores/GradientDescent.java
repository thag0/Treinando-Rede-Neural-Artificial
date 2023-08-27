package rna.otimizadores;

import java.util.ArrayList;

import rna.Camada;
import rna.Neuronio;

/**
 * Classe que implementa o algoritmo de Backpropagation para otimização de redes neurais.
 * Atualiza diretamente os pesos da rede com base no gradiente.
 */
public class GradientDescent extends Otimizador{


   @Override
   public void atualizar(ArrayList<Camada> redec, double taxaAprendizagem, double momentum){
      for(int i = 1; i < redec.size(); i++){//percorrer rede 
         
         Camada camada = redec.get(i);
         int nNeuronios = camada.obterQuantidadeNeuronios();
         nNeuronios -= (camada.temBias()) ? 1 : 0;
         for(int j = 0; j < nNeuronios; j++){//percorrer neurônios da camada atual
            
            Neuronio neuronio = camada.neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
               neuronio.pesos[k] += neuronio.gradiente[k];
            }
         }
      } 
   }
   
}
