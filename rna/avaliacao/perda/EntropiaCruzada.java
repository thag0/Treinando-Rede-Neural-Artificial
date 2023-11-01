package rna.avaliacao.perda;

public class EntropiaCruzada extends Perda{
   double epsilon = 1e-7;//evitar log 0

   @Override
   public double calcular(double[] previsto, double[] real){
      double ec = 0.0;

      for(int i = 0; i < real.length; i++){
         ec += real[i] * Math.log(previsto[i] + epsilon);
      }

      return -ec;
   }

   @Override
   public double[] derivada(double[] previsto, double[] real){
      //adaptação pra minha arquitetura por enquanto
      //não econtrei ainda uma boa resposta de como calcular isso
      double[] gradientes = new double[previsto.length];
      for(int i = 0; i < previsto.length; i++){
         gradientes[i] = real[i] - previsto[i];
      }
      return gradientes;
   }
}
