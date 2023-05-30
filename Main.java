import rna.RedeNeural;

class Main{
   public static void main(String[] args){
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
      RedeNeural rede = new RedeNeural(3, 5, 1, 2);

      double[] dados_teste = new double[3];
      double[] saida_teste = new double[1];

      double precisao = rede.calcularPrecisao(dados);
      System.out.println("precisão antes: " + precisao);

      for(int epochs = 0; epochs < 400; epochs++){
         for(int i = 0; i < dados.length; i++){
            dados_teste[0] = dados[i][0];
            dados_teste[1] = dados[i][1];
            dados_teste[2] = dados[i][2];
            saida_teste[0] = dados[i][3];         
            rede.backpropagation(dados_teste, saida_teste);
         }
      }

      precisao = rede.calcularPrecisao(dados);
      System.out.println("precisão depois: " + precisao);

   }
}