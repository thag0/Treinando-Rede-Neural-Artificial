import java.text.DecimalFormat;

import rna.RedeNeural;

class Main{

   static double[][] dadosOr = {
      {0, 0, 0},
      {0, 1, 1},
      {1, 0, 1},
      {1, 1, 1}
   };

   static double[][] dadosAnd = {
      {0, 0, 0},
      {0, 1, 0},
      {1, 0, 0},
      {1, 1, 1}
   };

   static double[][] dadosXor = {
      {0, 0, 0},
      {0, 1, 1},
      {1, 0, 1},
      {1, 1, 0}
   };

   static double[][] dadosXorCascata = {
      {0, 0, 0, 0},
      {0, 0, 1, 1},
      {0, 1, 0, 1},
      {0, 1, 1, 0},
      {1, 0, 0, 1},
      {1, 0, 1, 0},
      {1, 1, 0, 0},
      {1, 1, 1, 1},
   };

   public static void main(String[] args){
      limparConsole();

      double[][] dados = dadosXor;

      double[][] dadosEntrada = new double[dados.length][dados[0].length-1];
      double[][] dadosSaida = new double[dados.length][1];
      dadosEntrada = separarDadosEntrada(dados, dados.length, 2);
      dadosSaida = separarDadosSaida(dados, dadosEntrada, dadosSaida);
      
      RedeNeural rede = criarRede(dadosEntrada[0].length, 4, dadosSaida[0].length, 1);
      double custo1, custo2;

      custo1 = rede.funcaoDeCusto(dadosEntrada, dadosSaida);
      rede.diferencaFinita(dadosEntrada, dadosSaida, 0.001, 10*1000);
      custo2 = rede.funcaoDeCusto(dadosEntrada, dadosSaida);

      System.out.println("Custo antes: " + custo1 + "\nCusto depois: " + custo2);

      compararSaidaRede(rede, dadosEntrada, dadosSaida);
      
      int[] arc = rede.obterArquitetura();
      for(int i = 0; i < arc.length; i++) System.out.print("[" + arc[i] + "]");
   }


   public static RedeNeural criarRede(int nE, int nO, int nS, int qO){
      // RedeNeural rede = new RedeNeural(nE, nO, nS, qO);

      int[] arquitetura = {2, 2, 1};
      RedeNeural rede = new RedeNeural(arquitetura);
      rede.configurarFuncaoAtivacao(3, 3);
      rede.configurarAlcancePesos(1);
      rede.configurarTaxaAprendizagem(0.15);
      rede.compilar();

      return rede;
   }


   public static void exibirModeloXor(double[][] dadosEntrada, double[][] dadosSaida) {
      //modelo de rede que interpreta a porta lógica XOR
      //exemplo pego pela internet
      RedeNeural redeXor = modeloXOR();
      System.out.println("\nRede xor");
      compararSaidaRede(redeXor, dadosEntrada, dadosSaida);
      System.out.println("Custo rede xor: " + redeXor.funcaoDeCusto(dadosEntrada, dadosSaida));
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

         System.out.print("Entrada: ");
         for(int j = 0; j < entrada_rede.length; j++){
            System.out.print("|" + entrada_rede[j] + "|");
         }

         System.out.print(" Esperado ->");
         for(int j = 0; j < dadosSaida[0].length; j++){
            System.out.print(" " + dadosSaida[i][j]);
         }
         System.out.print(" Rede ->");
         for(int j = 0; j < rede.saida.neuronios.length; j++){
            System.out.print(" " + formatarFloat(saida_rede[j]));
         }
         System.out.println();
      }
   }


   public static void imprimirErrosRede(RedeNeural rede){

      for(int i = 0; i < rede.ocultas.length; i++){
         System.out.println("O" + i);
         for(int j = 0; j < rede.ocultas[i].neuronios.length; j++){
            System.out.println("[" + rede.ocultas[i].neuronios[j].erro + "]");
         }
         System.out.println();
      }

      for(int i = 0; i < rede.saida.neuronios.length; i++){
         System.out.println("[" + rede.saida.neuronios[i].erro +"]");
      }
   }


   public static RedeNeural modeloXOR(){
      RedeNeural rede =  new RedeNeural(2, 4, 1, 1);
      rede.configurarFuncaoAtivacao(3, 3);
      rede.compilar();

      //n1
      rede.entrada.neuronios[0].pesos[0] = 3.011978;
      rede.entrada.neuronios[0].pesos[1] = 0.537028;
      rede.entrada.neuronios[0].pesos[2] = 5.857057;
      rede.entrada.neuronios[0].pesos[3] = 2.036597;

      //n2
      rede.entrada.neuronios[1].pesos[0] = 3.107690;
      rede.entrada.neuronios[1].pesos[1] = 0.431248;
      rede.entrada.neuronios[1].pesos[2] = 5.830700;
      rede.entrada.neuronios[1].pesos[3] = 1.963531;
      
      //b1
      rede.entrada.neuronios[2].pesos[0] = -5.029354;
      rede.entrada.neuronios[2].pesos[1] = -0.545232;
      rede.entrada.neuronios[2].pesos[2] = -2.434113;
      rede.entrada.neuronios[2].pesos[3] = -3.516930;

      //n1 n2 n3 n4
      rede.ocultas[0].neuronios[0].pesos[0] = -6.179475;      
      rede.ocultas[0].neuronios[1].pesos[0] = -1.509866;
      rede.ocultas[0].neuronios[2].pesos[0] = 7.957819;      
      rede.ocultas[0].neuronios[3].pesos[0] = -4.044065;      
      
      //b2
      rede.ocultas[0].neuronios[4].pesos[0] = -2.709660;

      return rede;
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