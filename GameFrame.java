/* 
 * Author: Hazel Bains
 * Date: May 21
 * Description: GameFrame creates frame for running game
 */

import java.awt.*;
import javax.swing.*;

public class GameFrame extends JFrame {

        GamePanel panel;

        public GameFrame() {
                panel = new GamePanel();
                this.add(panel);
                this.setTitle("Breakout");
                this.setResizable(false); // frame will not change in size
                this.setBackground(Color.black);
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.pack();
                this.setVisible(true);
                this.setLocationRelativeTo(null);
        }

}
