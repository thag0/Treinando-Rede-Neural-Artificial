package rna.ativacoes;


/**
 * Implementação da função de ativação Tangente Hiperbólica 
 * para uso dentro da {@code Rede Neural}.
 */
public class TanH extends FuncaoAtivacao{

   /**
    * Instancia a função de ativação Tangente Hiperbólica.
    */
   public TanH(){

   }


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
