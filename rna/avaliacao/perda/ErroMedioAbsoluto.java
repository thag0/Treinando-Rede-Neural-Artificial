package rna.avaliacao.perda;

public class ErroMedioAbsoluto extends Perda{

   @Override
   public double calcular(double[] previsto, double[] real){
      int amostras = previsto.length;
      double mae = 0;

      for(int i = 0; i < amostras; i++){
         mae += Math.abs(real[i] - previsto[i]);
      }

      mae /= previsto.length;
      
      return mae;
   }

   @Override
   public double[] derivada(double[] previsto, double[] real){
      double[] erros = new double[previsto.length];
      for(int i = 0; i < previsto.length; i++){
         erros[i] = real[i] - previsto[i];
      }
      return erros;
   }
}
