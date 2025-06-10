import java.awt.Toolkit;
import javax.swing.JFrame;

public class Frame extends JFrame {
    Frame() {
        this.add(new Panel());
        this.setTitle("Meeb World");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("resources\\default\\meeb\\smiley_j.png"));;
    }
}
