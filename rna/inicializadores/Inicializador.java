package rna.inicializadores;

import java.util.Random;

/**
 * Classe responsável pelas funções de inicialização dos pesos
 * da Rede Neural.
 */
public class Inicializador{

   static Random random = new Random();

   /**
    * Configura o início do gerador aleatório.
    * @param seed nova seed de início.
    */
   public static void configurarSeed(long seed){
      random = new Random(seed);
   }
   
   /**
    * Inicializa os pesos aleatoriamente dentro do intervalo {@code -alcance : +alcance}
    * @param array array de pesos do neurônio
    * @param alcance valor que será usado como máximo e mínimo na aleatorização.
    */
   public static void aleatorio(double[] array, double alcance){
      for(int i = 0; i < array.length; i++){
         array[i] = random.nextDouble(-alcance, alcance);
      }
   }

   /**
    * Inicializa os pesos aleatoriamente dentro do intervalo {@code 0 : +alcance}
    * @param array array de pesos do neurônio
    * @param alcance valor que será usado como máximo e mínimo na aleatorização.
    */
   public static void aleatoriaPositiva(double[] array, double alcance){
      for(int i = 0; i < array.length; i++){
         array[i] = random.nextDouble(0, alcance);
      }
   }

   /**
    * Aplica o algoritmo de inicialização He nos pesos.
    * @param array array de pesos do neurônio
    * @param entradas quantidade de conexões do neurônio também corresponde a quantidade de saídas
    * da camada anterior.
    */
   public static void he(double[] array, int entradas){
      double desvioPadrao = Math.sqrt(2.0 / entradas);

      for(int i = 0; i < array.length; i++){
         array[i] = random.nextGaussian() * desvioPadrao;
      }
   }

   /**
    * Aplica o algoritmo de inicialização LeCun nos pesos.
    * @param array array de pesos do neurônio
    * @param entradas quantidade de conexões do neurônio também corresponde a quantidade de saídas
    * da camada anterior.
    */
   public static void leCun(double[] array, int entradas){
      double desvioPadrao = Math.sqrt(1.0 / entradas);

      for(int i = 0; i < array.length; i++){
         array[i] = random.nextGaussian() * desvioPadrao;
      }
   }

   /**
    * Aplica o algoritmo de inicialização Xavier/Glorot nos pesos.
    * @param array array de pesos do neurônio
    * @param entradas quantidade de conexões do neurônio também corresponde a quantidade de saídas
    * da camada anterior.
    * @param saidas quantidade de saídas da camada em que o neurônio está presente.
    */
   public static void xavier(double[] array, int entradas, int saidas){
      double desvioPadrao = Math.sqrt(2.0 / (entradas + saidas));

      for(int i = 0; i < array.length; i++){
         array[i] = random.nextGaussian() * desvioPadrao;
      }
   }
}
