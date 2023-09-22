package rna.treinamento;

import java.util.ArrayList;

import rna.estrutura.RedeNeural;
import rna.otimizadores.Otimizador;

/**
 * Disponibilzia uma interface para usar os métodos de treino e treino em
 * lote da Rede Neural.
 */
public class Treinador{

   /**
    * Dados de custo da rede neural durante cada época de treinamento.
    */
   ArrayList<Double> historico;

   /**
    * Auxiliar na verificação do cálculo do histórico de custos.
    */
   public boolean calcularHistorico = false;

   Treino treino;
   TreinoLote treinoLote;

   /**
    * Responsável por organizar os tipos de treino da rede neural.
    */
   public Treinador(){
      this.historico = new ArrayList<Double>();
      treino = new Treino(historico, calcularHistorico);
      treinoLote = new TreinoLote(historico, calcularHistorico);
   }

   /**
    * Configura o cálculo do custo da rede neural durante o processo de treinamento.
    * A mesma configuração se aplica ao treino em lote.
    * @param calcularHistorico calcular ou não o histórico de custo.
    */
    public void configurarHistoricoCusto(boolean calcularHistorico){
      this.calcularHistorico = calcularHistorico;
      treino.configurarHistorico(calcularHistorico);
      treinoLote.configurarHistorico(calcularHistorico);
   }

   /**
    * Treina a rede neural calculando os erros dos neuronios, seus gradientes para cada peso e 
    * passando essas informações para o otimizador configurado ajustar os pesos.
    * @param rede rede neural que será treinada.
    * @param otimizador otimizador configurado da rede.
    * @param entradas dados de entrada para o treino.
    * @param saidas dados de saída correspondente as entradas para o treino.
    * @param epochs quantidade de épocas de treinamento.
    * @param embaralhar embaralhar dados de treino para cada época.
    */
   public void treino(RedeNeural rede, Otimizador otimizador, double[][] entradas, double[][] saidas, int epochs, boolean embaralhar){
      treino.treino(rede, otimizador, entradas, saidas, epochs, embaralhar);
   }

   /**
    * Treina a rede neural calculando os erros dos neuronios, seus gradientes para cada peso e 
    * passando essas informações para o otimizador configurado ajustar os pesos.
    * @param rede rede neural que será treinada.
    * @param otimizador otimizador configurado da rede.
    * @param entradas dados de entrada para o treino.
    * @param saidas dados de saída correspondente as entradas para o treino.
    * @param epochs quantidade de épocas de treinamento.
    * @param embaralhar embaralhar dados de treino para cada época.
    * @param tamLote tamanho do lote.
    */
   public void treino(RedeNeural rede, Otimizador otimizador, double[][] entradas, double[][] saidas, int epochs, boolean embaralhar, int tamLote){
      treinoLote.treino(rede, otimizador, entradas, saidas, epochs, embaralhar, tamLote);
   }

   /**
    * Retorna uma lista contendo os valores de custo da rede
    * a cada época de treinamento.
    * @return lista com os custo por época durante a fase de treinamento.
    */
   public ArrayList<Double> obterHistorico(){
      return this.historico;
   }
   
}
