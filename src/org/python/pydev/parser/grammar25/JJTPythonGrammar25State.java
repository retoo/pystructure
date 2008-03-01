/* Generated By:JJTree: Do not edit this line. D:/jython/CVS.parser/org/python/parser\JJTPythonGrammarState.java */

// Modified by hand. The two closeNodeScope method have been rewritten
// completely and is used when building the AST tree bottom-up.

package org.python.pydev.parser.grammar25;

import org.python.pydev.core.structure.FastStack;
import org.python.pydev.parser.jython.Node;
import org.python.pydev.parser.jython.ParseException;
import org.python.pydev.parser.jython.SimpleNode;

class JJTPythonGrammar25State {
    private FastStack<SimpleNode> nodes;
    private IntStack marks;
    private IntStack lines;
    private IntStack columns;

    private int sp; // number of nodes on stack
    private int mk; // current mark
    private boolean node_created;

    TreeBuilder25 builder;

    JJTPythonGrammar25State() {
        nodes = new FastStack<SimpleNode>();
        marks = new IntStack();
        lines = new IntStack();
        columns = new IntStack();
        sp = 0;
        mk = 0;
        builder = new TreeBuilder25(this);
    }

    /* Determines whether the current node was actually closed and
       pushed.  This should only be called in the final user action of a
       node scope.  */
    boolean nodeCreated() {
        return node_created;
    }

    /* Call this to reinitialize the node stack.  It is called
       automatically by the parser's ReInit() method. */
    void reset() {
        nodes.removeAllElements();
        marks.removeAllElements();
        sp = 0;
        mk = 0;
    }

    /* Returns the root node of the AST.  It only makes sense to call
       this after a successful parse. */
    Node rootNode() {
        return nodes.getFirst();
    }

    /* Pushes a node on to the stack. */
    void pushNode(SimpleNode n) {
        nodes.push(n);
        ++sp;
    }

    /* Returns the node on the top of the stack, and remove it from the
       stack.  */
    SimpleNode popNode() {
        if (--sp < mk) {
            mk = marks.pop();
        }
        return nodes.pop();
    }

    /* Returns the node currently on the top of the stack. */
    SimpleNode peekNode() {
        return nodes.peek();
    }

    /* Returns the number of children on the stack in the current node
       scope. */
    int nodeArity() {
        return sp - mk;
    }

    void pushNodePos(int line, int col) {
        lines.push(line);
        columns.push(col);
    }

    SimpleNode setNodePos() {
        SimpleNode n = (SimpleNode) peekNode();
        
        int popLine = lines.pop();
        if(n.beginLine == 0)
            n.beginLine = popLine;
        
        int popCol = columns.pop();
        if(n.beginColumn == 0)
            n.beginColumn = popCol;
        return n;
    }


    void clearNodeScope(Node n) {
        while (sp > mk) {
            popNode();
        }
        mk = marks.pop();
    }


    void openNodeScope(Node n) {
        marks.push(mk);
        mk = sp;
    }


    /* A definite node is constructed from a specified number of
       children.  That number of nodes are popped from the stack and
       made the children of the definite node.  Then the definite node
       is pushed on to the stack. */
    void closeNodeScope(Node n, int num) throws ParseException {
        SimpleNode sn = (SimpleNode) n;
        mk = marks.pop();
        SimpleNode newNode = null;
        try {
            newNode = builder.closeNode(sn, num);
        } catch (ParseException exc) {
            throw exc;
        } catch (Exception exc) {
            exc.printStackTrace();
            throw new ParseException("Internal error:" + exc);
        }
        if (newNode == null) {
            throw new ParseException("Internal AST builder error");
        }
        pushNode(newNode);
        node_created = true;
    }


    /* A conditional node is constructed if its condition is true.  All
       the nodes that have been pushed since the node was opened are
       made children of the the conditional node, which is then pushed
       on to the stack.  If the condition is false the node is not
       constructed and they are left on the stack. */
    void closeNodeScope(Node n, boolean condition) throws ParseException {
        SimpleNode sn = (SimpleNode) n;
        if (condition) {
            SimpleNode newNode = null;
            try {
                newNode = builder.closeNode(sn, nodeArity());
            } catch (ParseException exc) {
                throw exc;
            } catch (ClassCastException exc) {
                exc.printStackTrace();
                throw new ParseException("Internal error:" + exc);
            } catch (Exception exc) {
                exc.printStackTrace();
                throw new ParseException("Internal error:" + exc);
            }
            if (newNode == null) {
                throw new ParseException("Internal AST builder error when closing node:"+sn);
            }
            if(marks.size() > 0){
                mk = marks.pop();
            }else{
                mk = 0;
            }
            pushNode(newNode);
            node_created = true;
        } else {
            mk = marks.pop();
            node_created = false;
        }
    }

}


/**
 * IntStack implementation. During all the tests, it didn't have it's size raised,
 * so, 50 is probably a good overall size... (max on python lib was 40)
 */
class IntStack {
    int[] stack;
    int sp = 0;

    public IntStack() {
        stack = new int[50];
    }


    public void removeAllElements() {
        sp = 0;
    }

    public int size() {
        return sp;
    }

    public int elementAt(int idx) {
        return stack[idx];
    }

    public void push(int val) {
        if (sp >= stack.length) {
            int[] newstack = new int[sp*2];
            System.arraycopy(stack, 0, newstack, 0, sp);
            stack = newstack;
        }
        stack[sp++] = val;
    }

    public int pop() {
        return stack[--sp];
    }
}
