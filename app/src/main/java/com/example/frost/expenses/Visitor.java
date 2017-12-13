package com.example.frost.expenses;

public interface Visitor {
    void visit(Food f);
    void visit(Travel t);
    void visit(Miscellaneous m);
    void visit(Emergency e);


}
