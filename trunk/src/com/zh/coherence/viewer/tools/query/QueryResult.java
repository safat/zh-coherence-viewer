package com.zh.coherence.viewer.tools.query;

import com.tangosol.coherence.dsltools.termtrees.Term;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 09.06.12
 * Time: 12:13
 */
public class QueryResult {
    private Object result;

    private Throwable error;

    private String shortMessage;

    private boolean containsError = false;

    private long startTime = System.currentTimeMillis();

    private long endTime;

    private int rowsNumber;

    private int top = -1;

    private Term term;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isContainsError() {
        return containsError;
    }

    public void setContainsError(boolean containsError) {
        this.containsError = containsError;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public int getRowsNumber() {
        return rowsNumber;
    }

    public void setRowsNumber(int rowsNumber) {
        this.rowsNumber = rowsNumber;
    }

    public String getShortMessage() {
        return shortMessage;
    }

    public void setShortMessage(String shortMessage) {
        this.shortMessage = shortMessage;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setError(Throwable error, String shortErrorStr) {
        this.error = error;
        this.shortMessage = shortErrorStr;
        this.containsError = true;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public String getErrorMessage() {
        if (shortMessage != null) {
            return shortMessage;
        } else if (error != null) {
            return error.getMessage();
        } else {
            return "ERROR";
        }
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}
