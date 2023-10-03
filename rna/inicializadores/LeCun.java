package rna.inicializadores;

public class LeCun extends Inicializador{
   
   /**
    * Aplica o algoritmo de inicialização LeCun nos pesos.
    * @param array array de pesos do neurônio
    * @param entradas quantidade de conexões do neurônio também corresponde a quantidade de saídas
    * da camada anterior.
    */
   @Override
   public void inicializar(double[] array, int entradas){
      double alcance = Math.sqrt(1.0 / entradas);

      for(int i = 0; i < array.length; i++){
         array[i] = super.random.nextGaussian() * alcance;
      }
   }

   @Override
   public void configurarSeed(long seed){
      super.configurarSeed(seed);
   }
}
