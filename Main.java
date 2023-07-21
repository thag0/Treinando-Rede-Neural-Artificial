import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import render.Janela;
import rna.RedeNeural;


class Main{
   static final int epocas = 40*1000;


   public static void main(String[] args){
      limparConsole();
      long t1bp, t2bp, t1df, t2df;
      double tfbp, tfdf;

      double[][] dados = Dados.dadosXorCascata;//escolher os dados
      int qEntradas = 3;
      int qSaidas = 1;

      // separar para o treino
      double[][] dadosEntrada = new double[dados.length][qEntradas];
      double[][] dadosSaida = new double[dados.length][qSaidas];
      dadosEntrada = separarDadosEntrada(dados, dados.length, qEntradas);
      dadosSaida = separarDadosSaida(dados, dadosEntrada, dadosSaida);
      
      RedeNeural redeBP = criarRede(qEntradas, qSaidas);
      RedeNeural redeDF = redeBP.clone();

      double custo1BP, custo2BP;
      double custo1DF, custo2DF;

      custo1BP = redeBP.funcaoDeCusto(dadosEntrada, dadosSaida);
      custo1DF = redeDF.funcaoDeCusto(dadosEntrada, dadosSaida);

      t1bp = System.nanoTime();
      redeBP.treinar(dadosEntrada, dadosSaida, epocas);
      t2bp = System.nanoTime();
      tfbp = (double)(t2bp - t1bp)/1_000_000_000;

      t1df = System.nanoTime();
      redeDF.diferencaFinita(dadosEntrada, dadosSaida, 0.001, epocas, 0.001);
      t2df = System.nanoTime();
      tfdf = (double)(t2df - t1df)/1_000_000_000;

      custo2BP = redeBP.funcaoDeCusto(dadosEntrada, dadosSaida);
      custo2DF = redeDF.funcaoDeCusto(dadosEntrada, dadosSaida);

      System.out.println("Custo antes Backpropagation: " + custo1BP + "\nCusto depois Backpropagation: " + custo2BP);
      System.out.println("\nCusto antes Diferenças finitas: " + custo1DF + "\nCusto depois Diferenças finitas: " + custo2DF);

      compararSaidaRede(redeBP, dadosEntrada, dadosSaida, "Desempenho com Backpropagation");
      compararSaidaRede(redeDF, dadosEntrada, dadosSaida, "Desempenho com Diferenças finitas");

      double precisaoBP = redeBP.calcularPrecisao(dadosEntrada, dadosSaida)*100;
      double precisaoDF = redeDF.calcularPrecisao(dadosEntrada, dadosSaida)*100;

      System.out.println();
      System.out.println("Precisão backpropagation = " + formatarFloat(precisaoBP) + "%");
      System.out.println("Precisão diferenças finitas = " + formatarFloat(precisaoDF) + "%");

      System.out.println("\nTempo backpropagation = " + tfbp + " s");
      System.out.println("Tempo diferenças finitas = " + tfdf + " s");
   }


   public static RedeNeural criarRede(int qEntradas, int qSaidas){
      int[] arquitetura = {qEntradas, 2, 2, qSaidas};
      RedeNeural rede = new RedeNeural(arquitetura);

      rede.configurarAlcancePesos(4);
      rede.configurarTaxaAprendizagem(0.5);
      rede.compilar();
      rede.configurarFuncaoAtivacao(3);

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
         saida_rede = rede.obterSaida();

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
}