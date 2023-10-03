package rna.inicializadores;

public class AleatorioPositivo extends Inicializador{

   /**
    * Inicializa os pesos aleatoriamente dentro do intervalo {@code -alcance : +alcance}
    * @param array array de pesos do neurônio
    * @param alcance valor que será usado como máximo e mínimo na aleatorização.
    */
   @Override
   public void inicializar(double[] array, double alcance){
      for(int i = 0; i < array.length; i++){
         array[i] = super.random.nextDouble(0, alcance);
      }
   }

   @Override
   public void configurarSeed(long seed){
      super.configurarSeed(seed);
   }
}
