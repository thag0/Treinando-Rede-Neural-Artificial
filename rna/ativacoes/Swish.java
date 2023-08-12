package rna.ativacoes;

public class Swish extends FuncaoAtivacao{
   
   @Override
   public double ativar(double x){
      return (x * sigmoid(x));
   }


   @Override
   public double derivada(double x){
      double sig = sigmoid(x);
      return sig + (x * sig * (1 -sig));
   }


   private double sigmoid(double x){
      return (1 / (1 + Math.exp(-x)) );
   }
}
