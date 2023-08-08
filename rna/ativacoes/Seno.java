package rna.ativacoes;

public class Seno extends FuncaoAtivacao{

   @Override
   public double ativar(double x){
      return Math.sin(x);
   }


   @Override
   public double derivada(double x){
      return Math.cos(x);
   } 
}
