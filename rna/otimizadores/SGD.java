package rna.otimizadores;

import java.util.ArrayList;

import rna.Camada;
import rna.Neuronio;

/**
 * Otimizador usando gradiente descendente estocástico com momentum.
 * Atualiza os pesos usando o gradiente e momentum para ajudar a otimizar o aprendizado.
 */
public class SGD extends Otimizador{
   boolean nesterov = false;


   public SGD(boolean nesterov){
      this.nesterov = nesterov;
   }


   public SGD(){}


   @Override
   public void atualizar(ArrayList<Camada> redec, double taxaAprendizagem, double momentum){
      for(int i = 1; i < redec.size(); i++){//percorrer rede 
         
         Camada camadaAtual = redec.get(i);
         int nNeuronios = camadaAtual.obterQuantidadeNeuronios();
         nNeuronios -= (camadaAtual.temBias()) ? 1 : 0;
         for(int j = 0; j < nNeuronios; j++){//percorrer neurônios da camada atual
            
            Neuronio neuronio = camadaAtual.neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
               if(nesterov){
                  double momentumAnterior = neuronio.momentum[k];
                  neuronio.pesos[k] += momentum * momentumAnterior;
                  neuronio.momentum[k] = momentum * momentumAnterior + neuronio.gradiente[k];
                  neuronio.pesos[k] -= taxaAprendizagem * neuronio.momentum[k];
                  
               }else{
                  neuronio.momentum[k] = (momentum * neuronio.momentum[k]) + neuronio.gradiente[k];
                  neuronio.pesos[k] += neuronio.momentum[k];
               }
            }
         }
      } 
   }
   
}
