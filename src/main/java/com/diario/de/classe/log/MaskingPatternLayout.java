package com.diario.de.classe.log;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaskingPatternLayout extends PatternLayout {

    private final List<Pattern> maskPatterns = new ArrayList<>();

    public void addMaskPattern(String maskPattern) {
        maskPatterns.add(Pattern.compile(maskPattern, Pattern.MULTILINE));
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        String message = super.doLayout(event);

        if (maskPatterns.isEmpty()) {
            return message;
        }

        String maskedMessage = message;

        for (Pattern pattern : maskPatterns) {
            Matcher matcher = pattern.matcher(maskedMessage);
            StringBuffer sb = new StringBuffer();

            while (matcher.find()) {

                String value = matcher.group(1);

                String maskedValue;

                if (value.contains("@")) {
                    // EMAIL
                    int atIndex = value.indexOf("@");
                    maskedValue = "***" + value.substring(atIndex);
                } else if (value.length() > 4) {
                    // CPF / dados gerais
                    maskedValue = "***" + value.substring(value.length() - 4);
                } else {
                    // SENHAS CURTAS
                    maskedValue = "***";
                }

                matcher.appendReplacement(sb, matcher.group().replace(value, maskedValue));
            }

            matcher.appendTail(sb);
            maskedMessage = sb.toString();
        }

        return maskedMessage;
    }
}