package utilitarios.geim;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import rna.RedeNeural;

/**
 * Gerenciador de arquivos do Geim
 */
class GerenciadorArquivos{
   
   /**
    * Contém as implementações de gestão de arquivos do Geim
    */
   public GerenciadorArquivos(){

   }


   public BufferedImage lerImagem(String caminho){
      BufferedImage imagem = null;
      
      try{
         imagem = ImageIO.read(getClass().getResourceAsStream(caminho));
      }catch(Exception e){ 
         e.printStackTrace();
      }
      
      if(imagem == null) throw new IllegalArgumentException("Erro ao ler a imagem \"" + caminho + "\"");

      return imagem;
   }


   public void exportarImagemPng(Pixel[][] estruturaImagem, String caminho){
      int alturaImagem = estruturaImagem.length;
      int larguraImagem = estruturaImagem[0].length;

      BufferedImage imagem = new BufferedImage(larguraImagem, alturaImagem, BufferedImage.TYPE_INT_RGB);

      int r = 0;
      int g = 0;
      int b = 0;
      int rgb = 0;

      for(int y = 0; y < alturaImagem; y++){
         for(int x = 0; x < larguraImagem; x++){
            r = estruturaImagem[y][x].getR();
            g = estruturaImagem[y][x].getG();
            b = estruturaImagem[y][x].getB();

            rgb = (r << 16) | (g << 8) | b;
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


   public void exportarImagemEscalaCinza(GerenciadorDadosImagem gdi, BufferedImage imagem, RedeNeural rede, float escala, String caminho){
      if(imagem == null) throw new IllegalArgumentException("A imagem fornecida é nula.");
      if(escala <= 0) throw new IllegalArgumentException("O valor de escala não pode ser menor que 1.");
      if(rede.obterCamadaSaida().obterQuantidadeNeuronios() != 1){
         throw new IllegalArgumentException("A rede deve trabalhar apenas com um neurônio na camada de saída para a escala de cinza.");
      }

      int nEntrada = rede.obterCamadaEntrada().obterQuantidadeNeuronios();
      nEntrada -= (rede.obterCamadaEntrada().temBias()) ? 1 : 0;

      double[] entradaRede = new double[nEntrada];
      double[] saidaRede = new double[rede.obterCamadaSaida().obterQuantidadeNeuronios()];
      int larguraFinal = (int)(imagem.getWidth() * escala);
      int alturaFinal = (int)(imagem.getHeight() * escala);

      Pixel[][] imagemAmpliada = gdi.gerarEstruturaImagem(larguraFinal, alturaFinal);
      
      int alturaImagem = imagemAmpliada.length;
      int larguraImagem = imagemAmpliada[0].length;

      for(int y = 0; y < alturaImagem; y++){
         for(int x = 0; x < larguraImagem; x++){

            entradaRede[0] = (double)x / (larguraImagem-1);
            entradaRede[1] = (double)y / (alturaImagem-1);

            rede.calcularSaida(entradaRede);

            saidaRede[0] = rede.obterCamadaSaida().neuronio(0).saida * 255;
           gdi.configurarCor(imagemAmpliada, x, y, (int)saidaRede[0], (int)saidaRede[0], (int)saidaRede[0]);
         }
      }

      this.exportarImagemPng(imagemAmpliada, caminho);
   }


   public void exportarImagemRGB(GerenciadorDadosImagem gdi, BufferedImage imagem, RedeNeural rede, float escala, String caminho){
      if(imagem == null) throw new IllegalArgumentException("A imagem fornecida é nula.");
      if(escala <= 0) throw new IllegalArgumentException("O valor de escala não pode ser menor que 1.");
      if(rede.obterCamadaSaida().obterQuantidadeNeuronios() != 3){
         throw new IllegalArgumentException("A rede deve trabalhar apenas com três neurônios na saída para RGB.");
      }

      //quantidade de neuronios de entrada
      int nEntrada = rede.obterCamadaEntrada().obterQuantidadeNeuronios();
      nEntrada -= (rede.obterCamadaEntrada().temBias()) ? 1 : 0;

      double[] entradaRede = new double[nEntrada];
      double[] saidaRede = new double[rede.obterCamadaSaida().obterQuantidadeNeuronios()];

      //estrutura de dados da imagem
      int larguraFinal = (int)(imagem.getWidth() * escala);
      int alturaFinal = (int)(imagem.getHeight() * escala);
      Pixel[][] imagemAmpliada = gdi.gerarEstruturaImagem(larguraFinal, alturaFinal);
      
      int alturaImagem = imagemAmpliada.length;
      int larguraImagem = imagemAmpliada[0].length;

      for(int y = 0; y < alturaImagem; y++){
         for(int x = 0; x < larguraImagem; x++){
            imagemAmpliada[y][x] = new Pixel();

            //posição do pixel
            entradaRede[0] = (double)x / (larguraImagem-1);
            entradaRede[1] = (double)y / (alturaImagem-1);

            rede.calcularSaida(entradaRede);

            //cor do pixel em rgb
            saidaRede[0] = rede.obterCamadaSaida().neuronio(0).saida * 255;
            saidaRede[1] = rede.obterCamadaSaida().neuronio(1).saida * 255;
            saidaRede[2] = rede.obterCamadaSaida().neuronio(2).saida * 255;
            gdi.configurarCor(imagemAmpliada, x, y, (int)saidaRede[0], (int)saidaRede[1], (int)saidaRede[2]);
         }
      }

      this.exportarImagemPng(imagemAmpliada, caminho);
   }
}
