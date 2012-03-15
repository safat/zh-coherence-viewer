/*
 * 01/07/2009
 *
 * CCellRenderer.java - A cell renderer for C completions.
 * Copyright (C) 2008 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA.
 */
package com.zh.coherence.viewer.tools.query;

import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;
import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.CompletionCellRenderer;
import org.fife.ui.autocomplete.FunctionCompletion;
import org.fife.ui.autocomplete.VariableCompletion;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;


/**
 * The cell renderer used for the C programming language.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class CCellRenderer extends CompletionCellRenderer {

    private Icon keywordIcon;
    private Icon functionIcon;
    private Icon emptyIcon;


    /**
     * Constructor.
     */
    public CCellRenderer() {
        keywordIcon = IconHelper.getInstance().getIcon(IconType.KEYWORD);
        functionIcon = IconHelper.getInstance().getIcon(IconType.FUNCTION);
        emptyIcon = new EmptyIcon(16);
    }

    /**
     * {@inheritDoc}
     */
    protected void prepareForOtherCompletion(JList list,
                                             Completion c, int index, boolean selected, boolean hasFocus) {
        super.prepareForOtherCompletion(list, c, index, selected, hasFocus);
        setIcon(emptyIcon);
    }


    /**
     * {@inheritDoc}
     */
    protected void prepareForVariableCompletion(JList list,
                                                VariableCompletion vc, int index, boolean selected,
                                                boolean hasFocus) {
        super.prepareForVariableCompletion(list, vc, index, selected,
                hasFocus);
        setIcon(keywordIcon);
    }


    /**
     * {@inheritDoc}
     */
    protected void prepareForFunctionCompletion(JList list,
                                                FunctionCompletion fc, int index, boolean selected,
                                                boolean hasFocus) {
        super.prepareForFunctionCompletion(list, fc, index, selected,
                hasFocus);
        setIcon(functionIcon);
    }


    /**
     * An standard icon that doesn't paint anything.  This can be used to take
     * up an icon's space when no icon is specified.
     *
     * @author Robert Futrell
     * @version 1.0
     */
    private static class EmptyIcon implements Icon, Serializable {

        private int size;

        public EmptyIcon(int size) {
            this.size = size;
        }

        public int getIconHeight() {
            return size;
        }

        public int getIconWidth() {
            return size;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
        }

    }


}