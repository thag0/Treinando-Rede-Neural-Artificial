package rna.ativacoes;


/**
 * Implementação da função de ativação Linear para uso dentro 
 * da {@code Rede Neural}.
 */
public class Linear extends FuncaoAtivacao{

   /**
    * Instancia a função de ativação Linear.
    */
   public Linear(){

   }


   @Override
   public double ativar(double x){
      return x;
   }


   @Override
   public double derivada(double x){
      return 1;
   }
}
