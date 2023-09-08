package rna.treinamento;

import java.io.Serializable;
import java.util.ArrayList;

import rna.Camada;
import rna.Neuronio;
import rna.RedeNeural;
import rna.otimizadores.Otimizador;


/**
 * Classe dedicada para lidar com o treinamento em lotes da rede neural.
 */
class TreinoLote implements Serializable{
   AuxiliaresTreino auxiliarTreino = new AuxiliaresTreino();

   public boolean calcularHistoricoCusto = false;
   ArrayList<Double> historicoCusto;//salvar mesmo historico de custo


   /**
    * Implementação do treino em lote.
    * @param historicoCusto
    */
   public TreinoLote(ArrayList<Double> historicoCusto){
      this.historicoCusto = historicoCusto;
   }


   public void treino(RedeNeural rede, Otimizador otimizador, double[][] entradas, double[][] saidas, int epochs, boolean embaralhar, int tamanhoLote){
      ArrayList<Camada> redec = auxiliarTreino.redeParaCamadas(rede);

      for(int i = 0; i < epochs; i++){
         if(embaralhar) auxiliarTreino.embaralharDados(entradas, saidas);

         for(int j = 0; j < entradas.length; j += tamanhoLote){
            int fimIndice = Math.min(j + tamanhoLote, entradas.length);
            double[][] entradaLote = auxiliarTreino.obterSubMatriz(entradas, j, fimIndice);
            double[][] saidaLote = auxiliarTreino.obterSubMatriz(saidas, j, fimIndice);

            //reiniciar gradiente do lote
            auxiliarTreino.zerarGradientesAcumulados(redec);
            for(int k = 0; k < entradaLote.length; k++){
               double[] entrada = entradaLote[k];
               double[] saida = saidaLote[k];

               rede.calcularSaida(entrada);
               backpropagationLote(redec, rede.obterTaxaAprendizagem(), saida);
            }

            //normalizar gradientes para enviar pro otimizador
            calcularMediaGradientesLote(redec, entradaLote.length);
            otimizador.atualizar(redec, rede.obterTaxaAprendizagem(), rede.obterTaxaMomentum());
         }

         //feedback de avanço da rede
         if(calcularHistoricoCusto){
            if(rede.obterCamadaSaida().temSoftmax()) historicoCusto.add(rede.avaliador.entropiaCruzada(entradas, saidas));
            else historicoCusto.add(rede.avaliador.erroMedioQuadrado(entradas, saidas));
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
   private void backpropagationLote(ArrayList<Camada> redec, double taxaAprendizagem, double[] saidas){
      auxiliarTreino.calcularErroSaida(redec, saidas);
      auxiliarTreino.calcularErroOcultas(redec);
      calcularGradientesAcumulados(redec, taxaAprendizagem);
   }


   /**
    * Método exclusivo para separar o cálculo dos gradientes em lote das conexões de cada
    * neurônio dentro da rede.
    * @param redec Rede Neural em formato de lista de camadas.
    * @param taxaAprendizagem valor de taxa de aprendizagem da rede neural.
    */
   private void calcularGradientesAcumulados(ArrayList<Camada> redec, double taxaAprendizagem){
      //percorrer rede, excluindo camada de entrada
      for(int i = 1; i < redec.size(); i++){ 
         
         Camada camadaAtual = redec.get(i);
         Camada camadaAnterior = redec.get(i-1);

         //não precisa e nem faz diferença calcular os gradientes dos bias
         int nNeuronios = camadaAtual.obterQuantidadeNeuronios();
         nNeuronios -= (camadaAtual.temBias()) ? 1 : 0;
         for(int j = 0; j < nNeuronios; j++){//percorrer neurônios da camada atual
            
            Neuronio neuronio = camadaAtual.neuronio(j);
            for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
               neuronio.gradienteAcumulado[k] += taxaAprendizagem * neuronio.erro * camadaAnterior.neuronio(k).saida;
            }
         }
      }
   }


   /**
    * Método exlusivo para separar a forma de calcular a média dos gradientes do lote.
    * @param redec Rede Neural em formato de lista de camadas.
    * @param tamanhoLote tamanho do lote.
    */
   private void calcularMediaGradientesLote(ArrayList<Camada> redec, int tamanhoLote){
      for(int i = 1; i < redec.size(); i++){ 
         
         Camada camadaAtual = redec.get(i);
         int nNeuronios = camadaAtual.obterQuantidadeNeuronios();
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
