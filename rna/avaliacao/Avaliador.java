package rna.avaliacao;

import java.io.Serializable;

import rna.avaliacao.metrica.Acuracia;
import rna.avaliacao.metrica.F1Score;
import rna.avaliacao.metrica.MatrizConfusao;

import rna.avaliacao.perda.EntropiaCruzada;
import rna.avaliacao.perda.EntropiaCruzadaBinaria;
import rna.avaliacao.perda.ErroMedioAbsoluto;
import rna.avaliacao.perda.ErroMedioQuadrado;
import rna.estrutura.RedeNeural;


/**
 * Interface para os métodos de avaliação e desempenho da rede neural.
 */
public class Avaliador implements Serializable{
   RedeNeural rede;

   //perda
   EntropiaCruzada entropiaCruzada = new EntropiaCruzada();
   EntropiaCruzadaBinaria entropiaCruzadaBinaria = new EntropiaCruzadaBinaria();
   ErroMedioQuadrado erroMedioQuadrado = new ErroMedioQuadrado();

   //métrica
   Acuracia acuracia = new Acuracia();
   ErroMedioAbsoluto erroMedioAbsoluto = new ErroMedioAbsoluto();
   MatrizConfusao matrizConfusao = new MatrizConfusao();
   F1Score f1Score = new F1Score();

   public Avaliador(RedeNeural rede){
      this.rede = rede;
   }

   /**
    * Calcula o erro médio quadrado da rede neural em relação aos dados de entrada e saída fornecidos.
    * @param entrada dados de entrada.
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
    * @param entrada dados de entrada.
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
   public double entropiaCruzadaBinaria(double[][] entrada, double[][] saida){
      return entropiaCruzadaBinaria.calcular(this.rede, entrada, saida);
   }

   /**
    * Calcula a matriz de confusão para avaliar o desempenho da rede em classificação.
    * <p>
    *    A matriz de confusão mostra a contagem de amostras que foram classificadas de forma correta 
    *    ou não em cada classe. As linhas representam as classes reais e as colunas as classes previstas pela rede.
    * </p>
    * @param entradas matriz com os dados de entrada 
    * @param saidas matriz com os dados de saída
    * @return matriz de confusão para avaliar o desempenho do modelo.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public int[][] matrizConfusao(double[][] entradas, double[][] saidas){
      return matrizConfusao.calcularMatriz(this.rede, entradas, saidas);
   }

   /**
    * Calcula o F1-Score ponderado para o modelo de rede neural em relação às entradas e saídas fornecidas.
    *
    * O F1-Score é uma métrica que combina a precisão e o recall para avaliar o desempenho de um modelo
    * de classificação. Ele é especialmente útil quando se lida com classes desbalanceadas ou quando se
    * deseja equilibrar a precisão e o recall.
    *
    * @param entradas matriz com os dados de entrada 
    * @param saidas matriz com os dados de saída
    * @return f1-score ponderado para o modelo em relação aos dados de entrada e saída.
    */
   public double f1Score(double[][] entradas, double[][] saidas){
      return f1Score.calcular(this.rede, entradas, saidas);
   }
}
