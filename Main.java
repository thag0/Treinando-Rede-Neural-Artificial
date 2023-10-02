import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import render.JanelaRede;
import render.JanelaTreino;

import rna.inicializadores.*;
import rna.ativacoes.*;
import rna.avaliacao.perda.*;
import rna.otimizadores.*;
import rna.estrutura.RedeNeural;
import utilitarios.ged.Dados;
import utilitarios.ged.Ged;
import utilitarios.geim.Geim;

class Main{
   //auxiliares
   static Ged ged = new Ged();
   static Geim geim = new Geim();
   
   // static final String caminhoArquivo = "/dados/imagens/dog.jpg";
   // static final String caminhoArquivo = "/dados/32x32/bloco.png";
   static final String caminhoArquivo = "/dados/mnist/8.png";

   static final String caminhoImagemExportada = "./resultados/imagem-ampliada";
   static final int epocas = 100*1000;
   static final float escalaRender = 8f;
   static final float escalaImagemExportada = 30f;

   // Sempre lembrar de quando mudar o dataset, também mudar a quantidade de dados de entrada e saída.
   
   public static void main(String[] args){
      ged.limparConsole();

      long t1, t2;
      long horas, minutos, segundos;

      //lendo os dados de entrada
      BufferedImage imagem = geim.lerImagem(caminhoArquivo);
      int nEntrada = 2;//quantidade de dados de entrada / entrada da rede
      int nSaida = 1;//quantidade de dados de saída / saída da rede
      
      //escolher a forma de importação dos dados
      double[][] dados;
      if(nSaida == 1) dados = geim.imagemParaDadosTreinoEscalaCinza(imagem);
      else if(nSaida == 3) dados = geim.imagemParaDadosTreinoRGB(imagem);
      else return;

      //separar para o treino
      double[][] treinoX = (double[][]) ged.separarDadosEntrada(dados, nEntrada);
      double[][] treinoY = (double[][]) ged.separarDadosSaida(dados, nSaida);
      System.out.println("Tamanho dos dados [" + dados.length + ", " + dados[0].length + "]");

      RedeNeural rede = criarRede(nEntrada, nSaida);
      System.out.println(rede.info());

      //treinar e marcar tempo
      t1 = System.nanoTime();
      System.out.println("Treinando.");
      
      treinoEmPainel(rede, imagem, treinoX, treinoY);
      
      t2 = System.nanoTime();
      long tempoDecorrido = t2 - t1;
      long segundosTotais = TimeUnit.NANOSECONDS.toSeconds(tempoDecorrido);
      horas = segundosTotais / 3600;
      minutos = (segundosTotais % 3600) / 60;
      segundos = segundosTotais % 60;

      //avaliar resultados
      double perda = rede.avaliador.erroMedioQuadrado(treinoX, treinoY);
      double precisao = 1 - rede.avaliador.erroMedioAbsoluto(treinoX, treinoY);

      System.out.println("Perda = " + perda);
      System.out.println("Precisão = " + (formatarDecimal(precisao*100, 2)) + "%");
      System.out.println("Tempo de treinamento: " + horas + "h " + minutos + "m " + segundos + "s");

      //salvando resultado
      System.out.println("\nSalvando imagem");
      if(nSaida == 1)geim.exportarImagemEscalaCinza(imagem, rede, escalaImagemExportada, caminhoImagemExportada);
      else if(nSaida == 3) geim.exportarImagemRGB(imagem, rede, escalaImagemExportada, caminhoImagemExportada);
      else System.out.println("Não é possível exportar a imagem");
   }

   public static RedeNeural criarRede(int entradas, int saidas){
      // int[] arq = {entradas, 82, 46, 46, saidas};//dog
      // int[] arq = {entradas, 42, 42, 42, saidas};//32x32
      int[] arq = {entradas, 13, 13, saidas};//28x28

      Perda perda = new ErroMedioQuadrado();
      Otimizador otm = new SGD(0.0001, 0.999);
      Inicializador ini = new Xavier();

      RedeNeural rede = new RedeNeural(arq);
      rede.compilar(perda, otm, ini);
      rede.configurarAtivacao(new Sigmoid());

      return rede;
   }

   public static void treinoEmPainel(RedeNeural rede, BufferedImage imagem, double[][] dadosEntrada, double[][] dadosSaida){
      final int fps = 60;
      int epocasPorFrame = 20;

      //acelerar o processo de desenho
      //bom em situações de janelas muito grandes
      int numThreads = (int)(Runtime.getRuntime().availableProcessors() * 0.75);

      JanelaTreino jt = new JanelaTreino(imagem.getWidth(), imagem.getHeight(), escalaRender);
      jt.desenharTreino(rede, 0, numThreads);
      
      //trabalhar com o tempo de renderização baseado no fps
      double intervaloDesenho = 1000000000/fps;
      double proximoTempoDesenho = System.nanoTime() + intervaloDesenho;
      double tempoRestante;
      
      int i = 0;
      while(i < epocas && jt.isVisible()){
         rede.treinar(dadosEntrada, dadosSaida, epocasPorFrame);
         jt.desenharTreino(rede, i, numThreads);

         try{
            tempoRestante = proximoTempoDesenho - System.nanoTime();
            tempoRestante /= 1000000;
            if(tempoRestante < 0) tempoRestante = 0;

            Thread.sleep((long)tempoRestante);
            proximoTempoDesenho += intervaloDesenho;

         }catch(Exception e){ }

         i += epocasPorFrame;
      }

      jt.dispose();
   }

	public static String pegarEntrada(){
		String entrada;

		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			entrada = br.readLine();
		
		}catch(Exception e){
			entrada = "";
		}

		return entrada;
	}

   public static void desenharRede(RedeNeural rede){
      JanelaRede janela = new JanelaRede();
      janela.desenhar(rede);
   }

   public static void compararSaidaRede(RedeNeural rede, double[][] dadosEntrada, double[][] dadosSaida, String texto){
      int nEntrada = rede.obterTamanhoEntrada();
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
            System.out.print("  " + formatarDecimal(saida_rede[j], 4));
         }
         System.out.println();
      }
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

   public static String formatarDecimal(double valor, int casas){
      String valorFormatado = "";

      String formato = "#.";
      for(int i = 0; i < casas; i++) formato += "#";

      DecimalFormat df = new DecimalFormat(formato);
      valorFormatado = df.format(valor);

      return valorFormatado;
   }

}