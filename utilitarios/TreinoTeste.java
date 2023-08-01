package utilitarios;

import java.util.Random;

/**
 * Implementações relacionadas a manipulação de dados de treino e teste da rede neural.
 */
class TreinoTeste{

   public TreinoTeste(){

   }


   public void embaralharDados(double[][] dados){
      Random random = new Random();
      int linhas = dados.length;

      for(int i = linhas - 1; i > 0; i--){
         int j = random.nextInt(i + 1);

         double[] temp = dados[i];
         dados[i] = dados[j];
         dados[j] = temp;
      }
   }


   public double[][] separarDadosEntrada(double[][] dados, int colunas){
      if (colunas > dados[0].length) {
         throw new IllegalArgumentException("O número de colunas fornecido é maior do que o número de colunas disponíveis nos dados.");
      }
      if(colunas < 1){
         throw new IllegalArgumentException("A quantidade de colunas extraídas não pode ser menor que um");
      }

      double[][] dadosEntrada = new double[dados.length][colunas];
      for(int i = 0; i < dadosEntrada.length; i++){
         for(int j = 0; j < colunas; j++){
            dadosEntrada[i][j] = dados[i][j];
         }
      }
      return dadosEntrada;
   }


   public double[][] separarDadosSaida(double[][] dados, int colunas){
      if(colunas > dados[0].length){
         throw new IllegalArgumentException("O número de colunas fornecido é maior do que o número de colunas disponíveis nos dados.");
      }
      if(colunas < 1){
         throw new IllegalArgumentException("A quantidade de colunas extraídas não pode ser menor que um");
      }

      double[][] dadosSaida = new double[dados.length][colunas];
      int indiceInicial = dados[0].length - colunas;

      for (int i = 0; i < dados.length; i++) { // percorrer linhas dos dados
         int contador1 = indiceInicial; // contador coluna dados
         int contador2 = 0; // contador coluna saída

         // copiando os valores 
         while (contador2 < colunas) {
            dadosSaida[i][contador2] = dados[i][contador1];
            contador1++;
            contador2++;
         }
      }

      return dadosSaida;
   }


   public double[][][] separarTreinoTeste(double[][] dados, float tamanhoTeste){
      if(dados == null) throw new IllegalArgumentException("O conjunto de dados é nulo.");
      if(tamanhoTeste < 0 || tamanhoTeste > 1){
         throw new IllegalArgumentException("O tamanho dos dados de teste deve ser maior que zero e menor que um.");
      }

      int linhasTeste = (int) (dados.length*tamanhoTeste);
      int linhasTreino = dados.length - linhasTeste;
      int colunas = dados[0].length;

      double[][] treino = new double[linhasTreino][colunas];
      double[][] teste = new double[linhasTeste][colunas];

      //método nativo, parece ser mais rápido que uma cópia manual
      System.arraycopy(dados, 0, treino, 0, linhasTreino);// copiar treino
      System.arraycopy(dados, linhasTreino, teste, 0, linhasTeste);// copiar teste

      return new double[][][]{treino, teste};
   }
}
