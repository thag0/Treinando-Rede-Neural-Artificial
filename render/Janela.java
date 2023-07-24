package render;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import rna.RedeNeural;

public class Janela extends JFrame{

   public Painel painel;

   public Janela(){
      try{
         BufferedImage icone = ImageIO.read(new File("./render/rede-neural.png"));
         setIconImage(icone);
      }catch(Exception e){}

      this.painel = new Painel();
      
      setTitle("Rede neural");
      add(painel);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setVisible(true);
      pack();
      setResizable(false);
      setLocationRelativeTo(null);
   }


   public void desenhar(RedeNeural rede){
      painel.desenhar(rede);
   }
}
