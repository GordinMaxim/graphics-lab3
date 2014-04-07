package ru.nsu.gordin.controller;

import ru.nsu.gordin.view.DrawPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MoveRightAction extends AbstractAction {
    private DrawPanel panel;

    public MoveRightAction(String text, ImageIcon icon,
                           String desc, Integer mnemonic, DrawPanel panel) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.panel = panel;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        panel.moveRight();
    }
}