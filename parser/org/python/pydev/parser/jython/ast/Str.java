// Autogenerated AST node
package org.python.pydev.parser.jython.ast;
import org.python.pydev.parser.jython.SimpleNode;
import java.io.DataOutputStream;
import java.io.IOException;

public class Str extends exprType implements str_typeType {
    public String s;
    public int type;
    public boolean unicode;
    public boolean raw;

    public Str(String s, int type, boolean unicode, boolean raw) {
        this.s = s;
        this.type = type;
        this.unicode = unicode;
        this.raw = raw;
    }

    public Str(String s, int type, boolean unicode, boolean raw, SimpleNode
    parent) {
        this(s, type, unicode, raw);
        this.beginLine = parent.beginLine;
        this.beginColumn = parent.beginColumn;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("Str[");
        sb.append("s=");
        sb.append(dumpThis(this.s));
        sb.append(", ");
        sb.append("type=");
        sb.append(dumpThis(this.type, str_typeType.str_typeTypeNames));
        sb.append(", ");
        sb.append("unicode=");
        sb.append(dumpThis(this.unicode));
        sb.append(", ");
        sb.append("raw=");
        sb.append(dumpThis(this.raw));
        sb.append("]");
        return sb.toString();
    }

    public void pickle(DataOutputStream ostream) throws IOException {
        pickleThis(43, ostream);
        pickleThis(this.s, ostream);
        pickleThis(this.type, ostream);
        pickleThis(this.unicode, ostream);
        pickleThis(this.raw, ostream);
    }

    public Object accept(VisitorIF visitor) throws Exception {
        return visitor.visitStr(this);
    }

    public void traverse(VisitorIF visitor) throws Exception {
    }

}
