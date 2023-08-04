package render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import rna.RedeNeural;

public class PainelTreino extends JPanel{
   final int largura;
   final int altura;
   RedeNeural rede;
   double[] entradaRede;

   BufferedImage imagem;
   int epocaAtual = 0;

   //evitar inicializações durante a renderização
   int r, b, g, rgb;
   int x, y;
   
   public PainelTreino(int larguraImagem, int alturaImagem, float escala){
      this.largura = (int) (larguraImagem*escala);
      this.altura = (int)  (alturaImagem*escala);

      imagem = new BufferedImage(this.largura, this.altura, BufferedImage.TYPE_INT_RGB);

      int arq[] = {1, 1, 1};
      this.rede = new RedeNeural(arq);
      this.rede.compilar();

      setPreferredSize(new Dimension(this.largura, this.altura));
      setBackground(new Color(30, 30, 30));
      setFocusable(true);
      setDoubleBuffered(true);
      setEnabled(true);
      setVisible(true);
   }


   public void desenhar(RedeNeural rede, int epocasPorFrame){
      this.rede = rede;
      
      int nEntrada = rede.entrada.neuronios.length;
      nEntrada -= (rede.entrada.temBias) ? 1 : 0;
      entradaRede = new double[nEntrada];

      if(rede.saida.neuronios.length == 1){//escala de cinza
         for(y = 0; y < this.altura; y++){
            for(x = 0; x < this.largura; x++){
               entradaRede[0] = (double)x / this.largura;
               entradaRede[1] = (double)y / this.altura;
               rede.calcularSaida(entradaRede);
               int cinza = (int)(rede.saida.neuronios[0].saida * 255);

               r = cinza;
               g = cinza;
               b = cinza;
               rgb = (r << 16) | (g << 8) | b;
               imagem.setRGB(x, y, rgb);
            }
         } 

      }else if(rede.saida.neuronios.length == 3){//rgb
         for(y = 0; y < this.altura; y++){
            for(x = 0; x < this.largura; x++){
               entradaRede[0] = (double)x / this.largura;
               entradaRede[1] = (double)y / this.altura;
               rede.calcularSaida(entradaRede);
               r = (int)(rede.saida.neuronios[0].saida * 255);
               g = (int)(rede.saida.neuronios[1].saida * 255);
               b = (int)(rede.saida.neuronios[2].saida * 255);
               rgb = (r << 16) | (g << 8) | b;
               imagem.setRGB(x, y, rgb);
            }
         }
      }

      epocaAtual = epocasPorFrame;
      repaint();
   }


   @Override
   protected void paintComponent(Graphics g){
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;

      g2.drawImage(imagem, 0, 0, null);

      g2.setColor(Color.BLACK);
      g2.drawString(("Época: " + epocaAtual), 6, 16);
      g2.setColor(Color.WHITE);
      g2.drawString(("Época: " + epocaAtual), 5, 15);

      g2.dispose();
   }
}
