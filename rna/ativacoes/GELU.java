package rna.ativacoes;

public class GELU extends FuncaoAtivacao{

   @Override
   public double ativar(double x){
      return 0.5 * x * (1.0 + Math.tanh(Math.sqrt(2.0 / Math.PI) * (x + 0.044715 * Math.pow(x, 3))));   
   }


   @Override
   public double derivada(double x){
      double cdf = 0.5 * (1.0 + Math.tanh(Math.sqrt(2.0 / Math.PI) * (x + 0.044715 * Math.pow(x, 3))));
      return 0.5 + 0.5 * Math.tanh(Math.sqrt(2.0 / Math.PI) * (x + 0.044715 * Math.pow(x, 3))) + x * cdf;
   }
}
