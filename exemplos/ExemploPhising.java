package exemplos;

import rna.estrutura.RedeNeural;
import rna.otimizadores.SGD;
import rna.inicializadores.*;
import rna.ativacoes.*;
import utilitarios.ged.Dados;
import utilitarios.ged.Ged;

public class ExemploPhising{

   public static void main(String[] args){
      limparConsole();
      Ged ged = new Ged();

      //manusear dados
      //tratamento específico para os dados de phishing
      //removendo coluna de id e nomes das classes
      Dados phishing = ged.lerCsv("./dados/datasets-maiores/PhishingData.csv");
      ged.removerLinha(phishing, 0);
      ged.removerColuna(phishing, 0);
      ged.removerNaoNumericos(phishing);
      ged.removerDuplicadas(phishing);

      //converter os dados da estrutura de texto em valores 
      //numéricos para o treino da rede neural.
      //separar em treino e teste para evitar overfitting
      double[][] dados = ged.dadosParaDouble(phishing);
      ged.embaralharDados(dados);
      double[][][] treinoTeste = ged.separarTreinoTeste(dados, 0.3f);
      double[][] treino = treinoTeste[0];
      double[][] teste = treinoTeste[1];

      double[][] treinoX, treinoY, testeX, testeY;
      int colunasDados = 9;// quantidade de características dos dados (feature)
      int colunasClasses = 1;// quantidade de classificações dos dados (class)

      treinoX = ged.separarDadosEntrada(treino, colunasDados);
      treinoY = ged.separarDadosSaida(treino, colunasClasses);
      testeX = ged.separarDadosEntrada(teste, colunasDados);
      testeY = ged.separarDadosSaida(teste, colunasClasses);

      //criando, configurando e treinando a rede neural.
      //os valores de configuração não devem ser tomados como regra e 
      //devem se adaptar ao problema e os dados apresentados.
      int[] arq = {colunasDados, 20, 15, colunasClasses};
      RedeNeural rede = new RedeNeural(arq);
      rede.compilar(new SGD(), new Xavier());
      rede.configurarAtivacao(new Sigmoid());
      rede.configurarAtivacao(rede.obterCamadaSaida(), new TanH());
      rede.treinar(treinoX, treinoY, 10_000);

      // avaliando os resultados da rede neural
      double perda = rede.avaliador.erroMedioQuadrado(testeX, testeY);
      System.out.println(rede.info());
      System.out.println("Perda: " + perda);
   }


   public static void limparConsole(){
      try{
         String nomeSistema = System.getProperty("os.name");

         if(nomeSistema.contains("Windows")){
         new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            return;
         }else{
            for (int i = 0; i < 100; i++){
               System.out.println();
            }
         }
      }catch(Exception e){
         return;
      }
   }
}
