package rna.ativacoes;

public class Linear extends FuncaoAtivacao{

   @Override
   public double ativar(double x){
      return x;
   }


   @Override
   public double derivada(double x){
      return 1;
   }
}
