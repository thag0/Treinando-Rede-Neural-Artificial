package rna.ativacoes;

public class ELU extends FuncaoAtivacao{
   private double alfa = 0.01;


   public ELU(double alfa){
      this.alfa = alfa;
   }


   public ELU(){
      
   }

   
   @Override
   public double ativar(double x){
      return (x > 0) ? x : (alfa * (Math.exp(x) - 1));
   }


   @Override
   public double derivada(double x){
      return (x > 0) ? 1 : (alfa * Math.exp(x));
   }  
}
