package rna.otimizadores;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

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

   /**
    * Aplica o algoritmo do Gradiente descendente para cada peso da rede neural.
    * <p>
    *    O Gradiente descendente funciona usando a seguinte expressão:
    * </p>
    * <pre>
    *    p[i] -= g[i]
    * </pre>
    * Onde:
    * <p>
    *    {@code p} - peso que será atualizado.
    * </p>
    *    {@code g} - gradiente correspondente a conexão do peso que será
    *    atualizado.
    * </p>
    */
    @Override
   public void atualizar(Camada[] redec, double taxaAprendizagem, double momentum){
      Neuronio neuronio;

      //percorrer rede, com exceção da camada de entrada
      for(int i = 1; i < redec.length; i++){
         
         Camada camada = redec[i];
         int nNeuronios = camada.quantidadeNeuroniosSemBias();
         for(int j = 0; j < nNeuronios; j++){
            
            neuronio = camada.neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){
               neuronio.pesos[k] -= neuronio.gradiente[k];
            }
         }
      } 
   }
   
}
