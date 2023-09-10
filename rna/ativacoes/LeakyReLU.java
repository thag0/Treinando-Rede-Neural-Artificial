package rna.ativacoes;


/**
 * Implementação da função de ativação LeakyReLU para uso dentro 
 * da {@code Rede Neural}.
 * <p>
 *    É possível configurar o valor de {@code alfa} para obter
 *    melhores resultados.
 * </p>
 */
public class LeakyReLU extends FuncaoAtivacao{

   /**
    * Valor alfa da função LeakyReLU.
    */
   private double alfa;


   /**
    * Instancia a função de ativação LeakyReLU com 
    * seu valor de alfa configurável.
    * @param alfa novo valor alfa.
    */
   public LeakyReLU(double alfa){
      this.alfa = alfa;
   }


   /**
    * Instancia a função de ativação LeakyReLU com 
    * seu valor de alfa padrão.
    * <p>
    *    O valor padrão para o alfa é {@code 0.01}.
    * </p>
    */
   public LeakyReLU(){
      this(0.01);
   }


   @Override
   public double ativar(double x){
      return (x > 0) ? x : (alfa * x);
   }


   @Override
   public double derivada(double x){
      return (x > 0) ? 1 : (alfa);
   }   
}
