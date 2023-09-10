package rna.ativacoes;


/**
 * Implementação da função de ativação SoftPlus para uso 
 * dentro da {@code Rede Neural}.
 */
public class SoftPlus extends FuncaoAtivacao{

   /**
    * Instancia a função de ativação SoftPlus.
    */
   public SoftPlus(){

   }

   @Override
   public double ativar(double x){
      return Math.log(1 + Math.exp(x));
   }


   @Override
   public double derivada(double x){
      return 1 / (1 + Math.exp(-x));
   }
}
