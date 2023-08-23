import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import render.JanelaRede;
import render.JanelaTreino;

import rna.RedeNeural;

import utilitarios.ged.Ged;
import utilitarios.geim.Geim;

class Main{
   //auxiliares
   static Ged ged = new Ged();
   static Geim geim = new Geim();
   
   // static final String caminhoArquivo = "/dados/32x32/bloco.png";
   static final String caminhoArquivo = "/dados/mnist/8.png";
   static final String caminhoImagemExportada = "./resultados/imagem-ampliada";
   static final int epocas = 100*1000;
   static final float escalaRender = 9f;
   static final float escalaImagemExportada = 40f;

   // Sempre lembrar de quando mudar o dataset, também mudar a quantidade de dados de entrada e saída.

   
   public static void main(String[] args){
      limparConsole();

      long t1, t2;
      long horas, minutos, segundos;

      //lendo os dados de entrada
      int qEntradas = 2;//quantidade de dados de entrada / entrada da rede
      int qSaidas = 1;//quantidade de dados de saída / saída da rede
      BufferedImage imagem = geim.lerImagem(caminhoArquivo);
      double[][] dados;

      //escolher a forma de importação dos dados
      if(qSaidas == 1) dados = geim.imagemParaDadosTreinoEscalaCinza(imagem);
      else if(qSaidas == 3) dados = geim.imagemParaDadosTreinoRGB(imagem);
      else return;

      //separar para o treino
      double[][] dadosEntrada = ged.separarDadosEntrada(dados, qEntradas);
      double[][] dadosSaida = ged.separarDadosSaida(dados, qSaidas);
      System.out.println("Tamanho dos dados [" + dados.length + ", " + dados[0].length + "]");

      RedeNeural rede = criarRede(qEntradas, qSaidas);
      System.out.println(rede.obterInformacoes());

      //treinar e marcar tempo
      t1 = System.nanoTime();
      System.out.println("Treinando.");

      treinoEmPainel(rede, imagem, dadosEntrada, dadosSaida);
      
      t2 = System.nanoTime();
      long tempoDecorrido = t2 - t1;
      long segundosTotais = TimeUnit.NANOSECONDS.toSeconds(tempoDecorrido);
      horas = segundosTotais / 3600;
      minutos = (segundosTotais % 3600) / 60;
      segundos = segundosTotais % 60;

      //avaliar resultados
      double precisao = rede.avaliador.erroMedioAbsoluto(dadosEntrada, dadosSaida);
      double custo = rede.avaliador.erroMedioQuadrado(dadosEntrada, dadosSaida);

      System.out.println("Custo = " + custo);
      System.out.println("Precisão = " + (formatarFloat(precisao*100)) + "%");
      System.out.println("Tempo de treinamento: " + horas + "h " + minutos + "m " + segundos + "s");

      // precisa treinar bastante
      System.out.println("\nSalvando imagem");
      if(qSaidas == 1)geim.exportarImagemEscalaCinza(imagem, rede, escalaImagemExportada, caminhoImagemExportada);
      else if(qSaidas == 3) geim.exportarImagemRGB(imagem, rede, escalaImagemExportada, caminhoImagemExportada);
      else System.out.println("Não é possível exportar a imagem");
   }


   public static RedeNeural criarRede(int qEntradas, int qSaidas){
      // int[] arquitetura = {qEntradas, 36, 36, 36, qSaidas};//32x32
      int[] arquitetura = {qEntradas, 12, 12, qSaidas};//28x28
      RedeNeural rede = new RedeNeural(arquitetura);

      rede.configurarAlcancePesos(1);
      rede.configurarTaxaAprendizagem(0.001);
      rede.configurarMomentum(0.99);
      rede.configurarOtimizador(2);
      rede.configurarInicializacaoPesos(2);
      rede.compilar();
      rede.configurarFuncaoAtivacao(2);
      return rede;
   }


   public static void treinoEmPainel(RedeNeural rede, BufferedImage imagem, double[][] dadosEntrada, double[][] dadosSaida){
      final int fps = 60;
      int epocasPorFrame = 10;

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

         i += 1*epocasPorFrame;
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


   public static void testarRede(RedeNeural rede, int tamanhoEntrada){
      JanelaRede janela = new JanelaRede();
      janela.painel.configurarRede(rede);
      janela.desenhar(rede);

      String entrada = "";
      while(true){
         System.out.print("\nFazer uma predição ? [s/n]: ");
         entrada = pegarEntrada();

         if(entrada.equalsIgnoreCase("s") || entrada.equalsIgnoreCase("sim")){
            double[] testeRede = new double[tamanhoEntrada];

            for(int i = 0; i < testeRede.length; i++){
               System.out.print("Entrada " + i + ": ");
               entrada = pegarEntrada();
               testeRede[i] = Double.parseDouble(entrada);
            }

            System.out.print("Predição da rede para ");
            for(int i = 0; i < testeRede.length; i++) System.out.print("[" + testeRede[i] + "]");
            System.out.print(" -> ");
            rede.calcularSaida(testeRede);

            janela.desenhar(rede);

            for(int i = 0; i < rede.obterCamadaSaida().obterQuantidadeNeuronios(); i++){
               System.out.print("[" + rede.obterCamadaSaida().neuronio(i).saida + "]");
            } 
            System.out.println();
         
         }else{
            janela.dispose();
            break;
         }

      }
   }


   public static void compararSaidaRede(RedeNeural rede, double[][] dadosEntrada, double[][] dadosSaida, String texto){
      int nEntrada = rede.obterCamadaEntrada().obterQuantidadeNeuronios();
      nEntrada -= (rede.obterCamadaEntrada().temBias()) ? 1 : 0;

      int nSaida = rede.obterCamadaSaida().obterQuantidadeNeuronios();

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


   public static void exportarHistoricoCustos(RedeNeural rede, Ged ged){
      System.out.println("Exportando histórico de custo");
      ArrayList<Double> custos = rede.obterHistoricoCusto();
      double[][] dadosErro = new double[custos.size()][1];

      for(int i = 0; i < dadosErro.length; i++){
         dadosErro[i][0] = custos.get(i);
      }

      ged.exportarCsv(dadosErro, "historico-custo");
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


   public static String formatarFloat(double valor){
      String valorFormatado = "";

      DecimalFormat df = new DecimalFormat("#.####");
      valorFormatado = df.format(valor);

      return valorFormatado;
   }

}