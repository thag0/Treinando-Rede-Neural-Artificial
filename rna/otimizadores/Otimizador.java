package rna.otimizadores;

import java.util.ArrayList;

import rna.Camada;
import rna.RedeNeural;


/**
 * Interface genérica para implementações de otimizadores do treino da rede neural.
 */
public abstract interface Otimizador{
   //funções necessárias

   /**
    * Calcula o erro de cada neurônio na rede neural.
    *
    * O método percorre a rede neural de trás para frente, calculando o erro de cada neurônio
    * na camada de saída e propagando esse erro para as camadas ocultas. 
    * @param redec lista de camadas da rede neural.
    * @param entrada dados de entrada do treinamento.
    * @param saida dados de saída correspondente aos valores de entrada.
    */
   public void calcularErro(ArrayList<Camada> redec, double[] entrada, double[] saida);

   /**
    * Treina a rede neural de acordo com o otimizador configurado.
    * @param rede rede para o treino
    * @param entrada dados de entrada
    * @param saida dados de saída
    */
   public void atualizar(RedeNeural rede, double[] entrada, double[] saida);
}
