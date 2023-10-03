package rna.ativacoes;

import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação Sigmóide para uso 
 * dentro da {@code Rede Neural}.
 */
public class Sigmoid extends FuncaoAtivacao{

   /**
    * Instancia a função de ativação Sigmóide.
    * <p>
    *    A função Sigmóide é uma função de ativação que transforma os valores de 
    *    entrada em um intervalo entre 0 e 1, transformando os valores de saída 
    *    num formato de "S" em relação a origem no eixo x.
    * </p>
    */
   public Sigmoid(){

   }

   private double sigmoid(double x){
      return 1 / (1 + Math.exp(-x));
   }

   @Override
   public void ativar(Neuronio[] neuronios){
      for(int i = 0; i < neuronios.length; i++){
         neuronios[i].saida = sigmoid(neuronios[i].somatorio);
      }
   }

   @Override
   public void derivada(Neuronio[] neuronios){
      //aproveitando o valor pre calculado
      for(int i = 0; i < neuronios.length; i++){
         neuronios[i].derivada = neuronios[i].saida * (1 - neuronios[i].saida);
      }
   }
}
