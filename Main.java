import java.text.DecimalFormat;

import rna.RedeNeural;

class Main{
   public static void main(String[] args){
      limparConsole();
      double[][] dados = {//xor
         {0, 0, 0},
         {0, 1, 1},
         {1, 0, 1},
         {1, 1, 0}
      };
      double[][] dados2 = {
         {0, 0, 0, 0},
         {0, 0, 1, 1},
         {0, 1, 0, 1},
         {0, 1, 1, 0},
         {1, 0, 0, 1},
         {1, 0, 1, 0},
         {1, 1, 0, 0},
         {1, 1, 1, 1}
      };

      double[][] dados_entrada = new double[dados.length][dados[0].length-1];
      double[][] dados_saida = new double[dados.length][1];

      //preencher dados de entrada
      for(int i = 0; i < dados_entrada.length; i++){
         for(int j = 0; j < dados_entrada[0].length; j++){
            dados_entrada[i][j] = dados[i][j];
         }
      }

      //preencher dados de saída
      for(int i = 0; i < dados_saida.length; i++){
         for(int j = 0; j < dados_saida[0].length; j++){
            dados_saida[i][j] = dados[i][dados_entrada[0].length + j];
         }
      }

      //modelo de rede que interpreta a porta lógica XOR
      RedeNeural rede = modeloXOR();

      //mostrar saída da rede em comparação com os dados
      double[] entrada_rede = new double[rede.entrada.neuronios.length-1];
      double[] saida_rede = new double[rede.saida.neuronios.length];

      //mostrar saída da rede comparada aos dados
      System.out.println();
      for(int i = 0; i < dados_entrada.length; i++){
         for(int j = 0; j < dados_entrada[0].length; j++){
            entrada_rede[j] = dados_entrada[i][j];
         }

         rede.calcularSaida(entrada_rede);
         saida_rede = rede.obterSaida();

         System.out.print("Entrada: ");
         for(int k = 0; k < entrada_rede.length; k++){
            System.out.print("|" + entrada_rede[k] + "|");
         }
         System.out.print(" Esperado -> " + dados[i][dados[0].length-1] + " Saída -> " + formatarFloat((float)saida_rede[0]) + "\n");
      }
      System.out.println("\nCusto: " + rede.funcaoDeCusto(dados_entrada, dados_saida) + "\n");

      Auxiliares.imprimirRede(rede);
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


   public static boolean errosValidos(RedeNeural rede){
      for(int i = 0; i < rede.ocultas.length; i++){
         for(int j = 0; j < rede.ocultas[i].neuronios.length; j++){
            for(int k = 0; k < rede.ocultas[i].neuronios[j].pesos.length; k++){
               if(Double.isNaN(rede.ocultas[i].neuronios[j].pesos[k])) return false;
            }
         }
      }
      return true;
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


   public static String formatarFloat(float valor){
      String valorFormatado = "";

      DecimalFormat df = new DecimalFormat("#.##");
      valorFormatado = df.format(valor);

      return valorFormatado;
   }
}