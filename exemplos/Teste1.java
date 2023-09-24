package exemplos;

public class Teste1 {
   public static void main(String[] args) {
      func(Float.TYPE);
   }

   static void func(Class<?> tipo){
      System.out.println(tipo.equals(Double.TYPE));
   }
}
