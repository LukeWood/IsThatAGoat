package goat;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TranslucentFrame {

    public static void main(String[] args) {
        new TranslucentFrame();
    }

    public TranslucentFrame() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
				    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception ex) {
				}

				JWindow frame = new JWindow();
				frame.setAlwaysOnTop(true);
				Random r = new Random();

				Goat g = new Goat(frame,Goat.COLORS[r.nextInt(Goat.COLORS.length)]);

				frame.addMouseListener(new MouseAdapter() {
				    @Override
				    public void mouseClicked(MouseEvent e) {
				    }
				});
				frame.setBackground(new Color(0,0,0,0));
				frame.setContentPane(new TranslucentPane());
				frame.add(g);
				frame.setLayout(null);
				frame.setSize(g.getWidth(),g.getHeight());
				frame.setVisible(true);
            }
        });
    }

    public class TranslucentPane extends JPanel {

        public TranslucentPane() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); 

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.0f));
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());

        }

    }

}