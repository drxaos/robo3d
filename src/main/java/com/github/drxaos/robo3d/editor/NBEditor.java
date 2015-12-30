package com.github.drxaos.robo3d.editor;

import net.antonioshome.nbeditorlib.NBEditorFactory;
import net.antonioshome.nbeditorlib.demo.syntax.SchemeEditorKit;
import net.antonioshome.nbeditorlib.demo.syntax.SchemeSettingsInitializer;

import javax.swing.*;
import java.awt.*;

public class NBEditor extends JPanel {

    SchemeEditorKit editorKit;
    JEditorPane editorPane;
    JComponent renderer;

    public NBEditor() {
        // Create an editor kit for scheme
        editorKit = new SchemeEditorKit();

        // Initialize Scheme language support
        NBEditorFactory.addSyntax(editorKit, new SchemeSettingsInitializer());

        // Create a plain editor pane
        editorPane = new JEditorPane();

        // Create a renderer to replace the editor pane *visually*
        renderer = NBEditorFactory.newTextRenderer(editorKit, editorPane);

        // Set the text *in the editor pane*
        editorPane.setText("; A scheme definition\n(define i (sqrt -1 ))\n");

        // Visualize the *renderer*
        setLayout(new BorderLayout());
        add(renderer, BorderLayout.CENTER);
    }

}
