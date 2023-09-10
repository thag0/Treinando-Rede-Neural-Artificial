package rna.ativacoes;


/**
 * Implementação da função de ativação Sigmóide para uso 
 * dentro da {@code Rede Neural}.
 */
public class Sigmoid extends FuncaoAtivacao{

   /**
    * Instancia a função de ativação Sigmoid.
    */
   public Sigmoid(){

   }
   

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
