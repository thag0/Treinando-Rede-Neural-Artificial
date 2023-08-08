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
      if(x > 0) return x;
      else return ((alfa) * x);
   }


   @Override
   public double derivada(double x){
      if(x > 0) return 1;
      else return alfa;
   }   
}
