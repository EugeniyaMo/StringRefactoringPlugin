package com.eugeniyamo.stringrefactoringplugin;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;

import javax.swing.*;
public class RefactorStringAction extends EditorAction {
    public RefactorStringAction() {
        this(new RefactorHandler());
    }

    protected RefactorStringAction(EditorActionHandler defaultHandler) {
        super(defaultHandler);
    }

    private static class RefactorHandler extends EditorWriteActionHandler {
        private RefactorHandler() {
        }

        @Override
        public void executeWriteAction(Editor editor, DataContext dataContext) {
            SelectionModel selectionModel = editor.getSelectionModel();
            RefactorStringDialog dialog = new RefactorStringDialog();
            dialog.pack();
            dialog.setVisible(true);
            String string[] = getWordsFromLine(dialog.getTextField().getText());
            String[] variants = {"camelCase", "PascalCase", "snake_case", "kebab-case"};
            boolean checked = true;

            int indexOfCase = dialog.getComboBox().getSelectedIndex();

            String replacedString = "";
            switch(indexOfCase) {
                case 0:
                    replacedString = makeCamelCase(string);
                    break;
                case 1:
                    replacedString = makePascalCase(string);
                    break;
                case 2:
                    replacedString = makeSnakeCase(string);
                    break;
                case 3:
                    replacedString = makeKebabCase(string);;
                    break;
            }
            if (dialog.isOkey()) {
                editor.getDocument().replaceString(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd(),
                        replacedString);
            }
        }

        private String[] getWordsFromLine(String string) {
            String[] words = string.split(" ");
            return words;
        }

        private String makeCamelCase(String[] words) {
            String returnedString = words[0];
            for (int i = 1; i < words.length; i++) {
                String word = words[i];
                returnedString += word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
            }
            return returnedString;
        }

        private String makePascalCase(String[] words) {
            String returnedString = "";
            for (String word : words) {
                returnedString += word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
            }
            return returnedString;
        }

        private String makeSnakeCase(String[] words) {
            String returnedString = "";
            for (String word : words) {
                returnedString += word.toLowerCase() + '_';
            }
            returnedString = returnedString.substring(0, returnedString.length() - 1);
            return returnedString;
        }

        private String makeKebabCase(String[] words) {
            String returnedString = "";
            for (String word : words) {
                returnedString += word.toLowerCase() + '-';
            }
            returnedString = returnedString.substring(0, returnedString.length() - 1);
            return returnedString;
        }

    }
}
