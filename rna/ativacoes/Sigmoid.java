package rna.ativacoes;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;

/**
 * Implementação da função de ativação Sigmóide para uso 
 * dentro da {@code Rede Neural}.
 */
public class Sigmoid extends Ativacao{

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
   public void calcular(Camada camada){
      for(Neuronio neuronio : camada.neuronios()){
         neuronio.saida = sigmoid(neuronio.somatorio);
      }
   }

   @Override
   public void derivada(Camada camada){
      //aproveitando o valor pre calculado
      for(Neuronio neuronio : camada.neuronios()){
         neuronio.derivada = neuronio.saida * (1 - neuronio.saida);
      }
   }
}
