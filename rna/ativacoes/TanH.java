package rna.ativacoes;

public class TanH extends FuncaoAtivacao{

   @Override
   public double ativar(double x){
      return Math.tanh(x);
   }


   @Override
   public double derivada(double x){
      double resultado = Math.tanh(x);
      return (1 - Math.pow(resultado, 2));
   }
}
