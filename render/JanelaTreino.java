package render;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import rna.RedeNeural;

public class JanelaTreino extends JFrame{

   public PainelTreino painelTreino;

   public JanelaTreino(int larguraImagem, int alturaImagem, float escala){
      try{
         BufferedImage icone = ImageIO.read(new File("./render/rede-neural.png"));
         setIconImage(icone);
      }catch(Exception e){}

      this.painelTreino = new PainelTreino(larguraImagem, alturaImagem, escala);
      
      setTitle("Rede neural");
      add(painelTreino);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setVisible(true);
      pack();
      setResizable(false);
      setLocationRelativeTo(null);
   }


   public void desenharTreino(RedeNeural rede){
      painelTreino.desenhar(rede);
   }
}
