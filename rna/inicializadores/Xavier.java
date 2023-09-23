package rna.inicializadores;

public class Xavier extends Inicializador{

   /**
    * Aplica o algoritmo de inicialização Xavier/Glorot nos pesos.
    * @param array array de pesos do neurônio
    * @param entradas quantidade de conexões do neurônio também corresponde a quantidade de saídas
    * da camada anterior.
    * @param saidas quantidade de saídas da camada em que o neurônio está presente.
    */
   @Override
   public void inicializar(double[] array, int entradas, int saidas){
      double desvioPadrao = Math.sqrt(2.0 / (entradas + saidas));

      for(int i = 0; i < array.length; i++){
         array[i] = random.nextGaussian() * desvioPadrao;
      }
   }
}
