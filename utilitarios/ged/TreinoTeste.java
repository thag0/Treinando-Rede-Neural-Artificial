package utilitarios.ged;

import java.util.Random;


/**
 * Gerenciador de treino e teste do Ged
 */
class TreinoTeste{

   /**
    * Implementações relacionadas a manipulação de dados de treino e teste da rede neural.
    */
   public TreinoTeste(){

   }


   public void embaralharDados(int[][] dados){
      Random random = new Random();
      int linhas = dados.length;

      int[] temp = new int[dados[0].length];
      int j;
      for(int i = linhas - 1; i > 0; i--){
         j = random.nextInt(i + 1);

         System.arraycopy(dados[i], 0, temp, 0, temp.length);
         System.arraycopy(dados[j], 0, dados[i], 0, dados[i].length);
         System.arraycopy(temp, 0, dados[j], 0, dados[j].length);
      }
   }


   public void embaralharDados(float[][] dados){
      Random random = new Random();
      int linhas = dados.length;

      float[] temp = new float[dados[0].length];
      int j;
      for(int i = linhas - 1; i > 0; i--){
         j = random.nextInt(i + 1);

         System.arraycopy(dados[i], 0, temp, 0, temp.length);
         System.arraycopy(dados[j], 0, dados[i], 0, dados[i].length);
         System.arraycopy(temp, 0, dados[j], 0, dados[j].length);
      }
   }


   public void embaralharDados(double[][] dados){
      Random random = new Random();
      int linhas = dados.length;

      double[] temp = new double[dados[0].length];
      int j;
      for(int i = linhas - 1; i > 0; i--){
         j = random.nextInt(i + 1);

         System.arraycopy(dados[i], 0, temp, 0, temp.length);
         System.arraycopy(dados[j], 0, dados[i], 0, dados[i].length);
         System.arraycopy(temp, 0, dados[j], 0, dados[j].length);
      }
   }


   public int[][] separarDadosEntrada(int[][] dados, int colunas){
      if (colunas > dados[0].length) {
         throw new IllegalArgumentException("O número de colunas fornecido é maior do que o número de colunas disponíveis nos dados.");
      }
      if(colunas < 1){
         throw new IllegalArgumentException("A quantidade de colunas extraídas não pode ser menor que um");
      }

      int[][] dadosEntrada = new int[dados.length][colunas];
      for(int i = 0; i < dadosEntrada.length; i++){
         System.arraycopy(dados[i], 0, dadosEntrada[i], 0, colunas);
      }
      
      return dadosEntrada;
   }


   public float[][] separarDadosEntrada(float[][] dados, int colunas){
      if (colunas > dados[0].length) {
         throw new IllegalArgumentException("O número de colunas fornecido é maior do que o número de colunas disponíveis nos dados.");
      }
      if(colunas < 1){
         throw new IllegalArgumentException("A quantidade de colunas extraídas não pode ser menor que um");
      }

      float[][] dadosEntrada = new float[dados.length][colunas];
      for(int i = 0; i < dadosEntrada.length; i++){
         System.arraycopy(dados[i], 0, dadosEntrada[i], 0, colunas);
      }
      
      return dadosEntrada;
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
         System.arraycopy(dados[i], 0, dadosEntrada[i], 0, colunas);
      }
      
      return dadosEntrada;
   }


   public int[][] separarDadosSaida(int[][] dados, int colunas){
      if(colunas > dados[0].length){
         throw new IllegalArgumentException("O número de colunas fornecido é maior do que o número de colunas disponíveis nos dados.");
      }
      if(colunas < 1){
         throw new IllegalArgumentException("A quantidade de colunas extraídas não pode ser menor que um");
      }

      int[][] dadosSaida = new int[dados.length][colunas];
      int indiceInicial = dados[0].length - colunas;

      for(int i = 0; i < dados.length; i++){
         System.arraycopy(dados[i], indiceInicial, dadosSaida[i], 0, colunas);
      }

      return dadosSaida;
   }


   public float[][] separarDadosSaida(float[][] dados, int colunas){
      if(colunas > dados[0].length){
         throw new IllegalArgumentException("O número de colunas fornecido é maior do que o número de colunas disponíveis nos dados.");
      }
      if(colunas < 1){
         throw new IllegalArgumentException("A quantidade de colunas extraídas não pode ser menor que um");
      }

      float[][] dadosSaida = new float[dados.length][colunas];
      int indiceInicial = dados[0].length - colunas;

      for(int i = 0; i < dados.length; i++){
         System.arraycopy(dados[i], indiceInicial, dadosSaida[i], 0, colunas);
      }

      return dadosSaida;
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

      for(int i = 0; i < dados.length; i++){
         System.arraycopy(dados[i], indiceInicial, dadosSaida[i], 0, colunas);
      }

      return dadosSaida;
   }


   public int[][][] separarTreinoTeste(int[][] dados, float tamanhoTeste){
      if(dados == null) throw new IllegalArgumentException("O conjunto de dados é nulo.");
      if(tamanhoTeste < 0 || tamanhoTeste > 1){
         throw new IllegalArgumentException("O tamanho dos dados de teste deve ser maior que zero e menor que um.");
      }

      int linhasTeste = (int) (dados.length*tamanhoTeste);
      int linhasTreino = dados.length - linhasTeste;
      int colunas = dados[0].length;

      int[][] treino = new int[linhasTreino][colunas];
      int[][] teste = new int[linhasTeste][colunas];

      //método nativo, parece ser mais rápido que uma cópia manual
      System.arraycopy(dados, 0, treino, 0, linhasTreino);// copiar treino
      System.arraycopy(dados, linhasTreino, teste, 0, linhasTeste);// copiar teste

      return new int[][][]{treino, teste};
   }


   public float[][][] separarTreinoTeste(float[][] dados, float tamanhoTeste){
      if(dados == null) throw new IllegalArgumentException("O conjunto de dados é nulo.");
      if(tamanhoTeste < 0 || tamanhoTeste > 1){
         throw new IllegalArgumentException("O tamanho dos dados de teste deve ser maior que zero e menor que um.");
      }

      int linhasTeste = (int) (dados.length*tamanhoTeste);
      int linhasTreino = dados.length - linhasTeste;
      int colunas = dados[0].length;

      float[][] treino = new float[linhasTreino][colunas];
      float[][] teste = new float[linhasTeste][colunas];

      //método nativo, parece ser mais rápido que uma cópia manual
      System.arraycopy(dados, 0, treino, 0, linhasTreino);// copiar treino
      System.arraycopy(dados, linhasTreino, teste, 0, linhasTeste);// copiar teste

      return new float[][][]{treino, teste};
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
