package rna.avaliacao.perda;

public class ErroMedioQuadrado extends Perda{

   @Override
   public double calcular(double[] previsto, double[] real){
      double mse = 0.0;
      for(int i = 0; i < previsto.length; i++){
         double d = previsto[i] - real[i];
         mse += d * d;
      }

      mse /= previsto.length;
      return mse;
   }

   @Override
   public double[] derivada(double[] previsto, double[] real){
      double[] erros = new double[previsto.length];
      for(int i = 0; i < previsto.length; i++){
         erros[i] = 2 * (real[i] - previsto[i]);
      }

      return erros;
   }
}
