package rna.ativacoes;

public class Swish extends FuncaoAtivacao{
   
   @Override
   public double ativar(double x){
      return (x * sigmoid(x));
   }


   @Override
   public double derivada(double x){
      double sig = sigmoid(x);
      return (x * sig + sigmoidDx(x));
   }  


   private double sigmoid(double x){
      return (1 / (1 + Math.exp(-x)) );
   }

   
   private double sigmoidDx(double x){
      double sig = ativar(x);
      return (sig * (1-sig));
   }
}
