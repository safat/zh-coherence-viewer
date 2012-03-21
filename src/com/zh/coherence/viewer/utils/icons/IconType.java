package com.zh.coherence.viewer.utils.icons;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 15.03.12
 * Time: 4:53
 */
public enum IconType {
    CLOSE_TAB("close-icon.gif"),
    EXIT("exit.png"),
    EXPLORER("explorer.png"),
    FUNCTION("function.png"),
    HISTORY("history.png"),
    KEYWORD("keyword.png"),
    LOGIN("login.png"),
    OK("ok.png"),
    PLUS("plus.png"),
    START("start-icon.png"),
    TEXT("text-icon.png"),
    WHITE_HINT("white-hint-icon.png"),
    WRITE("write.png"),
    YELLOW_HINT("yellow-hint-icon.png"),
    BACKUP("backup.png"),
    MINUS("minus.png"),
    CLOCK_64("clock-icon.png")
    ;

    private String type;

    private IconType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
