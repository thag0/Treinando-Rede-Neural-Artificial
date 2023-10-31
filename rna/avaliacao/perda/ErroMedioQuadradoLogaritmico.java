package rna.avaliacao.perda;

public class ErroMedioQuadradoLogaritmico extends Perda{

   @Override
   public double calcular(double[] previsto, double[] real){
      double msle = 0;

      for(int i = 0; i < previsto.length; i++){
         double d = Math.log(1 + real[i]) - Math.log(1 + previsto[i]);
         msle += d * d;
      }

      msle /= previsto.length;
      
      return msle;
   }

   @Override
   public double[] derivada(double[] previsto, double[] real){
      double[] erros = new double[previsto.length];
      for (int i = 0; i < previsto.length; i++) {
         erros[i] = 2 * (Math.log(1 + real[i]) - Math.log(1 + previsto[i]));
      }
      return erros;
   }
}
