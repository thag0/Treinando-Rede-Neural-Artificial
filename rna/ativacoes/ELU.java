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
      if(x > 0) return x;
      else return (alfa * (Math.exp(x)-1));
   }


   @Override
   public double derivada(double x){
      if(x > 0) return 1;
      else return (alfa * Math.exp(x));
   }  
}
