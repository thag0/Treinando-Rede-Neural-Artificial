package rna.ativacoes;

public class Sigmoid extends FuncaoAtivacao{

   @Override
   public double ativar(double x){
      return (1 / (1 + Math.exp(-x)) );
   }


   @Override
   public double derivada(double x){
      double sig = (1 / (1 + Math.exp(-x)) );
      return (sig * (1-sig));
   }
}
