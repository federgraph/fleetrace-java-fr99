package org.riggvar.fr99;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusBarHelper {
    FlowLayout StatusBarLayout = new FlowLayout();
    JLabel spPortIn = new JLabel();
    JLabel spPortOut = new JLabel();
    JLabel spIT = new JLabel();
    JLabel spRace = new JLabel();
    JLabel spWebServerPort = new JLabel();
    JLabel spConnectStatus = new JLabel();
    JLabel spPlugStatus = new JLabel();

    public void buildStatusBar(JPanel StatusBar) {
        spPortIn.setBorder(BorderFactory.createLoweredBevelBorder());
        spPortOut.setBorder(BorderFactory.createLoweredBevelBorder());
        spRace.setBorder(BorderFactory.createLoweredBevelBorder());
        spIT.setBorder(BorderFactory.createLoweredBevelBorder());
        spWebServerPort.setBorder(BorderFactory.createLoweredBevelBorder());
        spConnectStatus.setBorder(BorderFactory.createLoweredBevelBorder());
        spPlugStatus.setBorder(BorderFactory.createLoweredBevelBorder());

        StatusBar.setToolTipText("");
        spPortIn.setToolTipText("PortIn");
        spPortOut.setToolTipText("PortOut");
        spRace.setToolTipText("Race");
        spIT.setToolTipText("IT");
        spWebServerPort.setToolTipText("WebServerPort");
        spConnectStatus.setToolTipText("Internal Connection Status");
        spPlugStatus.setToolTipText("External Plug Status");

        spPortIn.setText("3027");
        spPortOut.setText("3028");
        spWebServerPort.setText("");
        spRace.setText("W1");
        spIT.setText("IT0");
        spPlugStatus.setText("Unplugged");
        spConnectStatus.setText("Disconnected");

        StatusBar.setLayout(StatusBarLayout);
        StatusBarLayout.setAlignment(FlowLayout.LEFT);
        StatusBar.setAlignmentY((float) 0.5);
        StatusBarLayout.setHgap(5);
        StatusBar.setDoubleBuffered(true);

        StatusBar.add(spPortIn);
        StatusBar.add(spPortOut);
        StatusBar.add(spWebServerPort);
        StatusBar.add(spRace);
        StatusBar.add(spIT);
        StatusBar.add(spPlugStatus);
        StatusBar.add(spConnectStatus);
    }

}
