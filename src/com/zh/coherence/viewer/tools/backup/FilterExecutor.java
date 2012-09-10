package com.zh.coherence.viewer.tools.backup;

import bsh.Interpreter;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.QueryHelper;
import com.tangosol.util.aggregator.Count;

import java.io.PrintWriter;

public class FilterExecutor {
    public static enum Executor {
        QUERY, SHELL
    }

    private Interpreter interpreter;

    public Filter execute(BackupFilter backupFilter) throws Exception {
        Filter filter;
        if (backupFilter.getFilterType() == BackupFilter.FilterType.QUERY) {
            filter = QueryHelper.createFilter(backupFilter.getSource());
        } else {
            if (interpreter == null) {
                interpreter = new Interpreter();
            }

            Object result = interpreter.eval(backupFilter.getSource());
            if(result == null){
                result = interpreter.get("filter");
            }
            if (result instanceof Filter) {
                filter = (Filter) result;
            } else {

                String className = result != null ? result.getClass().getName() : "NULL";
                throw new ClassCastException(className + " is not instanceof Filter");
            }
        }

        return filter;
    }

    public void checkScript(String cache, BackupFilter backupFilter, PrintWriter out) {
        out.write("Compile script...\t");
        out.flush();
        try {
            Filter filter = execute(backupFilter);
            out.write("Fine!\n");
            out.write("Try to execute filter...\n");
            out.flush();

            NamedCache namedCache = CacheFactory.getCache(cache);
            out.write("Cache size " + cache + ": " + namedCache.size() + "\n");
            out.flush();

            Integer size = (Integer) namedCache.aggregate(filter, new Count());
            out.write("Filtered: " + size);
            out.flush();
        } catch (Exception e) {
            out.write("\nERROR!\n\n");
            e.printStackTrace(out);
            out.flush();
        }

    }
}
