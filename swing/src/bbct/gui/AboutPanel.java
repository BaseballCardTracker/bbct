/*
 * This file is part of BBCT.
 *
 * Copyright 2012 codeguru <codeguru@users.sourceforge.net>
 *
 * BBCT is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BBCT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bbct.gui;

import java.awt.CardLayout;
import java.awt.Container;

/**
 * A panel that displays the name of this application and copyright information.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class AboutPanel extends javax.swing.JPanel {

    /**
     * Creates a new {@link AboutPanel}.
     */
    public AboutPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel infoPanel = new javax.swing.JPanel();
        javax.swing.JLabel TitleLabel = new javax.swing.JLabel();
        javax.swing.JLabel copyrightLabel = new javax.swing.JLabel();
        javax.swing.JLabel versionLabel = new javax.swing.JLabel();
        javax.swing.JLabel websiteLabel = new javax.swing.JLabel();
        javax.swing.JLabel gplLabel = new javax.swing.JLabel();
        javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();

        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                formAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        setLayout(new java.awt.BorderLayout());

        TitleLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        TitleLabel.setText("Baseball Card Tracker");

        copyrightLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        copyrightLabel.setText("<html>&copy 2012 codeguru</html>");

        versionLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        versionLabel.setText("Version 0.5.2 (beta)");

        websiteLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        websiteLabel.setText("Project website: http://www.sourceforge.net/p/bbct");

        gplLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        gplLabel.setText("<html>This software is licensed under the GPL. Visit<br> http://www.gnu.org/licenses/ for a copy of the license.</html>");

        javax.swing.GroupLayout infoPanelLayout = new javax.swing.GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 434, Short.MAX_VALUE)
            .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(infoPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(copyrightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(versionLabel)
                        .addComponent(websiteLabel)
                        .addComponent(gplLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
                        .addComponent(TitleLabel))
                    .addContainerGap()))
        );
        infoPanelLayout.setVerticalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 521, Short.MAX_VALUE)
            .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(infoPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(TitleLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(versionLabel)
                    .addGap(18, 18, 18)
                    .addComponent(copyrightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(websiteLabel)
                    .addGap(330, 330, 330)
                    .addComponent(gplLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        add(infoPanel, java.awt.BorderLayout.CENTER);

        okButton.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(okButton);

        add(buttonPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        Container parent = this.getParent();
        CardLayout cl = (CardLayout) parent.getLayout();

        cl.show(parent, BBCTFrame.MENU_CARD_NAME);
    }//GEN-LAST:event_okButtonActionPerformed

    private void formAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_formAncestorAdded
        Container topLevelAncestor = this.getTopLevelAncestor();

        if (topLevelAncestor instanceof BBCTFrame) {
            BBCTFrame frame = (BBCTFrame) topLevelAncestor;
            frame.setTitle(GUIResources.ABOUT_PANEL_TITLE);
            frame.setInstructions("Click OK when ready.");
            frame.setDefaultButton(this.okButton);
        }
    }//GEN-LAST:event_formAncestorAdded
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
}
