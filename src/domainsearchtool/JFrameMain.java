/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domainsearchtool;

import Controller.CheckDomainController;
import Controller.CheckFakePRController;
import Controller.CrawlerController;
import Controller.ExportController;
import Entity.CheckDomainObject;
import Entity.ColorAssortionObject;
import Entity.DomainAgeObject;
import Entity.FilterObject;
import Entity.ItemObject;
import Util.*;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import jxl.write.WriteException;

/**
 *
 * @author vutran
 */
public class JFrameMain extends javax.swing.JFrame
{
    /**
     * Creates new form JFrameMain
     */
    public final Context _context = Context.Instance();
    public FilterObject oFilter = null;
    public List<ColorAssortionObject> _listColorAssort = null;
    public boolean isSetFilter = false;
    private JDialogShowFilter2 jDialogFilter;
    private final Object OTableLocker = new Object();
    private ArrayList<String> arrPaging = new ArrayList<>();
    private int iPaging = 0;
    private CheckFakePRController fakeController = CheckFakePRController.Instance(_context);
    List<CheckDomainObject> _listCheckData = null;
    private boolean isRun = true;
    private long lStart = 0;
    private NumberFormat nf = NumberFormat.getNumberInstance();
    private int[] arrResultPage =
    {
        100, 250, 500
    };
    public JCheckBox[] arrCkbTLDs;
    String sFilterAuction = "";

    public JFrameMain()
    {
        initComponents();
        this.setTitle("Domain Search Tool");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(JFrameMain.class.getResource("icon.png")));
        CreateFolder();
        try
        {
            //Load trace log thread
            new LogException(_context.GetConfig().GetLogPath() + _context.GetConfig().GetLogFileName()).start();
        }
        catch (IOException ex)
        {
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
        }
        LoadListCheckData();
        LoadColorAssortConfig();
        LoadDefaultSetting();
        RegisterJCbomboBoxCategoryEvent();
        this.requestFocus();
    }

    private void LoadDefaultSetting()
    {
        arrCkbTLDs = new JCheckBox[]
        {
            jCkbCom, jCkbOrg, jCkbNet, jCkbBiz, jCkbInfo, jCkbCa, jCkbTv, jCkbMobi,
            jCkbUs, jCkbWs, jCkbCc, jCkbMe, jCkbCoUk, jCkbPro, jCkbAsia, jCkbName
        };
        ConfigJCheckboxTLD();
        //Load Combobox Result/page
        for (int i : arrResultPage)
        {
            jCboResultPage.addItem(i);
        }
        //Config NumberFormat
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        //Load Object filter
        LoadFilterConfig();
    }

    private void RegisterJCbomboBoxCategoryEvent()
    {
        Enumeration<AbstractButton> enumCategories = buttonGroup1.getElements();
        while (enumCategories.hasMoreElements())
        {
            JRadioButton jRad = (JRadioButton) enumCategories.nextElement();
            jRad.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    isSetFilter = false;
                }
            });
        }
    }

    private void CreateFolder()
    {
        Util.File.CreateFolder(_context.GetConfig().GetConfigPath());
        Util.File.CreateFolder(_context.GetConfig().GetOutputPath());
        Util.File.CreateFolder(_context.GetConfig().GetLogPath());
    }

    private void LoadListCheckData()
    {
        //Delete yesterday file
        Util.File.DeleteFile_AfterNday(_context.GetConfig().GetConfigPath()
                + _context.GetConfig().GetCheckDomainFileName(), 6);
        try
        {
            //Load file today
            _listCheckData = XmlData.LoadListCheckDomainObjectFromXml(_context.GetConfig().GetConfigPath() + _context.GetConfig().GetCheckDomainFileName());
            if (_listCheckData == null)
            {
                _listCheckData = new ArrayList<>();
            }
        }
        catch (IOException ex)
        {
            _listCheckData = new ArrayList<>();
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
        }
    }

    void StartStopCheckDomainAge(boolean isStart)
    {
        if (isStart)
        {
            jLabMessage.setText("Check domain age is running...");
            jTabItem.getTableHeader().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            jTabItem.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }
        else
        {
            jLabMessage.setText("Check domain age is finished");
            jTabItem.getTableHeader().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            jTabItem.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        jTabItem.setEnabled(!isStart);
    }

    private void StartStopCrawler(boolean isStart)
    {
        if (isStart)
        {
            jProgressBar.setValue(0);
            lStart = System.currentTimeMillis();
            isRun = true;
            jLabMessage.setText("Searching.......");
            jLabPageNo.setText("-");
            jTabItem.getTableHeader().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            jTabItem.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }
        else
        {
            jTabItem.getTableHeader().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            jTabItem.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            if (jTabItem.getRowCount() == 0)
            {
                jLabMessage.setText(isSetFilter ? "No domains are found, try different filter" : "No domains found in this category, try a different category");
            }
            else
            {
                double longTime = System.currentTimeMillis() - lStart;
                longTime = longTime / 1000;
                jLabMessage.setText("Search Completed in " + nf.format(longTime) + " s");
                jLabPageNo.setText("" + (iPaging + 1));
            }
        }
        jBtnRefresh.setEnabled(!isStart);
        jBtnShowFilter.setEnabled(!isStart);
        jBtnColorAssort.setEnabled(!isStart);
        jBtnExport.setEnabled(!isStart);
        jBtnNext.setEnabled(!isStart);
        jBtnBack.setEnabled(!isStart);
        jBtnStop.setEnabled(isStart);
        jTabItem.getTableHeader().setEnabled(!isStart);
    }

    private void LoadFilterConfig()
    {
        if (jDialogFilter == null)
        {
            jDialogFilter = new JDialogShowFilter2(this, true);
        }
        try
        {
            oFilter = XmlData.LoadFilterConfigFromXml(_context.GetConfig().GetConfigPath() + _context.GetConfig().GetFilterConfigFileName());
            jDialogFilter.SetDefaultConfig(oFilter);
            boolean[] arrCkbValue = oFilter.getArrTLDs();
            for (int i = 0; i < arrCkbValue.length; i++)
            {
                arrCkbTLDs[i].setSelected(arrCkbValue[i]);
            }
            jCkbDomainExtAll.setSelected(CheckAllOrNone());
        }
        catch (IOException ex)
        {
            oFilter = jDialogFilter.CreateFilterObject();
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
        }
    }

    private void LoadColorAssortConfig()
    {
        try
        {
            _listColorAssort = Util.XmlData.LoadListColorAssortObjectFromXml(_context.GetConfig().GetConfigPath() + _context.GetConfig().GetColorAssortConfigFileName());
            if (_listColorAssort == null)
            {
                _listColorAssort = new ArrayList<>();
            }
        }
        catch (IOException ex)
        {
            _listColorAssort = new ArrayList<>();
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
        }
    }

    private void ConfigJCheckboxTLD()
    {
        for (JCheckBox jCheck : arrCkbTLDs)
        {
            jCheck.addActionListener(new java.awt.event.ActionListener()
            {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    JCheckBox jCheck = (JCheckBox) evt.getSource();
                    if (jCheck.isSelected())
                    {
                        //Kiem tra all
                        jCkbDomainExtAll.setSelected(CheckAllOrNone());
                    }
                    else
                    {
                        //Bo check
                        jCkbDomainExtAll.setSelected(false);
                    }
                    isSetFilter = true;
                }
            });
        }
    }

    private boolean CheckAllOrNone()
    {
        for (JCheckBox jCheck : arrCkbTLDs)
        {
            if (!jCheck.isSelected())
            {
                return false;
            }
        }
        return true;
    }

    private List<ItemObject> GetRowsFromTable()
    {
        List<ItemObject> lstItem = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) jTabItem.getModel();
        for (int i = 0; i < model.getRowCount(); i++)
        {
            ItemObject oItem = new ItemObject();
            oItem.setsDomainName(model.getValueAt(i, 1) == null ? null : model.getValueAt(i, 1).toString());
            oItem.setiGooglePR(model.getValueAt(i, 2) == null ? 0 : Integer.parseInt(model.getValueAt(i, 2).toString()));
            oItem.setsFakePR(model.getValueAt(i, 3) == null ? null : model.getValueAt(i, 3).toString());
            oItem.setiBacklink(model.getValueAt(i, 4) == null ? null : Integer.parseInt(model.getValueAt(i, 4).toString()));
            oItem.setiGoogleIndex(model.getValueAt(i, 5) == null ? 0 : Integer.parseInt(model.getValueAt(i, 5).toString()));
            oItem.setoDoMainAge(model.getValueAt(i, 6) == null ? null : (DomainAgeObject) model.getValueAt(i, 6));
            oItem.setiAlexa(model.getValueAt(i, 7) == null ? null : Integer.parseInt(model.getValueAt(i, 7).toString()));
            oItem.setsDmoz(model.getValueAt(i, 8) == null ? null : model.getValueAt(i, 8).toString());
            oItem.setiPrice(model.getValueAt(i, 9) == null ? null : Integer.parseInt(model.getValueAt(i, 9).toString()));
            oItem.setsAuctionType(model.getValueAt(i, 10) == null ? null : model.getValueAt(i, 10).toString());
            oItem.setsAuctionEnd(model.getValueAt(i, 11) == null ? null : model.getValueAt(i, 11).toString());
            lstItem.add(oItem);
        }
        return lstItem;
    }

    private List<String> getArrayDomainExt()
    {
        String s = "tld_";
        List<String> list = new ArrayList();
        for (int i = 0; i < arrCkbTLDs.length; i++)
        {
            JCheckBox jCkb = arrCkbTLDs[i];
            if (jCkb.isSelected())
            {
                list.add(s + jCkb.getText());
            }
        }
        return list;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jRadBtnGDExpire = new javax.swing.JRadioButton();
        jRadBtnGDCloseout = new javax.swing.JRadioButton();
        jRadBtnSnapRelease = new javax.swing.JRadioButton();
        jRadBtnSedoAuction = new javax.swing.JRadioButton();
        jRadBtnNJRelease = new javax.swing.JRadioButton();
        jRadBtnPedingDelete = new javax.swing.JRadioButton();
        jBtnShowFilter = new javax.swing.JButton();
        jBtnColorAssort = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jBtnRefresh = new javax.swing.JButton();
        jBtnExport = new javax.swing.JButton();
        jBtnStop = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jCboResultPage = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jCkbCom = new javax.swing.JCheckBox();
        jCkbOrg = new javax.swing.JCheckBox();
        jCkbNet = new javax.swing.JCheckBox();
        jCkbBiz = new javax.swing.JCheckBox();
        jCkbInfo = new javax.swing.JCheckBox();
        jCkbCa = new javax.swing.JCheckBox();
        jCkbTv = new javax.swing.JCheckBox();
        jCkbMobi = new javax.swing.JCheckBox();
        jCkbMe = new javax.swing.JCheckBox();
        jCkbWs = new javax.swing.JCheckBox();
        jCkbUs = new javax.swing.JCheckBox();
        jCkbCc = new javax.swing.JCheckBox();
        jCkbAsia = new javax.swing.JCheckBox();
        jCkbCoUk = new javax.swing.JCheckBox();
        jCkbPro = new javax.swing.JCheckBox();
        jCkbName = new javax.swing.JCheckBox();
        jCkbDomainExtAll = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabItem = new javax.swing.JTable();
        jLabPageNo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabMessage = new javax.swing.JLabel();
        jBtnBack = new javax.swing.JButton();
        jBtnNext = new javax.swing.JButton();
        jProgressBar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAutoRequestFocus(false);
        setBackground(java.awt.SystemColor.window);

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));

        jLabel3.setBackground(new java.awt.Color(0, 0, 0));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/domainsearchtool/logo.png"))); // NOI18N
        jLabel3.setFocusable(false);
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel3.setIconTextGap(2);
        jLabel3.setOpaque(true);

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Search by category", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11))); // NOI18N

        buttonGroup1.add(jRadBtnGDExpire);
        jRadBtnGDExpire.setSelected(true);
        jRadBtnGDExpire.setText("Godaddy Expiring");
        jRadBtnGDExpire.setOpaque(false);

        buttonGroup1.add(jRadBtnGDCloseout);
        jRadBtnGDCloseout.setText("Godday Closeouts");
        jRadBtnGDCloseout.setOpaque(false);

        buttonGroup1.add(jRadBtnSnapRelease);
        jRadBtnSnapRelease.setText("Snap PR-release");
        jRadBtnSnapRelease.setOpaque(false);

        buttonGroup1.add(jRadBtnSedoAuction);
        jRadBtnSedoAuction.setText("Sedo Auction");
        jRadBtnSedoAuction.setOpaque(false);

        buttonGroup1.add(jRadBtnNJRelease);
        jRadBtnNJRelease.setText("NJ Pre-Release");
        jRadBtnNJRelease.setOpaque(false);

        buttonGroup1.add(jRadBtnPedingDelete);
        jRadBtnPedingDelete.setText("Pending Delete");
        jRadBtnPedingDelete.setOpaque(false);

        jBtnShowFilter.setText("Show Filter");
        jBtnShowFilter.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jBtnShowFilterActionPerformed(evt);
            }
        });

        jBtnColorAssort.setText("Color Assortion");
        jBtnColorAssort.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jBtnColorAssortActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadBtnGDCloseout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadBtnGDExpire, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(49, 49, 49)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadBtnSedoAuction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadBtnSnapRelease, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(48, 48, 48)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadBtnNJRelease, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadBtnPedingDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBtnShowFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBtnColorAssort, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadBtnSnapRelease)
                    .addComponent(jRadBtnGDExpire)
                    .addComponent(jRadBtnNJRelease)
                    .addComponent(jBtnShowFilter))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadBtnGDCloseout)
                    .addComponent(jRadBtnSedoAuction)
                    .addComponent(jRadBtnPedingDelete)
                    .addComponent(jBtnColorAssort))
                .addGap(0, 5, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jRadBtnGDCloseout, jRadBtnGDExpire, jRadBtnNJRelease, jRadBtnPedingDelete, jRadBtnSedoAuction, jRadBtnSnapRelease});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jBtnColorAssort, jBtnShowFilter});

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Results Options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11))); // NOI18N

        jBtnRefresh.setBackground(java.awt.Color.white);
        jBtnRefresh.setText("Search");
        jBtnRefresh.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jBtnRefreshActionPerformed(evt);
            }
        });

        jBtnExport.setBackground(java.awt.Color.white);
        jBtnExport.setText("Export");
        jBtnExport.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jBtnExportActionPerformed(evt);
            }
        });

        jBtnStop.setBackground(java.awt.Color.white);
        jBtnStop.setText("Stop");
        jBtnStop.setEnabled(false);
        jBtnStop.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jBtnStopActionPerformed(evt);
            }
        });

        jLabel2.setText("Results per page:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jCboResultPage, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jBtnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(16, 16, 16)
                        .addComponent(jBtnStop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jBtnExport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jCboResultPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jBtnRefresh)
                        .addComponent(jBtnStop))
                    .addComponent(jBtnExport))
                .addGap(5, 5, 5))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 153));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "TLD's"));

        jCkbCom.setText("com");
        jCkbCom.setOpaque(false);

        jCkbOrg.setText("org");
        jCkbOrg.setOpaque(false);

        jCkbNet.setText("net");
        jCkbNet.setOpaque(false);

        jCkbBiz.setText("biz");
        jCkbBiz.setOpaque(false);

        jCkbInfo.setText("info");
        jCkbInfo.setOpaque(false);

        jCkbCa.setText("ca");
        jCkbCa.setOpaque(false);

        jCkbTv.setText("tv");
        jCkbTv.setOpaque(false);

        jCkbMobi.setText("mobi");
        jCkbMobi.setOpaque(false);

        jCkbMe.setText("me");
        jCkbMe.setOpaque(false);

        jCkbWs.setText("ws");
        jCkbWs.setOpaque(false);

        jCkbUs.setText("us");
        jCkbUs.setOpaque(false);

        jCkbCc.setText("cc");
        jCkbCc.setOpaque(false);

        jCkbAsia.setText("asia");
        jCkbAsia.setOpaque(false);

        jCkbCoUk.setText("co.uk");
        jCkbCoUk.setOpaque(false);

        jCkbPro.setText("pro");
        jCkbPro.setOpaque(false);

        jCkbName.setText("name");
        jCkbName.setOpaque(false);

        jCkbDomainExtAll.setText("All / None");
        jCkbDomainExtAll.setOpaque(false);
        jCkbDomainExtAll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jCkbDomainExtAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jCkbDomainExtAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(95, 95, 95)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCkbCom, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(jCkbBiz, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(58, 58, 58)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCkbOrg, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(jCkbCa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(58, 58, 58)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCkbNet, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                    .addComponent(jCkbTv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(62, 62, 62)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCkbMobi, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addComponent(jCkbInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(54, 54, 54)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCkbMe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCkbName, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
                .addGap(51, 51, 51)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCkbCoUk, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                    .addComponent(jCkbUs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(48, 48, 48)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCkbWs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCkbPro, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE))
                .addGap(33, 33, 33)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCkbAsia, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(jCkbCc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(13, 13, 13))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jCkbUs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCkbCoUk))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jCkbWs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCkbPro))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCkbCom)
                    .addComponent(jCkbCc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jCkbBiz)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jCkbAsia))))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jCkbOrg)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCkbCa))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jCkbNet)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCkbTv))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jCkbInfo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCkbMobi))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jCkbMe)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCkbName)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCkbDomainExtAll, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jCkbCc, jCkbCom, jCkbInfo, jCkbMe, jCkbNet, jCkbOrg, jCkbUs, jCkbWs});

        jTabItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        jTabItem.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTabItem);

        jLabPageNo.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        jLabPageNo.setForeground(java.awt.Color.gray);
        jLabPageNo.setText("-");

        jLabel1.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        jLabel1.setForeground(java.awt.Color.gray);
        jLabel1.setText("Page:");

        jLabMessage.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        jLabMessage.setForeground(java.awt.Color.gray);
        jLabMessage.setText("Message...");

        jBtnBack.setBackground(java.awt.Color.darkGray);
        jBtnBack.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        jBtnBack.setText("BACK");
        jBtnBack.setEnabled(false);
        jBtnBack.setOpaque(false);
        jBtnBack.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jBtnBackActionPerformed(evt);
            }
        });

        jBtnNext.setBackground(java.awt.Color.darkGray);
        jBtnNext.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        jBtnNext.setText("NEXT");
        jBtnNext.setEnabled(false);
        jBtnNext.setOpaque(false);
        jBtnNext.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jBtnNextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabPageNo)
                        .addGap(18, 18, 18)
                        .addComponent(jBtnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jBtnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)))
                .addContainerGap())
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jBtnBack, jBtnNext});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jBtnBack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBtnNext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabPageNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jProgressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jPanel1, jPanel2});

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jBtnBack, jBtnNext, jLabMessage, jLabPageNo, jLabel1, jProgressBar});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnShowFilterActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jBtnShowFilterActionPerformed
    {//GEN-HEADEREND:event_jBtnShowFilterActionPerformed
        jDialogFilter.setVisible(true);
    }//GEN-LAST:event_jBtnShowFilterActionPerformed

    private void jBtnColorAssortActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jBtnColorAssortActionPerformed
    {//GEN-HEADEREND:event_jBtnColorAssortActionPerformed
        new JDialogColorAssortion(this, true).setVisible(true);
    }//GEN-LAST:event_jBtnColorAssortActionPerformed

    private void jBtnRefreshActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jBtnRefreshActionPerformed
    {//GEN-HEADEREND:event_jBtnRefreshActionPerformed
        StartStopCrawler(true);
        ConfigJTable();
        arrPaging.clear();
        iPaging = 0;
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    String sParams = CreatePostParams("");
                    arrPaging.add(iPaging, sParams);
//                        System.out.println(sParams);
                    String sContent = _context.GetHttp().DoPost("http://www.namecatch.com/lists/ajax_a", sParams);
                    try
                    {
//                            Util.File.CreateFile("item.html", sContent);
                        //Create thread parser and add row into table
                        ParserData(sContent);
                    }
                    catch (UnsupportedEncodingException | InterruptedException | ExecutionException ex)
                    {
                        Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
                    }
                    //Paging
                    Pattern patternNextPage = Pattern.compile("home_page\\(\\'(\\d+)\\'\\);\\\\\">Next");
                    Matcher matcherNextPage = patternNextPage.matcher(sContent);
                    StartStopCrawler(false);
                    if (matcherNextPage.find())
                    {
                        jBtnNext.setEnabled(true);
//                            System.out.println("Page: " + matcherNextPage.group(1));
                        sParams = CreatePostParams(matcherNextPage.group(1));
                        arrPaging.add(sParams);
                    }
                    else
                    {
                        jBtnNext.setEnabled(false);
                    }
                    jBtnBack.setEnabled(false);
                }
                catch (IOException ex)
                {
                    jLabMessage.setText("Input or Output Stream is fail");
                    Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
                }
                catch (NullPointerException ex)
                {
                    jLabMessage.setText("Timeout expired. Connect to server is fail");
                    Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
                }
            }
        }).start();
    }//GEN-LAST:event_jBtnRefreshActionPerformed

    private void jBtnExportActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jBtnExportActionPerformed
    {//GEN-HEADEREND:event_jBtnExportActionPerformed
        if (jTabItem.getModel().getRowCount() == 0)
        {
            return;
        }
        JFileChooser fChooser = new JFileChooser();
        fChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fChooser.setDialogTitle("Export to excel/csv file");
        fChooser.setAcceptAllFileFilterUsed(false);
        fChooser.setApproveButtonText("Export");
        fChooser.setSelectedFile(new java.io.File(_context.GetConfig().GetOutputPath() + "export" + _context.GetConfig().GetExcelFile()));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel & CSV file",
                _context.GetConfig().GetExcelFile(), _context.GetConfig().GetCsvFile());
        fChooser.setFileFilter(filter);
        if (fChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                List<ItemObject> lstItem = GetRowsFromTable();
                if (ExportController.ExportResultToFile(_context, this, fChooser.getSelectedFile(), lstItem))
                {
                    JOptionPane.showMessageDialog(this, "Export file was successfully!");
                }
            }
            catch (IOException | WriteException ex)
            {
                Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
            }
        }
    }//GEN-LAST:event_jBtnExportActionPerformed

    private void jBtnStopActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jBtnStopActionPerformed
    {//GEN-HEADEREND:event_jBtnStopActionPerformed
        isRun = false;
    }//GEN-LAST:event_jBtnStopActionPerformed

    private void jBtnBackActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jBtnBackActionPerformed
    {//GEN-HEADEREND:event_jBtnBackActionPerformed
        iPaging -= 1;
        StartStopCrawler(true);
        ConfigJTable();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    String sParams = arrPaging.get(iPaging);
                    String sContent = _context.GetHttp().DoPost("http://www.namecatch.com/lists/ajax_a", sParams);
                    try
                    {
                        //                        Util.File.CreateFile("item.html", sContent);
                        //Create thread parser and add row into table
                        ParserData(sContent);
                    }
                    catch (UnsupportedEncodingException | InterruptedException | ExecutionException ex)
                    {
                        Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
                    }
                    StartStopCrawler(false);
                    jBtnNext.setEnabled(true);
                    if (iPaging == 0)
                    {
                        jBtnBack.setEnabled(false);
                    }
                }
                catch (IOException ex)
                {
                    jLabMessage.setText("Input or Output Stream is fail");
                    Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
                }
                catch (NullPointerException ex)
                {
                    jLabMessage.setText("Timeout expired. Connect to server is fail");
                    Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
                }
            }
        }).start();
    }//GEN-LAST:event_jBtnBackActionPerformed

    private void jBtnNextActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jBtnNextActionPerformed
    {//GEN-HEADEREND:event_jBtnNextActionPerformed
        iPaging += 1;
        StartStopCrawler(true);
        ConfigJTable();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    String sParams = arrPaging.get(iPaging);
                    String sContent = _context.GetHttp().DoPost("http://www.namecatch.com/lists/ajax_a", sParams);
                    try
                    {
                        //                        Util.File.CreateFile("item.html", sContent);
                        //Create thread parser and add row into table
                        ParserData(sContent);
                    }
                    catch (UnsupportedEncodingException | InterruptedException | ExecutionException ex)
                    {
                        Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
                    }
                    //Paging
                    Pattern patternNextPage = Pattern.compile("home_page\\(\\'(\\d+)\\'\\);\\\\\">Next");
                    Matcher matcherNextPage = patternNextPage.matcher(sContent);
                    StartStopCrawler(false);
                    if (matcherNextPage.find())
                    {
                        jBtnNext.setEnabled(true);
                        System.out.println("Page: " + matcherNextPage.group(1));
                        sParams = CreatePostParams(matcherNextPage.group(1));
                        arrPaging.add(sParams);
                    }
                    else
                    {
                        jBtnNext.setEnabled(false);
                    }
                    jBtnBack.setEnabled(true);
                }
                catch (IOException ex)
                {
                    jLabMessage.setText("Input or Output Stream is fail");
                    Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
                }
                catch (NullPointerException ex)
                {
                    jLabMessage.setText("Timeout expired. Connect to server is fail");
                    Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
                }
            }
        }).start();
    }//GEN-LAST:event_jBtnNextActionPerformed

    private void jCkbDomainExtAllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jCkbDomainExtAllActionPerformed
    {//GEN-HEADEREND:event_jCkbDomainExtAllActionPerformed
        for (JCheckBox jCheck : arrCkbTLDs)
        {
            jCheck.setSelected(jCkbDomainExtAll.isSelected());
        }
        isSetFilter = true;
    }//GEN-LAST:event_jCkbDomainExtAllActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jBtnBack;
    private javax.swing.JButton jBtnColorAssort;
    private javax.swing.JButton jBtnExport;
    private javax.swing.JButton jBtnNext;
    private javax.swing.JButton jBtnRefresh;
    private javax.swing.JButton jBtnShowFilter;
    private javax.swing.JButton jBtnStop;
    private javax.swing.JComboBox jCboResultPage;
    private javax.swing.JCheckBox jCkbAsia;
    private javax.swing.JCheckBox jCkbBiz;
    private javax.swing.JCheckBox jCkbCa;
    private javax.swing.JCheckBox jCkbCc;
    private javax.swing.JCheckBox jCkbCoUk;
    private javax.swing.JCheckBox jCkbCom;
    private javax.swing.JCheckBox jCkbDomainExtAll;
    private javax.swing.JCheckBox jCkbInfo;
    private javax.swing.JCheckBox jCkbMe;
    private javax.swing.JCheckBox jCkbMobi;
    private javax.swing.JCheckBox jCkbName;
    private javax.swing.JCheckBox jCkbNet;
    private javax.swing.JCheckBox jCkbOrg;
    private javax.swing.JCheckBox jCkbPro;
    private javax.swing.JCheckBox jCkbTv;
    private javax.swing.JCheckBox jCkbUs;
    private javax.swing.JCheckBox jCkbWs;
    private javax.swing.JLabel jLabMessage;
    private javax.swing.JLabel jLabPageNo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JProgressBar jProgressBar;
    private javax.swing.JRadioButton jRadBtnGDCloseout;
    private javax.swing.JRadioButton jRadBtnGDExpire;
    private javax.swing.JRadioButton jRadBtnNJRelease;
    private javax.swing.JRadioButton jRadBtnPedingDelete;
    private javax.swing.JRadioButton jRadBtnSedoAuction;
    private javax.swing.JRadioButton jRadBtnSnapRelease;
    private javax.swing.JScrollPane jScrollPane1;
    javax.swing.JTable jTabItem;
    // End of variables declaration//GEN-END:variables

    /*
     * Methods load jTable
     */
    private SortableTableModel CreateTableModel()
    {
        String[] arrColName =
        {
            "Sr.No.", "Domain", "<html><center>Google<br>PR</center></html>",
            "<html><center>Fake<br>PR</center></html>", "<html><center>Back<br>links</center></html>",
            "<html><center>Google<br>Index</center></html>", "Age", "Alexa",
            "Dmoz", "<html><center>Price<br>(USD)</center></html>", "Auction Type", "Auction End",
            "CheckObject", "URI"
        };
        SortableTableModel model = new SortableTableModel()
        {
            @Override
            public Class getColumnClass(int col)
            {
                switch (col)
                {
                    case 0:
                        return String.class;
                    case 1:
                        return String.class;
                    case 2:
                        return Integer.class;
                    case 3:
                        return String.class;
                    case 4:
                        return Integer.class;
                    case 5:
                        return Integer.class;
                    case 6:
                        return DomainAgeObject.class;
                    case 7:
                        return Integer.class;
                    case 8:
                        return String.class;
                    case 9:
                        return Integer.class;
                    case 10:
                        return String.class;
                    case 11:
                        return String.class;
                    default:
                        return Object.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int col)
            {
//                switch (col)
//                {
//                    case 1:
//                    {
//                        super.isCellEditable(row, col);
//                        return true;
//                    }
//                }
                return false;
            }

            @Override
            public void setValueAt(Object obj, int row, int col)
            {
                switch (col)
                {
                    case 2:
                        super.setValueAt(new Integer(obj.toString()), row, col);
                        return;
                    case 4:
                        super.setValueAt(new Integer(obj.toString()), row, col);
                        return;
                    case 5:
                        super.setValueAt(new Integer(obj.toString()), row, col);
                        return;
                    case 7:
                        super.setValueAt(new Integer(obj.toString()), row, col);
                        return;
                    case 9:
                        super.setValueAt(new Integer(obj.toString()), row, col);
                        return;
                    default:
                        super.setValueAt(obj, row, col);
                }
            }
        };
        for (String s : arrColName)
        {
            model.addColumn(s);
        }
        return model;
    }

    private void RemoveAllEventJTable()
    {
        //Remove all MouseListener event
        MouseMotionListener[] arrMouseMotion = jTabItem.getMouseMotionListeners();
        if (arrMouseMotion != null)
        {
            for (MouseMotionListener mouseMotion : arrMouseMotion)
            {
                if (mouseMotion instanceof HyperlinkCellRenderer.HyperlinkMouseListener)
                {
                    jTabItem.removeMouseMotionListener(mouseMotion);
                }
            }
        }
        //--------------------
        MouseListener[] arrMouseLis = jTabItem.getMouseListeners();
        if (arrMouseLis != null)
        {
            for (MouseListener mouseLis : arrMouseLis)
            {
                if (mouseLis instanceof HyperlinkCellRenderer.HyperlinkMouseListener)
                {
                    jTabItem.removeMouseListener(mouseLis);
                }
            }
        }
    }

    private void ConfigJTable()
    {
        jTabItem.setModel(CreateTableModel());
        RemoveAllEventJTable();
        SortButtonRenderer renderer = new SortButtonRenderer();
        jTabItem.getColumnModel().getColumn(1).setHeaderRenderer(renderer);
        jTabItem.getColumnModel().getColumn(2).setHeaderRenderer(renderer);
        jTabItem.getColumnModel().getColumn(3).setHeaderRenderer(renderer);
        jTabItem.getColumnModel().getColumn(4).setHeaderRenderer(renderer);
        jTabItem.getColumnModel().getColumn(5).setHeaderRenderer(renderer);
        jTabItem.getColumnModel().getColumn(6).setHeaderRenderer(renderer);
        jTabItem.getColumnModel().getColumn(7).setHeaderRenderer(renderer);
        jTabItem.getColumnModel().getColumn(8).setHeaderRenderer(renderer);
        jTabItem.getColumnModel().getColumn(9).setHeaderRenderer(renderer);

        HeaderButtonRenderer btnRenderer = new HeaderButtonRenderer();
        jTabItem.getColumnModel().getColumn(0).setHeaderRenderer(btnRenderer);
        jTabItem.getColumnModel().getColumn(10).setHeaderRenderer(btnRenderer);
        jTabItem.getColumnModel().getColumn(11).setHeaderRenderer(btnRenderer);

        jTabItem.getColumnModel().getColumn(0).setPreferredWidth(40);
        jTabItem.getColumnModel().getColumn(1).setPreferredWidth(140);
        jTabItem.getColumnModel().getColumn(2).setPreferredWidth(50);
        jTabItem.getColumnModel().getColumn(3).setPreferredWidth(40);
        jTabItem.getColumnModel().getColumn(4).setPreferredWidth(40);
        jTabItem.getColumnModel().getColumn(5).setPreferredWidth(50);
        jTabItem.getColumnModel().getColumn(6).setPreferredWidth(100);
        jTabItem.getColumnModel().getColumn(7).setPreferredWidth(50);
        jTabItem.getColumnModel().getColumn(8).setPreferredWidth(50);
        jTabItem.getColumnModel().getColumn(9).setPreferredWidth(50);
        jTabItem.getColumnModel().getColumn(10).setPreferredWidth(100);
        jTabItem.getColumnModel().getColumn(11).setPreferredWidth(100);
        jTabItem.removeColumn(jTabItem.getColumnModel().getColumn(12));
        jTabItem.removeColumn(jTabItem.getColumnModel().getColumn(12));
        jTabItem.getTableHeader().setPreferredSize(new Dimension(jTabItem.getColumnModel().getTotalColumnWidth(), 45));
        ((DefaultTableCellRenderer) jTabItem.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        //Set align cell
        jTabItem.getColumnModel().getColumn(0).setCellRenderer(FormatNumberCellRenderer.PositionRenderer(1));
        HyperlinkCellRenderer urlRenderer = new HyperlinkCellRenderer(new LinkAction(this, jTabItem), true, 1, _context);
        jTabItem.getColumnModel().getColumn(1).setCellRenderer(urlRenderer);
        jTabItem.getColumnModel().getColumn(2).setCellRenderer(new GooglePRCellRenderer(_listColorAssort));
        jTabItem.getColumnModel().getColumn(3).setCellRenderer(FormatNumberCellRenderer.PositionRenderer(1));
        jTabItem.getColumnModel().getColumn(4).setCellRenderer(new BacklinksCellRenderer(_listColorAssort));
        jTabItem.getColumnModel().getColumn(5).setCellRenderer(FormatNumberCellRenderer.PositionRenderer(1));
        HyperlinkCellRenderer domainAgeRenderer = new HyperlinkCellRenderer(new CheckDomainAgeAction(this, jTabItem), true, 1, _context);
        jTabItem.getColumnModel().getColumn(6).setCellRenderer(domainAgeRenderer);
        jTabItem.getColumnModel().getColumn(7).setCellRenderer(FormatNumberCellRenderer.PositionRenderer(1));
        jTabItem.getColumnModel().getColumn(8).setCellRenderer(new DmozCellRenderer(_listColorAssort));
        jTabItem.getColumnModel().getColumn(9).setCellRenderer(FormatNumberCellRenderer.getCurrencyRenderer(Locale.US, 0));
        jTabItem.getColumnModel().getColumn(10).setCellRenderer(urlRenderer);
        jTabItem.getColumnModel().getColumn(11).setCellRenderer(FormatNumberCellRenderer.PositionRenderer(1));
        jTabItem.setRowHeight(26);

        JTableHeader header = jTabItem.getTableHeader();
        header.addMouseListener(new HeaderListener(header, renderer, this));
    }

    private void AddRowToTable(int iSrNo, ItemObject oItem, CheckDomainObject oCheck)
    {
        synchronized (OTableLocker)
        {
            DefaultTableModel tableModel = (DefaultTableModel) jTabItem.getModel();
            Object[] arrRow =
            {
                iSrNo, oItem.getsDomainName(), oItem.getiGooglePR(), oItem.getsFakePR(),
                oItem.getiBacklink(), oItem.getiGoogleIndex(), oItem.getoDoMainAge(), oItem.getiAlexa(),
                oItem.getsDmoz(), oItem.getiPrice(), oItem.getsAuctionType(), oItem.getsAuctionEnd(),
                oCheck, oItem.getUriDomainLink()
            };
            tableModel.addRow(arrRow);
        }
    }

    private String CreatePostParams(String sPage) throws UnsupportedEncodingException
    {
        if (jRadBtnGDExpire.isSelected())
        {
            sFilterAuction = PostConstant.sGD_PreRelease;
        }
        else if (jRadBtnGDCloseout.isSelected())
        {
            sFilterAuction = PostConstant.sGD_Closeout;
        }
        else if (jRadBtnSnapRelease.isSelected())
        {
            sFilterAuction = PostConstant.sSnap_PreRelease;
        }
        else if (jRadBtnSedoAuction.isSelected())
        {
            sFilterAuction = PostConstant.sSedo_Auction;
        }
        else if (jRadBtnNJRelease.isSelected())
        {
            sFilterAuction = PostConstant.sName_PreRelease;
        }
        else if (jRadBtnPedingDelete.isSelected())
        {
            sFilterAuction = PostConstant.sPending_Delete;
        }
        int iStartEndType = jDialogFilter.GetStartEndType();
        List<String> listDomainExt = getArrayDomainExt();
        String sDomainExt = "";
        for (String s : listDomainExt)
        {
            sDomainExt += s + "=" + Encode("1") + "&";
        }
        int iResultCount = 1;
        switch (Integer.parseInt(jCboResultPage.getSelectedItem().toString()))
        {
            case 100:
            {
                iResultCount = 1;
                break;
            }
            case 250:
            {
                iResultCount = 2;
                break;
            }
            case 500:
            {
                iResultCount = 3;
                break;
            }
        }
        return "action=" + Encode("1") + "&filter_category=" + Encode(jDialogFilter.GetFilterCategory()) + "&page=" + ((sPage.length() == 0) ? "" : Encode(sPage))
                + "&price_range_min=" + Encode(String.valueOf(jDialogFilter.GetMinPrice()))
                + "&price_range_max=" + Encode(String.valueOf(jDialogFilter.GetMaxPrice()))
                + "&domain_length_min=" + Encode("0") + "&domain_length_max=" + Encode("22")
                + "&date_filter_min=" + Encode("0") + "&date_filter_max=" + Encode("9")
                + "&user_list_type=" + Encode("1") + "&order_col=" + Encode("name")
                + "&order_dir=" + Encode("ASC")
                + "&filter_auction[]=" + Encode(sFilterAuction)
                + (oFilter.isIsNoHyphen() ? "" : "&dashes=" + Encode("1"))
                + (oFilter.isIsNoNumber() ? "" : "&digits=" + Encode("1"))
                + "&starts_ends_type=" + Encode(String.valueOf(iStartEndType))
                + "&starts_ends_bulk="
                + "&starts_ends=" + Encode(jDialogFilter.sStartEnd)
                + "&" + sDomainExt + "result_count=" + Encode(String.valueOf(iResultCount)) + "&current_url=";
    }

    private String Encode(String s) throws UnsupportedEncodingException
    {
        return URLEncoder.encode(s, "UTF-8");
    }

    private CheckDomainObject SearchCheckDomainObject(String sDomainName)
    {
        for (CheckDomainObject oCheckData : _listCheckData)
        {
            if (oCheckData.getsDomainName().equalsIgnoreCase(sDomainName))
            {
                return oCheckData;
            }
        }
        return null;
    }

    private int GetMatchCount(String sContent, Pattern pattern)
    {
        Matcher matcher = pattern.matcher(sContent);
        int iCount = 0;
        while (matcher.find())
        {
            iCount++;
        }
        return iCount;
    }

    private String CreateAffilicateLink(String sDomain)
    {
        switch (sFilterAuction)
        {
            case PostConstant.sGD_PreRelease:
                return "https://auctions.godaddy.com/trpItemListing.aspx?miid=&isc=cjctdnamb1";
            case PostConstant.sGD_Closeout:
                return "https://auctions.godaddy.com/trpItemListing.aspx?miid=&isc=cjctdnamb1";
            case PostConstant.sSnap_PreRelease:
                return "https://www.snapnames.com/domain/" + sDomain + ".action?aff=5555";
            case PostConstant.sSedo_Auction:
                return "http://www.sedo.com/search/details.php4?tracked=&partnerid=56284&language=e&domain=" + sDomain + "&origin=partner";
            case PostConstant.sName_PreRelease:
                return "http://www.namejet.com/Pages/Auctions/BackorderDetails.aspx?domainname=" + sDomain;
            case PostConstant.sPending_Delete:
                return "https://www.snapnames.com/domain/" + sDomain + ".action?aff=5555";
        }
        return "";
    }

    private void ParserData(final String sContent) throws UnsupportedEncodingException, InterruptedException, ExecutionException, IOException
    {
        String sFilterDomain = "";
        String sDomainCharacter = oFilter.getsCharacter();
        if (oFilter.isIsNoCharacter())
        {
            sFilterDomain = "\\\\d"
                    + (sDomainCharacter.equals(jDialogFilter.COMBOBOX_NULL_SYMBOL) ? "+" : "{" + sDomainCharacter + "}\\..*?");
        }
        else
        {
            sFilterDomain = "."
                    + (sDomainCharacter.equals(jDialogFilter.COMBOBOX_NULL_SYMBOL) ? "*?" : "{" + sDomainCharacter + "}\\..*?");
        }

        Pattern patternItem = Pattern.compile("<div class=\\\\\"c_any c1\\\\\">(" + sFilterDomain + ")<\\\\/div>.*?"
                + "<div class=\\\\\"c_any c2\\\\\">(.*?)<\\\\/div>.*?"
                + "<div class=\\\\\"c_any c3\\\\\">\\$(.*?)<\\\\/div>.*?"
                + "<div class=\\\\\"c_any c4\\\\\">(.*?)<\\\\/div>.*?"
                + "<div class=\\\\\"c_any c8\\\\\">\\\\n    \\\\t<a target=\\\\\"_blank\\\\\" href=\\\\\"(.*?)\\\\\">go<\\\\/a>\\\\n", Pattern.DOTALL);
//                System.out.println(patternItem.pattern());
        Matcher matcherItem = patternItem.matcher(sContent);
        int iSrNo = 1;
        int iProgress = 0;
        jProgressBar.setMaximum(GetMatchCount(sContent, patternItem));
        while (matcherItem.find() && isRun)
        {
            ItemObject oItem = new ItemObject();
            String sDomainName = matcherItem.group(1).trim();
            oItem.setsDomainName(sDomainName.toUpperCase());
            CheckDomainObject oCheckData = SearchCheckDomainObject(sDomainName);
            if (oCheckData == null)//not exists
            {
                //Create & initialize CheckDomainObject by call PostCheckDomainObject function
                oCheckData = new CheckDomainController(_context).GetCheckDomainObject(sDomainName);
                //Check fake pagerank
//                try
//                {
//                    oCheckData.setIsFakePagerank(fakeController.GetFakePRObject(sDomainName));
//                }
//                catch (InterruptedException | ExecutionException ex)
//                {
//                    oCheckData.setIsFakePagerank(false);
//                    Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
//                }
                //End create object and insert into _listCheckData
                _listCheckData.add(oCheckData);
            }
            //Setprogressbar
            iProgress++;
            jProgressBar.setValue(iProgress);
            //Set values
            if (oFilter.isIsNoFake() && oCheckData.isIsFakePagerank())
            {
                continue;
            }
            if (oFilter.isIsDmoz() && !oCheckData.isIsDmoz())
            {
                continue;
            }
            if (oFilter.isIsAlexaRanking() && oCheckData.getiAlexaPagerank() == 0)
            {
                continue;
            }
            if (FilterWith_GooglePagerank(oCheckData.getiGooglePagerank()))
            {
                continue;
            }
            if (FilterWith_Backlinks(oCheckData.getiBacklinks()))
            {
                continue;
            }
            int iMinAge = 0;
            try
            {
                iMinAge = oCheckData.getoDoMainAge().getiDomainAge();
            }
            catch (NumberFormatException ex)
            {
            }
            catch (NullPointerException ex)
            {
                DomainAgeObject oDomainAge = new DomainAgeObject();
                oDomainAge.setsDomainAge("CHECK AGE");
                oDomainAge.setiDomainAge(-1);
                oCheckData.setoDoMainAge(oDomainAge);
            }
            if (FilterWith_MinAge(iMinAge))
            {
                continue;
            }
            oItem.setsFakePR(oCheckData.isIsFakePagerank() ? CheckDomainObject.FAKE_YES : CheckDomainObject.FAKE_NO);
            oItem.setsDmoz(oCheckData.isIsDmoz() ? CheckDomainObject.DMOZ_YES : CheckDomainObject.DMOZ_NO);
            oItem.setiGooglePR(oCheckData.getiGooglePagerank());
            oItem.setoDoMainAge(oCheckData.getoDoMainAge());
            oItem.setiBacklink(oCheckData.getiBacklinks());
            oItem.setiAlexa(oCheckData.getiAlexaPagerank());

            oItem.setsAuctionEnd(matcherItem.group(2));
            try
            {
                oItem.setiPrice(Integer.parseInt(matcherItem.group(3)));
            }
            catch (NumberFormatException ex)
            {
                oItem.setiPrice(0);
            }
            try
            {
                oItem.setiGoogleIndex(Integer.parseInt(matcherItem.group(4)));
            }
            catch (NumberFormatException ex)
            {
                oItem.setiGoogleIndex(0);
            }
            oItem.setsAuctionType("Auction");
//            String sUrl = matcherItem.group(5);
//            sUrl = sUrl == null ? "" : sUrl.replace("\\", "") + "&isc=cjctdnamb1";
            String sUrl = CreateAffilicateLink(sDomainName);
            try
            {
                oItem.setUriDomainLink(new URI(sUrl));
            }
            catch (URISyntaxException ex)
            {
                oItem.setUriDomainLink(null);
                Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
            }
            AddRowToTable(iSrNo, oItem, oCheckData);
            iSrNo++;
        }
        try
        {
            //Update file Xml
            Util.XmlData.SaveListCheckDomainObjectToXml(_listCheckData,
                    _context.GetConfig().GetConfigPath() + _context.GetConfig().GetCheckDomainFileName());
        }
        catch (IOException ex)
        {
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
        }
    }

    private boolean FilterWith_GooglePagerank(int iPagerank)
    {
        if (oFilter.getsPageRank().equals(jDialogFilter.COMBOBOX_NULL_SYMBOL))
        {
            return false;
        }
        if (Integer.parseInt(oFilter.getsPageRank()) == iPagerank)
        {
            return false;
        }
        return true;
    }

    private boolean FilterWith_Backlinks(int iBacklinks)
    {
        if (oFilter.getsBackLinks().equals(jDialogFilter.COMBOBOX_NULL_SYMBOL))
        {
            return false;
        }
        if (Integer.parseInt(oFilter.getsBackLinks()) <= iBacklinks)
        {
            return false;
        }
        return true;
    }

    private boolean FilterWith_MinAge(int iMinAge)
    {
        if (oFilter.getsMinAge().equals(jDialogFilter.COMBOBOX_NULL_SYMBOL))
        {
            return false;
        }
        if (Integer.parseInt(oFilter.getsMinAge()) <= iMinAge)
        {
            return false;
        }
        return true;
    }
}

class HeaderListener extends MouseAdapter
{
    JTableHeader header;
    SortButtonRenderer renderer;
    JFrameMain instance;

    HeaderListener(JTableHeader header, SortButtonRenderer renderer, JFrameMain instance)
    {
        this.header = header;
        this.renderer = renderer;
        this.instance = instance;
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if (instance.jTabItem.getRowCount() == 0)
        {
            return;
        }
        int col = header.columnAtPoint(e.getPoint());
        if (col == 0 || col == 10 || col == 11)
        {
            return;
        }
        int sortCol = header.getTable().convertColumnIndexToModel(col);
        renderer.setPressedColumn(col);
        renderer.setSelectedColumn(col);
        header.repaint();

        if (header.getTable().isEditing())
        {
            header.getTable().getCellEditor().stopCellEditing();
        }

        boolean isAscent;
        if (SortButtonRenderer.DOWN == renderer.getState(col))
        {
            isAscent = true;
        }
        else
        {
            isAscent = false;
        }
        ((SortableTableModel) header.getTable().getModel()).sortByColumn(sortCol, isAscent);
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        int col = header.columnAtPoint(e.getPoint());
        renderer.setPressedColumn(-1);                // clear
        header.repaint();
    }
}

class LinkAction extends AbstractAction
{
    private javax.swing.JTable jTabItem;
    private JFrameMain _parent;

    public LinkAction(JFrameMain parent, javax.swing.JTable jTab)
    {
        this._parent = parent;
        this.jTabItem = jTab;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        int row = ((Integer) e.getSource()).intValue();
        DefaultTableModel model = (DefaultTableModel) jTabItem.getModel();
        URI uri = model.getValueAt(row, 13) == null ? null : (URI) model.getValueAt(row, 13);
        if (uri == null)
        {
            return;
        }
        switch (_parent.sFilterAuction)
        {
            case PostConstant.sGD_PreRelease:
            {
                String sDomain = model.getValueAt(row, 1) == null ? "" : model.getValueAt(row, 1).toString();
                ActionGoDaddy(sDomain, uri);
                break;
            }
            case PostConstant.sGD_Closeout:
            {
                String sDomain = model.getValueAt(row, 1) == null ? "" : model.getValueAt(row, 1).toString();
                ActionGoDaddy(sDomain, uri);
                break;
            }
            default:
            {
                ActionDefault(uri);
                break;
            }
        }
    }

    private void ActionGoDaddy(String sDomain, URI uri)
    {
        try
        {
            final String sItemParam = "miid=";
            String sItemID = CrawlerController.GetItemIDFromGodaddy(_parent._context, sDomain);
            String sUrl = uri.toString();
            sUrl = sUrl.replace(sItemParam, sItemParam + sItemID);
//            System.out.println(sUrl);
            ActionDefault(new URI(sUrl));
        }
        catch (URISyntaxException | IOException ex)
        {
            Logger.getLogger(LinkAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void ActionDefault(URI uri)
    {
        try
        {
            DemoUtilities.browse(uri);
        }
        catch (IOException ex)
        {
            Logger.getLogger(LinkAction.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
        }
    }
}

class CheckDomainAgeAction extends AbstractAction
{
    private javax.swing.JTable jTabItem;
    private JFrameMain _parent;

    public CheckDomainAgeAction(JFrameMain parent, javax.swing.JTable jTab)
    {
        this._parent = parent;
        this.jTabItem = jTab;
    }

    @Override
    public void actionPerformed(final ActionEvent e)
    {
        new Thread()
        {
            @Override
            public void run()
            {
                JFrameCheckDomainAgePleaseWait.Instance().setVisible(true);
                _parent.StartStopCheckDomainAge(true);
                int row = ((Integer) e.getSource()).intValue();
                DefaultTableModel model = (DefaultTableModel) jTabItem.getModel();
                String sDomain = model.getValueAt(row, 1) == null ? "" : model.getValueAt(row, 1).toString();
                CheckDomainObject oCheck = model.getValueAt(row, 12) == null ? null : (CheckDomainObject) model.getValueAt(row, 12);
                try
                {
                    try
                    {
                        oCheck = new CheckDomainController(_parent._context, oCheck).GetCheckDomainAgeObject(sDomain);
                        model.setValueAt(oCheck.getoDoMainAge(), row, 6);
                        UpdateDataToXml(oCheck);
                    }
                    catch (InterruptedException ex)
                    {
                        Logger.getLogger(CheckDomainAgeAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    _parent.StartStopCheckDomainAge(false);
                    JFrameCheckDomainAgePleaseWait.Instance().setVisible(false);
                }
                catch (IOException | NullPointerException ex)
                {
                    Logger.getLogger(CheckDomainAgeAction.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
                }
            }
        }.start();
    }

    private void UpdateDataToXml(CheckDomainObject oCheckReplace)
    {
        for (CheckDomainObject oCheck : _parent._listCheckData)
        {
            if (oCheck.getsDomainName().equalsIgnoreCase(oCheckReplace.getsDomainName()))
            {
                oCheck.setoDoMainAge(oCheckReplace.getoDoMainAge());
                break;
            }
        }
        try
        {
            //Update file Xml
            Util.XmlData.SaveListCheckDomainObjectToXml(_parent._listCheckData,
                    _parent._context.GetConfig().GetConfigPath() + _parent._context.GetConfig().GetCheckDomainFileName());
        }
        catch (IOException ex)
        {
            Logger.getLogger(CheckDomainAgeAction.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
        }
    }
}