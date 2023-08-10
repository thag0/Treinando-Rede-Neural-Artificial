package rna.treino;

import java.util.ArrayList;

import rna.Camada;
import rna.Neuronio;
import rna.RedeNeural;
import rna.otimizadores.Otimizador;


/**
 * Classe de treino da rede neural.
 */
public class Treino{
   AuxiliaresTreino auxiliarTreino = new AuxiliaresTreino();

   public boolean calcularHistoricoCusto;

   /**
    * Dados de custo da rede durante o treinamento.
    */
   ArrayList<Double> historicoCusto = new ArrayList<>();

   public Treino(){
      this.calcularHistoricoCusto = false;
   }


   /**
    * Treina a rede neural calculando os erros dos neuronios, seus gradientes para cada peso e 
    * passando essas informações para o otimizador configurado ajustar os pesos.
    * @param rede Instância da rede.
    * @param otimizador Otimizador configurado da rede.
    * @param entradas dados de entrada para o treino.
    * @param saidas dados de saída correspondente as entradas para o treino.
    * @param epochs quantidade de épocas de treinamento.
    * @param embaralhar embaralhar dados de treino para cada época.
    */
   public void treino(RedeNeural rede, Otimizador otimizador, double[][] entradas, double[][] saidas, int epochs, boolean embaralhar){
      double[] dadosEntrada = new double[entradas[0].length];//tamanho de colunas da entrada
      double[] dadosSaida = new double[saidas[0].length];//tamanho de colunas da saída
      
      int[] indices = new int[entradas.length];
      for(int i = 0; i < indices.length; i++) indices[i] = i;

      //transformar a rede numa lista de camadas pra facilitar minha vida
      ArrayList<Camada> redec = new ArrayList<>();
      redec.add(rede.obterCamadaEntrada());
      for(int i = 0; i < rede.obterQuantidadeOcultas(); i++){
         redec.add(rede.obterCamadaOculta(i));
      }
      redec.add(rede.obterCamadaSaida());

      if(calcularHistoricoCusto) this.historicoCusto.clear();//evitar acumulo de memória

      int i, j;
      for(i = 0; i < epochs; i++){//quantidade de épocas
         if(embaralhar) auxiliarTreino.embaralharDados(entradas, saidas);

         for(j = 0; j < entradas.length; j++){//percorrer amostras
            //preencher dados de entrada e saída
            dadosEntrada = entradas[j];
            dadosSaida = saidas[j];

            rede.calcularSaida(dadosEntrada);
            backpropagation(redec, rede.obterTaxaAprendizagem(), dadosSaida);
            
            otimizador.atualizar(redec, rede.obterTaxaAprendizagem(), rede.obterTaxaMomentum());
         }

         if(calcularHistoricoCusto){
            if(rede.obterCamadaSaida().softmax) historicoCusto.add(rede.entropiaCruzada(entradas, saidas));
            else historicoCusto.add(rede.erroMedioQuadrado(entradas, saidas));
         }
      }
   }


   /**
    * Retropropaga o erro da rede neural de acordo com os dados de entrada e saída esperados e calcula
    * os gradientes dos pesos de cada neurônio.
    * @param saida array com as saídas esperadas das amostras.
    */
   private void backpropagation(ArrayList<Camada> redec, double taxaAprendizagem, double[] saidas){
      //erro da saída
      calcularErroSaida(redec, saidas);

      //erro ocultas
      //começar da ultima oculta
      for(int i = redec.size()-2; i >= 1; i--){// percorrer camadas ocultas de trás pra frente
         
         Camada camadaAtual = redec.get(i);
         int qNeuronioAtual = camadaAtual.obterQuantidadeNeuronios();
         if(camadaAtual.temBias) qNeuronioAtual -= 1;
         for (int j = 0; j < qNeuronioAtual; j++){//percorrer neurônios da camada atual
         
            Neuronio neuronio = camadaAtual.neuronios[j];
            double somaErros = 0.0;
            for(Neuronio neuronioProximo : redec.get(i+1).neuronios){//percorrer neurônios da camada seguinte
               somaErros += neuronioProximo.pesos[j] * neuronioProximo.erro;
            }
            neuronio.erro = somaErros * camadaAtual.funcaoAtivacaoDx(neuronio.somatorio);
         }
      }

      //cálculo dos gradientes
      for(int i = 1; i < redec.size(); i++){//percorrer rede 
         
         Camada camadaAtual = redec.get(i);
         Camada camadaAnterior = redec.get(i-1);

         //não precisa e nem faz diferença calcular os gradientes dos bias
         int nNeuronios = camadaAtual.obterQuantidadeNeuronios();
         nNeuronios -= (camadaAtual.temBias) ? 1 : 0;
         for(int j = 0; j < nNeuronios; j++){//percorrer neurônios da camada atual
            
            Neuronio neuronio = camadaAtual.neuronios[j];
            for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
               neuronio.gradiente[k] = taxaAprendizagem * neuronio.erro * camadaAnterior.neuronios[k].saida;
            }
         }
      } 
   }


   /**
    * Método exclusivo para separar a forma de calcular os erros da camada de saída.
    * Dando suporte não apenas para problemas de regressão.
    * <p>
    *    Isso ainda ta em teste para problemas de classificação, para regressão funciona normalmente.
    * </p>
    * @param saidas array com as saídas esperadas
    */
   private void calcularErroSaida(ArrayList<Camada> redec, double[] saidas){
      Camada saida = redec.get(redec.size()-1);
      if(saida.argmax){//classificação


      }else if(saida.softmax){//classificação
         for(int i = 0; i < saida.neuronios.length; i++){
            Neuronio neuronio = saida.neuronios[i];
            neuronio.erro = (saidas[i] - neuronio.saida);
         }
      
      }else{//regressão
         for(int i = 0; i < saida.neuronios.length; i++){
            Neuronio neuronio = saida.neuronios[i];
            neuronio.erro = ((saidas[i] - neuronio.saida) * saida.funcaoAtivacaoDx(neuronio.somatorio));
         }
      }
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