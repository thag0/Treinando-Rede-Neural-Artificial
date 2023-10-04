package rna.treinamento;

import java.util.ArrayList;
import java.util.Random;

import rna.avaliacao.perda.Perda;
import rna.estrutura.Camada;
import rna.estrutura.Neuronio;
import rna.estrutura.RedeNeural;
import rna.otimizadores.GD;
import rna.otimizadores.GDM;
import rna.otimizadores.Otimizador;

/**
 * Classe dedicada para lidar com o treinamento em lotes da rede neural.
 * <p>
 *    Implementa a lógica de treino em lotes da rede, calculando erros, 
 *    gradientes e atualizando seus pesos de acordo com o otimizador configurado.
 * </p>
 */
class TreinoLote{
   public boolean calcularHistorico = false;
   ArrayList<Double> historico;
   AuxiliarTreino aux = new AuxiliarTreino();

   Random random = new Random();

   /**
    * Implementação do treino em lote.
    * @param historicoCusto
    */
   public TreinoLote(ArrayList<Double> historicoCusto, boolean calcularHistorico){
      this.historico = historicoCusto;
      this.calcularHistorico = calcularHistorico;
   }

   /**
    * Configura a seed inicial do gerador de números aleatórios.
    * @param seed nova seed.
    */
    public void configurarSeed(long seed){
      this.random.setSeed(seed);
      this.aux.configurarSeed(seed);
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
    * @param tamLote tamanho do lote.
    */
   public void treino(RedeNeural rede, Perda perda, Otimizador otimizador, double[][] entradas, double[][] saidas, int epochs, int tamLote){      
      Camada[] redec = rede.obterCamadas();

      boolean embaralhar = true;
      if(otimizador instanceof GD || otimizador instanceof GDM){
         embaralhar = false;
      }

      for(int i = 0; i < epochs; i++){
         if(embaralhar) aux.embaralharDados(entradas, saidas);

         for(int j = 0; j < entradas.length; j += tamLote){
            int fimIndice = Math.min(j + tamLote, entradas.length);
            double[][] entradaLote = aux.obterSubMatriz(entradas, j, fimIndice);
            double[][] saidaLote = aux.obterSubMatriz(saidas, j, fimIndice);

            //reiniciar gradiente do lote
            zerarGradientesAcumulados(redec);
            for(int k = 0; k < entradaLote.length; k++){
               double[] entrada = entradaLote[k];
               double[] saida = saidaLote[k];

               rede.calcularSaida(entrada);
               backpropagationLote(redec, perda, saida);
            }

            //normalizar gradientes para enviar pro otimizador
            calcularMediaGradientesLote(redec, entradaLote.length);
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
    * os gradientes acumulados de cada lote.
    * @param redec Rede Neural em formato de lista de camadas.
    * @param taxaAprendizagem valor de taxa de aprendizagem da rede neural.
    * @param saidas array com as saídas esperadas das amostras.
    */
   private void backpropagationLote(Camada[] redec, Perda perda, double[] saidas){
      aux.calcularErroSaida(redec[redec.length-1], perda, saidas);
      aux.calcularErroOcultas(redec);
      calcularGradientesAcumulados(redec);
   }

   /**
    * Zera todos os gradientes dos neurônios para o cálculo do gradiente em lote.
    * @param redec Rede Neural em formato de array de camadas.
    */
   void zerarGradientesAcumulados(Camada[] redec){
      for(int i = 0; i < redec.length; i++){ 
         for(int j = 0; j < redec[i].quantidadeNeuronios(); j++){
            
            Neuronio neuronio = redec[i].neuronio(j);
            for(int k = 0; k < neuronio.gradientesAcumulados.length; k++){
               neuronio.gradientesAcumulados[k] = 0;
            }
         }
      }
   }

   /**
    * Método exclusivo para separar o cálculo dos gradientes em lote das conexões de cada
    * neurônio dentro da rede.
    * @param redec Rede Neural em formato de lista de camadas.
    * @param taxaAprendizagem valor de taxa de aprendizagem da rede neural.
    */
   private void calcularGradientesAcumulados(Camada[] redec){
      for(int i = 0; i < redec.length; i++){ 
         for(int j = 0; j < redec[i].quantidadeNeuronios(); j++){
            
            Neuronio neuronio = redec[i].neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){
               neuronio.gradientes[k] = -neuronio.erro * neuronio.entradas[k];
               neuronio.gradientesAcumulados[k] += neuronio.gradientes[k];
            }
         }
      }
   }

   /**
    * Método exlusivo para separar a forma de calcular a média dos gradientes do lote.
    * @param redec Rede Neural em formato de lista de camadas.
    * @param tamLote tamanho do lote.
    */
   private void calcularMediaGradientesLote(Camada[] redec, int tamLote){
      for(int i = 0; i < redec.length; i++){
         for(int j = 0; j < redec[i].quantidadeNeuronios(); j++){

            Neuronio neuronio = redec[i].neuronio(j);
            for(int k = 0; k < neuronio.gradientes.length; k++){
               neuronio.gradientes[k] = neuronio.gradientesAcumulados[k] / tamLote;
            }
         }
      }
   }
}
