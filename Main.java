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

      RedeNeural rede = new RedeNeural(3, 3, 1, 2);

      double[] dados_teste = new double[3];
      double[] saida_teste = new double[1];

      dados_teste[0] = dados[2][0];
      dados_teste[1] = dados[2][1];
      dados_teste[2] = dados[2][2];
      saida_teste[0] = dados[2][3];

      System.out.println("Precisão antes: " + rede.calcularPrecisao(dados));

      rede.backpropagation(dados_teste, saida_teste);
      imprimirErrosRede(rede);

      do{
         for(int i = 0; i < dados.length; i++){
            dados_teste[0] = dados[i][0];
            dados_teste[1] = dados[i][1];
            dados_teste[2] = dados[i][2];
            saida_teste[0] = dados[i][3];         
            rede.backpropagation(dados_teste, saida_teste);
         }
      }while(rede.calcularPrecisao(dados) < 30.0);

      System.out.println("------------------");
      imprimirErrosRede(rede);
      System.out.println(rede.calcularPrecisao(dados));

      //imprimirErrosRede(rede);

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