package com.eugeniyamo.stringrefactoringplugin;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.PairFunction;

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
            String[] selectedWords = getWordsFromLine(selectionModel.getSelectedText());
            String[] variants = {"camelCase", "PascalCase", "snake_case", "kebab-case"};
            boolean checked = true;
            final int indexOfCase = Messages.showCheckboxMessageDialog(
                    "Select the desired option",
                    "Modification",
                    variants,
                    "Change the notation", checked, 1, 1,
                    Messages.getInformationIcon(), new PairFunction<Integer, JCheckBox, Integer>() {
                        @Override
                        public Integer fun(Integer exitCode, JCheckBox cb) {
                            return exitCode;
                        }
                    });
            String replacedString = "";
            switch(indexOfCase) {
                case 0:
                    replacedString = makeCamelCase(selectedWords);
                    break;
                case 1:
                    replacedString = makePascalCase(selectedWords);
                    break;
                case 2:
                    replacedString = makeSnakeCase(selectedWords);
                    break;
                case 3:
                    replacedString = makeKebabCase(selectedWords);;
                    break;
            }
            editor.getDocument().replaceString(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd(),
                    replacedString);
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
