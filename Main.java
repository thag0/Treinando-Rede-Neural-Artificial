import rna.RedeNeural;

class Main{
   public static void main(String[] args){
      limparConsole();
      double[][] dados = {
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

      //modelo de rede
      RedeNeural rede = new RedeNeural(3, 2, 1, 1);
      rede.configurarAlcancePesos(2);
      rede.configurarFuncaoAtivacao(3, 3);
      rede.configurarBias(true);
      rede.configurarTaxaAprendizagem(0.01);
      rede.compilar();


      double custoAntes, custoDepois;

      custoAntes = rede.funcaoDeCusto(dados_entrada, dados_saida);
      System.out.println(custoAntes);

      rede.treinar(dados_entrada, dados_saida, 10*1000);

      custoDepois = rede.funcaoDeCusto(dados_entrada, dados_saida);
      System.out.println(custoDepois);
      System.out.println();

      if(custoAntes > custoDepois) System.out.println("Custo diminuiu");
      else if(custoAntes < custoDepois) System.out.println("Custo aumentou");


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
         System.out.print(" Esperado -> " + dados[i][3] + " Saída -> " + saida_rede[0] + "\n");
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
}