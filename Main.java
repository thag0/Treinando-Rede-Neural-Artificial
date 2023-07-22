import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import render.Janela;
import rna.RedeNeural;


class Main{
   static final int epocas = 20*1000;


   public static void main(String[] args){
      limparConsole();

      double[][] dados = Dados.dadosXorCascata;//escolher os dados
      int qEntradas = 3;//quantidade de dados de entrada
      int qSaidas = 1;//quantidade de dados de saída

      // separar para o treino
      double[][] dadosEntrada = new double[dados.length][qEntradas];
      double[][] dadosSaida = new double[dados.length][qSaidas];
      dadosEntrada = separarDadosEntrada(dados, dados.length, qEntradas);
      dadosSaida = separarDadosSaida(dados, dadosEntrada, dadosSaida);
      
      RedeNeural rede = criarRede(qEntradas, qSaidas);
      rede.treinar(dadosEntrada, dadosSaida, epocas);
      double precisao = rede.calcularPrecisao(dadosEntrada, dadosSaida);
      
      compararSaidaRede(rede, dadosEntrada, dadosSaida, "Rede treinada");
      System.out.println("\nCusto = " + rede.funcaoDeCusto(dadosEntrada, dadosSaida));
      System.out.println("Precisão = " + (formatarFloat(precisao*100)) + "%");
   }


   public static RedeNeural criarRede(int qEntradas, int qSaidas){
      int[] arquitetura = {qEntradas, 4, 4, qSaidas};
      RedeNeural rede = new RedeNeural(arquitetura);

      rede.configurarAlcancePesos(3);
      rede.configurarTaxaAprendizagem(0.02);
      rede.compilar();
      rede.configurarFuncaoAtivacao(4);

      return rede;
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
      String entrada = "";
      while(true){
         System.out.print("\nFazer uma predição ? [s/n]: ");
         entrada = pegarEntrada();
         if(entrada.equalsIgnoreCase("n")) break;

         double[] testeRede = new double[tamanhoEntrada];

         for(int i = 0; i < testeRede.length; i++){
            System.out.print("valor " + i + ": ");
            entrada = pegarEntrada();
            testeRede[i] = Double.parseDouble(entrada);
         }

         System.out.print("Predição da rede para ");
         for(int i = 0; i < testeRede.length; i++) System.out.print("[" + testeRede[i] + "]");
         System.out.print(" -> ");
         rede.calcularSaida(testeRede);
         for(int i = 0; i < rede.saida.neuronios.length; i++) System.out.print("[" + rede.saida.neuronios[i].saida + "]");
         System.out.println();
      }
   }


   //public static double[][] separarDadosEntrada(double[][] dadosEntrada, double[][] dados)
   public static double[][] separarDadosEntrada(double[][] dados, int linhas, int colunas){
      double[][] dadosEntrada = new double[linhas][colunas];
      for(int i = 0; i < linhas; i++){
         for(int j = 0; j < colunas; j++){
            dadosEntrada[i][j] = dados[i][j];
         }
      }
      return dadosEntrada;
   }


   public static double[][] separarDadosSaida(double[][] dados, double[][] dadosEntrada, double[][] dadosSaida){
      for(int i = 0; i < dadosSaida.length; i++){
         for(int j = 0; j < dadosSaida[0].length; j++){
            dadosSaida[i][j] = dados[i][dadosEntrada[0].length + j];
         }
      }
      return dadosSaida;
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

         System.out.print("Dado " + i + " |");
         for(int j = 0; j < entrada_rede.length; j++){
            System.out.print(" " + entrada_rede[j] + " ");
         }

         System.out.print(" - ");
         for(int j = 0; j < dadosSaida[0].length; j++){
            System.out.print(" " + dadosSaida[i][j]);
         }
         System.out.print(" | Rede ->");
         for(int j = 0; j < rede.saida.neuronios.length; j++){
            System.out.print(" " + formatarFloat(saida_rede[j]));
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

      DecimalFormat df = new DecimalFormat("#.######");
      valorFormatado = df.format(valor);

      return valorFormatado;
   }


   public static void compararTreinos(double[][] dadosEntrada, double[][] dadosSaida, int qEntradas, int qSaidas){
      //calcular diferença de tempo 
      long tempo1Bp, tempo2Bp;
      long tempo1Df, tempo2Df;
      double tempofinalBp, tempoFinalDf;

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
      redeBp.treinar(dadosEntrada, dadosSaida, epocas);
      tempo2Bp = System.nanoTime();
      tempofinalBp = (double)(tempo2Bp - tempo1Bp)/1_000_000_000;

      //custos após o treino
      custo2Df = redeDf.funcaoDeCusto(dadosEntrada, dadosSaida);
      custo2Bp = redeBp.funcaoDeCusto(dadosEntrada, dadosSaida);

      System.out.println("Custo antes com Diferenças Finitas: " + custo1Df + "\nCusto depois com Diferenças Finitas: " + custo2Df);
      System.out.println("\nCusto antes com Backpropagation: " + custo1Bp + "\nCusto depois com Backpropagation: " + custo2Bp);


      compararSaidaRede(redeDf, dadosEntrada, dadosSaida, "Rede treinada com Diferenças Finitas");
      compararSaidaRede(redeBp, dadosEntrada, dadosSaida, "Rede treinada com Backpropagation");

      double precisaoDf = redeDf.calcularPrecisao(dadosEntrada, dadosSaida)*100;
      double precisaoBp = redeBp.calcularPrecisao(dadosEntrada, dadosSaida)*100;

      System.out.println();
      System.out.println("Precisão com Diferenças Finitas = " + formatarFloat(precisaoDf) + "%");
      System.out.println("Precisão com Backpropagation = " + formatarFloat(precisaoBp) + "%");

      System.out.println("\nTempo treinando com Diferenças Finitas = " + tempoFinalDf + " s");
      System.out.println("Tempo treinando com Backpropagation = " + tempofinalBp + " s");
   }
}