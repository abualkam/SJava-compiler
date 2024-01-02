package main.Parser;

import main.Constants.Massages;
import main.Constants.Regexes;
import main.MyException.SyntaxErrorException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public static final String GLOBAL_VARIABLE = "GLOBAL VARIABLE";
    public static final String METHOD = "METHOD";
    private final ListIterator<String> tokensIterator;
    private String token;


    public Parser(String filePath) throws IOException, SyntaxErrorException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        List<String> tokens = new ArrayList<>();
        String line ;
        while ((line = br.readLine()) != null)
        {
            if (line.matches("\\s*(//.*)?\\s*"))
                continue;
            line = line.trim();
            char suffix = line.charAt(line.length()-1);
            switch (suffix) {
                case ';' -> {
                    if (line.matches(Regexes.DEFINING_MULTI_VARIABLE))
                        tokens.addAll(List.of(separateVariables(line).split("\n")));
                    else
                        throw new SyntaxErrorException(Massages.INVALID_VARIABLE_DECLARATION + line);
                }
                case '{' -> {
                    Pattern pattern = Pattern.compile(Regexes.METHOD_DEFINE);
                    Matcher matcher = pattern.matcher(line);
                    if(matcher.find())
                    {
                        StringBuilder method = new StringBuilder();
                        String methodName = matcher.group(2);
                        method.append(line).append("\n");
                        int counter = 1;
                        while ((line = br.readLine()) != null)
                        {
                            if (line.matches("\\s*(//.*)?\\s*"))
                                continue;
                            line = line.trim();
                            if(line.matches(Regexes.IF_WHILE))
                                counter++;
                            else if(line.matches("\\s*[}]\\s*"))
                                counter--;

                            else  if (line.matches(Regexes.DEFINING_MULTI_VARIABLE)) {
                                    method.append(separateVariables(line));
                                    continue;
                                }
                            else if (line.matches(Regexes.CALLING_METHOD) || line.matches(Regexes.RETURN)
                                        || line.matches(Regexes.CHANGING_VARIABLE_VALUE)) {

                                }
                            else{
                                throw new SyntaxErrorException("Unrecognized code line, line :" + line);

                            }

                            method.append(line).append("\n");
                            if(counter == 0)
                                break;
                        }
                        method.deleteCharAt(method.length()-1);
                        tokens.add(method.toString());
                    }
                    else {
                        throw new SyntaxErrorException(Massages.INVALID_METHOD_DECLARATION + line);
                    }

                }
                default -> {
                    throw new SyntaxErrorException(Massages.INVALID_SUFFIX + line);

                }
                //TODO:raise exception
            }
            }
        tokensIterator = tokens.listIterator();
    }

    private String separateVariables(String line) throws SyntaxErrorException {
        StringBuilder components = new StringBuilder();
        String regex = "(final)?\\s*"+ Regexes.VARIABLE_TYPES+"\\s+(.+);";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        int lastEnd = 0;
        if(matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            if (start > lastEnd)
                throw new SyntaxErrorException(Massages.INVALID_VARIABLE_DECLARATION + line);
            String isFinal = (matcher.group(1) == null) ? "" : "final " ;
            String type = matcher.group(2);
            String s = matcher.group(3);
            String[] namesAndValues = s.split(",");
            String regex2 = "(\\w+)\\s*(?:=\\s*([^;\\s]+))?";
            Pattern pattern2 = Pattern.compile(regex2);
            for(String str: namesAndValues)
            {
                Matcher matcher2 = pattern2.matcher(str);
                if(matcher2.find())
                {
                    String name = matcher2.group(1);
//                    if(name.contains("."))
//                        throw new SyntaxErrorException(Massages.INVALID_VARIABLE_DECLARATION + line);
//                    name = methodName + name;
                    String value = matcher2.group(2);
                    value = (value == null)? "" : "= " +value;
                    String variable = isFinal + type + " " +name + " " + value+" ;";
                    components.append(variable).append("\n");
                }
                else
                    throw new SyntaxErrorException(Massages.INVALID_VARIABLE_DECLARATION + line);

            }
            lastEnd = end;
        }
        if (lastEnd < line.length())
            throw new SyntaxErrorException(Massages.INVALID_VARIABLE_DECLARATION + line);
        return components.toString();
    }


    public boolean hasTokens()
    {
        return  tokensIterator.hasNext();
    }
    public void advance() {
        this.token = tokensIterator.next();
    }

    public String getToken(){
        return this.token;
    }

    public String tokenType(){
        int length = this.token.length();
        char lastChar = this.token.charAt(length - 1);
        return switch (lastChar) {
            case ';' -> GLOBAL_VARIABLE;
            case '}' -> METHOD;
            default -> "";
        };

    }


}
