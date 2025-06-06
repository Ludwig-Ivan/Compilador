package compilador;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.collection.ListModification;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

public class Pestana extends StackPane {

    private static final Map<String, String> GROUP_TO_STYLE;
    private CodeArea codeArea;
    private String ruta;

    static {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("KEYWORD", "keyword");
        map.put("IDENTIFIER", "identifier");
        map.put("NUMBER", "number");
        map.put("OPERATOR", "operator");
        map.put("PAREN", "paren");
        map.put("BRACE", "brace");
        map.put("BRACKET", "bracket");
        map.put("SEMICOLON", "semicolon");
        map.put("STRING", "string");
        map.put("CHAR", "char");
        map.put("COMMENT", "comment");
        // map.put("ERROR", "error");
        GROUP_TO_STYLE = Collections.unmodifiableMap(map);
    }

    private static final String[] KEYWORDS = new String[] {
            "if", "else", "while", "do", "return", "print", "int", "float", "char",
            "cadena", "bool", "procedure", "true", "false", "break", "continue", "func", "main", "program", "for",
            "read"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String IDENTIFIER_PATTERN = "[a-zA-Z_][a-zA-Z0-9_]*";
    private static final String NUMBER_PATTERN = "\\d+\\.?\\d*";
    private static final String OPERATOR_PATTERN = "\\+|-|\\*|/|%|==|!=|<=|>=|<|>|&&|\\|\\||!|=|\\?|:";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"]|\\.)*\"|\"([^\"]|\\.)*";
    private static final String CHAR_PATTERN = "'([^'\\\\]|\\\\.)'|'([^'\\\\]|\\\\.)";
    private static final String COMMENT_PATTERN = "//[^\n]*" + // Comentario de línea
            "|" + "(?s)/\\*.*?\\*/" + // Comentario de bloque válido
            "|" + "/\\*.*";
    // TODO : Revisar este patron Error
    // private static final String ERROR_PATTERN =
    // "[^\\s\\w\"';,(){}\\[\\]+\\-*/%=<>!&|?:]";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<COMMENT>" + COMMENT_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<IDENTIFIER>" + IDENTIFIER_PATTERN + ")"
                    + "|(?<NUMBER>" + NUMBER_PATTERN + ")"
                    + "|(?<OPERATOR>" + OPERATOR_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<CHAR>" + CHAR_PATTERN + ")");
    // + "|(?<ERROR>" + ERROR_PATTERN + ")");

    public Pestana(String n) {
        this.ruta = n;
        codeArea = new CodeArea();
        codeArea.setEditable(true);
        codeArea.setParagraphGraphicFactory(line -> {
            Node graphic = LineNumberFactory.get(codeArea).apply(line);
            graphic.getStyleClass().add("line-number");
            return graphic;
        });
        codeArea.setContextMenu(new DefaultContextMenu());

        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            codeArea.getVisibleParagraphs().addModificationObserver(
                    new VisibleParagraphStyler<>(codeArea, this::computeHighlighting));
        });

        codeArea.getVisibleParagraphs().addModificationObserver(
                new VisibleParagraphStyler<>(codeArea, this::computeHighlighting));

        final Pattern whiteSpace = Pattern.compile("^\\s+");
        codeArea.addEventHandler(KeyEvent.KEY_PRESSED, KE -> {
            if (KE.getCode() == KeyCode.ENTER) {
                int caretPosition = codeArea.getCaretPosition();
                int currentParagraph = codeArea.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher(
                        codeArea.getParagraph(currentParagraph - 1).getSegments().get(0));
                if (m0.find()) {
                    Platform.runLater(() -> codeArea.insertText(caretPosition, m0.group()));
                }
            }
        });

        setId(ruta);
        getChildren().add(new VirtualizedScrollPane<>(codeArea));
    }

    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        while (matcher.find()) {
            String styleClass = null;
            for (Map.Entry<String, String> entry : GROUP_TO_STYLE.entrySet()) {
                if (matcher.group(entry.getKey()) != null) {
                    styleClass = entry.getValue();
                    break;
                }
            }

            if (styleClass == null)
                styleClass = "error"; // Por si acaso

            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    private class VisibleParagraphStyler<PS, SEG, S>
            implements Consumer<ListModification<? extends Paragraph<PS, SEG, S>>> {
        private final GenericStyledArea<PS, SEG, S> area;
        private final Function<String, StyleSpans<S>> computeStyles;
        private int prevParagraph, prevTextLength;

        public VisibleParagraphStyler(GenericStyledArea<PS, SEG, S> area,
                Function<String, StyleSpans<S>> computeStyles) {
            this.computeStyles = computeStyles;
            this.area = area;
        }

        @Override
        public void accept(ListModification<? extends Paragraph<PS, SEG, S>> lm) {
            if (lm.getAddedSize() > 0) {
                Platform.runLater(() -> {
                    int paragraph = Math.min(area.firstVisibleParToAllParIndex() + lm.getFrom(),
                            area.getParagraphs().size() - 1);
                    String text = area.getText(paragraph, 0, paragraph,
                            area.getParagraphLength(paragraph));

                    if (paragraph != prevParagraph || text.length() != prevTextLength) {
                        if (paragraph < area.getParagraphs().size() - 1) {
                            int startPos = area.getAbsolutePosition(paragraph, 0);
                            area.setStyleSpans(startPos, computeStyles.apply(text));
                        }
                        prevTextLength = text.length();
                        prevParagraph = paragraph;
                    }
                });
            }
        }
    }

    private class DefaultContextMenu extends ContextMenu {
        private MenuItem fold, unfold, print;

        public DefaultContextMenu() {
            fold = new MenuItem("Fold selected text");
            fold.setOnAction(AE -> {
                hide();
                fold();
            });

            unfold = new MenuItem("Unfold from cursor");
            unfold.setOnAction(AE -> {
                hide();
                unfold();
            });

            print = new MenuItem("Print");
            print.setOnAction(AE -> {
                hide();
                print();
            });

            getItems().addAll(fold, unfold, print);
        }

        private void fold() {
            ((CodeArea) getOwnerNode()).foldSelectedParagraphs();
        }

        private void unfold() {
            CodeArea area = (CodeArea) getOwnerNode();
            area.unfoldParagraphs(area.getCurrentParagraph());
        }

        private void print() {
            System.out.println(((CodeArea) getOwnerNode()).getText());
        }
    }

    public String getRuta() {
        return ruta;
    }

    public String getText() {
        String text = codeArea.getText();
        return text;
    }

    public void setText(String txt) {
        codeArea.replaceText(txt);
        Platform.runLater(() -> {
            StyleSpans<Collection<String>> styles = computeHighlighting(txt);
            codeArea.setStyleSpans(0, styles);
        });
    }

    public String getSelectedText() {
        return codeArea.getSelectedText(); // Suponiendo que usas un TextArea llamado textArea
    }

    public void setFontSize(int fontsize) {
        codeArea.setStyle(String.format("-fx-font-size: %dpx;", fontsize));
    }

}