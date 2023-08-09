package rna.ativacoes;

public class Swish extends FuncaoAtivacao{
   
   @Override
   public double ativar(double x){
      return x / (1.0 + Math.exp(-x));
   }


   @Override
   public double derivada(double x){
      double sigmoid = ativar(x);
      return sigmoid + (1.0 - sigmoid) * x;
   }
}
