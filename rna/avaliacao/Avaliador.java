package rna.avaliacao;

import rna.RedeNeural;

import rna.avaliacao.metrica.Acuracia;
import rna.avaliacao.metrica.ErroMedioAbsoluto;
import rna.avaliacao.metrica.MatrizConfusao;

import rna.avaliacao.perda.EntropiaCruzada;
import rna.avaliacao.perda.EntropiaCruzadaBinaria;
import rna.avaliacao.perda.ErroMedioQuadrado;


/**
 * Interface para os métodos de avaliação e desempenho da rede neural.
 */
public class Avaliador{
   RedeNeural rede;

   //perda
   EntropiaCruzada entropiaCruzada = new EntropiaCruzada();
   EntropiaCruzadaBinaria entropiaCruzadaBinaria = new EntropiaCruzadaBinaria();
   ErroMedioQuadrado erroMedioQuadrado = new ErroMedioQuadrado();

   //métrica
   Acuracia acuracia = new Acuracia();
   ErroMedioAbsoluto erroMedioAbsoluto = new ErroMedioAbsoluto();
   MatrizConfusao matrizConfusao = new MatrizConfusao();


   public Avaliador(RedeNeural rede){
      this.rede = rede;
   }


   /**
    * Calcula o erro médio quadrado da rede neural em relação aos dados de entrada e saída fornecidos.
    * @param dados dados de entrada.
    * @param saida dados de saída contendo os resultados respectivos para as entradas.
    * @return erro médio quadrado da rede em relação ao dados fornecidos (custo/perda).
    */
   public double erroMedioQuadrado(double[][] entrada, double[][] saida){
      return erroMedioQuadrado.calcular(this.rede, entrada, saida);
   }


   /**
    * Calcula o erro médio absoluto entre as saídas previstas pela rede neural e os valores reais.
    * @param entrada dados de entrada.
    * @param saida dados de saída contendo os resultados respectivos para as entradas.
    * @return A precisão da rede neural em forma de probabilidade.
    */
   public double erroMedioAbsoluto(double[][] entrada, double[][] saida){
      return erroMedioAbsoluto.calcular(this.rede, entrada, saida);
   }


   /**
    * Calcula a precisão da rede neural em relação aos dados de entrada e saída fornecidos (classificação).
    * @param entrada dados de entrada.
    * @param saida dados de saída contendo os resultados respectivos para as entradas.
    * @return A acurácia da rede neural em forma de probabilidade.
    */
   public double acuracia(double[][] entrada, double[][] saida){
      return acuracia.calcular(this.rede, entrada, saida);
   }


   /**
    * Calcula a entropia cruzada entre as saídas previstas pela rede neural
    * e as saídas reais fornecidas.
    * @param dados dados de entrada.
    * @param saida dados de saída contendo os resultados respectivos para as entradas.
    * @return entropia cruzada da rede em relação ao dados fornecidos (custo/perda).
    */
   public double entropiaCruzada(double[][] entrada, double[][] saida){  
      return entropiaCruzada.calcular(this.rede, entrada, saida);
   }


   /**
    * Calcula a entropia cruzada binária entre as saídas previstas pela rede neural
    * e as saídas reais fornecidas.
    * @param entrada Os dados de entrada para os quais a rede neural calculará as saídas.
    * @param saida As saídas reais correspondentes aos dados de entrada.
    * @return valor da entropia cruzada binária.
    */
   public double EntropiaCruzadaBinaria(double[][] entrada, double[][] saida){
      return entropiaCruzadaBinaria.calcular(this.rede, entrada, saida);
   }

   /**
    * Calcula a matriz de confusão para avaliar o desempenho da rede em classificação.
    *
    * A matriz de confusão mostra a contagem de amostras que foram classificadas de forma correta ou não em cada classe.
    * As linhas representam as classes reais e as colunas as classes previstas pela rede.
    * @param entradas matriz com os dados de entrada 
    * @param saidas matriz com os dados de saída
    * @return matriz de confusão para avaliar o desempenho do modelo.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public int[][] matrizConfusao(double[][] entradas, double[][] saidas){
      return matrizConfusao.calcularMatriz(this.rede, entradas, saidas);
   } 
}
