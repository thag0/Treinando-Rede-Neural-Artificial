package rna.treinamento;

import java.util.ArrayList;

import rna.Camada;
import rna.Neuronio;
import rna.RedeNeural;
import rna.otimizadores.Otimizador;

//TODO 
//implementar treino em lotes multithread

/**
 * <p>
 *    Classe de treino da rede neural.
 * </p>
 * To tentando separar bem e deixar o mais abstrato possível pra 
 * facilitar novas implementações e reaproveitar código.
 */
public class Treino{
   
   //Dados de custo da rede durante o treinamento.
   ArrayList<Double> historicoCusto = new ArrayList<>();
   public boolean calcularHistoricoCusto;
    
   AuxiliaresTreino auxiliarTreino = new AuxiliaresTreino();
   TreinoLote treinoLote = new TreinoLote(historicoCusto);

   public Treino(){
      this.calcularHistoricoCusto = false;
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
    */
   public void treino(RedeNeural rede, Otimizador otimizador, double[][] entradas, double[][] saidas, int epochs, boolean embaralhar){
      double[] dadosEntrada = new double[entradas[0].length];//tamanho de colunas da entrada
      double[] dadosSaida = new double[saidas[0].length];//tamanho de colunas da saída

      //transformar a rede numa lista de camadas pra facilitar minha vida
      ArrayList<Camada> redec = redeParaCamadas(rede);

      for(int i = 0; i < epochs; i++){//quantidade de épocas
         //aplicar gradiente estocástico
         if(embaralhar) auxiliarTreino.embaralharDados(entradas, saidas);

         for(int j = 0; j < entradas.length; j++){//percorrer amostras
            //preencher dados de entrada e saída
            dadosEntrada = entradas[j];
            dadosSaida = saidas[j];

            //calcular desempenho da rede,
            //erros e gradientes
            //e atualizar os pesos
            rede.calcularSaida(dadosEntrada);
            backpropagation(redec, rede.obterTaxaAprendizagem(), dadosSaida);
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
      treinoLote.treino(rede, otimizador, entradas, saidas, epochs, embaralhar, tamLote);
   }


   /**
    * Serializa a rede no formato de lista de camadas pra facilitar (a minha vida)
    * o manuseio e generalização das operações.
    * @param rede Rede Neural
    * @return lista de camadas da rede neural.
    */
   private ArrayList<Camada> redeParaCamadas(RedeNeural rede){
      ArrayList<Camada> redec = new ArrayList<>();

      redec.add(rede.obterCamadaEntrada());
      for(int i = 0; i < rede.obterQuantidadeOcultas(); i++){
         redec.add(rede.obterCamadaOculta(i));
      }
      redec.add(rede.obterCamadaSaida());

      return redec;
   }


   /**
    * Retropropaga o erro da rede neural de acordo com os dados de entrada e saída esperados e calcula
    * os gradientes dos pesos de cada neurônio.
    * @param redec Rede Neural em formato de lista de camadas.
    * @param taxaAprendizagem valor de taxa de aprendizagem da rede neural.
    * @param saidas array com as saídas esperadas das amostras.
    */
   private void backpropagation(ArrayList<Camada> redec, double taxaAprendizagem, double[] saidas){
      auxiliarTreino.calcularErroSaida(redec, saidas);
      auxiliarTreino.calcularErroOcultas(redec);
      calcularGradientes(redec, taxaAprendizagem);
   }


   /**
    * Método exclusivo para separar o cálculo dos gradientes das conexões de cada
    * neurônio dentro da rede.
    * @param redec Rede Neural em formato de lista de camadas.
    * @param taxaAprendizagem valor de taxa de aprendizagem da rede neural.
    */
   private void calcularGradientes(ArrayList<Camada> redec, double taxaAprendizagem){
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
               neuronio.gradiente[k] = taxaAprendizagem * neuronio.erro * camadaAnterior.neuronio(k).saida;
            }
         }
      }
   }


   /**
    * Configura o cálculo do custo da rede neural durante o processo de treinamento.
    * A mesma configuração se aplica ao treino em lote.
    * @param calcularHistorico calcular ou não o histórico de custo.
    */
   public void configurarHistoricoCusto(boolean calcularHistorico){
      this.calcularHistoricoCusto = calcularHistorico;
      this.treinoLote.calcularHistoricoCusto = calcularHistorico;
   }


   /**
    * Retorna uma lista contendo os valores de custo da rede
    * a cada época de treinamento.
    * @return lista com os custo por época durante a fase de treinamento.
    */
   public ArrayList<Double> obterHistoricoCusto(){
      return this.historicoCusto;
   }
}
