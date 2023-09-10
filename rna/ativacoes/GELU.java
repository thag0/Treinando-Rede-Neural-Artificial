package rna.ativacoes;


/**
 * Implementação da função de ativação GELU para uso dentro 
 * da {@code Rede Neural}.
 */
public class GELU extends FuncaoAtivacao{


   /**
    * Intancia uma nova função de ativação GELU.
    */
   public GELU(){

   }


   @Override
   public double ativar(double x){
      return 0.5 * x * (1.0 + Math.tanh(Math.sqrt(2.0 / Math.PI) * (x + 0.044715 * Math.pow(x, 3))));   
   }


   @Override
   public double derivada(double x){
      double cdf = 0.5 * (1.0 + Math.tanh(Math.sqrt(2.0 / Math.PI) * (x + 0.044715 * Math.pow(x, 3))));
      return 0.5 * (1.0 + cdf + x * Math.exp(-Math.pow(x, 2) / 2.0) / Math.sqrt(2.0 * Math.PI));
   }
}
