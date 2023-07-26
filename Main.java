import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.text.DecimalFormat;
import java.util.ArrayList;

import render.Janela;
import rna.RedeNeural;

import utilitarios.ConversorDados;
import utilitarios.GerenciadorDados;
import utilitarios.GerenciadorImagem;


class Main{
   //auxiliares
   static ConversorDados conversor = new ConversorDados();
   static GerenciadorDados gerenciador = new GerenciadorDados();
   static GerenciadorImagem gi = new GerenciadorImagem();
   
   static final int epocas = 15*1000;

   // Sempre lembrar de quando mudar o dataset, também mudar a quantidade de dados de entrada e saída.

   
   public static void main(String[] args){
      limparConsole();
 
      //lendo os dados de entrada
      BufferedImage imagemTeste = gi.lerImagem("/dados/mnist/4.png");
      double[][] dados = gi.imagemParaDadosTreinoEscalaCinza(gi, imagemTeste);//escolher os dados
      int qEntradas = 2;//quantidade de dados de entrada / entrada da rede
      int qSaidas = 1;//quantidade de dados de saída / saída da rede

      // separar para o treino
      double[][] dadosEntrada = gerenciador.separarDadosEntrada(dados, qEntradas);
      double[][] dadosSaida = gerenciador.separarDadosSaida(dados, qSaidas);

      RedeNeural rede = criarRede(qEntradas, qSaidas);
      rede.treinoGradienteEstocastico(dadosEntrada, dadosSaida, epocas);
      double precisao = rede.calcularPrecisao(dadosEntrada, dadosSaida);
      
      System.out.println(rede.obterInformacoes());
      System.out.println("Custo = " + rede.funcaoDeCusto(dadosEntrada, dadosSaida));
      System.out.println("Precisão = " + (formatarFloat(precisao*100)) + "%");

      gi.ampliarImagem(imagemTeste, rede, 30);// precisa treinar bastante

      desenharRede(rede);
   }


   public static RedeNeural criarRede(int qEntradas, int qSaidas){
      int[] arquitetura = {qEntradas, 9, 9, qSaidas};
      RedeNeural rede = new RedeNeural(arquitetura);

      rede.configurarAlcancePesos(2);
      rede.configurarTaxaAprendizagem(0.4);
      rede.compilar();
      rede.configurarFuncaoAtivacao(2);

      return rede;
   }


   public static ArrayList<String[]> tratarDados(ArrayList<String[]> lista){
      lista = gerenciador.removerColunaDados(lista, 0);
      gerenciador.removerLinhaDados(lista, 0);

      return lista;
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
      Janela janela = new Janela();
      janela.desenhar(rede);
   }


   public static void testarRede(RedeNeural rede, int tamanhoEntrada){
      Janela janela = new Janela();
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

            for(int i = 0; i < rede.saida.neuronios.length; i++) System.out.print("[" + rede.saida.neuronios[i].saida + "]");
            System.out.println();
         
         }else{
            janela.dispose();
            break;
         }

      }
   }


   public static void compararSaidaRede(RedeNeural rede, double[][] dadosEntrada, double[][] dadosSaida, String texto){
      int nEntrada = rede.entrada.neuronios.length;
      nEntrada -= (rede.entrada.temBias) ? 1 : 0; 

      double[] entrada_rede = new double[nEntrada];
      double[] saida_rede = new double[rede.saida.neuronios.length];

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
         for(int j = 0; j < rede.saida.neuronios.length; j++){
            System.out.print("  " + formatarFloat(saida_rede[j]));
         }
         System.out.println();
      }
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


   /**
    * Compara o treino entre backpropagation e diferenças finitas 
    */
   public static void compararBpDf(double[][] dadosEntrada, double[][] dadosSaida, int qEntradas, int qSaidas){
      //calcular diferença de tempo 
      long tempo1Bp, tempo2Bp;
      long tempo1Df, tempo2Df;
      double tempofinalBp, tempoFinalDf;

      //usando exatamente o mesmo modelo de rede
      RedeNeural redeDf = criarRede(qEntradas, qSaidas);
      RedeNeural redeBp = redeDf.clone();

      double custo1Bp, custo2Bp, custo1Df, custo2Df;

      custo1Df = redeDf.funcaoDeCusto(dadosEntrada, dadosSaida);
      custo1Bp = redeBp.funcaoDeCusto(dadosEntrada, dadosSaida);


      //calculando tempo com diferenças finitas
      tempo1Df = System.nanoTime();
      redeDf.diferencaFinita(dadosEntrada, dadosSaida, 0.001, epocas, 0.0001);
      tempo2Df = System.nanoTime();
      tempoFinalDf = (double)(tempo2Df - tempo1Df)/1_000_000_000;

      //calculando tempo com backpropagation
      tempo1Bp = System.nanoTime();
      redeBp.treinoBackpropagation(dadosEntrada, dadosSaida, epocas);
      tempo2Bp = System.nanoTime();
      tempofinalBp = (double)(tempo2Bp - tempo1Bp)/1_000_000_000;

      //custos após o treino
      custo2Df = redeDf.funcaoDeCusto(dadosEntrada, dadosSaida);
      custo2Bp = redeBp.funcaoDeCusto(dadosEntrada, dadosSaida);

      System.out.println("Custo antes com Diferenças Finitas: " + custo1Df + "\nCusto depois com Diferenças Finitas: " + custo2Df);
      System.out.println("\nCusto antes com Backpropagation: " + custo1Bp + "\nCusto depois com Backpropagation: " + custo2Bp);

      // compararSaidaRede(redeDf, dadosEntrada, dadosSaida, "Rede treinada com Diferenças Finitas");
      // compararSaidaRede(redeBp, dadosEntrada, dadosSaida, "Rede treinada com Backpropagation");

      double precisaoDf = redeDf.calcularPrecisao(dadosEntrada, dadosSaida)*100;
      double precisaoBp = redeBp.calcularPrecisao(dadosEntrada, dadosSaida)*100;

      System.out.println("\nPrecisão com Diferenças Finitas = " + formatarFloat(precisaoDf) + "%");
      System.out.println("Precisão com Backpropagation = " + formatarFloat(precisaoBp) + "%");

      System.out.println("\nTempo treinando com Diferenças Finitas = " + tempoFinalDf + " s");
      System.out.println("Tempo treinando com Backpropagation = " + tempofinalBp + " s");
   }


   /**
    * Compara o treino entre backpropagation e gradiente estocástico
    */   
   public static void compararBpGe(double[][] dadosEntrada, double[][] dadosSaida, int qEntradas, int qSaidas){
      //calcular diferença de tempo 
      long tempo1Bp, tempo2Bp;
      long tempo1Ge, tempo2Ge;
      double tempofinalBp, tempoFinalGe;

      //usando exatamente o mesmo modelo de rede
      RedeNeural redeBp = criarRede(qEntradas, qSaidas);
      RedeNeural redeGe = redeBp.clone();

      double custo1Bp, custo2Bp, custo1Ge, custo2Ge;

      custo1Bp = redeBp.funcaoDeCusto(dadosEntrada, dadosSaida);
      custo1Ge = redeGe.funcaoDeCusto(dadosEntrada, dadosSaida);

      //calculando tempo com backpropagation
      tempo1Bp = System.nanoTime();
      redeBp.treinoBackpropagation(dadosEntrada, dadosSaida, epocas);
      tempo2Bp = System.nanoTime();
      tempofinalBp = (double)(tempo2Bp - tempo1Bp)/1_000_000_000;

      //calculando tempo com gradiente estocástico
      tempo1Ge = System.nanoTime();
      redeGe.treinoGradienteEstocastico(dadosEntrada, dadosSaida, epocas);
      tempo2Ge = System.nanoTime();
      tempoFinalGe = (double)(tempo2Ge - tempo1Ge)/1_000_000_000;

      //custos após o treino
      custo2Bp = redeBp.funcaoDeCusto(dadosEntrada, dadosSaida);
      custo2Ge = redeGe.funcaoDeCusto(dadosEntrada, dadosSaida);

      System.out.println("\nCusto antes com Backpropagation: " + custo1Bp + "\nCusto depois com Backpropagation: " + custo2Bp);
      System.out.println("Custo antes com Gradiente Estocástico: " + custo1Ge + "\nCusto depois com Gradiente Estocástico: " + custo2Ge);

      // compararSaidaRede(redeBp, dadosEntrada, dadosSaida, "Rede treinada com Backpropagation");
      // compararSaidaRede(redeGe, dadosEntrada, dadosSaida, "Rede treinada com Gradiente Estocástico");

      double precisaoBp = redeBp.calcularPrecisao(dadosEntrada, dadosSaida)*100;
      double precisaoGe = redeGe.calcularPrecisao(dadosEntrada, dadosSaida)*100;

      System.out.println("\nPrecisão com Backpropagation = " + formatarFloat(precisaoBp) + "%");
      System.out.println("Precisão com Gradiente Estocástico = " + formatarFloat(precisaoGe) + "%");

      System.out.println("\nTempo treinando com Backpropagation = " + tempofinalBp + " s");
      System.out.println("Tempo treinando com Gradiente Estocástico = " + tempoFinalGe + " s");
   }

}