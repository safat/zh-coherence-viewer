package com.zh.coherence.viewer.tools.backup;

import bsh.Interpreter;
import com.tangosol.io.pof.reflect.SimplePofPath;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.QueryHelper;
import com.tangosol.util.aggregator.Count;
import com.tangosol.util.extractor.PofExtractor;

import java.io.PrintWriter;

public class FilterExecutor {
    public static enum Executor {
        QUERY, SHELL
    }

    private Interpreter interpreter;

    public Filter execute(String script, Executor executor) throws Exception {
        Filter filter = null;
        if (executor == Executor.QUERY) {
            filter = QueryHelper.createFilter(script);
        } else {
            if (interpreter == null) {
                interpreter = new Interpreter();
            }

            Object result = interpreter.eval(script);
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

        PofExtractor ex = new PofExtractor(null, new SimplePofPath(0), PofExtractor.KEY);
        return filter;
    }

    public void checkScript(String cache, String script, Executor executor, PrintWriter out) {
        out.write("Compile script...\t");
        out.flush();
        try {
            Filter filter = execute(script, executor);
            System.err.println("FILTER: " + filter);
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
