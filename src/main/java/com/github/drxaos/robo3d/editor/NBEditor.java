package com.github.drxaos.robo3d.editor;

import com.github.drxaos.robo3d.editor.syntax.JSKit;
import com.github.drxaos.robo3d.editor.syntax.JSSettingsInitializer;
import org.netbeans.editor.ext.ExtKit;

import javax.swing.*;
import java.awt.*;

public class NBEditor extends JPanel {

    ExtKit editorKit;
    JEditorPane editorPane;
    JComponent renderer;

    public NBEditor() {
        // Create an editor kit for scheme
        editorKit = new JSKit();

        // Initialize Scheme language support
        NBEditorFactory.addSyntax(editorKit, new JSSettingsInitializer(editorKit.getClass()));

        // Create a plain editor pane
        editorPane = new JEditorPane();

        // Create a renderer to replace the editor pane *visually*
        renderer = NBEditorFactory.newTextRenderer(editorKit, editorPane);

        // Set the text *in the editor pane*
        editorPane.setText("// JavaScript\n\nfunction main(){\n    print('hello');\n}");

        // Visualize the *renderer*
        setLayout(new BorderLayout());
        add(renderer, BorderLayout.CENTER);
    }

}
