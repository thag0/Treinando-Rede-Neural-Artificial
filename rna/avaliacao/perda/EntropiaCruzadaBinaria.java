package rna.avaliacao.perda;

public class EntropiaCruzadaBinaria extends Perda{

   @Override
   public double calcular(double[] previsto, double[] real){
      double ecb = 0;

      for(int i = 0; i < real.length; i++){
         ecb += -real[i] * Math.log(previsto[i]) - (1 - real[i] * Math.log(1 - previsto[i]));
      }

      return ecb;
   }

   @Override
   public double[] derivada(double[] previsto, double[] real){
      //também não econtrei ainda uma boa resposta de como calcular isso

      double[] erros = new double[previsto.length];
      for(int i = 0; i < previsto.length; i++){
         erros[i] = real[i] - previsto[i];
      }
      return erros;
   }
}
