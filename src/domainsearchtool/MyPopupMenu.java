/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domainsearchtool;

import Controller.WebStatsDomainController;
import Entity.WebStatsDomainObject;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

/**
 *
 * @author vutran
 */
public class MyPopupMenu extends JPopupMenu implements ClipboardOwner
{
    private int iRow;
    private JTable table;
    private Context _context;

    public MyPopupMenu(JTable table, int iRow, Context context)
    {
        _context = context;
        this.table = table;
        this.iRow = iRow;
        CreatePopup();
    }

    private void CreatePopup()
    {
        JMenuItem item1 = new JMenuItem("Open with...");
        JMenuItem item2 = new JMenuItem("Copy to clipboard");
        JMenuItem item3 = new JMenuItem("Check domain status");
//        this.add(item1);
        this.add(item2);
        this.add(item3);
        item1.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Action_Item1(e);
            }
        });
        item2.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Action_Item2(e);
            }
        });
        item3.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Action_Item3(e);
            }
        });
    }

    private void Action_Item1(ActionEvent event)
    {
        for (Map.Entry<Object, Object> e : System.getProperties().entrySet())
        {
            System.out.println(e);
        }
    }

    private void Action_Item2(ActionEvent e)
    {
        String sSelect = table.getValueAt(iRow, 1).toString();
        StringSelection stringSelection = new StringSelection(sSelect);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this);
    }

    private void Action_Item3(ActionEvent e)
    {
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    JFrameWebStatsDomainPleaseWait pleaseWait = new JFrameWebStatsDomainPleaseWait();
                    pleaseWait.setVisible(true);
                    String sDomain = table.getValueAt(iRow, 1).toString();
                    WebStatsDomainObject oWebStats = new WebStatsDomainController(_context).GetWebStatsDomainObject(sDomain);
                    JDialogWebStatsDomain jWebStats = new JDialogWebStatsDomain(null, true, oWebStats, sDomain);
                    pleaseWait.dispose();
                    jWebStats.setVisible(true);
                }
                catch (IOException | InterruptedException ex)
                {
                    Logger.getLogger(MyPopupMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents)
    {
    }
}
