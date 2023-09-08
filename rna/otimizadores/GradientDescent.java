package rna.otimizadores;

import java.util.ArrayList;

import rna.Camada;
import rna.Neuronio;

/**
 * Classe que implementa o algoritmo de Descida do Gradiente para otimização de redes neurais.
 * Atualiza diretamente os pesos da rede com base no gradiente.
 */
public class GradientDescent extends Otimizador{


   /**
    * Inicializa uma nova instância de otimizador da Descida do Gradiente.
    */
   public GradientDescent(){

   }


   @Override
   public void atualizar(ArrayList<Camada> redec, double taxaAprendizagem, double momentum){
      Camada camada;
      Neuronio neuronio;

      for(int i = 1; i < redec.size(); i++){//percorrer rede 
         
         camada = redec.get(i);
         int nNeuronios = camada.quantidadeNeuronios() - ((camada.temBias()) ? 1 : 0);
         for(int j = 0; j < nNeuronios; j++){//percorrer neurônios da camada atual
            
            neuronio = camada.neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
               neuronio.pesos[k] += neuronio.gradiente[k];
            }
         }
      } 
   }
   
}
