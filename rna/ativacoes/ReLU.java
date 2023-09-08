package rna.ativacoes;


/**
 * Implementação da função de ativação ReLU para uso dentro 
 * da {@code Rede Neural}.
 */
public class ReLU extends FuncaoAtivacao{

   /**
    * Instancia a função de ativação ReLU.
    */
   public ReLU(){

   }
   
   
   @Override
   public double ativar(double x){
      return (x > 0) ? x : 0;
   }


   @Override
   public double derivada(double x){
      return (x > 0) ? 1 : 0;
   }
}
