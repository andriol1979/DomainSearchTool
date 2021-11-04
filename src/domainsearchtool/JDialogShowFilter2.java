/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domainsearchtool;

import Entity.CboPriceObject;
import Entity.FilterObject;
import Util.CustomListCellRenderer;
import Util.XmlData;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author huynguyen
 */
public class JDialogShowFilter2 extends javax.swing.JDialog
{
    /**
     * Creates new form JDialogShowFilter2
     */
    private JFrameMain jMain;
    public String sStartEnd = "";
    public final String COMBOBOX_NULL_SYMBOL = "-";
    private String[] arrPriceDisplays =
    {
        "0", "1 ~ 5", "6 ~ 10", "11 ~ 25", "26 ~ 50", "51 ~ 75", "76 ~ 100",
        "101 ~ 200", "201 ~ 300", "301 ~ 400", "401 ~ 500", "501 ~ 750", "751 ~ 1000", "1001 ~ 2000", "MAX"
    };
    private int[] arrPriceValues =
    {
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14
    };
    private int[] arrBackLink =
    {
        1, 10, 20, 50, 100, 200, 500, 1000
    };

    public JDialogShowFilter2(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        this.jMain = (JFrameMain) parent;
        initComponents();
        this.setTitle("Filter");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(JFrameMain.class.getResource("icon.png")));
        SetValueComboboxPrice();
        SetValueCombobox();
    }

    private void SetValueComboboxPrice()
    {
        jCboMinPrice.setRenderer(CustomListCellRenderer.PositionRenderer(1));
        jCboMaxPrice.setRenderer(CustomListCellRenderer.PositionRenderer(1));
        for (int i = 0; i < arrPriceDisplays.length; i++)
        {
            jCboMinPrice.addItem(new CboPriceObject(arrPriceDisplays[i], arrPriceValues[i]));
            jCboMaxPrice.addItem(new CboPriceObject(arrPriceDisplays[i], arrPriceValues[i]));
        }
        jCboMinPrice.setSelectedIndex(0);
        jCboMaxPrice.setSelectedIndex(arrPriceDisplays.length - 1);
    }

    private void SetValueCombobox()
    {
        jCboCharacter.addItem(COMBOBOX_NULL_SYMBOL);
        jCboNumber.addItem(COMBOBOX_NULL_SYMBOL);
        jCboPR.addItem(COMBOBOX_NULL_SYMBOL);
        jCboBacklink.addItem(COMBOBOX_NULL_SYMBOL);
        jCboMinAge.addItem(COMBOBOX_NULL_SYMBOL);
        for (int i = 1; i <= 30; i++)
        {
            jCboCharacter.addItem(i + "Xer Domains");
        }
        for (int i = 3; i <= 5; i++)
        {
            jCboNumber.addItem(i + " Digit Domains");
        }
        for (int i = 0; i <= 10; i++)
        {
            jCboPR.addItem(i + " Pr Number");
        }
        for (int i : arrBackLink)
        {
            jCboBacklink.addItem(i);
        }
        for (int i = 1990; i <= 2012; i++)
        {
            jCboMinAge.addItem(i);
        }
        jCboCharacter.setSelectedIndex(0);
        jCboNumber.setSelectedIndex(0);
        jCboPR.setSelectedIndex(0);
        jCboBacklink.setSelectedIndex(0);
        jCboMinAge.setSelectedIndex(0);
    }

    private int GetSelectIndex_MinPrice(FilterObject oFilter)
    {
        for (int i = 0; i < arrPriceValues.length; i++)
        {
            if (oFilter.getoMinPrice().getiValue() == arrPriceValues[i])
            {
                return i;
            }
        }
        return 0;
    }

    private int GetSelectIndex_MaxPrice(FilterObject oFilter)
    {
        for (int i = 0; i < arrPriceValues.length; i++)
        {
            if (oFilter.getoMaxPrice().getiValue() == arrPriceValues[i])
            {
                return i;
            }
        }
        return arrPriceValues.length - 1;
    }

    private int GetSelectIndex_Backlinks(FilterObject oFilter)
    {
        if (oFilter.getsBackLinks().equals(COMBOBOX_NULL_SYMBOL))
        {
            return 0;
        }
        for (int i = 0; i < arrBackLink.length; i++)
        {
            if (arrBackLink[i] == Integer.parseInt(oFilter.getsBackLinks()))
            {
                return i + 1;
            }
        }
        return 0;
    }

    private int GetSelectIndex_MinAge(FilterObject oFilter)
    {
        if (oFilter.getsMinAge().equals(COMBOBOX_NULL_SYMBOL))
        {
            return 0;
        }
        int j = 0;
        for (int i = 1990; i <= 2012; i++)
        {
            if (i == Integer.parseInt(oFilter.getsMinAge()))
            {
                return j;
            }
            j++;
        }
        return 0;
    }

    public void SetDefaultConfig(FilterObject oFilter)
    {
        jTxtSearchBegin.setText(oFilter == null ? "" : oFilter.getsSearchBegin());
        jTxtSearchContain.setText(oFilter == null ? "" : oFilter.getsSearchContain());
        jTxtSearchEndwith.setText(oFilter == null ? "" : oFilter.getsSearchEndWith());
        if (oFilter == null)
        {
            jCboMinPrice.setSelectedIndex(0);
            jCboMaxPrice.setSelectedIndex(arrPriceDisplays.length - 1);
        }
        else
        {
            jCboMinPrice.setSelectedIndex(GetSelectIndex_MinPrice(oFilter));
            jCboMaxPrice.setSelectedIndex(GetSelectIndex_MaxPrice(oFilter));
        }
        jCkbDmozListed.setSelected(oFilter == null ? false : oFilter.isIsDmoz());
        jCkbAlexaRanking.setSelected(oFilter == null ? false : oFilter.isIsAlexaRanking());
        jCkbNoFake.setSelected(oFilter == null ? false : oFilter.isIsNoFake());
        jCkbNoNumber.setSelected(oFilter == null ? false : oFilter.isIsNoNumber());
        jCkbNoCharacter.setSelected(oFilter == null ? false : oFilter.isIsNoCharacter());
        jCkbNoHyphen.setSelected(oFilter == null ? false : oFilter.isIsNoHyphen());
//        System.out.println("Char: " + oFilter.getsCharacter());
        jCboCharacter.setSelectedIndex(oFilter == null || oFilter.getsCharacter().equals(COMBOBOX_NULL_SYMBOL)
                ? 0 : Integer.parseInt(oFilter.getsCharacter()));
        jCboNumber.setSelectedIndex(oFilter == null ? 0 : Integer.parseInt(oFilter.getsNumber()));
        jCboPR.setSelectedIndex(oFilter == null || oFilter.getsPageRank().equals(COMBOBOX_NULL_SYMBOL)
                ? 0 : Integer.parseInt(oFilter.getsPageRank()) + 1);
        jCboBacklink.setSelectedIndex(oFilter == null ? 0 : GetSelectIndex_Backlinks(oFilter));
        jCboMinAge.setSelectedIndex(oFilter == null ? 0 : GetSelectIndex_MinAge(oFilter));
    }

    public FilterObject CreateFilterObject()
    {
        FilterObject oFilter = new FilterObject();
        oFilter.setsSearchBegin(jTxtSearchBegin.getText().trim());
        oFilter.setsSearchContain(jTxtSearchContain.getText().trim());
        oFilter.setsSearchEndWith(jTxtSearchEndwith.getText().trim());
        oFilter.setoMinPrice((CboPriceObject) jCboMinPrice.getSelectedItem());
        oFilter.setoMaxPrice((CboPriceObject) jCboMaxPrice.getSelectedItem());
        oFilter.setIsDmoz(jCkbDmozListed.isSelected());
        oFilter.setIsAlexaRanking(jCkbAlexaRanking.isSelected());
        oFilter.setIsNoFake(jCkbNoFake.isSelected());
        oFilter.setIsNoNumber(jCkbNoNumber.isSelected());
        oFilter.setIsNoCharacter(jCkbNoCharacter.isSelected());
        oFilter.setIsNoHyphen(jCkbNoHyphen.isSelected());
        oFilter.setsCharacter(jCboCharacter.getSelectedItem().toString().substring(0, 1));
        oFilter.setsNumber(String.valueOf(jCboNumber.getSelectedIndex()));
        oFilter.setsPageRank(jCboPR.getSelectedItem().toString().toString().substring(0, 1));
        oFilter.setsBackLinks(jCboBacklink.getSelectedItem().toString());
        oFilter.setsMinAge(jCboMinAge.getSelectedItem().toString());
        boolean[] arrTLDs = new boolean[jMain.arrCkbTLDs.length];
        for (int i = 0; i < jMain.arrCkbTLDs.length; i++)
        {
            arrTLDs[i] = jMain.arrCkbTLDs[i].isSelected();
        }
        oFilter.setArrTLDs(arrTLDs);
        return oFilter;
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

        jPanel2 = new javax.swing.JPanel();
        jCkbDmozListed = new javax.swing.JCheckBox();
        jCkbNoFake = new javax.swing.JCheckBox();
        jCkbNoHyphen = new javax.swing.JCheckBox();
        jCkbNoCharacter = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jTxtSearchBegin = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jCboPR = new javax.swing.JComboBox();
        jCboCharacter = new javax.swing.JComboBox();
        jCboMinAge = new javax.swing.JComboBox();
        jCboBacklink = new javax.swing.JComboBox();
        jTxtSearchContain = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jCboMaxPrice = new javax.swing.JComboBox();
        jCboMinPrice = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jBtnClearFilter = new javax.swing.JButton();
        jCkbNoNumber = new javax.swing.JCheckBox();
        jBtnSetFilter = new javax.swing.JButton();
        jCboNumber = new javax.swing.JComboBox();
        jCkbAlexaRanking = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTxtSearchEndwith = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        jCkbDmozListed.setText("Dmoz listed");
        jCkbDmozListed.setOpaque(false);

        jCkbNoFake.setText("no Fake PRs");
        jCkbNoFake.setOpaque(false);

        jCkbNoHyphen.setText("no Hyphens");
        jCkbNoHyphen.setOpaque(false);

        jCkbNoCharacter.setText("no Characters");
        jCkbNoCharacter.setOpaque(false);

        jLabel1.setText("Domain search Begin with:");

        jLabel8.setText("Backlinks");

        jLabel6.setText("Character");

        jCboPR.setMaximumRowCount(15);

        jCboCharacter.setMaximumRowCount(15);

        jCboMinAge.setMaximumRowCount(15);

        jCboBacklink.setMaximumRowCount(15);

        jLabel3.setText("ends with");

        jLabel7.setText("PR Filter");

        jLabel2.setText("contains");

        jCboMaxPrice.setMaximumRowCount(15);

        jCboMinPrice.setMaximumRowCount(15);

        jLabel10.setText("Number");

        jLabel9.setText("min AGE");

        jBtnClearFilter.setText("Clear Filter");
        jBtnClearFilter.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jBtnClearFilterActionPerformed(evt);
            }
        });

        jCkbNoNumber.setText("no Numbers");
        jCkbNoNumber.setOpaque(false);

        jBtnSetFilter.setText("Set Filter");
        jBtnSetFilter.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jBtnSetFilterActionPerformed(evt);
            }
        });

        jCboNumber.setMaximumRowCount(15);

        jCkbAlexaRanking.setText("Alexa Ranking");
        jCkbAlexaRanking.setOpaque(false);

        jLabel5.setText("Max Price:");

        jLabel4.setText("Min Price:");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(23, 23, 23)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel4)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel1))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jCboMinPrice, 0, 118, Short.MAX_VALUE)
                            .add(jTxtSearchBegin))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel5)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel2)))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jCkbNoFake)
                            .add(jCkbAlexaRanking)
                            .add(jCkbDmozListed))
                        .add(81, 81, 81)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jCkbNoNumber)
                            .add(jCkbNoHyphen)
                            .add(jCkbNoCharacter))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jTxtSearchContain)
                    .add(jCboMaxPrice, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jCboCharacter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jCboNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jCboPR, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(27, 27, 27)
                        .add(jLabel3))
                    .add(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel10)
                            .add(jLabel6)
                            .add(jLabel7))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jCboBacklink, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jCboMinAge, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel8)
                            .add(jLabel9))
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jTxtSearchEndwith, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 104, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jBtnClearFilter)
                            .add(jBtnSetFilter))
                        .add(0, 20, Short.MAX_VALUE))))
        );

        jPanel2Layout.linkSize(new java.awt.Component[] {jCboBacklink, jCboCharacter, jCboMaxPrice, jCboMinAge, jCboMinPrice, jCboNumber, jCboPR, jTxtSearchBegin, jTxtSearchContain, jTxtSearchEndwith}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel2Layout.linkSize(new java.awt.Component[] {jCkbAlexaRanking, jCkbDmozListed, jCkbNoFake}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel2Layout.linkSize(new java.awt.Component[] {jCkbNoCharacter, jCkbNoHyphen, jCkbNoNumber}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel2Layout.linkSize(new java.awt.Component[] {jLabel10, jLabel6, jLabel7}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel2Layout.linkSize(new java.awt.Component[] {jBtnClearFilter, jBtnSetFilter}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(22, 22, 22)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel1)
                            .add(jTxtSearchBegin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(13, 13, 13)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel4)
                            .add(jCboMinPrice, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jLabel2)
                        .add(11, 11, 11)
                        .add(jLabel5))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jTxtSearchContain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel3)
                            .add(jTxtSearchEndwith, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jBtnSetFilter))
                        .add(8, 8, 8)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jCboMaxPrice, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jBtnClearFilter))))
                .add(29, 29, 29)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jCkbDmozListed)
                        .add(0, 0, 0)
                        .add(jCkbAlexaRanking)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jCkbNoFake, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jCboCharacter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel6)
                            .add(jCboBacklink, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel8))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jCboNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel10)
                            .add(jCboMinAge, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel9))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jCboPR, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel7)))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jCkbNoNumber)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jCkbNoCharacter)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jCkbNoHyphen)))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(new java.awt.Component[] {jCboBacklink, jCboCharacter, jCboMaxPrice, jCboMinAge, jCboMinPrice, jCboNumber, jCboPR, jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jTxtSearchBegin, jTxtSearchContain, jTxtSearchEndwith}, org.jdesktop.layout.GroupLayout.VERTICAL);

        jPanel2Layout.linkSize(new java.awt.Component[] {jCkbAlexaRanking, jCkbDmozListed, jCkbNoCharacter, jCkbNoFake, jCkbNoHyphen, jCkbNoNumber}, org.jdesktop.layout.GroupLayout.VERTICAL);

        jPanel2Layout.linkSize(new java.awt.Component[] {jBtnClearFilter, jBtnSetFilter}, org.jdesktop.layout.GroupLayout.VERTICAL);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnClearFilterActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jBtnClearFilterActionPerformed
    {//GEN-HEADEREND:event_jBtnClearFilterActionPerformed
        SetDefaultConfig(null);
        jBtnSetFilterActionPerformed(evt);
    }//GEN-LAST:event_jBtnClearFilterActionPerformed

    private void jBtnSetFilterActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jBtnSetFilterActionPerformed
    {//GEN-HEADEREND:event_jBtnSetFilterActionPerformed
        jMain.isSetFilter = true;
        FilterObject oFilter = new FilterObject();
        oFilter.setsSearchBegin(jTxtSearchBegin.getText().trim());
        oFilter.setsSearchContain(jTxtSearchContain.getText().trim());
        oFilter.setsSearchEndWith(jTxtSearchEndwith.getText().trim());
        oFilter.setoMinPrice((CboPriceObject) jCboMinPrice.getSelectedItem());
        oFilter.setoMaxPrice((CboPriceObject) jCboMaxPrice.getSelectedItem());
        oFilter.setIsDmoz(jCkbDmozListed.isSelected());
        oFilter.setIsAlexaRanking(jCkbAlexaRanking.isSelected());
        oFilter.setIsNoFake(jCkbNoFake.isSelected());
        oFilter.setIsNoNumber(jCkbNoNumber.isSelected());
        oFilter.setIsNoCharacter(jCkbNoCharacter.isSelected());
        oFilter.setIsNoHyphen(jCkbNoHyphen.isSelected());
        oFilter.setsCharacter(jCboCharacter.getSelectedItem().toString().substring(0, 1));
        oFilter.setsNumber(String.valueOf(jCboNumber.getSelectedIndex()));
        oFilter.setsPageRank(jCboPR.getSelectedItem().toString().substring(0, 1));
        oFilter.setsBackLinks(jCboBacklink.getSelectedItem().toString());
        oFilter.setsMinAge(jCboMinAge.getSelectedItem().toString());
        boolean[] arrTLDs = new boolean[jMain.arrCkbTLDs.length];
        for (int i = 0; i < jMain.arrCkbTLDs.length; i++)
        {
            arrTLDs[i] = jMain.arrCkbTLDs[i].isSelected();
        }
        oFilter.setArrTLDs(arrTLDs);
        try
        {
            jMain.oFilter = oFilter;
            XmlData.SaveFilterConfigToXml(oFilter, jMain._context.GetConfig().GetConfigPath() + jMain._context.GetConfig().GetFilterConfigFileName());
            if (JOptionPane.showConfirmDialog(this, "Save Filter configure was successfully! Do you want to close form?", "Message",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.OK_OPTION)
            {
                this.dispose();
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(JDialogShowFilter2.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
        }
    }//GEN-LAST:event_jBtnSetFilterActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnClearFilter;
    private javax.swing.JButton jBtnSetFilter;
    private javax.swing.JComboBox jCboBacklink;
    private javax.swing.JComboBox jCboCharacter;
    private javax.swing.JComboBox jCboMaxPrice;
    private javax.swing.JComboBox jCboMinAge;
    private javax.swing.JComboBox jCboMinPrice;
    private javax.swing.JComboBox jCboNumber;
    private javax.swing.JComboBox jCboPR;
    private javax.swing.JCheckBox jCkbAlexaRanking;
    private javax.swing.JCheckBox jCkbDmozListed;
    private javax.swing.JCheckBox jCkbNoCharacter;
    private javax.swing.JCheckBox jCkbNoFake;
    private javax.swing.JCheckBox jCkbNoHyphen;
    private javax.swing.JCheckBox jCkbNoNumber;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTxtSearchBegin;
    private javax.swing.JTextField jTxtSearchContain;
    private javax.swing.JTextField jTxtSearchEndwith;
    // End of variables declaration//GEN-END:variables

    public int GetStartEndType()
    {
        if (jTxtSearchBegin.getText().trim().length() > 0)
        {
            sStartEnd = jTxtSearchBegin.getText().trim();
            return 1;
        }
        else if (jTxtSearchContain.getText().trim().length() > 0)
        {
            sStartEnd = jTxtSearchContain.getText().trim();
            return 2;
        }
        else if (jTxtSearchEndwith.getText().trim().length() > 0)
        {
            sStartEnd = jTxtSearchEndwith.getText().trim();
            return 3;
        }
        else
        {
            sStartEnd = "";
            return 1;
        }
    }

    public int GetMinPrice()
    {
        CboPriceObject cboObj = (CboPriceObject) jCboMinPrice.getSelectedItem();
        return cboObj.getiValue();
    }

    public int GetMaxPrice()
    {
        CboPriceObject cboObj = (CboPriceObject) jCboMaxPrice.getSelectedItem();
        return cboObj.getiValue();
    }

    public String GetFilterCategory()
    {
        try
        {
            String s = jCboNumber.getSelectedItem().toString().substring(0, 1);
            int iNumber = Integer.parseInt(s);
            switch (iNumber)
            {
                case 3:
                    return "nnn";
                case 4:
                    return "nnnn";
                case 5:
                    return "nnnnn";
            }
        }
        catch (NumberFormatException ex)
        {
            return "all";
        }
        return "all";
    }
}
