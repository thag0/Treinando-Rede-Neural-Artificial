package utilitarios;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import rna.RedeNeural;

public class GerenciadorImagem{

   public GerenciadorImagem(){}


   /**
    * lê imagem 
    * @param caminho
    * @return
    */
   public BufferedImage lerImagem(String caminho){
      BufferedImage imagem = null;
      
      try{
         imagem = ImageIO.read(getClass().getResourceAsStream(caminho));
      }catch(Exception e){ }
      
      if(imagem == null) throw new IllegalArgumentException("Erro ao ler a imagem \"" + caminho + "\"");

      return imagem;
   }


   /**
    * Gera uma estrutura de dados que corresponde a uma matriz bidimensional do tamamho da imagem baseada na largura
    * e altura fornecida. Cada elemento dentro dessa matriz possui um array com três elementos, cada elemento corresponde a uma cor RGB.
    * <ul>
    *    <li>O elemento 0 é a cor vermelha (0 - 255).</li>
    *    <li>O elemento 1 é a cor verde (0 - 255).</li>
    *    <li>O elemento 2 é a cor azul (0 - 255).</li>
    * </ul>
    * <p>
    *    Todos os valores de cores da imagem serão copiados para a estrutura de dados.
    * </p>
    * @param imagem imagem com suas dimensões e cores.
    * @return estrutura de dados baseada na imagem.
    */
   public ArrayList<ArrayList<Integer[]>> gerarEstruturaImagem(BufferedImage imagem){
      //dimensões da imagem
      int largura = imagem.getWidth();
      int altura = imagem.getHeight();

      ArrayList<ArrayList<Integer[]>> dadosRGB;
      dadosRGB = new ArrayList<>();

      int r = 0, g = 0, b = 0;
      for(int y = 0; y < altura; y++){
         
         dadosRGB.add(new ArrayList<>());
         for(int x = 0; x < largura; x++){
            dadosRGB.get(y).add(new Integer[3]);

            r  = this.getR(imagem, x, y);// vermelho
            g  = this.getG(imagem, x, y);// verde
            b = this.getB(imagem, x, y);// azul 
            this.configurarCor(dadosRGB, x, y, r, g, b);
         }
      }

      return dadosRGB;
   }


   /**
    * Gera uma estrutura de dados que corresponde a uma matriz bidimensional do tamamho da imagem baseada na largura
    * e altura fornecida. Cada elemento dentro dessa matriz possui um array com três elementos, cada elemento corresponde a uma cor RGB.
    * <ul>
    *    <li>O elemento 0 é a cor vermelha (0 - 255).</li>
    *    <li>O elemento 1 é a cor verde (0 - 255).</li>
    *    <li>O elemento 2 é a cor azul (0 - 255).</li>
    * </ul>
    * <p>
    *    Todos os valores de cores da imagem serão copiados para a estrutura de dados.
    * </p>
    * @param imagem imagem com suas dimensões e cores.
    * @return estrutura de dados baseada na imagem.
    */
   public ArrayList<ArrayList<Integer[]>> gerarEstruturaImagem(int largura, int altura){
      //dimensões da imagem

      ArrayList<ArrayList<Integer[]>> dadosRGB;
      dadosRGB = new ArrayList<>();

      int r = 0, g = 0, b = 0;
      for(int y = 0; y < altura; y++){
         
         dadosRGB.add(new ArrayList<>());
         for(int x = 0; x < largura; x++){
            dadosRGB.get(y).add(new Integer[3]); 
            this.configurarCor(dadosRGB, x, y, r, g, b);
         }
      }

      return dadosRGB;
   }


   /**
    * Define a configuração de cor RGB em um ponto específico da estrutura da imagem.
    * @param imagemRGB estrutura de dados da imagem.
    * @param x coordenada x da imagem.
    * @param y cooredana y da imagem.
    * @param r valor de intensidade da cor vermelha no ponto especificado.
    * @param g valor de intensidade da cor verde no ponto especificado.
    * @param b valor de intensidade da cor azul no ponto especificado.
    * @throws IllegalArgumentException se a estrutura de dados da imagem estiver nula ou vazia.
    * @throws IllegalArgumentException se o valor de x ou y estiver fora dos índices válidos de acordo com o tamanho dos dados.
    * @throws IllegalArgumentException se o valor de R, G ou B não estiver no intervalo entre 0 e 255.
    */
   public void configurarCor(ArrayList<ArrayList<Integer[]>> imagemRGB, int x, int y, int r, int g, int b){
      if(imagemRGB == null) throw new IllegalArgumentException("A estrutura da imagem é nula");
      if(imagemRGB.size() == 0) throw new IllegalArgumentException("A estrutura da imagem está vazia.");
      
      if(y < 0 || y > imagemRGB.size()-1) throw new IllegalArgumentException("O valor x de fornecido está fora de alcance.");
      if(x < 0 || x > imagemRGB.get(0).size()-1) throw new IllegalArgumentException("O valor y de fornecido está fora de alcance.");

      if(r < 0 || r > 255) throw new IllegalArgumentException("O valor de r fornecido é inválido.");
      if(g < 0 || g > 255) throw new IllegalArgumentException("O valor de g fornecido é inválido.");
      if(b < 0 || b > 255) throw new IllegalArgumentException("O valor de b fornecido é inválido.");

      imagemRGB.get(y).get(x)[0] = r;// vermelho
      imagemRGB.get(y).get(x)[1] = g;// verde
      imagemRGB.get(y).get(x)[2] = b;// azul
   }


   /**
    * Exibe via terminal os valores de intensidade de cor vermelha, verde e azul de cada elemento da estrutura da imagem.
    * @param imagemRGB estrutura de dados da imagem.
    */
   public void exibirImagemRGB(ArrayList<ArrayList<Integer[]>> imagemRGB){
      for(int i = 0; i < imagemRGB.size(); i++){
         for(int j = 0; j < imagemRGB.get(i).size(); j++){
            String buffer = "[r: " + String.valueOf(imagemRGB.get(i).get(j)[0]) + " ";
            buffer += "g: " + String.valueOf(imagemRGB.get(i).get(j)[1]) + " ";
            buffer += "b: " + String.valueOf(imagemRGB.get(i).get(j)[2]) + "] ";
            System.out.print(buffer);
         }
         System.out.println();
      }
   }


   /**
    * Salva a estrutura de imagem em uma imagem png.
    * @param caminho caminho relativo, deve conter o nome do arquivo, sem extensão
    * @param dadosRGB estrutura de dados da imagem.
    */
   public void exportarImagemPng(String caminho, ArrayList<ArrayList<Integer[]>> dadosRGB){
      int larguraImagem = dadosRGB.get(0).size();
      int alguraImagem = dadosRGB.size();

      BufferedImage imagem = new BufferedImage(larguraImagem, alguraImagem, BufferedImage.TYPE_INT_RGB);

      int r = 0;
      int g = 0;
      int b = 0;

      for(int y = 0; y < alguraImagem; y++){
         for(int x = 0; x < larguraImagem; x++){
            r = dadosRGB.get(y).get(x)[0];
            g = dadosRGB.get(y).get(x)[1];
            b = dadosRGB.get(y).get(x)[2];

            int rgb = (r << 16) | (g << 8) | b;
            imagem.setRGB(x, y, rgb);
         }
      }

      try{
         File arquivo = new File((caminho + ".png"));
         ImageIO.write(imagem, "png", arquivo);

      }catch(Exception e){
         System.out.println("Erro ao exportar imagem");
         e.printStackTrace();
      }
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


   public double[][] imagemParaDadosTreinoEscalaCinza(GerenciadorImagem gi, BufferedImage imagem){
      int larguraImagem = imagem.getWidth();
      int alturaImagem = imagem.getHeight();

      double[][] dadosImagem = new double[larguraImagem * alturaImagem][3];

      int contador = 0;

      for (int y = 0; y < alturaImagem; y++) {
         for (int x = 0; x < larguraImagem; x++) {
            int r = gi.getR(imagem, x, y);
            int g = gi.getG(imagem, x, y);
            int b = gi.getB(imagem, x, y);

            double escalaCinza = (r + g + b) / 3.0;

            // Preencha os dados na matriz
            double xNormalizado = (double) x / (larguraImagem-1);
            double yNormalizado = (double) y / (alturaImagem-1);
            dadosImagem[contador][0] =  xNormalizado;// x
            dadosImagem[contador][1] =  yNormalizado;// y
            dadosImagem[contador][2] = escalaCinza/255;// escala de cinza

            contador++;
         }
      }

      return dadosImagem;
   }


   /**
    * <p>
    *    Amplia a imagem e salva num arquivo .png externo usando uma rede neural. 
    * </p>
    * O processo acontece normalizando as coordenadas dos pixels numa escala entre 0 e 1, onde quanto mais próximo de 1 o valor for
    * maior o x ou y se aproxima da largura ou altura da imagem.
    * A rede deve ser treianda previamente para lidar com as entradas normalizadas, assim ela consegue normalizar também os valores 
    * de brilho da imagem em resolução maior.
    * @param imagem imagem que será ampliada.
    * @param rede rede neural treinada para lidar com a imagem.
    * @param escala escala de ampliação da nova imagem.
    * @throws IllegalArgumentException se a imagem for nula.
    * @throws IllegalArgumentException se o valor de escala for menor que 1.
    */
   public void ampliarImagem(BufferedImage imagem, RedeNeural rede, int escala){
      if(imagem == null) throw new IllegalArgumentException("A imagem fornecida é nula.");
      if(escala < 1) throw new IllegalArgumentException("O valor de escala não pode ser menor que 1.");

      int nEntrada = rede.obterCamadaEntrada().neuronios.length;
      nEntrada -= (rede.obterCamadaEntrada().temBias) ? 1 : 0;

      double[] entradaRede = new double[nEntrada];
      double[] saidaRede = new double[rede.saida.neuronios.length];
      ArrayList<ArrayList<Integer[]>> imagemAmpliada =this.gerarEstruturaImagem(imagem.getHeight()*escala, imagem.getWidth()*escala);
      
      int alturaImagem = imagemAmpliada.size();
      int larguraImagem = imagemAmpliada.get(0).size();

      for(int y = 0; y < alturaImagem; y++){
         for(int x = 0; x < larguraImagem; x++){

            entradaRede[0] = (double)x / (larguraImagem-1);
            entradaRede[1] = (double)y / (alturaImagem-1);

            rede.calcularSaida(entradaRede);

            saidaRede[0] = rede.saida.neuronios[0].saida * 255;
           this.configurarCor(imagemAmpliada, x, y, (int)saidaRede[0], (int)saidaRede[0], (int)saidaRede[0]);
         }
      }

      this.exportarImagemPng("./imagem-ampliada", imagemAmpliada);
   }
}
