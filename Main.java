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

      double[] dados_teste = new double[rede.entrada.neuronios.length];
      double[] saida_teste = new double[1];

      dados_teste[0] = dados[3][0];
      dados_teste[1] = dados[3][1];
      dados_teste[2] = dados[3][2];
      saida_teste[0] = dados[3][3];

      System.out.println("Precisão antes: " + rede.calcularPrecisao(dados));

      rede.backpropagation(dados_teste, saida_teste);
      System.out.println("------------------");

      long contador = 0;
      do{
         if(!errosValidos(rede)){
            System.out.println("-Pesos NaN-");
            break;
         }
         for(int i = 0; i < dados.length; i++){
            dados_teste[0] = dados[i][0];
            dados_teste[1] = dados[i][1];
            dados_teste[2] = dados[i][2];
            saida_teste[0] = dados[i][3];
            rede.backpropagation(dados_teste, saida_teste);
         }
         contador++;
      }while(rede.calcularPrecisao(dados) < 40.0);

      System.out.println("Precisão depois: " +rede.calcularPrecisao(dados));
      System.out.println("Quantidade de epocas: " + contador);
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