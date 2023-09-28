package rna.treinamento;

import java.util.ArrayList;

import rna.avaliacao.perda.Perda;
import rna.estrutura.Camada;
import rna.estrutura.RedeNeural;
import rna.otimizadores.GDM;
import rna.otimizadores.GD;
import rna.otimizadores.Otimizador;

/**
 * Classe de treino da rede neural.
 * <p>
 *    Implementa a lógica de treino da rede, calculando erros, gradientes
 *    e atualizando seus pesos de acordo com o otimizador configurado.
 * </p>
 */
class Treino{
   public boolean calcularHistorico = false;
   ArrayList<Double> historico;
   AuxiliarTreino aux = new AuxiliarTreino();

   /**
    * Objeto de treino sequencial da rede.
    * @param historicoCusto lista de custos da rede durante cada época de treino.
    */
   public Treino(ArrayList<Double> historicoCusto, boolean calcularHistorico){
      this.historico = historicoCusto;
      this.calcularHistorico = calcularHistorico;
   }

   /**
    * Configura o cálculo de custos da rede neural durante cada
    * época de treinamento.
    * @param calcularHistorico true armazena os valores de custo da rede, false não faz nada.
    */
   public void configurarHistorico(boolean calcularHistorico){
      this.calcularHistorico = calcularHistorico;
   }

   /**
    * Treina a rede neural calculando os erros dos neuronios, seus gradientes para cada peso e 
    * passando essas informações para o otimizador configurado ajustar os pesos.
    * @param rede instância da rede.
    * @param perda função de perda (ou custo) usada para calcular os erros da rede.
    * @param otimizador otimizador configurado da rede.
    * @param entradas dados de entrada para o treino.
    * @param saidas dados de saída correspondente as entradas para o treino.
    * @param epochs quantidade de épocas de treinamento.
    * @param embaralhar embaralhar dados de treino para cada época.
    */
   public void treino(RedeNeural rede, Perda perda, Otimizador otimizador, double[][] entradas, double[][] saidas, int epochs){
      double[] entrada = new double[entradas[0].length];// quantidade de colunas da entrada
      double[] saida = new double[saidas[0].length];// quantidade de colunas da saída

      boolean embaralhar = true;
      if(otimizador instanceof GD || otimizador instanceof GDM){
         embaralhar = false;
      }

      //transformar a rede numa lista de camadas pra facilitar minha vida
      Camada[] redec = rede.obterCamadas();

      for(int i = 0; i < epochs; i++){
         //aplicar gradiente estocástico
         //alterando a organização dos dados em cada época
         if(embaralhar) aux.embaralharDados(entradas, saidas);

         //percorrer amostras
         for(int j = 0; j < entradas.length; j++){
            //preencher dados de entrada e saída
            System.arraycopy(entradas[j], 0, entrada, 0, entrada.length);
            System.arraycopy(saidas[j], 0, saida, 0, saida.length);

            //calcular desempenho da rede,
            //erros e gradientes
            //e atualizar os pesos
            rede.calcularSaida(entrada);
            backpropagation(redec, perda, saida);
            otimizador.atualizar(redec);
         }

         //feedback de avanço da rede
         if(calcularHistorico){
            historico.add(perda.calcular(rede, entradas, saidas));
         }
      }
   }

   /**
    * Retropropaga o erro da rede neural de acordo com os dados de entrada e saída esperados e calcula
    * os gradientes dos pesos de cada neurônio.
    * @param redec Rede Neural em formato de lista de camadas.
    * @param saidas array com as saídas esperadas das amostras.
    */
   private void backpropagation(Camada[] redec, Perda perda, double[] saidas){
      aux.calcularErroSaida(redec, perda, saidas);
      aux.calcularErroOcultas(redec);
      calcularGradientes(redec);
   }

   /**
    * Método exclusivo para separar o cálculo dos gradientes das conexões de cada
    * neurônio dentro da rede.
    * @param redec Rede Neural em formato de lista de camadas.
    */
   private void calcularGradientes(Camada[] redec){
      for(int i = 0; i < redec.length; i++){ 
         
         int nNeuronios = redec[i].quantidadeNeuronios();
         for(int j = 0; j < nNeuronios; j++){
            redec[i].neuronio(j).calcularGradiente();
         }
      }
   }

}
