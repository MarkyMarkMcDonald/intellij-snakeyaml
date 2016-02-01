package lv.kid.vermut.intellij.yaml.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import lv.kid.vermut.intellij.yaml.lexer.YamlLexer;
import lv.kid.vermut.intellij.yaml.lexer.YamlTokenTypes;
import lv.kid.vermut.intellij.yaml.psi.impl.*;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Pavels.Veretennikovs on 2015.06.27..
 */
public class YamlParserDefinition implements ParserDefinition {
    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new YamlLexer();
    }

    @Override
    public PsiParser createParser(Project project) {
        return new YamlPsiParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return YamlNodes.FILE;
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return TokenSet.create(YamlTokenTypes.YAML_Whitespace);
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return TokenSet.EMPTY;

        // No idea why, but this ruins fragile balance in SnakeYAML. Will have to sacrifice TODOs
        // return TokenSet.create(YamlTokenTypes.YAML_Comment);
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.create(YamlTokenTypes.YAML_Scalar);
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        IElementType type = node.getElementType();

        if (type == YamlNodes.YAML_ScalarNode) return new YamlScalarImpl(node);
        else if (type == YamlNodes.YAML_SequenceNode) return new YamlSequenceImpl(node);
        else if (type == YamlNodes.YAML_MappingNode) return new YamlMappingImpl(node);
        else if (type == YamlNodes.YAML_NodeTuple) return new YamlTupleImpl(node);
        else if (type == YamlNodes.YAML_KeyNode) return new YamlTupleKeyImpl(node);
        else if (type == YamlNodes.YAML_ValueNode) return new YamlTupleValueImpl(node);

        throw new RuntimeException("Non existent YAML type");
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new YamlFileImpl(viewProvider);
    }

    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MUST;
    }
}
