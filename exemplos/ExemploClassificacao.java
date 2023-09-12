package exemplos;

import java.text.DecimalFormat;

import rna.ativacoes.Softmax;
import rna.estrutura.RedeNeural;
import rna.otimizadores.Adam;
import utilitarios.ged.Dados;
import utilitarios.ged.Ged;

public class ExemploClassificacao{
   
   public static void main(String[] args){
      limparConsole();
      Ged ged = new Ged();

      //carregando dados e tratando
      //removendo linha com nomes das categorias
      //tranformando a ultima coluna em categorização binária
      Dados iris = ged.lerCsv("./dados/datasets-maiores/iris.csv");
      ged.removerLinha(iris, 0);
      int[] shape = ged.shapeDados(iris);
      int ultimoIndice = shape[1]-1;
      ged.categorizar(iris, ultimoIndice);
      System.out.println("Shape dados = [" + shape[0] + ", " + shape[1] + "]");

      //separando dados de treino e teste
      double[][] dados = ged.dadosParaDouble(iris);
      ged.embaralharDados(dados);
      double[][][] treinoTeste = ged.separarTreinoTeste(dados, 0.25f);
      double[][] treino = treinoTeste[0];
      double[][] teste = treinoTeste[1];
      int qEntradas = 4;// dados de entrada (features)
      int qSaidas = 3;// classificações (class)

      ged.embaralharDados(treino);
      double[][] treinoEntrada = ged.separarDadosEntrada(treino, qEntradas);
      double[][] treinoSaida = ged.separarDadosSaida(treino, qSaidas);

      ged.embaralharDados(teste);
      double[][] testeEntrada = ged.separarDadosEntrada(teste, qEntradas);
      double[][] testeSaida = ged.separarDadosSaida(teste, qSaidas);

      //criando e configurando a rede neural
      int[] arq = {qEntradas, 4, 4, qSaidas};
      RedeNeural rede = new RedeNeural(arq);
      rede.configurarTaxaAprendizagem(0.001);
      rede.configurarMomentum(0.99);
      rede.configurarOtimizador(new Adam());
      rede.configurarInicializacaoPesos(2);
      rede.compilar();
      rede.configurarFuncaoAtivacao(2);
      rede.configurarFuncaoAtivacao(rede.obterCamadaSaida(), new Softmax());
      
      //treinando e avaliando os resultados
      rede.treinar(treinoEntrada, treinoSaida, 3_000);
      System.out.println(rede.info());
      double acuraria = rede.avaliador.acuracia(testeEntrada, testeSaida);
      double custo = rede.avaliador.entropiaCruzada(testeEntrada, testeSaida);
      System.out.println("Acurácia = " + (acuraria * 100) + "%");
      System.out.println("Custo = " + custo);

      int[][] matrizConfusao = rede.avaliador.matrizConfusao(testeEntrada, testeSaida);
      Dados m = new Dados(matrizConfusao);
      m.editarNome("Matriz confusão");
      m.imprimir();
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


   public static void compararSaidaRede(RedeNeural rede, double[][] dadosEntrada, double[][] dadosSaida, String texto){
      int nEntrada = rede.obterCamadaEntrada().quantidadeNeuronios();
      nEntrada -= (rede.obterCamadaEntrada().temBias()) ? 1 : 0;

      int nSaida = rede.obterCamadaSaida().quantidadeNeuronios();

      double[] entrada_rede = new double[nEntrada];
      double[] saida_rede = new double[nSaida];

      System.out.println("\n" + texto);

      //mostrar saída da rede comparada aos dados
      for(int i = 0; i < dadosEntrada.length; i++){
         for(int j = 0; j < dadosEntrada[0].length; j++){
            entrada_rede[j] = dadosEntrada[i][j];
         }

         rede.calcularSaida(entrada_rede);
         saida_rede = rede.obterSaidas();

         //apenas formatação
         if(i < 10) System.out.print("Dado 00" + i + " |");
         else if(i < 100) System.out.print("Dado 0" + i + " |");
         else System.out.print("Dado " + i + " |");
         for(int j = 0; j < entrada_rede.length; j++){
            System.out.print(" " + entrada_rede[j] + " ");
         }

         System.out.print(" - ");
         for(int j = 0; j < dadosSaida[0].length; j++){
            System.out.print(" " + dadosSaida[i][j]);
         }
         System.out.print(" | Rede ->");
         for(int j = 0; j < nSaida; j++){
            System.out.print("  " + formatarFloat(saida_rede[j]));
         }
         System.out.println();
      }
   }


   public static String formatarFloat(double valor){
      String valorFormatado = "";

      DecimalFormat df = new DecimalFormat("#.####");
      valorFormatado = df.format(valor);

      return valorFormatado;
   }
}
