package exemplos;

import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import render.JanelaRede;
import rna.ativacoes.*;
import rna.avaliacao.perda.EntropiaCruzada;
import rna.estrutura.Camada;
import rna.estrutura.Neuronio;
import rna.estrutura.RedeNeural;
import rna.inicializadores.*;
import rna.otimizadores.SGD;
import rna.serializacao.Serializador;
import utilitarios.ged.Ged;
import utilitarios.geim.Geim;
import utilitarios.ged.Dados;

@SuppressWarnings("unused")
public class TestandoMnist{
   static Ged ged = new Ged();
   static Geim geim = new Geim();

   public static void main(String[] args){
      ged.limparConsole();

      //lendo as imagens
      BufferedImage[] imagens = new BufferedImage[10];
      for(int i = 0; i < 10; i++){
         String caminho = "/dados/mnist/" + i + ".png";
         BufferedImage imagem = geim.lerImagem(caminho);
         imagens[i] = imagem;
      }

      //serializando cada imagem para uma linha de dados
      int[][] imagensVetorizadas = new int[10][];
      for(int i = 0; i < 10; i++){
         int[][] imagem = geim.obterCinza(imagens[i]);
         int[] arrayImagem = (int[]) ged.matVetorizar(imagem);
         imagensVetorizadas[i] = arrayImagem;
      }

      Dados dados = new Dados(imagensVetorizadas);
      ged.adicionarColuna(dados);
      int ultimaColuna = dados.shape()[1] - 1;
      for(int i = 0; i < 10; i++){
         dados.editarItem(i, ultimaColuna, Integer.toString(i));
      }
      ged.categorizar(dados, ultimaColuna);
      int[] shape = dados.shape();

      double[][] treino = ged.dadosParaDouble(dados);
      double[][] treinoX = (double[][]) ged.separarDadosEntrada(treino, shape[1]-10);
      double[][] treinoY = (double[][]) ged.separarDadosSaida(treino, 10);
      System.out.println(dados.shapeInfo());

      //criação da rede
      int[] arq = {784, 16, 16, 10};
      RedeNeural rede = new RedeNeural(arq);
      rede.compilar(new EntropiaCruzada(), new SGD(0.0001, 0.9), new LeCun());
      rede.configurarAtivacao(new LeakyReLU());
      rede.configurarAtivacao(rede.obterCamadaSaida(), new Softmax());
      System.out.println(rede.info());

      rede.treinar(treinoX, treinoY, 1_000);

      System.out.println("Número de parâmetros da rede = " + rede.obterQuantidadeParametros());
      System.out.println("perda = " + rede.avaliador.entropiaCruzada(treinoX, treinoY));
      testarRede(rede, treinoX, treinoY);
   }

   static void testarRede(RedeNeural rede, double[][] entrada, double[][] saida){

      for(int i = 0; i < entrada.length; i++){
         rede.calcularSaida(entrada[i]);
         double[] saidaRede = rede.obterSaidas();
         
         System.out.println("Real - Previsto -> " + indiceMaior(saida[i]) + " - " + indiceMaior(saidaRede));
      }
   }

   static void imprimirImagem(int[][] dados){
      for(int y = 0; y < dados.length; y++){
         for(int x = 0; x < dados[y].length; x++){
            if(dados[y][x] == 0) System.out.print("    ");
            else if(dados[y][x] < 10) System.out.print("00" + dados[y][x] + " ");
            else if(dados[y][x] < 100) System.out.print("0" + dados[y][x] + " ");
            else System.out.print(dados[y][x] + " ");
         }
         System.out.println();
      }
   }

   static String formatarDecimal(double valor, int casas){
      String valorFormatado = "";

      String formato = "#.";
      for(int i = 0; i < casas; i++) formato += "#";

      DecimalFormat df = new DecimalFormat(formato);
      valorFormatado = df.format(valor);

      return valorFormatado;
   }

   static int indiceMaior(double[] arr){
      double maiorValor = arr[0];
      int indice = 0;

      for(int i = 0; i < arr.length; i++){
         if(arr[i] > maiorValor){
            maiorValor = arr[i];
            indice = i;
         }
      }

      return indice;
   }

   public static void exportarHistoricoCustos(RedeNeural rede, Ged ged){
      System.out.println("Exportando histórico de custo");
      ArrayList<Double> custos = rede.obterHistoricoCusto();
      double[][] dadosErro = new double[custos.size()][1];

      for(int i = 0; i < dadosErro.length; i++){
         dadosErro[i][0] = custos.get(i);
      }

      Dados dados = new Dados(dadosErro);
      ged.exportarCsv(dados, "historico-custo");
   }
}
