package rna.inicializadores;

public class Aleatorio extends Inicializador{

   /**
    * Inicializa os pesos aleatoriamente dentro do intervalo {@code -alcance : +alcance}
    * @param array array de pesos do neurônio
    * @param alcance valor que será usado como máximo e mínimo na aleatorização.
    */
   @Override
   public void inicializar(double[] array, double alcance){
      for(int i = 0; i < array.length; i++){
         array[i] = random.nextDouble(-alcance, alcance);
      }
   }
}
