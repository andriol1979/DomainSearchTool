/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domainsearchtool;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author vutran
 */
public class DomainSearchTool
{
    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        try
        {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e)
        {
            // handle exception
        }
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new JFrameMain().setVisible(true);
            }
        });
    }
}
