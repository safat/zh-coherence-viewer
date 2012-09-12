package com.zh.coherence.viewer.tools.query.config.template;

import org.fife.ui.rsyntaxtextarea.CodeTemplateManager;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.templates.CodeTemplate;
import org.fife.ui.rsyntaxtextarea.templates.StaticCodeTemplate;

import javax.swing.*;

public class TemplateListModel extends AbstractListModel {

    private CodeTemplateManager manager = RSyntaxTextArea.getCodeTemplateManager();
    private CodeTemplateWrapper[] templates;

    public TemplateListModel() {
        RSyntaxTextArea.setTemplateDirectory("config/code_templates");
        reload();
    }

    @Override
    public int getSize() {
        return templates.length;
    }

    @Override
    public Object getElementAt(int index) {
        return templates[index];
    }

    public void reload(){
        CodeTemplate[] cta = manager.getTemplates();
        templates = new CodeTemplateWrapper[cta.length];
        for (int i = 0; i < cta.length; i++) {
            templates[i] = new CodeTemplateWrapper(cta[i]);
        }
        fireContentsChanged(this, 0, templates.length);
    }

    public CodeTemplateWrapper addTemplate(CodeTemplate template){
        manager.addTemplate(template);
        String id = template.getID();
        reload();
        for(CodeTemplateWrapper wrapper : templates){
            if(wrapper.ct.getID().equals(id)){
                return wrapper;
            }
        }

        return null;
    }

    public class CodeTemplateWrapper{
        public CodeTemplate ct;

        public CodeTemplateWrapper(CodeTemplate ct) {
            this.ct = ct;
        }

        @Override
        public String toString() {
            return ct.getID();
        }
    }
}
