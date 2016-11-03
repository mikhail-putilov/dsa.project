package ru.innopolis.mputilov.dsap;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.antlr.JavaBaseListener;
import org.antlr.JavaParser;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mputilov on 29/09/16.
 */
@Slf4j
public class ImportsContextListener extends JavaBaseListener {
    @Getter
    private Set<JavaParser.ImportDeclarationContext> importDeclarations = new HashSet<>();

    @Override
    public void exitImportDeclaration(JavaParser.ImportDeclarationContext ctx) {
        importDeclarations.add(ctx);
    }
}
