package exemplos;
import java.util.ArrayList;

import render.JanelaRede;
import rna.RedeNeural;
import utilitarios.ged.Ged;

public class ExemploPhising{

   public static void main(String[] args){
      limparConsole();
      Ged ged = new Ged();

      //manusear dados
      //tratamento específico para os dados de phishing
      //removendo coluna de id e nomes das classes
      ArrayList<String[]> phishing = ged.lerCsv("./dados/datasets-maiores/PhishingData.csv");
      ged.removerLinha(phishing, 0);
      ged.removerColuna(phishing, 0);
      ged.removerNaoNumericos(phishing);

      //converter os dados da estrutura de texto em valores 
      //numéricos para o treino da rede neural.
      //separar em treino e teste para evitar overfitting
      double[][] dados = ged.listaParaDadosDouble(phishing);
      double[][][] treinoTeste = ged.separarTreinoTeste(dados, 0.3f);
      double[][] treino = treinoTeste[0];
      double[][] teste = treinoTeste[1];
      ged.embaralharDados(treino);
      ged.embaralharDados(teste);

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
      int[] arq = {colunasDados, 18, 16, 12, colunasClasses};
      RedeNeural rede = new RedeNeural(arq);
      rede.configurarTaxaAprendizagem(0.01);
      rede.configurarMomentum(0.9);
      rede.configurarOtimizador(5);
      rede.compilar();
      rede.configurarFuncaoAtivacao(2);
      rede.configurarFuncaoAtivacao(rede.obterCamadaSaida(), 3);
      rede.treinar(treinoX, treinoY, 4_000);


      // avaliando os resultados da rede neural
      double precisao = rede.calcularPrecisao(testeX, testeY);
      double custo = rede.erroMedioQuadrado(testeX, testeY);
      System.out.println(rede.obterInformacoes());
      System.out.println("Custo: " + custo);
      System.out.println("Precisão: " + (precisao * 100) + "%");

      JanelaRede jr = new JanelaRede();
      jr.desenhar(rede);
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
