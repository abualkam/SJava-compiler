package main.Compiler;

import main.Components.Component;
import main.Components.Variable.GlobalVariable;
import main.Components.Method;
import main.MyException.SyntaxErrorException;
import main.Parser.Parser;

import java.io.IOException;
import java.util.ArrayList;

public class SJava {



    public static void main(String[] args){
        try {

            Parser parser = new Parser(args[0]);
            ArrayList<Component> components = new ArrayList<>();

            while (parser.hasTokens())
            {
                parser.advance();
                Component component;
                switch (parser.tokenType()) {
                    case Parser.GLOBAL_VARIABLE -> {
                        component = new GlobalVariable(parser.getToken());
                        components.add(component);
                    }
                    case Parser.METHOD -> {
                        component = new Method(parser.getToken());
                        components.add(component);
                    }
                }
            }
            for(Component component : components)
            {
                component.compile();
            }
            System.out.println(0);
            System.out.println("File was compiled successfully");
        }
        catch (IOException e)
        {
            System.out.println(2);
            System.out.println(e.getMessage());
        }
        catch (SyntaxErrorException e)
        {
            System.out.println(1);
            System.out.println(e.getMessage());
        }

    }
}

