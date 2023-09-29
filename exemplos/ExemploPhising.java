package exemplos;

import rna.estrutura.RedeNeural;
import rna.otimizadores.SGD;
import rna.inicializadores.*;
import rna.ativacoes.*;
import utilitarios.ged.Dados;
import utilitarios.ged.Ged;

public class ExemploPhising{

   public static void main(String[] args){
      Ged ged = new Ged();
      ged.limparConsole();

      //manusear dados
      //tratamento específico para os dados de phishing
      //removendo coluna de id e nomes das classes
      Dados phishing = ged.lerCsv("./dados/datasets-maiores/PhishingData.csv");
      ged.removerLinha(phishing, 0);
      ged.removerColuna(phishing, 0);
      ged.removerNaoNumericos(phishing);
      ged.removerDuplicadas(phishing);

      for(int i = 0; i < phishing.shape()[1]; i++){
         phishing.normalizar(i);
      }

      //converter os dados da estrutura de texto em valores 
      //numéricos para o treino da rede neural.
      //separar em treino e teste para evitar overfitting
      double[][] dados = ged.dadosParaDouble(phishing);
      ged.embaralharDados(dados);
      double[][][] treinoTeste = (double[][][]) ged.separarTreinoTeste(dados, 0.25f);
      double[][] treino = treinoTeste[0];
      double[][] teste = treinoTeste[1];

      double[][] treinoX, treinoY, testeX, testeY;
      int colunasDados = 9;// quantidade de características dos dados (feature)
      int colunasClasses = 1;// quantidade de classificações dos dados (class)

      treinoX = (double[][]) ged.separarDadosEntrada(treino, colunasDados);
      treinoY = (double[][]) ged.separarDadosSaida(treino, colunasClasses);
      testeX  = (double[][]) ged.separarDadosEntrada(teste, colunasDados);
      testeY  = (double[][]) ged.separarDadosSaida(teste, colunasClasses);

      //criando, configurando e treinando a rede neural.
      //os valores de configuração não devem ser tomados como regra e 
      //devem se adaptar ao problema e os dados apresentados.
      int[] arq = {colunasDados, 12, 12, 12, colunasClasses};
      RedeNeural rede = new RedeNeural(arq);
      rede.compilar(new SGD(0.0001, 0.99, true), new Xavier());
      rede.configurarAtivacao(new Sigmoid());
      rede.treinar(treinoX, treinoY, 15_000);

      // avaliando os resultados da rede neural
      double perda = rede.avaliador.erroMedioQuadrado(testeX, testeY);
      System.out.println(rede.info());
      System.out.println("Perda: " + perda);
      System.out.println(1 - perda);
   }
}
