package utilitarios.geim;

import java.awt.image.BufferedImage;

/**
 * Gerenciador de dados de imagem para o Geim
 */
class GerenciadorDadosImagem{
   
   /**
    * Contém implementações de manuseio de dados de imagem.
    */
   public GerenciadorDadosImagem(){

   }


   public Pixel[][] gerarEstruturaImagem(int largura, int altura){
      if(largura <= 0 || altura <= 0){
         throw new IllegalArgumentException("Os valores de altura e largura fornecidos são inválidos");
      }

      Pixel[][] dadosImagem = new Pixel[altura][largura];
      for(int y = 0; y < altura; y++){
         for(int x = 0; x < largura; x++){
            dadosImagem[y][x] = new Pixel(0, 0, 0);
         }
      }

      return dadosImagem;
   }


   public Pixel[][] gerarEstruturaImagem(BufferedImage imagem){
      if(imagem == null) throw new IllegalArgumentException("A imagem fornecida é nula.");
      if(imagem.getWidth() <= 0) throw new IllegalArgumentException("A largura da imagem não pode ser menor ou igual a zero.");
      if(imagem.getHeight() <= 0) throw new IllegalArgumentException("A altura da imagem não pode ser menor ou igual a zero.");
      
      //dimensões da imagem
      int largura = imagem.getWidth();
      int altura = imagem.getHeight();
      Pixel[][] dadosImagem = new Pixel[altura][largura];
      for(int y = 0; y < altura; y++){         
         for(int x = 0; x < largura; x++){
            dadosImagem[y][x] = new Pixel();
            dadosImagem[y][x].setR(this.getR(imagem, x, y));
            dadosImagem[y][x].setG(this.getG(imagem, x, y));
            dadosImagem[y][x].setB(this.getB(imagem, x, y));
         }
      }

      return dadosImagem;
   }


   public void configurarCor(Pixel[][] estruturaImagem, int x, int y, int r, int g, int b){
      if(estruturaImagem == null) throw new IllegalArgumentException("A estrutura da imagem é nula");   
      if(y < 0 || y > estruturaImagem.length){
         throw new IllegalArgumentException("O valor x de fornecido está fora de alcance.");
      }
      if(x < 0 || x > estruturaImagem[0].length){
         throw new IllegalArgumentException("O valor y de fornecido está fora de alcance.");
      }

      estruturaImagem[y][x].setR(r);
      estruturaImagem[y][x].setG(g);
      estruturaImagem[y][x].setB(b);
   }


   public int getR(BufferedImage imagem, int x, int y){
      int rgb = imagem.getRGB(x, y);
      int r = (rgb >> 16) & 0xFF;
      return r;
   }


   public int getG(BufferedImage imagem, int x, int y){
      int rgb = imagem.getRGB(x, y);
      int g = (rgb >> 8) & 0xFF;
      return g;
   }


   public int getB(BufferedImage imagem, int x, int y){
      int rgb = imagem.getRGB(x, y);
      int b = rgb & 0xFF;
      return b;
   }


   public double[][] imagemParaDadosTreinoEscalaCinza(BufferedImage imagem){
      if(imagem == null) throw new IllegalArgumentException("A imagem fornecida é nula.");

      int larguraImagem = imagem.getWidth();
      int alturaImagem = imagem.getHeight();

      double[][] dadosImagem = new double[larguraImagem * alturaImagem][3];

      int contador = 0;
      for(int y = 0; y < alturaImagem; y++){
         for(int x = 0; x < larguraImagem; x++){
            int r = this.getR(imagem, x, y);
            int g = this.getG(imagem, x, y);
            int b = this.getB(imagem, x, y);

            // preenchendo os dados na matriz
            double xNormalizado = (double) x / (larguraImagem - 1);
            double yNormalizado = (double) y / (alturaImagem - 1);
            double escalaCinza = (r + g + b) / 3.0;
            
            dadosImagem[contador][0] = xNormalizado;// x
            dadosImagem[contador][1] = yNormalizado;// y
            dadosImagem[contador][2] = escalaCinza / 255;// escala de cinza

            contador++;
         }
      }

      return dadosImagem;
   }


   public double[][] imagemParaDadosTreinoRGB(BufferedImage imagem){
      if(imagem == null) throw new IllegalArgumentException("A imagem fornecida é nula.");
      int larguraImagem = imagem.getWidth();
      int alturaImagem = imagem.getHeight();

      double[][] dadosImagem = new double[larguraImagem * alturaImagem][5];

      int contador = 0;

      for (int y = 0; y < alturaImagem; y++) {
         for (int x = 0; x < larguraImagem; x++) {
            int r = this.getR(imagem, x, y);
            int g = this.getG(imagem, x, y);
            int b = this.getB(imagem, x, y);

            // preenchendo os dados na matriz
            double xNormalizado = (double) x / (larguraImagem-1);
            double yNormalizado = (double) y / (alturaImagem-1);  
            double rNormalizado = (double) r / 255;
            double gNormalizado = (double) g / 255;
            double bNormalizado = (double) b / 255;

            dadosImagem[contador][0] =  xNormalizado;// x
            dadosImagem[contador][1] =  yNormalizado;// y
            dadosImagem[contador][2] = rNormalizado;// vermelho
            dadosImagem[contador][3] = gNormalizado;// verde
            dadosImagem[contador][4] = bNormalizado;// azul

            contador++;
         }
      }

      return dadosImagem;
   }
}
