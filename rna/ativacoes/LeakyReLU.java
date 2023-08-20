package rna.ativacoes;

public class LeakyReLU extends FuncaoAtivacao{
   private double alfa = 0.01;


   public LeakyReLU(){

   }


   public LeakyReLU(double alfa){
      this.alfa = alfa;
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
