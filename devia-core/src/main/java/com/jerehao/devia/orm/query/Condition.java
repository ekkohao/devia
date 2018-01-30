/*
 * Copyright (c) 2018, jerehao.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jerehao.devia.orm.query;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="http://jerehao.com">jerehao</a>
 * @version 0.0.1 2018-01-26 14:34 jerehao
 */
public abstract class Condition extends SQLClause {

    private final List<Term> terms = new LinkedList<>();

    private final List<RelationOperator> operators = new LinkedList<>();

    private RelationOperator prev = null;

    Condition(SQLSentence sqlSentence) {
        super(sqlSentence);
    }

    public Relation add(Property left, Operator operator, Object right) {
        terms.add(new Term(left, operator, right));

        return new Relation(this);
    }

    void and(int seq) {
        RelationOperator ro = new RelationOperator(RelationOperator.AND, seq);

        if(prev != null)
            prev.setNext(ro);

        prev = ro;
        operators.add(ro);
    }

    void or(int seq) {
        RelationOperator ro = new RelationOperator(RelationOperator.OR, seq);

        if(prev != null)
            prev.setNext(ro);

        prev = ro;
        operators.add(ro);
    }

    List<Term> getTerms() {
        return terms;
    }

    List<RelationOperator> getOperators() {
        return operators;
    }

    public static class Term {
        private Property left;
        private Operator operator;
        private Object right;

        public Term(Property left, Operator operator, Object right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        public Property getLeft() {
            return left;
        }

        public Operator getOperator() {
            return operator;
        }

        public Object getRight() {
            return right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Term term = (Term) o;

            return (left != null ? left.equals(term.left) : term.left == null)
                    && operator == term.operator
                    && (right != null ? right.equals(term.right) : term.right == null);
        }

        @Override
        public int hashCode() {
            int result = left != null ? left.hashCode() : 0;
            result = 31 * result + (operator != null ? operator.hashCode() : 0);
            result = 31 * result + (right != null ? right.hashCode() : 0);
            return result;
        }
    }

    public static class RelationOperator {
        private static final String AND = "AND";

        private static final String OR = "OR";

        RelationOperator next = null;

        private int sequence;

        private String operator;

        public RelationOperator(String operator, int sequence) {
            this.operator = operator;
            this.sequence = sequence;
        }

        public RelationOperator getNext() {
            return next;
        }

        public void setNext(RelationOperator next) {
            this.next = next;
        }

        public int getSequence() {
            return sequence;
        }
        public String getOperator() {
            return operator;
        }
    }
}

class Relation {

    private Condition condition;

    private final static int DEFAULT_SEQ = Integer.MAX_VALUE;

    Relation(Condition condition) {
        this.condition = condition;
    }

    Condition and() {
        condition.and(DEFAULT_SEQ);
        return condition;
    }

    Condition and(int seq) {
        condition.and(seq);
        return condition;
    }

    Condition or() {
        condition.or(DEFAULT_SEQ);
        return condition;
    }

    Condition or(int seq) {
        condition.or(seq);
        return condition;
    }
}


