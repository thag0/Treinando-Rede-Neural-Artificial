package rna.avaliacao.perda;

public class EntropiaCruzadaBinaria extends Perda{
   double epsilon = 1e-7;

   @Override
   public double calcular(double[] previsto, double[] real){
      double ecb = 0;

      for(int i = 0; i < real.length; i++){
         ecb += (-(real[i] * Math.log(previsto[i] + epsilon)) + (1 - real[i]) * (Math.log(1 - previsto[i] + epsilon)));
      }

      return ecb;
   }

   @Override
   public double[] derivada(double[] previsto, double[] real){
      //também não econtrei ainda uma boa resposta de como calcular isso
      double[] gradientes = new double[previsto.length];
      int n = previsto.length;

      for(int i = 0; i < n; i++){
         gradientes[i] = real[i] - previsto[i];
         // gradientes[i] = (1 / n) * (1 / previsto[i]) * (previsto[i] - real[i]);
      }
      return gradientes;
   }
}
