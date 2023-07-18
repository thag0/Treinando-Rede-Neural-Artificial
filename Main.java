import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import rna.RedeNeural;

class Main{

   public static void main(String[] args){
      limparConsole();

      double[][] dados = Dados.dadosXor;//escolher os dados

      //separar para o treino
      double[][] dadosEntrada = new double[dados.length][dados[0].length-1];
      double[][] dadosSaida = new double[dados.length][1];
      dadosEntrada = separarDadosEntrada(dados, dados.length, dados[0].length-1);
      dadosSaida = separarDadosSaida(dados, dadosEntrada, dadosSaida);
      
      RedeNeural rede = criarRede(dadosEntrada[0].length, 2, dadosSaida[0].length, 1);
      double custo1, custo2;

      custo1 = rede.funcaoDeCusto(dadosEntrada, dadosSaida);
      rede.diferencaFinita(dadosEntrada, dadosSaida, 0.001, 25*1000);
      custo2 = rede.funcaoDeCusto(dadosEntrada, dadosSaida);

      System.out.println("Custo antes: " + custo1 + "\nCusto depois: " + custo2);

      compararSaidaRede(rede, dadosEntrada, dadosSaida);

      System.out.println(rede.obterInformacoes());
      // System.out.println(rede);
      // testarRede(rede, dadosEntrada[0].length);
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


   public static RedeNeural criarRede(int nE, int nO, int nS, int qO){
      RedeNeural rede = new RedeNeural(nE, nO, nS, qO);
      rede.configurarFuncaoAtivacao(2, 2);
      rede.configurarAlcancePesos(2);
      rede.configurarTaxaAprendizagem(0.5);
      rede.compilar();

      return rede;
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


   public static void compararSaidaRede(RedeNeural rede, double[][] dadosEntrada, double[][] dadosSaida){
      double[] entrada_rede = new double[rede.entrada.neuronios.length-1];
      double[] saida_rede = new double[rede.saida.neuronios.length];

      System.out.println();

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