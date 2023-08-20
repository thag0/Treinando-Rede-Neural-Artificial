package rna.ativacoes;

public class SoftPlus extends FuncaoAtivacao{

   @Override
   public double ativar(double x){
      return Math.log(1 + Math.exp(x));
   }


   @Override
   public double derivada(double x){
      return 1 / (1 + Math.exp(-x));
   }
}
