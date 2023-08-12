package rna.ativacoes;

public class TanH extends FuncaoAtivacao{

   @Override
   public double ativar(double x){
      return Math.tanh(x);
   }


   @Override
   public double derivada(double x){
      double tanh = Math.tanh(x);
      return (1 - (tanh * tanh));
   }
}
