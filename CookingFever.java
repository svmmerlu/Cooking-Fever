import javax.swing.*;
//sdf
public class CookingFever extends JFrame {
    CookingFeverPanel game = new CookingFeverPanel();

    public CookingFever() {
        super("CookingFever");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // make the window non-resizable
        add(game);
        pack(); // size the frame to fit the preferred size of the panel
        setLocationRelativeTo(null); // center the frame on the screen
        setVisible(true);
    }
 
    public static void main(String[] arguments) {
        new CookingFever();
    }
}