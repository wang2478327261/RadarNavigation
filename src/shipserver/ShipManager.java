package shipserver;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class ShipManager extends JFrame {

    private static final long serialVersionUID = 5649146024506368826L;

    private SmallPanel smallpanel;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    ShipManager frame = new ShipManager();
                    frame.setVisible(true);
                } catch (Exception e) {
                    System.exit(1);
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ShipManager() {
        initComponents();
    }

    private void initComponents() {
        setTitle("ShipManager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(20, 20, 1208, 735);
        smallpanel = new SmallPanel();
        setContentPane(smallpanel);
    }

}
