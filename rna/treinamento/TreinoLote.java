package rna.treinamento;

import java.util.ArrayList;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;
import rna.estrutura.RedeNeural;
import rna.otimizadores.Otimizador;

//TODO verificar se está funcionando após as modificações do calculo do gradiente

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

   /**
    * Implementação do treino em lote.
    * @param historicoCusto
    */
   public TreinoLote(ArrayList<Double> historicoCusto, boolean calcularHistorico){
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
    * @param otimizador otimizador configurado da rede.
    * @param entradas dados de entrada para o treino.
    * @param saidas dados de saída correspondente as entradas para o treino.
    * @param epochs quantidade de épocas de treinamento.
    * @param embaralhar embaralhar dados de treino para cada época.
    * @param tamLote tamanho do lote.
    */
   public void treino(RedeNeural rede, Otimizador otimizador, double[][] entradas, double[][] saidas, int epochs, boolean embaralhar, int tamLote){
      Camada[] redec = aux.redeParaCamadas(rede);

      for(int i = 0; i < epochs; i++){
         if(embaralhar) aux.embaralharDados(entradas, saidas);

         for(int j = 0; j < entradas.length; j += tamLote){
            int fimIndice = Math.min(j + tamLote, entradas.length);
            double[][] entradaLote = aux.obterSubMatriz(entradas, j, fimIndice);
            double[][] saidaLote = aux.obterSubMatriz(saidas, j, fimIndice);

            //reiniciar gradiente do lote
            aux.zerarGradientesAcumulados(redec);
            for(int k = 0; k < entradaLote.length; k++){
               double[] entrada = entradaLote[k];
               double[] saida = saidaLote[k];

               rede.calcularSaida(entrada);
               backpropagationLote(redec, saida);
            }

            //normalizar gradientes para enviar pro otimizador
            calcularMediaGradientesLote(redec, entradaLote.length);
            otimizador.atualizar(redec);
         }

         //feedback de avanço da rede
         if(calcularHistorico){
            if(rede.obterCamadaSaida().temSoftmax()) historico.add(rede.avaliador.entropiaCruzada(entradas, saidas));
            else historico.add(rede.avaliador.erroMedioQuadrado(entradas, saidas));
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
   private void backpropagationLote(Camada[] redec, double[] saidas){
      aux.calcularErroSaida(redec, saidas);
      aux.calcularErroOcultas(redec);
      calcularGradientesAcumulados(redec);
   }

   /**
    * Método exclusivo para separar o cálculo dos gradientes em lote das conexões de cada
    * neurônio dentro da rede.
    * @param redec Rede Neural em formato de lista de camadas.
    * @param taxaAprendizagem valor de taxa de aprendizagem da rede neural.
    */
   private void calcularGradientesAcumulados(Camada[] redec){
      //percorrer rede, excluindo camada de entrada
      for(int i = 1; i < redec.length; i++){ 
         
         Camada camadaAtual = redec[i];
         Camada camadaAnterior = redec[i-1];

         //não precisa e nem faz diferença calcular os gradientes dos bias
         int nNeuronios = camadaAtual.quantidadeNeuronios();
         nNeuronios -= (camadaAtual.temBias()) ? 1 : 0;
         for(int j = 0; j < nNeuronios; j++){//percorrer neurônios da camada atual
            
            Neuronio neuronio = camadaAtual.neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
               neuronio.gradienteAcumulado[k] += neuronio.erro * camadaAnterior.neuronio(k).saida;
            }
         }
      }
   }

   /**
    * Método exlusivo para separar a forma de calcular a média dos gradientes do lote.
    * @param redec Rede Neural em formato de lista de camadas.
    * @param tamanhoLote tamanho do lote.
    */
   private void calcularMediaGradientesLote(Camada[] redec, int tamanhoLote){
      for(int i = 1; i < redec.length; i++){ 
         
         Camada camadaAtual = redec[i];
         int nNeuronios = camadaAtual.quantidadeNeuronios();
         nNeuronios -= (camadaAtual.temBias()) ? 1 : 0;
         for(int j = 0; j < nNeuronios; j++){//percorrer neurônios da camada atual
            
            Neuronio neuronio = camadaAtual.neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
               neuronio.gradiente[k] = neuronio.gradienteAcumulado[k] / tamanhoLote;
            }
         }
      }
   }
}
