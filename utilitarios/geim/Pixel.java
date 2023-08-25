package utilitarios.geim;


/**
 * Classe responsável por representar o valor de cor de um pixel de acordo com o padrão
 * de cor rgb.
 */
public class Pixel{
   private int r;
   private int g;
   private int b;


   /**
    * Cria uma intância de um pixel.
    * Os valores fornecidos serão configurados automaticamente.
    * @param r
    * @param g
    * @param b
    */
   public Pixel(int r, int g, int b){
      this.r = r;
      this.g = g;
      this.b = b;
   }


   /**
    * Cria uma intância de um pixel.
    * Os valores fornecidos serão inicializados como 0.
    */
   public Pixel(){
      this.r = 0;
      this.g = 0;
      this.b = 0;
   }


   /**
    * Configura o valor de cor vermelha do pixel.
    * <p><strong>
    *    Valores acima de 255 serão automaticamente configurados como 255.
    * </strong></p>
    * <p><strong>
    *    Valores abaixo de 0 serão automaticamente configurados como 0.
    * </strong></p>
    * @param r novo valor de cor vermelha.
    */
   public void setR(int r){
      if(r > 255) this.r = 255;
      else if(r < 0) this.r = 0;
      else this.r = r;
   }


   /**
    * Configura o valor de cor verde do pixel.
    * <p><strong>
    *    Valores acima de 255 serão automaticamente configurados como 255.
    * </strong></p>
    * <p><strong>
    *    Valores abaixo de 0 serão automaticamente configurados como 0.
    * </strong></p>
    * @param g novo valor de cor verde.
    */
   public void setG(int g){
      if(g > 255) this.g = 255;
      else if(g < 0) this.g = 0;
      else this.g = g;
   }


   /**
    * Configura o valor de cor azul do pixel.
    * <p><strong>
    *    Valores acima de 255 serão automaticamente configurados como 255.
    * </strong></p>
    * <p><strong>
    *    Valores abaixo de 0 serão automaticamente configurados como 0.
    * </strong></p>
    * @param b novo valor de cor azul.
    */
   public void setB(int b){
      if(b > 255) this.b = 255;
      else if(b < 0) this.b = 0;
      else this.b = b;
   }


   /**
    * @return valor da cor vemelha do pixel.
    */
   public int getR(){
      return this.r;
   }


   /**
    * @return valor da cor verde do pixel.
    */
   public int getG(){
      return this.g;
   }


   /**
    * @return valor da cor azul do pixel.
    */
   public int getB(){
      return this.b;
   }
}
