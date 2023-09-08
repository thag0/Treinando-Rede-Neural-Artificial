package rna.ativacoes;


/**
 * Implementação da função de ativação Seno para uso dentro 
 * da {@code Rede Neural}.
 */
public class Seno extends FuncaoAtivacao{

   /**
    * Instancia a função de ativação Seno.
    */
   public Seno(){

   }

   @Override
   public double ativar(double x){
      return Math.sin(x);
   }


   @Override
   public double derivada(double x){
      return Math.cos(x);
   } 
}
